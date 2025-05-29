package ru.ichaporgin.ralearningapp.ui.recipes.recipe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.ichaporgin.ralearningapp.databinding.ItemIngredientBinding
import ru.ichaporgin.ralearningapp.model.Ingredient
import java.math.BigDecimal
import java.math.RoundingMode

class IngredientsAdapter(
    private val dataset: List<Ingredient>,
    private var quantity: Int = 1
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
        val totalQuantity = BigDecimal(ingredient.quantity) * BigDecimal(quantity)
        val displayQuality = totalQuantity
            .setScale(1, RoundingMode.HALF_UP)
            .stripTrailingZeros()
            .toPlainString()

        with(holder.binding) {
            tvIngredientName.text = ingredient.description
            tvIngredientAmount.text = displayQuality
            tvIngredientUnit.text = ingredient.unitOfMeasure
        }
    }

    override fun getItemCount() = dataset.size

    fun updateIngredients(progress: Int) {
        quantity = progress
        notifyDataSetChanged()
    }
}