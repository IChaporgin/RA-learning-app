import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import ru.ichaporgin.ralearningapp.model.Category
import ru.ichaporgin.ralearningapp.model.Recipe


interface RecipeApiService {
    @GET("recipes")
    fun getRecipes(): Call<List<Recipe>>

    @GET("recipe/{id}")
    fun getRecipe(@Path("id") id: Int): Call<Recipe>

    @GET("category/{id}")
    fun getCategory(@Path("id") id: Int): Call<Category>

    @GET("category/{id}/recipes")
    fun getRecipes(@Path("id") id: Int): Call<List<Recipe>>
    @GET("category")
    fun getCategories(): Call<List<Category>>
}