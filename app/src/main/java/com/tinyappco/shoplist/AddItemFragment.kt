package com.tinyappco.shoplist

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_add_item.*

class AddItemFragment : Fragment() {

    private var addItemListener : AddItemFragmentListener? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is AddItemFragmentListener) {
            addItemListener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_item, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btnIncrementCount.setOnClickListener{
            incrementCount()
        }

        val enterHandler = EnterHandler()
        etItem.setOnEditorActionListener(enterHandler)
        etCount.setOnEditorActionListener(enterHandler)

        etItem.requestFocus()
        //display keyboard
        activity?.getWindow()?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
    }

    private fun incrementCount() {
        var userCount = etCount.text.toString().toIntOrNull()
        if (userCount == null) {
            userCount = 1
        }
        etCount.setText((userCount + 1).toString())
    }

    private fun validProductName()  : Boolean {
        return etItem.text.length > 0
    }

    private fun productCount() : Int {
        val userCount = etCount.text.toString().toIntOrNull()
        return if (userCount == null) 1 else userCount
    }

    interface AddItemFragmentListener {
        fun onItemAdded(item: ShoppingListItem)
    }

    inner class EnterHandler : TextView.OnEditorActionListener {
        override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
            //user has pressed tick button on soft keyboard, or pressed enter key
            if (actionId == EditorInfo.IME_ACTION_DONE || KeyEvent.KEYCODE_ENTER.equals(event?.keyCode)) {

                if (validProductName()) {

                    val product = ShoppingListItem(etItem.text.toString(),productCount())

                    addItemListener?.onItemAdded(product)

                    etItem.setText("")
                    etCount.setText("1")

                    //we have consumed (handled) this event (key press)
                    return true
                }
            }

            //we have not consumed this event (i.e. different key pressed or no valid product entered yet
            return false
        }
    }
}
