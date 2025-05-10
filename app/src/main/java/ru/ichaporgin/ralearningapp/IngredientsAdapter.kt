package ru.ichaporgin.ralearningapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.ichaporgin.ralearningapp.databinding.ItemIngredientBinding

class IngredientsAdapter(private val dataset: List<Ingredient>) :
    RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemIngredientBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding =
            ItemIngredientBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ingredient = dataset[position]
        with(holder.binding) {
            tvIngredientName.text = ingredient.description
            tvIngredientAmount.text = ingredient.quantity
            tvIngredientUnit.text = ingredient.unitOfMeasure
        }
    }

    override fun getItemCount() = dataset.size

}