package ru.ichaporgin.ralearningapp.ui.recipes.favorite

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ru.ichaporgin.ralearningapp.R
import ru.ichaporgin.ralearningapp.data.NavigationArgs
import ru.ichaporgin.ralearningapp.databinding.FragmentFavoritesBinding
import ru.ichaporgin.ralearningapp.ui.recipes.recipesList.RecipesListAdapter

class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentFavoritesBinding must not to be null")

    private val viewModel: FavoritesViewModel by viewModels()
    private val adapter = RecipesListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycle()
        initUI()
        viewModel.loadData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecycle() {
        Log.d("FavoritesFragment", "initRecycler called")
        binding.rvFavorites.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFavorites.adapter = adapter
        adapter.setOnItemClickListener(object :
            RecipesListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }
        })
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        val bundle = bundleOf(NavigationArgs.ARG_RECIPE_ID to recipeId)
        parentFragmentManager.commit {
            findNavController().navigate(R.id.recipeFragment, bundle)
        }
    }

    private fun initUI() {
        viewModel.selectedFavorites.observe(viewLifecycleOwner) { state ->
            adapter.dataSet = state.recipes

            if (state.recipes.isEmpty()) {
                binding.tvRecipesEmpty.visibility = View.VISIBLE
                binding.rvFavorites.visibility = View.GONE
            } else {
                binding.tvRecipesEmpty.visibility = View.GONE
                binding.rvFavorites.visibility = View.VISIBLE
            }

            state.imageFavorite?.let {
                binding.imgFavorites.setImageDrawable(it)
            }
        }
    }
}