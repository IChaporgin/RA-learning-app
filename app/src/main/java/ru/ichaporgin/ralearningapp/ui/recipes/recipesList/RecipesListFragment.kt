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
import ru.ichaporgin.ralearningapp.R
import ru.ichaporgin.ralearningapp.data.NavigationArgs
import ru.ichaporgin.ralearningapp.databinding.FragmentRecipesListBinding
import ru.ichaporgin.ralearningapp.ui.category.CategoriesListFragmentDirections

class RecipesListFragment : Fragment() {
    private var _binding: FragmentRecipesListBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentRecipesListBinding must not to be null")
    private var categoryId: Int? = null
    private var categoryTitle: String? = null
    private var categoryImage: String? = null

    private val adapter = RecipesListAdapter()
    private val viewModel: RecipesListViewModel by viewModels()
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

//        val direction = CategoriesListFragmentDirections
//            .actionCategoriesListFragmentToRecipesListFragment(
//                categoryId = categ,
//
//            )
//        args.let {
//            categoryId = args.categoryId
//            categoryTitle = args.categoryName
//            categoryImage = args.categoryImageUrl
        }
//        arguments?.let { bundle ->
//            categoryId = bundle.getInt(NavigationArgs.ARG_CATEGORY_ID)
//            categoryTitle = bundle.getString(NavigationArgs.ARG_CATEGORY_NAME)
//            categoryImage = bundle.getString(NavigationArgs.ARG_CATEGORY_IMAGE_URL)
//        }

        binding.txTitleRecipes.text = getString(R.string.recipes_title_text, categoryTitle)

        categoryId?.let { viewModel.loadRecipes(it) }
        categoryImage?.let { viewModel.loadImageFromAssets(it) }
//        TODO: По кривому написано обращение к данным, пока не могу придумать, как сделать лаконичнее
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
        viewModel.recipesState.observe(viewLifecycleOwner) { state ->
            adapter.dataSet = state.recipes
            state.image?.let { drawable ->
                binding.imgRecipes.setImageDrawable(drawable)
            }
        }
    }
}