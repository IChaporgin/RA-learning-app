package ru.ichaporgin.ralearningapp.ui.category

import CategoriesListAdapter
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ru.ichaporgin.ralearningapp.databinding.FragmentListCategoriesBinding

@AndroidEntryPoint
class CategoriesListFragment : Fragment() {
    private var _binding: FragmentListCategoriesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentListCategoriesBinding must not to be null")

    private val model: CategoriesListViewModel by viewModels()
    private val categoriesAdapter = CategoriesListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadImageFromAssets()
        initRecycler()
        initUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecycler() {
        Log.d("CategoriesListFragment", "initRecycler called")
        binding.rvCategories.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvCategories.adapter = categoriesAdapter
        categoriesAdapter.setOnItemClickListener(object :
            CategoriesListAdapter.OnItemClickListener {
            override fun onItemClick(categoryId: Int) {
                openRecipesByCategoryId(categoryId)
            }
        })
    }

    private fun openRecipesByCategoryId(categoryId: Int) {
        model.getCategoryById(categoryId) { category ->
            if (category != null) {
                val direction = CategoriesListFragmentDirections
                    .actionCategoriesListFragmentToRecipesListFragment(category)
                findNavController().navigate(direction)
            } else {
                Log.e("CategoriesListFragment", "Отсутствует категория:: $categoryId")
                Toast.makeText(
                    requireContext(),
                    "Категория не найдена",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun initUI() {
        model.categoriesState.observe(viewLifecycleOwner) { state ->
            loadImageFromAssets()
            categoriesAdapter.dataSet = state.categories
            if (state.isError) {
                Toast.makeText(requireContext(), "Ошибка в загрузке категорий!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        model.loadData()
    }

    private fun loadImageFromAssets() {
        try {
            val inputStream = requireContext().assets.open("categories.png")
            val drawable = Drawable.createFromStream(inputStream, null)
            binding.imgCategory.setImageDrawable(drawable)
        } catch (e: Exception) {
            Log.e("CategoriesListFragment", "Ошибка загрузки картинки из assets", e)
        }
    }
}