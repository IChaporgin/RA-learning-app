package ru.ichaporgin.ralearningapp

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.ichaporgin.ralearningapp.databinding.ItemRecipesBinding
import java.io.InputStream

class RecipesListAdapter(private val dataSet: List<Recipe>) : RecyclerView.Adapter<RecipesListAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(categoryId: Int)
    }

    private var itemClickListener: OnItemClickListener? = null
    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    class ViewHolder (val binding: ItemRecipesBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecipesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = dataSet.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val recipe: Recipe = dataSet[position]
            with(holder.binding) {
                textItemRecipeName.text = recipe.title
                loadCategoryImage(recipe.imageUrl, holder)
                imgItemRecipe.contentDescription =
                    holder.itemView.context.getString(
                        ru.ichaporgin.ralearningapp.R.string.category_image_description,
                        recipe.title
                    )
                root.setOnClickListener { itemClickListener?.onItemClick(recipe.id) }

            }
        } catch (e: Exception) {
            Log.e("!!!", "File error!!!", e)
        }
        holder.binding.imgItemRecipe
    }
    private fun loadCategoryImage(imageUrl: String, viewHolder: ViewHolder) {
        try {
            val inputStream: InputStream = viewHolder.itemView.context.assets.open(imageUrl)
            val drawable = Drawable.createFromStream(inputStream, null)
            with(viewHolder.binding) {
                imgItemRecipe.setImageDrawable(drawable)
            }
        } catch (e: Exception) {
            Log.e("!!!", "Load image error: $imageUrl", e)
        }
    }
}