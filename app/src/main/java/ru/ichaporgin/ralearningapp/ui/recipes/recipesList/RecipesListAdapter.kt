package ru.ichaporgin.ralearningapp.ui.recipes.recipesList

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.ichaporgin.ralearningapp.R
import ru.ichaporgin.ralearningapp.data.Constants
import ru.ichaporgin.ralearningapp.databinding.ItemRecipesBinding
import ru.ichaporgin.ralearningapp.model.Recipe
import java.io.InputStream

class RecipesListAdapter() :
    RecyclerView.Adapter<RecipesListAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(categoryId: Int)
    }

    var dataSet: List<Recipe> = emptyList()
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
//            loadCategoryImage(recipe.imageUrl, holder)
            val imgRecipe = Constants.IMG_URL + recipe.imageUrl
            Glide.with(holder.itemView.context)
                .load(imgRecipe)
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

//    private fun loadCategoryImage(imageUrl: String, viewHolder: ViewHolder) {
//        try {
//            val inputStream: InputStream = viewHolder.itemView.context.assets.open(imageUrl)
//            val drawable = Drawable.createFromStream(inputStream, null)
//            with(viewHolder.binding) {
//                imgItemRecipe.setImageDrawable(drawable)
//            }
//        } catch (e: Exception) {
//            Log.e("!!!", "Load image error: $imageUrl", e)
//        }
//    }
}