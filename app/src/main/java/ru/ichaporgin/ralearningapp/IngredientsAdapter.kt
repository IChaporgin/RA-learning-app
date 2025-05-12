package ru.ichaporgin.ralearningapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.ichaporgin.ralearningapp.databinding.ItemIngredientBinding

class IngredientsAdapter(
    private val dataset: List<Ingredient>,
    private var portions: Int = 1
) :
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
        val amount = ingredient.quantity.toDoubleOrNull()
        val newPortions = if (amount != null) {
            (amount * portions).toString().removeSuffix(".0")
        } else {
            ingredient.quantity
        }

        with(holder.binding) {
            tvIngredientName.text = ingredient.description
            tvIngredientAmount.text = newPortions.toString()
            tvIngredientUnit.text = ingredient.unitOfMeasure
        }
    }

    override fun getItemCount() = dataset.size

    fun updatePortions(newPortions: Int) {
        portions = newPortions
        notifyDataSetChanged()
    }

}