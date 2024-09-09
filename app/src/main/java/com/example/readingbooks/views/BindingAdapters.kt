package com.example.readingbooks.views


import android.widget.EditText
import android.widget.ImageView
import androidx.core.widget.doOnTextChanged
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.lifecycle.LiveData
import com.bumptech.glide.Glide
import com.example.readinglist.R

@BindingAdapter("app:text")
fun setText(customTextInput: CustomTextInput, text: String?) {
    // Ensure that the text is only updated if it's different to avoid infinite loops
    if (customTextInput.getText() != text) {
        customTextInput.setText(text ?: "")
    }
}

@BindingAdapter("app:text")
fun setText(customTextInput: CustomTextInput, text: LiveData<String>) {
    text.value?.let {
        if (customTextInput.getText() != it) {
            customTextInput.setText(it)
        }
    }
}

@InverseBindingAdapter(attribute = "app:text")
fun getText(customTextInput: CustomTextInput): String {
    return customTextInput.getText() ?: ""
}

@BindingAdapter("app:textAttrChanged")
fun setTextWatcher(customTextInput: CustomTextInput, textAttrChanged: InverseBindingListener?) {
    customTextInput.findViewById<EditText>(R.id.text_input_edit_text)
        ?.doOnTextChanged { _, _, _, _ ->
            textAttrChanged?.onChange()
        }

    @BindingAdapter("helperTextEnabled")
    fun setHelperTextEnabled(customTextInput: CustomTextInput, enabled: Boolean) {
        customTextInput.helperTextEnabled = enabled
    }

    @InverseBindingAdapter(attribute = "helperTextEnabled")
    fun getHelperTextEnabled(customTextInput: CustomTextInput): Boolean {
        return customTextInput.helperTextEnabled
    }

    @BindingAdapter("helperTextEnabledAttrChanged")
    fun setHelperTextEnabledListener(
        customTextInput: CustomTextInput,
        attrChange: InverseBindingListener
    ) {
        customTextInput.findViewById<EditText>(R.id.text_input_edit_text)
            .doOnTextChanged { _, _, _, _ ->
                attrChange.onChange()
            }
    }

    @BindingAdapter("helperText")
    fun setHelperText(customTextInput: CustomTextInput, helperText: String?) {
        customTextInput.helperText = helperText ?: ""
    }

    @InverseBindingAdapter(attribute = "helperText")
    fun getHelperText(customTextInput: CustomTextInput): String? {
        return customTextInput.helperText
    }

    @BindingAdapter("helperTextAttrChanged")
    fun setHelperTextWatcher(
        customTextInput: CustomTextInput,
        helperTextAttrChanged: InverseBindingListener
    ) {
        customTextInput.findViewById<EditText>(R.id.text_input_edit_text)
            .doOnTextChanged { _, _, _, _ ->
                helperTextAttrChanged.onChange()
            }

        @BindingAdapter("imageUrl")
        fun setImageUrl(imageView: ImageView, url: String?) {
            if (url != null && url.isNotEmpty()) {
                Glide.with(imageView.context)
                    .load(url)
                    .into(imageView)
            } else {
                imageView.setImageDrawable(null)  // Optionally set a default image or placeholder here
            }
        }
    }
}