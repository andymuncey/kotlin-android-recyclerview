package com.tinyappco.shoplist

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), AddItemFragment.AddItemFragmentListener {

    override fun onItemAdded(item: ShoppingListItem) {
        listFrag.adapter.addItem(item)

        val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.hideSoftInputFromWindow(window.decorView.rootView.windowToken, 0)
    }

    private lateinit var listFrag: ShopListFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        listFrag = frList as ShopListFragment
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val addItem = menu?.findItem(R.id.menu_insert)
        if (isLandscape()) {
            addItem?.isVisible = false
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.clear_complete){
            listFrag.adapter.removeFoundItems()
            return true
        }
        if (item.itemId == R.id.menu_insert){
            addItem()
            return true
        }
        if  (item.itemId == R.id.action_settings){
            val intent = Intent(this,SettingsActivity::class.java)
            startActivity(intent)
        }


        return super.onOptionsItemSelected(item)
    }

    private fun addItem() {
        val intent = Intent(this,AddItemActivity::class.java)
        startActivityForResult(intent,0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val newItem = data?.getSerializableExtra("item") as ShoppingListItem
            listFrag.adapter.addItem(newItem)
        }
    }
}
