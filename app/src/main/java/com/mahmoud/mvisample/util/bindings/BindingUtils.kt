package com.mahmoud.mvisample.util.bindings

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingConversion
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mahmoud.mvisample.util.Logger
@BindingConversion
fun visibility(visible: Boolean) = if (visible) View.VISIBLE else View.GONE

@BindingConversion
fun visibility(visible: Throwable?) = if (visible == null) View.GONE else View.VISIBLE



@BindingMethods(
    value = [
        BindingMethod(
            type = RecyclerView::class,
            attribute = "adapter",
            method = "setAdapter",
        )
    ]
)
object BindingUtils

    @BindingAdapter(value = ["imageUri", "placeholder"], requireAll = false)
    fun ImageView.showImageWithUri(imageUri: String?, placeholder: Drawable? = null) {
        when (imageUri) {
            null -> {
                Logger.d("UnSetting Image Url")
                Glide.with(this)
                    .load(placeholder)
                    .into(this)
            }
            else -> {

                Glide.with(this)
                    .load(imageUri)
                    .into(this)
            }
        }
    }



