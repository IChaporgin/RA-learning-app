package ru.ichaporgin.ralearningapp.ui.category

import CategoriesListAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import ru.ichaporgin.ralearningapp.databinding.FragmentListCategoriesBinding

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
        val category = model.getCategoryById(categoryId)
        if (category == null) {
            Log.e("CategoriesListFragment", "Категория не найдена: id=$categoryId")
            return
        }

        val direction = CategoriesListFragmentDirections
            .actionCategoriesListFragmentToRecipesListFragment(category)

        parentFragmentManager.commit {
            findNavController().navigate(direction)
        }
    }

    private fun initUI() {
        model.categoriesState.observe(viewLifecycleOwner) { state ->
            state.categoriesImage?.let {
                binding.imgCategory.setImageDrawable(it)
            }
            categoriesAdapter.dataSet = state.categories
        }
        model.loadData()
    }
}