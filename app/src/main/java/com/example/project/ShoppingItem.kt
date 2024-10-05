package com.example.project

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

data class ShoppingItem(
    val id: Int,
    var name: String,
    var quantity: Int,
    var unit: String,
    var category: String,
    var isPurchased: Boolean = false
) : Serializable