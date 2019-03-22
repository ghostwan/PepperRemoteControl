package com.ghostwan.pepperremote.ui.launcher

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ghostwan.pepperremote.R
import com.ghostwan.pepperremote.data.model.App
import kotlinx.android.synthetic.main.row_app.view.*

class AppAdapter(private val onClick: (App) -> Unit,
                 private val onLongClick: (App) -> Unit) : ListAdapter<App, AppAdapter.Holder>(AppDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_app, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position), onClick, onLongClick)
    }

    class Holder(row: View): RecyclerView.ViewHolder(row) {
        fun bind(app: App, onClick: (App) -> Unit, onLongClick: (App) -> Unit) {
            itemView.name.text = app.name
            itemView.image.setImageDrawable(app.drawable)
            itemView.setOnClickListener { onClick(app) }
            itemView.setOnLongClickListener {
                onLongClick(app)
                true
            }
        }
    }
}

class AppDiffCallback : DiffUtil.ItemCallback<App>() {
    override fun areItemsTheSame(oldItem: App, newItem: App): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: App, newItem: App): Boolean {
        return oldItem == newItem
    }
}