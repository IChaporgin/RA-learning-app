package ru.ichaporgin.ralearningapp.ui.recipes.recipe

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import ru.ichaporgin.ralearningapp.R
import ru.ichaporgin.ralearningapp.data.Constants
import ru.ichaporgin.ralearningapp.data.NavigationArgs
import ru.ichaporgin.ralearningapp.databinding.FragmentRecipeBinding
import ru.ichaporgin.ralearningapp.model.Recipe

class RecipeFragment : Fragment() {
    private var _binding: FragmentRecipeBinding? = null
    private var ingredientsAdapter: IngredientsAdapter? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentRecipeBinding must not to be null")
    private val model: RecipeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var recipeId = arguments?.getInt(NavigationArgs.ARG_RECIPE_ID)
        recipeId?.let { id ->
            model.loadRecipe(id)
        }
        binding.seekBar.max = Constants.MAX_PORTIONS
        binding.seekBar.min = Constants.MIN_PORTIONS

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
        initUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecycler(recipe: Recipe?) {
        if (recipe == null) return
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

        binding.rvIngredients.apply {
            this.layoutManager = LinearLayoutManager(context)
            adapter = ingredientsAdapter
            addItemDecoration(decoration)
        }

        binding.rvMethod.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = MethodAdapter(recipe.method)
            addItemDecoration(decoration)
        }
    }

    private fun initUI() {

        model.selectedRecipe.observe(viewLifecycleOwner) { state ->
            val recipe = state.recipe
            val portion = state.portion
            val isFavorite = state.isFavorite

            binding.txTitleRecipe.text = recipe?.title

            if (ingredientsAdapter == null) {
                initRecycler(recipe)
            }

            try {
                val assetManager = requireContext().assets
                val inputStream = assetManager.open(recipe?.imageUrl.toString())
                val drawable = Drawable.createFromStream(inputStream, null)

                if (drawable != null) {
                    binding.imgRecipe.setImageDrawable(drawable)
                    binding.imgRecipe.contentDescription = recipe?.title
                    Log.d("RecipesListFragment", "Картинка успешно загружена")
                } else {
                    Log.e("RecipesiesListFragment", "Drawable == null")
                }
            } catch (e: Exception) {
                Log.e("RecipesListFragment", "Ошибка загрузки картинки", e)
            }

            ingredientsAdapter?.updateIngredients(portion)
            binding.portions.text = getString(R.string.portions, portion)
            binding.seekBar.progress = portion
            updateFavoriteIcon(isFavorite)
            binding.btnFavoriteAdd.setOnClickListener {
                model.onFavoritesClicked()
            }
        }
    }

    private fun updateFavoriteIcon(isFavorite: Boolean) {
        binding.btnFavoriteAdd.setImageResource(
            if (isFavorite) R.drawable.ic_heart_fill else R.drawable.ic_heart
        )
    }
}