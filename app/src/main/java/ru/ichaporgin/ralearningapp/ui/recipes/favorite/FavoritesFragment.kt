package ru.ichaporgin.ralearningapp.ui.recipes.favorite

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ru.ichaporgin.ralearningapp.RecipesApplication
import ru.ichaporgin.ralearningapp.databinding.FragmentFavoritesBinding
import ru.ichaporgin.ralearningapp.ui.recipes.recipesList.RecipesListAdapter

class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentFavoritesBinding must not to be null")

    private lateinit var model: FavoritesViewModel
    private val adapter = RecipesListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appContainer = (requireActivity().application as RecipesApplication).appContainer
        model = appContainer.favoritesViewModelFactory.create()
    }

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
        loadImageFromAssets()
        initRecycle()
        initUI()
        model.loadData()
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
        val direction = FavoritesFragmentDirections
            .actionFavoritesFragmentToRecipeFragment(recipeId)
        parentFragmentManager.commit {
            findNavController().navigate(direction)
        }
    }

    private fun initUI() {
        model.selectedFavorites.observe(viewLifecycleOwner) { state ->
            adapter.dataSet = state.recipes

            if (state.recipes.isEmpty()) {
                binding.tvRecipesEmpty.visibility = View.VISIBLE
                binding.rvFavorites.visibility = View.GONE
            } else {
                binding.tvRecipesEmpty.visibility = View.GONE
                binding.rvFavorites.visibility = View.VISIBLE
            }
        }
    }

    private fun loadImageFromAssets() {
        try {
            val inputStream = requireContext().assets.open("bcg_favorites.png")
            val drawable = Drawable.createFromStream(inputStream, null)
            binding.imgFavorites.setImageDrawable(drawable)
        } catch (e: Exception) {
            Log.e("FavoritesListFragment", "Ошибка загрузки картинки из assets", e)
        }
    }
}