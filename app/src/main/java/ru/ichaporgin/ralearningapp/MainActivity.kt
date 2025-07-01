package ru.ichaporgin.ralearningapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.findNavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.ichaporgin.ralearningapp.data.Constants
import ru.ichaporgin.ralearningapp.databinding.ActivityMainBinding
import ru.ichaporgin.ralearningapp.model.Category
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for ActivityMainBinding must not to be null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val thread = Thread {
            try {
                val url = URL("${Constants.BASE_URL}category")
                val connection = url.openConnection() as HttpsURLConnection
                connection.connect()
                val json = connection.inputStream.bufferedReader().readText()
                val type = object : TypeToken<List<Category>>() {}.type
                val category: List<Category> = Gson().fromJson(json, type)
                Log.i("!!!", "JSON Category: $category")
                Log.i("!!!", "ResponseCode: ${connection.responseCode}")
                Log.i("!!!", "ResponseMessage: ${connection.responseMessage}")
                Log.i("!!!", "ResponseCode: ${connection.inputStream.bufferedReader().readText()}")
                Log.i("!!!", "Выполняю запрос на потоке: ${Thread.currentThread().name}")
                connection.disconnect()
            } catch (e: Exception) {
                Log.e("!!!", "Ошибка соединения:", e)
            }
        }

        thread.start()
        Log.i("!!!", "Метод onCreate() выполняется на потоке: ${Thread.currentThread().name}")

        binding.btnCategory.setOnClickListener {
            findNavController(R.id.nav_host_fragment).navigate(R.id.categoriesListFragment)
        }

        binding.btnFavorite.setOnClickListener {
            findNavController(R.id.nav_host_fragment).navigate(R.id.favoritesFragment)
        }


        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true

        @Suppress("DEPRECATION")
        window.navigationBarColor = ContextCompat.getColor(this, R.color.background_color)
        @Suppress("DEPRECATION")
        window.statusBarColor = ContextCompat.getColor(this, R.color.background_color)
    }
}