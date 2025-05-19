package ru.ichaporgin.ralearningapp

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import ru.ichaporgin.ralearningapp.databinding.FragmentRecipeBinding

class RecipeFragment : Fragment() {
    private var _binding: FragmentRecipeBinding? = null
    private var ingredientsAdapter: IngredientsAdapter? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentRecipeBinding must not to be null")
    private var recipeId: Int? = null
    private var isFavorite = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.seekBar.max = MAX_PORTIONS
        binding.seekBar.min = MIN_PORTIONS
        isFavorite = getFavorites().contains(recipeId.toString())

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, p2: Boolean) {
                val portionsCount = progress
                binding.portions.text = getString(R.string.portions, portionsCount)
                ingredientsAdapter?.updateIngredients(portionsCount)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        val initialPortions = if (binding.seekBar.progress < 1) 1 else binding.seekBar.progress
        binding.portions.text = getString(R.string.portions, initialPortions)

        @Suppress("DEPRECATION")
        val recipe = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(NavigationArgs.ARG_RECIPE, Recipe::class.java)
        } else {
            arguments?.getParcelable(NavigationArgs.ARG_RECIPE)
        }
        recipe?.let {
            recipeId = it.id
            isFavorite = getFavorites().contains(recipeId.toString())
            initRecycler(it)
            initUI(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecycler(recipe: Recipe) {
        ingredientsAdapter = IngredientsAdapter(recipe.ingredients)
        val context = requireContext()
        val ingredientsLayoutManager = LinearLayoutManager(context)
        val decoration =
            MaterialDividerItemDecoration(context, ingredientsLayoutManager.orientation).apply {
                isLastItemDecorated = false
                dividerInsetStart = resources.getDimensionPixelSize(R.dimen.ingredient_margin)
                dividerInsetEnd = resources.getDimensionPixelSize(R.dimen.ingredient_margin)
                dividerColor = ContextCompat.getColor(context, R.color.divider_color)
            }

        binding.rvIngredients.layoutManager = LinearLayoutManager(context)
        binding.rvIngredients.adapter = ingredientsAdapter
        binding.rvIngredients.addItemDecoration(decoration)

        binding.rvMethod.layoutManager = LinearLayoutManager(context)
        binding.rvMethod.adapter = MethodAdapter(recipe.method)
        binding.rvMethod.addItemDecoration(decoration)
    }

    private fun initUI(recipe: Recipe) {
        binding.txTitleRecipe.text = recipe.title
        try {
            val assetManager = requireContext().assets
            val inputStream = assetManager.open(recipe.imageUrl.toString())
            val drawable = Drawable.createFromStream(inputStream, null)
            if (drawable != null) {
                binding.imgRecipe.setImageDrawable(drawable)
                binding.imgRecipe.contentDescription = recipe.title
                Log.d("RecipesListFragment", "Картинка успешно загружена")
            } else {
                Log.e("RecipesiesListFragment", "Drawable == null")
            }
        } catch (e: Exception) {
            Log.e("RecipesListFragment", "Ошибка загрузки картинки", e)
        }
        updateFavoriteIcon()
        binding.btnFavoriteAdd.setOnClickListener {
            toggleFavorite()
        }
    }

    private fun toggleFavorite() {
        val id = recipeId?.toString() ?: return
        val favorites = getFavorites()

        isFavorite = if (favorites.contains(id)) {
            favorites.remove(id)
            false
        } else {
            favorites.add(id)
            true
        }

        saveFavorites(favorites)
        updateFavoriteIcon()
    }

    private fun updateFavoriteIcon() {
        binding.btnFavoriteAdd.setImageResource(
            if (isFavorite) R.drawable.ic_heart_fill else R.drawable.ic_heart
        )
    }

    private fun saveFavorites(id: Set<String>) {
        val sharedPref = requireContext()
            .getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        sharedPref.edit {
            putStringSet(FAVORITES_KEY, id)
            apply()
        }
        Log.d("RecipeFragment", "Сохранённые избранные: $id")
    }

    private fun getFavorites(): MutableSet<String> {
        val pref = requireContext().getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        val favoriteSet = pref.getStringSet(FAVORITES_KEY, emptySet())
        Log.d("RecipeFragment", "Получение данных: $favoriteSet")
        return HashSet(favoriteSet)

    }

    companion object {
        const val MIN_PORTIONS = 1
        const val MAX_PORTIONS = 5
        const val SHARED_PREFS_NAME = "favorites_prefs"
        const val FAVORITES_KEY = "favorites_ids"
    }
}