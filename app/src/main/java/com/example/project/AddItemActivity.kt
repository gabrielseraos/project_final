package com.example.project

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.project.databinding.ActivityAddItemBinding

class AddItemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddItemBinding
    private var itemId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityAddItemBinding.inflate(layoutInflater)
        setContentView(binding.root)


        itemId = intent.getIntExtra("ITEM_ID", -1)
        val itemName = intent.getStringExtra("NAME")
        val itemQuantity = intent.getIntExtra("QUANTITY", 1)
        val itemUnit = intent.getStringExtra("UNIT")
        val itemCategory = intent.getStringExtra("CATEGORY")


        binding.editItemName.setText(itemName)
        binding.editItemQuantity.setText(itemQuantity.toString())
        binding.editItemUnit.setText(itemUnit)


        val categories = resources.getStringArray(R.array.categories_array)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategory.adapter = adapter


        itemCategory?.let { category ->
            val categoryPosition = categories.indexOf(category)
            if (categoryPosition >= 0) {
                binding.spinnerCategory.setSelection(categoryPosition)
            }
        }


        binding.buttonSaveItem.setOnClickListener {
            val name = binding.editItemName.text.toString()
            val quantity = binding.editItemQuantity.text.toString().toIntOrNull() ?: 1
            val unit = binding.editItemUnit.text.toString()
            val category = binding.spinnerCategory.selectedItem.toString()


            val resultIntent = Intent().apply {
                putExtra("ITEM_ID", itemId)
                putExtra("NAME", name)
                putExtra("QUANTITY", quantity)
                putExtra("UNIT", unit)
                putExtra("CATEGORY", category)
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}
