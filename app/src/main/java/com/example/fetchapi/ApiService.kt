import com.example.fetchapi.ApiResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService{
    @Headers("Api-Key:pJIDJt8OaTTYopXXFmwiX28GEEa95mdAdM86Am9bhYADKvodET")
    @Multipart
    @POST("identification")
    fun uploadImage(
        @Part image: MultipartBody.Part,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody
    ): Call<ApiResponse>
}
