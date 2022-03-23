package me.ajiew.core.binding

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.databinding.BindingAdapter
import me.ajiew.core.util.messenger.BindingCommand


@BindingAdapter(value = ["textChanged"], requireAll = false)
fun addTextChangedListener(editText: EditText, textChanged: BindingCommand<String>) {
    editText.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(text: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(text: CharSequence, start: Int, count: Int, after: Int) {
            textChanged.execute(text.toString())
        }

        override fun afterTextChanged(editable: Editable) {}
    })
}