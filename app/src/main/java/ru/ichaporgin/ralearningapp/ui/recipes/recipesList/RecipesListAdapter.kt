package ru.ichaporgin.ralearningapp.ui.recipes.recipesList

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.ichaporgin.ralearningapp.R
import ru.ichaporgin.ralearningapp.data.Constants
import ru.ichaporgin.ralearningapp.databinding.ItemRecipesBinding
import ru.ichaporgin.ralearningapp.model.Recipe

class RecipesListAdapter() :
    RecyclerView.Adapter<RecipesListAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(categoryId: Int)
    }

    var dataSet: List<Recipe> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private var itemClickListener: OnItemClickListener? = null
    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    class ViewHolder(val binding: ItemRecipesBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecipesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = dataSet.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe: Recipe = dataSet[position]
        with(holder.binding) {
            textItemRecipeName.text = recipe.title
            val imgRecipe = Constants.IMG_URL + recipe.imageUrl
            Glide.with(holder.itemView.context)
                .load(imgRecipe)
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_error)
                .into(holder.binding.imgItemRecipe)
            imgItemRecipe.contentDescription =
                holder.itemView.context.getString(
                    R.string.recipe_image_description,
                    recipe.title
                )
            root.setOnClickListener { itemClickListener?.onItemClick(recipe.id) }
        }
        holder.binding.imgItemRecipe
    }
}