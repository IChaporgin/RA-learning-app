package ru.ichaporgin.ralearningapp

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.ContextCompat
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
    private var isFavorite = true

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
        binding.btnFavoriteAdd.setOnClickListener {
            isFavorite = !isFavorite
            addToFavorite()
        }
        addToFavorite()
    }

    private fun addToFavorite(){
        val favoriteStatus = if (isFavorite) {
            R.drawable.ic_heart
        } else {
            R.drawable.ic_heart_fill
        }
        binding.btnFavoriteAdd.apply {
            setImageResource(favoriteStatus)
        }
    }

    companion object {
        const val MIN_PORTIONS = 1
        const val MAX_PORTIONS = 5
    }
}