package uz.example.apppinterest.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import uz.example.apppinterest.R
import uz.example.apppinterest.database.Saved

class SavedPhotoAdapter() :
    RecyclerView.Adapter<SavedPhotoAdapter.SavedPhotoVH>() {

    private var photos: ArrayList<Saved> = ArrayList()
    lateinit var photoClick: ((Saved) -> Unit)

    inner class SavedPhotoVH(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(photo: Saved) {
            val ivHomePhoto: ImageView = view.findViewById(R.id.ivHomePhoto)
            val tvHomePhotoTitle: TextView = view.findViewById(R.id.tvHomePhotoTitle)

            Picasso.get()
                .load(photo.url)
                .into(ivHomePhoto)

            if (photo.description.isNotBlank()) {
                tvHomePhotoTitle.text = photo.description
            }

            ivHomePhoto.setOnClickListener {
                photoClick.invoke(photo)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedPhotoVH {
        return SavedPhotoVH(
            LayoutInflater.from(parent.context).inflate(R.layout.item_home_photo, parent, false)
        )
    }

    override fun onBindViewHolder(holder: SavedPhotoVH, position: Int) {
        holder.bind(getItem(position))

    }

    fun submitData(list: List<Saved>) {
        photos.addAll(list)
        notifyDataSetChanged()
    }

    private fun getItem(position: Int): Saved = photos[position]

    override fun getItemCount(): Int = photos.size
}