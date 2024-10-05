package com.example.project

import java.io.Serializable


data class ShoppingList(
    val id: Int,
    var title: String,
    var imageUri: String? = null,
    var items: MutableList<ShoppingItem> = mutableListOf()
) : Serializable


