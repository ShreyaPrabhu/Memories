package com.example.shreya.makememories

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shreya.makememories.room.MemoryEntity
import timber.log.Timber

class RecyclerAdapter: RecyclerView.Adapter<RecyclerAdapter.MemoryHolder>() {
    private var memoryEntitys: List<MemoryEntity> = ArrayList()
    private var listener: ((item: MemoryEntity) -> Unit)? = null
    private var onLonglistener: ((item: MemoryEntity) -> Unit)? = null

    fun setOnItemClickListener(listener: (item:MemoryEntity ) -> Unit) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoryHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.memory_item, parent, false)
        return MemoryHolder(itemView)
    }

    override fun onBindViewHolder(holder: MemoryHolder, position: Int) {
        val currentMemory = memoryEntitys[position]
        holder.textViewTitle.text = currentMemory.imageCaption
        holder.textViewDescription.text = currentMemory.imageDescription
        Timber.i("Image ref: " + currentMemory.imageReference.toString())
        holder.imageViewReference.setImageBitmap(BitmapFactory.decodeFile(currentMemory.imageReference));
    }

    override fun getItemCount(): Int {
        return memoryEntitys.size
    }

    fun setMemos(memos: List<MemoryEntity>) {
        this.memoryEntitys = memos
        notifyDataSetChanged()
    }

    inner class MemoryHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewTitle: TextView = itemView.findViewById(R.id.text_view_title)
        var textViewDescription: TextView = itemView.findViewById(R.id.text_view_description)
        var imageViewReference: ImageView = itemView.findViewById(R.id.image_reference)

        init {
            itemView.setOnClickListener { listener?.invoke(memoryEntitys[adapterPosition]) }
        }

    }
}