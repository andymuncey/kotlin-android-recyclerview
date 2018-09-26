package com.tinyappco.shoplist

import java.io.Serializable

class ShoppingListItem(val name: String, var count: Int) : Serializable {

    var purchased : Boolean = false

}