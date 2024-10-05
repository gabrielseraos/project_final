package com.example.project

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project.databinding.ActivityItemManagementBinding

class ItemManagementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityItemManagementBinding
    private lateinit var adapter: AdapterShoppingItem
    private val shoppingItems = mutableListOf<ShoppingItem>()
    private var listId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityItemManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)


        listId = intent.getIntExtra("LIST_ID", -1)
        val items = intent.getSerializableExtra("ITEMS") as? List<ShoppingItem>
        items?.let {
            shoppingItems.addAll(it)
        }


        binding.recyclerViewItems.layoutManager = LinearLayoutManager(this)
        adapter = AdapterShoppingItem(shoppingItems, ::editItem, ::deleteItem, ::togglePurchased)
        binding.recyclerViewItems.adapter = adapter


        binding.buttonAddItem.setOnClickListener {
            val intent = Intent(this, AddItemActivity::class.java)
            startActivityForResult(intent, 1) // Código de requisição para adicionar item
        }


        binding.buttonSaveList.setOnClickListener {
            saveListAndReturn()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val itemId = data?.getIntExtra("ITEM_ID", -1) ?: return
            val name = data.getStringExtra("NAME") ?: ""
            val quantity = data.getIntExtra("QUANTITY", 1)
            val unit = data.getStringExtra("UNIT") ?: "un"
            val category = data.getStringExtra("CATEGORY") ?: "Outros"


            if (requestCode == 2) {
                val item = shoppingItems.find { it.id == itemId }
                item?.let {
                    it.name = name
                    it.quantity = quantity
                    it.unit = unit
                    it.category = category
                    adapter.notifyDataSetChanged()
                }
            } else if (requestCode == 1) {
                val newItem = ShoppingItem(shoppingItems.size + 1, name, quantity, unit, category)
                shoppingItems.add(newItem)
                adapter.notifyDataSetChanged()
            }
        }
    }


    private fun saveListAndReturn() {
        val resultIntent = Intent().apply {
            putExtra("LIST_ID", listId)
            putExtra("ITEMS", ArrayList(shoppingItems))
        }
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }


    private fun editItem(item: ShoppingItem) {
        val intent = Intent(this, AddItemActivity::class.java).apply {
            putExtra("ITEM_ID", item.id)
            putExtra("NAME", item.name)
            putExtra("QUANTITY", item.quantity)
            putExtra("UNIT", item.unit)
            putExtra("CATEGORY", item.category)
        }
        startActivityForResult(intent, 2)
    }

    private fun deleteItem(item: ShoppingItem) {
        shoppingItems.remove(item)
        adapter.notifyDataSetChanged()
    }

    private fun togglePurchased(item: ShoppingItem) {
        item.isPurchased = !item.isPurchased
        adapter.notifyDataSetChanged()
    }
}
