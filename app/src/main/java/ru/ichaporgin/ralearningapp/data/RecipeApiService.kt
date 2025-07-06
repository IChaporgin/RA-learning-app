import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import ru.ichaporgin.ralearningapp.model.Category


interface RecipeApiService {
    @GET("category")
    fun getCategories(): Call<List<Category>>
}