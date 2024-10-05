package com.example.project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterShoppingItem(
    private val shoppingItems: MutableList<ShoppingItem>,
    private val onEdit: (ShoppingItem) -> Unit,
    private val onDelete: (ShoppingItem) -> Unit,
    private val onTogglePurchased: (ShoppingItem) -> Unit
) : RecyclerView.Adapter<AdapterShoppingItem.ShoppingItemViewHolder>() {

    inner class ShoppingItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.textName)
        val quantityTextView: TextView = view.findViewById(R.id.textQuantity)
        val purchasedCheckbox: CheckBox = view.findViewById(R.id.checkboxPurchased)
        val editButton: Button = view.findViewById(R.id.buttonEdit)
        val deleteButton: Button = view.findViewById(R.id.buttonDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_shopping_item, parent, false)
        return ShoppingItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShoppingItemViewHolder, position: Int) {
        val shoppingItem = shoppingItems[position]
        holder.nameTextView.text = shoppingItem.name
        holder.quantityTextView.text = "${shoppingItem.quantity} ${shoppingItem.unit}"
        holder.purchasedCheckbox.isChecked = shoppingItem.isPurchased




        holder.purchasedCheckbox.setOnCheckedChangeListener { _, isChecked ->
            onTogglePurchased(shoppingItem.apply { isPurchased = isChecked })
        }

        holder.editButton.setOnClickListener { onEdit(shoppingItem) }
        holder.deleteButton.setOnClickListener { onDelete(shoppingItem) }
    }

    override fun getItemCount(): Int = shoppingItems.size
}