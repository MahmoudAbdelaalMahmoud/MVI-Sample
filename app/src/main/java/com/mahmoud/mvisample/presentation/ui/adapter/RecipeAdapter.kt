package com.mahmoud.mvisample.presentation.ui.adapter

import android.view.ViewGroup
import androidx.annotation.IntDef
import androidx.recyclerview.widget.RecyclerView
import com.mahmoud.mvisample.domain.model.Recipe
import com.mahmoud.mvisample.util.propagateTo
import kotlin.properties.Delegates

class RecipeAdapter(private val actionHandler: IActionHandler) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var recipes by Delegates.observable(mutableListOf<Recipe>()) { _, old, new ->
        if (old != new)
            status = ItemListStatus.Normal
        propagateTo(old) {
            newItems = new
            compareItems = { old, new -> old.pk == new.pk }
        }
    }




    @ItemListStatus
    var status: Int = ItemListStatus.Normal
        set(value) {
            if (field == value) return
            if (value == ItemListStatus.Loading || value == ItemListStatus.Error) {
                if (field == ItemListStatus.Normal) {
                    field = value
                    notifyItemInserted(recipes.size)
                } else {
                    field = value
                    notifyItemChanged(recipes.size)
                }
            } else {
                field = value
                notifyItemRemoved(recipes.size)
            }
        }



    fun setData(newList: List<Recipe>) {
        status = ItemListStatus.Normal
        recipes = newList.toMutableList()
    }


    override fun getItemViewType(position: Int): Int {
        return if (position == recipes.size)
            status
        else ItemListStatus.Normal

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ItemListStatus.Normal -> RecipeViewHolder initializeWith parent andClickHandler {
                actionHandler.openRecipe(it)
            }
            ItemListStatus.Error -> {
                RecipeErrorViewHolder initializeWith parent andClickHandler { actionHandler.retry() }
            }
            else -> RecipeLoadingViewHolder initializeWith parent
        }
    }

    override fun getItemCount(): Int {
        return if (status == ItemListStatus.Normal) recipes.size
        else recipes.size + 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is RecipeViewHolder)
            holder.bind(recipes[position])
    }


}

@IntDef(ItemListStatus.Loading, ItemListStatus.Normal)
annotation class ItemListStatus {
    companion object {
        const val Loading = 2
        const val Error = 3
        const val Normal = 1
    }
}
