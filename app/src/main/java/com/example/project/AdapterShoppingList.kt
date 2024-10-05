package com.example.project

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterShoppingList(
    private val shoppingLists: MutableList<ShoppingList>,
    private val onEdit: (ShoppingList) -> Unit,
    private val onDelete: (ShoppingList) -> Unit
) : RecyclerView.Adapter<AdapterShoppingList.ShoppingListViewHolder>() {

    inner class ShoppingListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.textTitle)
        val imageView: ImageView = view.findViewById(R.id.imageView)
        val editButton: Button = view.findViewById(R.id.buttonEdit)
        val deleteButton: Button = view.findViewById(R.id.buttonDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_shopping_list, parent, false)
        return ShoppingListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShoppingListViewHolder, position: Int) {
        val shoppingList = shoppingLists[position]
        holder.titleTextView.text = shoppingList.title


        if (shoppingList.imageUri.isNullOrEmpty()) {
            holder.imageView.setImageResource(R.drawable.placeholder_image)
        } else {
            holder.imageView.setImageURI(Uri.parse(shoppingList.imageUri))
        }


        holder.editButton.setOnClickListener {
            val intent = Intent(holder.itemView.context, ItemManagementActivity::class.java).apply {
                putExtra("LIST_ID", shoppingList.id)
                putExtra("LIST_TITLE", shoppingList.title)
            }
            holder.itemView.context.startActivity(intent)
        }

        holder.editButton.setOnClickListener {
            onEdit(shoppingList)
        }


        holder.deleteButton.setOnClickListener {
            onDelete(shoppingList)
            notifyItemRemoved(position)
        }
    }

    override fun getItemCount(): Int = shoppingLists.size


    fun removeShoppingList(shoppingList: ShoppingList) {
        val position = shoppingLists.indexOf(shoppingList)
        if (position >= 0) {
            shoppingLists.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}
