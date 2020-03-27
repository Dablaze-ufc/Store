package com.irobot.myapplication.data

data class CheckOutItems(val finalCart : MutableList<CartItem>,
                         val phoneNumber: String,
                         val id: String,
                         val price: String,
                         val areItemsDelivered: Boolean = false)