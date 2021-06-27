package com.mahmoud.mvisample.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mahmoud.mvisample.R
import com.mahmoud.mvisample.databinding.StateViewBinding

class RecipeStateAdapter(private val handler: IActionHandler) :
    LoadStateAdapter<UserStateViewHolder>() {
    override fun onBindViewHolder(holder: UserStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): UserStateViewHolder =
        UserStateViewHolder.create(parent, handler)

}

class UserStateViewHolder(
    private val binding: StateViewBinding,
    handler: IActionHandler,
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.actionHandler = handler
    }

    fun bind(loadState: LoadState) {
        binding.vs = loadState.toViewState()
    }

    companion object {
        fun create(parent: ViewGroup, handler: IActionHandler): UserStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.state_view, parent, false)
            val binding = StateViewBinding.bind(view)
            return UserStateViewHolder(binding, handler)
        }
    }
}

data class ViewState(
    val loading: Boolean,
    val error: Throwable? = null,
    val isEmpty: Boolean = false,
)

fun LoadState.toViewState() = ViewState(
    loading = this is LoadState.Loading,
    error = if (this is LoadState.Error) this.error else null
)

fun CombinedLoadStates.toViewState(itemCount: Int) = ViewState(
    loading = refresh is LoadState.Loading,
    error = if (refresh is LoadState.Error) (refresh as? LoadState.Error)?.error
    else null,
    isEmpty = refresh is LoadState.NotLoading && itemCount == 0
)

