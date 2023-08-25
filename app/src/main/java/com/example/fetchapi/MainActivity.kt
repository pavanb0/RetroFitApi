import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.fetchapi.ApiResponse
import com.example.fetchapi.NetworkService
import com.example.fetchapi.R
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class MainActivity : AppCompatActivity() {

    private val PICK_IMAGE_REQUEST = 1
    private lateinit var selectedImageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set up your UI components and button click listener
        val uploadButton = findViewById<Button>(R.id.uploadButton)
        // Example button click listener
        uploadButton.setOnClickListener {
            checkPermissionsAndPickImage()
        }
    }

    private fun checkPermissionsAndPickImage() {
        // Check for permissions and pick image
        // This code snippet should be completed based on permissions handling
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.data!!
            // Now you have the selected image URI, proceed to upload it
            uploadImage(selectedImageUri)
        }
    }

    private fun uploadImage(imageUri: Uri) {
        val filePath = getRealPathFromURI(imageUri)

        if (filePath != null) {
            val imageFile = File(filePath)
            val imageRequestBody = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
            val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, imageRequestBody)

            val latitude = "your_latitude_value".toRequestBody("text/plain".toMediaTypeOrNull())
            val longitude = "your_longitude_value".toRequestBody("text/plain".toMediaTypeOrNull())

            val apiService = NetworkService.apiService
            val call = apiService.uploadImage(imagePart, latitude, longitude)

            call.enqueue(object : Callback<ApiResponse> {
                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                    // Handle success response
                }

                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    // Handle failure
                }
            })
        } else {
            // Handle case where file path is not valid
        }
    }

    private fun getRealPathFromURI(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, projection, null, null, null)
        cursor?.use {
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            if (it.moveToFirst()) {
                return it.getString(columnIndex)
            }
        }
        return null
    }
}
