package ru.ichaporgin.ralearningapp

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.ichaporgin.ralearningapp.databinding.FragmentListCategoriesBinding

class CategoriesListFragment: Fragment() {
    private var _binding : FragmentListCategoriesBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding for FragmentListCategoriesBinding must not to be null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
}