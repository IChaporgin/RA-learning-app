package ru.ichaporgin.ralearningapp.ui.recipes.recipesList

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import ru.ichaporgin.ralearningapp.R
import ru.ichaporgin.ralearningapp.data.Constants
import ru.ichaporgin.ralearningapp.databinding.FragmentRecipesListBinding

@AndroidEntryPoint
class RecipesListFragment : Fragment() {
    private var _binding: FragmentRecipesListBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentRecipesListBinding must not to be null")
    private var categoryId: Int? = null
    private var categoryTitle: String? = null
    private var categoryImage: String? = null
    private val model: RecipesListViewModel by viewModels()
    private val adapter = RecipesListAdapter()
    private val args: RecipesListFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBundleData()
        initRecycler()
        initUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initBundleData() {
        categoryId = args.category.id
        categoryTitle = args.category.title
        categoryImage = Constants.IMG_URL + args.category.imageUrl

        categoryTitle?.let {
            binding.txTitleRecipes.text = getString(R.string.recipes_title_text, categoryTitle)
        }
        categoryId?.let { model.loadRecipes(it) }
        Glide.with(this)
            .load(categoryImage)
            .placeholder(R.drawable.img_placeholder)
            .error(R.drawable.img_error)
            .into(binding.imgRecipes)
    }

    private fun initRecycler() {
        Log.d("RecipesListFragment", "initRecycler called")
        binding.rvRecipes.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRecipes.adapter = adapter
        adapter.setOnItemClickListener(object :
            RecipesListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }
        })
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        val direction = RecipesListFragmentDirections
            .actionRecipesListFragmentToRecipeFragment(recipeId)
        parentFragmentManager.commit {
            findNavController().navigate(direction)
        }
    }

    private fun initUI() {
        model.recipesState.observe(viewLifecycleOwner) { state ->
            adapter.dataSet = state.recipes
        }
    }
}