package com.tinyappco.shoplist

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class MainActivity : AppCompatActivity() {

    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var adapter: RecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        layoutManager = LinearLayoutManager(this)
        rvShoppingList.layoutManager = layoutManager

        adapter = RecyclerAdapter()
        rvShoppingList.adapter = adapter

        handleDragging()

        loadList()

    }

    private fun saveList(){
        val fileOutputStream = openFileOutput("list.dat", Context.MODE_PRIVATE)
        val objectOutputStream = ObjectOutputStream(fileOutputStream)
        objectOutputStream.writeObject(adapter.list)
        objectOutputStream.close()
        fileOutputStream.close()
    }

    private fun loadList(){

        try {
            val fileInputStream = openFileInput("list.dat")
            val objectInputStream = ObjectInputStream(fileInputStream)

            @Suppress("UNCHECKED_CAST")
            val list = objectInputStream.readObject() as? MutableList<ShoppingListItem>
            if (list != null) {
                adapter.list = list
            }
            objectInputStream.close()
            fileInputStream.close()
        }
        catch (e: java.io.FileNotFoundException){
            //loading has failed, probably first run
            Toast.makeText(this,"No existing list found", Toast.LENGTH_LONG).show()
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        saveList()
    }

    override fun onPause() {
        super.onPause()
        saveList()
    }

    private fun handleDragging(){
        val dragCallback = DragCallback()
        val touchHelper = ItemTouchHelper(dragCallback)
        touchHelper.attachToRecyclerView(rvShoppingList)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if (item?.itemId == R.id.clear_complete){
            adapter.removeFoundItems()
            return true
        }
        if (item?.itemId == R.id.menu_insert){
            addItem()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun addItem() {
        val intent = Intent(this,AddItemActivity::class.java)
        startActivityForResult(intent,0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val newItem = data?.getSerializableExtra("item") as ShoppingListItem
            adapter.addItem(newItem)
        }
    }

    inner class DragCallback : ItemTouchHelper.Callback() {
        override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
            val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
            return makeMovementFlags(dragFlags, 0)
        }

        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            adapter.notifyItemMoved(viewHolder.adapterPosition,target.adapterPosition)
            return true
        }

        override fun onSwiped(recyclerView: RecyclerView.ViewHolder, direction: Int) {

        }

    }


}
