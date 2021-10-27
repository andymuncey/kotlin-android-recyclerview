package com.tinyappco.shoplist

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tinyappco.shoplist.databinding.FragmentListBinding
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class ShopListFragment : Fragment() {


    private lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var adapter: RecyclerAdapter

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }


    private fun loadList(){

        try {
            val fileInputStream = activity?.openFileInput("list.dat")
            val objectInputStream = ObjectInputStream(fileInputStream)

            @Suppress("UNCHECKED_CAST")
            val list = objectInputStream.readObject() as? MutableList<ShoppingListItem>
            if (list != null) {
                adapter.list = list
            }
            objectInputStream.close()
            fileInputStream?.close()
        }
        catch (e: java.io.FileNotFoundException){
            //loading has failed, probably first run
            Toast.makeText(activity,"No existing list found", Toast.LENGTH_LONG).show()
        }
    }

    override fun onPause() {
        super.onPause()
        saveList()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        saveList()
    }

    private fun saveList(){
        val fileOutputStream = activity?.openFileOutput("list.dat", Context.MODE_PRIVATE)
        val objectOutputStream = ObjectOutputStream(fileOutputStream)
        objectOutputStream.writeObject(adapter.list)

        objectOutputStream.close()
        fileOutputStream?.close()
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = RecyclerAdapter()
        binding.rvShoppingList.adapter = adapter

        layoutManager = LinearLayoutManager(activity)
        binding.rvShoppingList.layoutManager = layoutManager

        handleDragging()

        loadList()

    }


    private fun handleDragging(){
        val dragCallback = DragCallback()
        val touchHelper = ItemTouchHelper(dragCallback)
        touchHelper.attachToRecyclerView(binding.rvShoppingList)
    }


    inner class DragCallback : ItemTouchHelper.Callback() {
        override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
            val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
            return makeMovementFlags(dragFlags, ItemTouchHelper.LEFT)
        }

        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            val movedItem = adapter.list.removeAt(viewHolder.bindingAdapterPosition)
            adapter.list.add(target.bindingAdapterPosition, movedItem)
            adapter.notifyItemMoved(viewHolder.bindingAdapterPosition,target.bindingAdapterPosition)
            return true
        }

        override fun onSwiped(recyclerView: RecyclerView.ViewHolder, direction: Int) {
            adapter.list.removeAt(recyclerView.bindingAdapterPosition)
            adapter.notifyItemRemoved(recyclerView.bindingAdapterPosition)
        }
    }


}
