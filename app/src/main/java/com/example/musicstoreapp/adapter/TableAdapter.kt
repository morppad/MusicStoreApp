package com.example.musicstoreapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musicstoreapp.R

class TableAdapter(private val data: List<Map<String, Any>>) :
    RecyclerView.Adapter<TableAdapter.TableViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TableViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_table_row, parent, false)
        return TableViewHolder(view)
    }

    override fun onBindViewHolder(holder: TableViewHolder, position: Int) {
        val row = data[position]
        holder.textView.text = row.entries.joinToString { "${it.key}: ${it.value}" }
    }

    override fun getItemCount(): Int = data.size

    class TableViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.textRow)
    }
}
