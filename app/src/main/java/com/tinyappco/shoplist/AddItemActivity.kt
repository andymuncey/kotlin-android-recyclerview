package com.tinyappco.shoplist

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class AddItemActivity : AppCompatActivity(), AddItemFragment.AddItemFragmentListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)
    }

    override fun onItemAdded(item: ShoppingListItem) {
        val intent = Intent()
        intent.putExtra("item", item)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}
