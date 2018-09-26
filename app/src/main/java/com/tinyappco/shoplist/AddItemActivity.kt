package com.tinyappco.shoplist

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_add_item.*

class AddItemActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        btnIncrementCount.setOnClickListener{
            incrementCount()
        }

        val enterHandler = EnterHandler()
        etItem.setOnEditorActionListener(enterHandler)
        etCount.setOnEditorActionListener(enterHandler)

        etItem.requestFocus()
        //display keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
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

    inner class EnterHandler : TextView.OnEditorActionListener {
        override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
            //user has pressed tick button on soft keyboard, or pressed enter key
            if (actionId == EditorInfo.IME_ACTION_DONE || KeyEvent.KEYCODE_ENTER.equals(event?.keyCode)) {

                if (validProductName()) {

                    val product = ShoppingListItem(etItem.text.toString(),productCount())

                    val intent = Intent()
                    intent.putExtra("item", product)
                    setResult(Activity.RESULT_OK, intent)
                    finish()

                    //we have consumed (handled) this event (key press)
                    return true
                }
            }

            //we have not consumed this event (i.e. different key pressed or no valid product entered yet
            return false
        }
    }
}
