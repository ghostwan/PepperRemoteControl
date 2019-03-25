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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class AppAdapter(
    private val onClick: suspend (App) -> Unit,
    private val onLongClick: suspend (App) -> Unit, private val ui: CoroutineScope
) : ListAdapter<App, AppAdapter.Holder>(AppDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_app, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position), onClick, onLongClick, ui)
    }

    class Holder(row: View) : RecyclerView.ViewHolder(row) {
        fun bind(
            app: App,
            onClick: suspend (App) -> Unit,
            onLongClick: suspend (App) -> Unit,
            ui: CoroutineScope
        ) {
            itemView.name.text = app.name
            itemView.image.setImageDrawable(app.drawable)
            itemView.setOnClickListener { ui.launch { onClick(app) } }
            itemView.setOnLongClickListener {
                ui.launch {
                    onLongClick(app)
                }
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