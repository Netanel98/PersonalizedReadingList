package com.example.readingbooks.views


import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.compose.ui.graphics.Color
import androidx.core.content.res.getColor
import androidx.core.content.res.getString
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.example.readinglist.R

class CustomTextInput @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.appcompat.R.attr.editTextStyle
) : LinearLayout(context, attrs, defStyleAttr) {

    private var textInputLayout: TextInputLayout
    private var textInputEditText: TextInputEditText

    var text: String
        get() = textInputEditText.text.toString()
        set(value) {
            textInputEditText.setText(value)
        }

    var helperText: String?
        get() = textInputLayout.helperText
        set(value) {
            textInputLayout.helperText = value
        }

    var helperTextEnabled: Boolean
        get() = textInputLayout.isHelperTextEnabled
        set(value) {
            textInputLayout.isHelperTextEnabled = value
        }

    private var hint: String
        get() = textInputLayout.hint.toString()
        set(value) {
            textInputLayout.hint = value
        }

    var helperTextColor: Int
        get() = textInputLayout.helperTextCurrentTextColor
        set(value) {
            textInputLayout.setHelperTextColor(ColorStateList.valueOf(value))
        }

    var inputType: Int
        get() = textInputEditText.inputType
        set(value) {
            textInputEditText.inputType = value
        }

    var lines: Int
        get() = textInputEditText.lines
        set(value) {
            textInputEditText.setLines(value)
        }

    init {
        orientation = VERTICAL // Ensure the LinearLayout is vertical
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.custom_text_input, this, true)

        textInputLayout = view.findViewById(R.id.text_input_layout)
        textInputEditText = view.findViewById(R.id.text_input_edit_text)

        context.theme.obtainStyledAttributes(attrs, R.styleable.CustomTextInput, defStyleAttr, 0).apply {
            try {
                hint = getString(R.styleable.CustomTextInput_android_hint) ?: ""
                helperText = getString(R.styleable.CustomTextInput_helperText)
                text = getString(R.styleable.CustomTextInput_android_text) ?: ""
                if (hasValue(R.styleable.CustomTextInput_helperTextColor)) {
                    helperTextColor = getColor(R.styleable.CustomTextInput_helperTextColor, Color(0))
                }
                inputType = getInt(R.styleable.CustomTextInput_android_inputType, inputType)
                lines = getInt(R.styleable.CustomTextInput_android_lines, lines)
            } finally {
                recycle()
            }
        }
    }
}