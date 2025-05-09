package ru.ichaporgin.ralearningapp

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.ichaporgin.ralearningapp.databinding.FragmentRecipeBinding

class RecipeFragment : Fragment() {
    private var _binding: FragmentRecipeBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentRecipeBinding must not to be null")
    private var recipeImage: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        @Suppress("DEPRECATION")
        val recipe = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(NavigationArgs.ARG_RECIPE, Recipe::class.java)
        } else {
            arguments?.getParcelable(NavigationArgs.ARG_RECIPE)
        }
        recipe?.let {
            binding.txTitleRecipe.text = it.title
        }
        try {
            val assetManager = requireContext().assets
            val inputStream = assetManager.open(recipe?.imageUrl.toString())
            val drawable = Drawable.createFromStream(inputStream, null)
            if (drawable != null) {
                binding.imgRecipe.setImageDrawable(drawable)
                Log.d("RecipesListFragment", "Картинка успешно загружена")
            } else {
                Log.e("RecipesiesListFragment", "Drawable == null")
            }
        } catch (e: Exception) {
            Log.e("RecipesListFragment", "Ошибка загрузки картинки", e)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}