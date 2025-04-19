import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.ichaporgin.ralearningapp.Category
import ru.ichaporgin.ralearningapp.R
import java.io.InputStream

class CategoriesListAdapter(private val dataSet: List<Category>) :
    RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var imageView: ImageView = view.findViewById(R.id.imgItemCategory)
        var textItemView: TextView = view.findViewById(R.id.textItemCategoryName)
        var descriptionItemView: TextView = view.findViewById(R.id.textItemCategoryDescription)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val view = inflater.inflate(R.layout.item_category, viewGroup, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val category: Category = dataSet[position]
        viewHolder.textItemView.text = category.title
        viewHolder.descriptionItemView.text = category.description
        val inputStream: InputStream = viewHolder.imageView.context.assets.open(category.imageUrl)
        val drawable = Drawable.createFromStream(inputStream, null)
        viewHolder.imageView.setImageDrawable(drawable)

    }

    override fun getItemCount() = dataSet.size

}