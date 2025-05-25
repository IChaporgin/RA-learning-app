package ru.ichaporgin.ralearningapp.ui.recipes.recipe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.ichaporgin.ralearningapp.databinding.ItemMethodBinding

class MethodAdapter(private val dataset: List<String>) :
    RecyclerView.Adapter<MethodAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemMethodBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemMethodBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val method = dataset[position]
        with(holder.binding) {
            txIngredientMethod.text = method.toString()
        }
    }

    override fun getItemCount() = dataset.size
}