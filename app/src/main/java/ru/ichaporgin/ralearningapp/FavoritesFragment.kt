package ru.ichaporgin.ralearningapp

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.recyclerview.widget.LinearLayoutManager
import ru.ichaporgin.ralearningapp.databinding.FragmentFavoritesBinding

class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentFavoritesBinding must not to be null")

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
        try {
            val assetManager = requireContext().assets
            val inputStream = assetManager.open("bcg_favorites.png")
            val drawable = Drawable.createFromStream(inputStream, null)
            if (drawable != null) {
                binding.imgFavorites.setImageDrawable(drawable)
                Log.d("CategoriesListFragment", "Картинка успешно загружена")
            } else {
                Log.e("CategoriesListFragment", "Drawable == null")
            }
        } catch (e: Exception) {
            Log.e("CategoriesListFragment", "Ошибка загрузки картинки", e)
        }
        initRecycle()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecycle() {
        Log.d("FavoritesFragment", "initRecycler called")
        binding.rvFavorites.layoutManager = LinearLayoutManager(requireContext())
        val favoriteRecipes = getFavorites()
        val favoritesAdapter = RecipesListAdapter(STUB.getRecipesByIds(favoriteRecipes))
        binding.rvFavorites.adapter = favoritesAdapter
        favoritesAdapter.setOnItemClickListener(object :
            RecipesListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }
        })
    }

    private fun getFavorites(): MutableSet<String> {
        val pref =
            requireContext().getSharedPreferences(Constants.SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        val favoriteSet = pref.getStringSet(Constants.FAVORITES_KEY, emptySet())
        Log.d("FavoriteFragment", "Получение данных: $favoriteSet")
        return HashSet(favoriteSet)
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        val recipe = STUB.getRecipeById(recipeId)
        if (recipe == null) {
            Log.e("!!!", "Recipe $recipeId not found")
            return
        }
        val bundle = bundleOf(NavigationArgs.ARG_RECIPE to recipe)
        parentFragmentManager.commit {
            setReorderingAllowed(false)
            replace<RecipeFragment>(R.id.mainContainer, args = bundle)
            addToBackStack(null)
        }
    }
}