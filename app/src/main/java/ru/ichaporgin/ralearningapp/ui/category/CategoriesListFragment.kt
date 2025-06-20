package ru.ichaporgin.ralearningapp.ui.category

import CategoriesListAdapter
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
import androidx.recyclerview.widget.GridLayoutManager
import ru.ichaporgin.ralearningapp.R
import ru.ichaporgin.ralearningapp.data.NavigationArgs
import ru.ichaporgin.ralearningapp.data.STUB
import ru.ichaporgin.ralearningapp.databinding.FragmentListCategoriesBinding
import ru.ichaporgin.ralearningapp.ui.recipes.recipesList.RecipesListFragment

class CategoriesListFragment : Fragment() {
    private var _binding: FragmentListCategoriesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentListCategoriesBinding must not to be null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        try {
            val assetManager = requireContext().assets
            val inputStream = assetManager.open("categories.png")
            val drawable = Drawable.createFromStream(inputStream, null)
            if (drawable != null) {
                binding.imgCategory.setImageDrawable(drawable)
                Log.d("CategoriesListFragment", "Картинка успешно загружена")
            } else {
                Log.e("CategoriesListFragment", "Drawable == null")
            }
        } catch (e: Exception) {
            Log.e("CategoriesListFragment", "Ошибка загрузки картинки", e)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecycler() {
        Log.d("CategoriesListFragment", "initRecycler called")
        binding.rvCategories.layoutManager = GridLayoutManager(requireContext(), 2)
        val categoriesAdapter = CategoriesListAdapter(STUB.getCategories())
        binding.rvCategories.adapter = categoriesAdapter
        categoriesAdapter.setOnItemClickListener(object :
            CategoriesListAdapter.OnItemClickListener {
            override fun onItemClick(categoryId: Int) {
                openRecipesByCategoryId(categoryId)
            }
        })
    }

    private fun openRecipesByCategoryId(categoryId: Int) {
        val category = STUB.getCategories().find { it.id == categoryId }
        if (category == null) {
            Log.e("!!!", "Category $categoryId not found")
            return
        }
        val bundle = bundleOf(
            NavigationArgs.ARG_CATEGORY_ID to categoryId,
            NavigationArgs.ARG_CATEGORY_NAME to category.title,
            NavigationArgs.ARG_CATEGORY_IMAGE_URL to category.imageUrl
        )

        parentFragmentManager.commit {
            setReorderingAllowed(false)
            replace<RecipesListFragment>(R.id.mainContainer, args = bundle)
            addToBackStack(null)
        }
    }
}