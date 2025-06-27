package ru.ichaporgin.ralearningapp.ui.recipes.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import ru.ichaporgin.ralearningapp.R
import ru.ichaporgin.ralearningapp.data.Constants
import ru.ichaporgin.ralearningapp.databinding.FragmentRecipeBinding

class RecipeFragment : Fragment() {
    private var _binding: FragmentRecipeBinding? = null
    private var ingredientsAdapter: IngredientsAdapter? = null
    private var methodAdapter: MethodAdapter? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentRecipeBinding must not to be null")
    private val viewModel: RecipeViewModel by viewModels()
    private val args: RecipeFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recipeId = args.recipeId
        recipeId.let { id ->
            viewModel.loadRecipe(id)
        }

        binding.seekBar.min = Constants.MIN_PORTIONS
        binding.seekBar.max = Constants.MAX_PORTIONS

        binding.seekBar.setOnSeekBarChangeListener(
            PortionSeekBarListener { progress ->
                viewModel.updatePortion(progress)
            }
        )

        initAdapter()
        initUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUI() {
        viewModel.selectedRecipe.observe(viewLifecycleOwner) { state ->
            val recipe = state.recipe
            val portion = state.portionCount
            val isFavorite = state.isFavorite
            val text = state.recipe?.title

            state.recipeImage?.let { drawable ->
                binding.imgRecipe.setImageDrawable(drawable)
                binding.imgRecipe.contentDescription = recipe?.title
            }

            ingredientsAdapter?.dataset = recipe?.ingredients ?: emptyList()
            ingredientsAdapter?.updateIngredients(portion)

            methodAdapter?.dataset = recipe?.method ?: emptyList()

            binding.portions.text = getString(R.string.portions, portion)

            updateFavoriteIcon(isFavorite)
            binding.btnFavoriteAdd.setOnClickListener {
                viewModel.onFavoritesClicked()
            }
            binding.txTitleRecipe.text = text
        }
    }

    private fun initAdapter() {
        ingredientsAdapter = IngredientsAdapter()
        methodAdapter = MethodAdapter()
        val context = requireContext()
        val ingredientsLayoutManager = LinearLayoutManager(context)
        val decoration =
            MaterialDividerItemDecoration(
                context,
                ingredientsLayoutManager.orientation
            ).apply {
                isLastItemDecorated = false
                dividerInsetStart =
                    resources.getDimensionPixelSize(R.dimen.ingredient_margin)
                dividerInsetEnd =
                    resources.getDimensionPixelSize(R.dimen.ingredient_margin)
                dividerColor = ContextCompat.getColor(context, R.color.divider_color)
            }
        binding.rvIngredients.apply {
            this.layoutManager = LinearLayoutManager(context)
            adapter = ingredientsAdapter
            addItemDecoration(decoration)
        }

        binding.rvMethod.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = methodAdapter
            addItemDecoration(decoration)
        }

    }

    private fun updateFavoriteIcon(isFavorite: Boolean) {
        binding.btnFavoriteAdd.setImageResource(
            if (isFavorite) R.drawable.ic_heart_fill else R.drawable.ic_heart
        )
    }
}

internal class PortionSeekBarListener(val onChangeIngredients: (Int) -> Unit) :
    SeekBar.OnSeekBarChangeListener {
    override fun onProgressChanged(
        p0: SeekBar?,
        progress: Int,
        p2: Boolean
    ) {
        onChangeIngredients(progress)
    }

    override fun onStartTrackingTouch(p0: SeekBar?) {}
    override fun onStopTrackingTouch(p0: SeekBar?) {}
}