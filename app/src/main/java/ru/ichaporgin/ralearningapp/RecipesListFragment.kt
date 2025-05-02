package ru.ichaporgin.ralearningapp

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.ichaporgin.ralearningapp.databinding.FragmentRecipesListBinding

class RecipesListFragment : Fragment() {
    private var _binding: FragmentRecipesListBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentRecipesListBinding must not to be null")
    private var categoryId: Int? = null
    private var categoryTitle: String? = null
    private var categoryImage: String? = null


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
        binding.txTitleRecipes.text = view.context.getString(R.string.recipes_title_text, categoryTitle)

        try {
            val assetManager = requireContext().assets
            val inputStream = assetManager.open(categoryImage.toString())
            val drawable = Drawable.createFromStream(inputStream, null)
            if (drawable != null) {
                binding.imgRecipes.setImageDrawable(drawable)
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

    private fun initBundleData() {
        arguments?.let { bundle ->
            categoryId = bundle.getInt(NavigationArgs.ARG_CATEGORY_ID)
            categoryTitle = bundle.getString(NavigationArgs.ARG_CATEGORY_NAME)
            categoryImage = bundle.getString(NavigationArgs.ARG_CATEGORY_IMAGE_URL)
        }
    }

}
