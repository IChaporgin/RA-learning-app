import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.ichaporgin.ralearningapp.model.Category
import ru.ichaporgin.ralearningapp.R
import ru.ichaporgin.ralearningapp.databinding.ItemCategoryBinding
import java.io.InputStream

class CategoriesListAdapter(private val dataSet: List<Category>) :
    RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(categoryId: Int)
    }

    private var itemClickListener: OnItemClickListener? = null
    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    class ViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root)

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        try {
            val category: Category = dataSet[position]
            with(viewHolder.binding) {
                textItemCategoryName.text = category.title
                textItemCategoryDescription.text = category.description
                loadCategoryImage(category.imageUrl, viewHolder)
                imgItemCategory.contentDescription =
                    viewHolder.itemView.context.getString(
                        R.string.recipe_image_description,
                        category.title
                    )
                root.setOnClickListener { itemClickListener?.onItemClick(category.id) }

            }
        } catch (e: Exception) {
            Log.e("!!!", "File error!!!", e)
        }
        viewHolder.binding.imgItemCategory
    }

    override fun getItemCount() = dataSet.size


    private fun loadCategoryImage(imageUrl: String, viewHolder: ViewHolder) {
        try {
            val inputStream: InputStream = viewHolder.itemView.context.assets.open(imageUrl)
            val drawable = Drawable.createFromStream(inputStream, null)
            with(viewHolder.binding) {
                imgItemCategory.setImageDrawable(drawable)
            }
        } catch (e: Exception) {
            Log.e("!!!", "Load image error: $imageUrl", e)
        }
    }
}