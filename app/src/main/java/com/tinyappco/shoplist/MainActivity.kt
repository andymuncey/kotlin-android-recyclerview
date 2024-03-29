package com.tinyappco.shoplist

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.tinyappco.shoplist.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), AddItemFragment.AddItemFragmentListener {

    override fun onItemAdded(item: ShoppingListItem) {
        listFrag.adapter.addItem(item)

        val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.hideSoftInputFromWindow(window.decorView.rootView.windowToken, 0)
    }

    private lateinit var listFrag: ShopListFragment
    private lateinit var binding: ActivityMainBinding

    val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val newItem = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.data?.getSerializableExtra("item",ShoppingListItem::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.data?.getSerializableExtra("item") as ShoppingListItem

            }
            if (newItem != null) {
                listFrag.adapter.addItem(newItem)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setSupportActionBar(binding.toolbar)

        //listFrag = binding.frList as ShopListFragment //this doesn't work - maybe due to different parent view in each layout
        listFrag = supportFragmentManager.findFragmentById(R.id.frList) as ShopListFragment
        //todo: find out how to do this with view binding



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
        resultLauncher.launch(intent)
    }

}
