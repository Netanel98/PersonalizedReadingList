package com.example.readingbooks.views


import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.withStyledAttributes
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.example.readinglist.R

class CustomTextInput @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.appcompat.R.attr.editTextStyle  // Ensure appcompat is imported correctly
) : LinearLayout(context, attrs, defStyleAttr) {

    private lateinit var textInputLayout: TextInputLayout
    private lateinit var textInputEditText: TextInputEditText

    var text: String
        get() = textInputEditText.text?.toString() ?: ""
        set(value) {
            textInputEditText.setText(value)
        }

    init {
        orientation = VERTICAL
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.custom_text_input, this, true)
        textInputLayout = view.findViewById(R.id.text_input_layout)
        textInputEditText = view.findViewById(R.id.text_input_edit_text)
        context.withStyledAttributes(attrs, R.styleable.CustomTextInput, defStyleAttr, 0).apply {
            hint = getString(R.styleable.CustomTextInput_android_hint) ?: ""
            helperText = getString(R.styleable.CustomTextInput_helperText)
            text = getString(R.styleable.CustomTextInput_android_text) ?: ""
        }
    }
}