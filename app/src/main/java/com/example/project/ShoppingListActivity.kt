package com.example.project

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project.databinding.ActivityShoppingListBinding

class ShoppingListActivity : AppCompatActivity() {

    private lateinit var adapter: AdapterShoppingList
    private lateinit var binding: ActivityShoppingListBinding
    private val shoppingLists = mutableListOf<ShoppingList>()
    private val filteredLists = mutableListOf<ShoppingList>() // Lista filtrada

    companion object {
        const val REQUEST_CODE_ADD_LIST = 1
        const val REQUEST_CODE_EDIT_LIST = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityShoppingListBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = AdapterShoppingList(filteredLists, ::editList, ::deleteList)
        binding.recyclerView.adapter = adapter


        filteredLists.addAll(shoppingLists)


        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterListsAndItems(newText ?: "")
                return true
            }
        })


        binding.buttonAddList.setOnClickListener {
            val intent = Intent(this, AddListActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_ADD_LIST)
        }


        binding.buttonLogout.setOnClickListener {
            logoutUser()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_ADD_LIST && resultCode == Activity.RESULT_OK) {

            val title = data?.getStringExtra("TITLE")
            val imageUri = data?.getStringExtra("IMAGE_URI")

            if (title != null) {

                val newList = ShoppingList(shoppingLists.size + 1, title, imageUri)
                shoppingLists.add(newList)
                filteredLists.add(newList) // Adiciona à lista filtrada para exibição
                adapter.notifyDataSetChanged()
            }
        } else if (requestCode == REQUEST_CODE_EDIT_LIST && resultCode == Activity.RESULT_OK) {

            val listId = data?.getIntExtra("LIST_ID", -1) ?: return
            val updatedItems = data?.getSerializableExtra("ITEMS") as? List<ShoppingItem>

            if (updatedItems != null) {
                val list = shoppingLists.find { it.id == listId }
                list?.let {
                    it.items.clear()
                    it.items.addAll(updatedItems)
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }


    private fun filterListsAndItems(query: String) {
        filteredLists.clear()


        for (list in shoppingLists) {
            if (list.title.contains(query, ignoreCase = true)) {
                filteredLists.add(list)
            } else {

                val matchingItems = list.items.filter { it.name.contains(query, ignoreCase = true) }
                if (matchingItems.isNotEmpty()) {

                    val filteredList = list.copy(items = matchingItems.toMutableList())
                    filteredLists.add(filteredList)
                }
            }
        }


        adapter.notifyDataSetChanged()
    }


    private fun editList(shoppingList: ShoppingList) {
        val intent = Intent(this, ItemManagementActivity::class.java).apply {
            putExtra("LIST_ID", shoppingList.id)
            putExtra("ITEMS", ArrayList(shoppingList.items))
        }
        startActivityForResult(intent, REQUEST_CODE_EDIT_LIST)
    }


    private fun deleteList(shoppingList: ShoppingList) {
        shoppingLists.remove(shoppingList)
        filteredLists.remove(shoppingList)
        adapter.notifyDataSetChanged()
    }


    private fun logoutUser() {
        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()

        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
