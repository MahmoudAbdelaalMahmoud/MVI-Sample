package com.mahmoud.mvisample.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mahmoud.mvisample.databinding.RecipeListFragmentView
import com.mahmoud.mvisample.domain.model.Recipe
import com.mahmoud.mvisample.domain.mvi.RecipeListActions
import com.mahmoud.mvisample.presentation.mvi_flow.RecipeViewModel
import com.mahmoud.mvisample.presentation.ui.adapter.IActionHandler
import com.mahmoud.mvisample.presentation.ui.adapter.RecipeAdapter
import com.mahmoud.mvisample.util.*
import com.mahmoud.mvisample.util.safeOffer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class RecipeListFragment : Fragment(), IActionHandler {
    private lateinit var paginator: RecyclerPaginator
    lateinit var binding: RecipeListFragmentView
    private val recipeViewModel: RecipeViewModel by viewModels()

    private val loadMoreChannel = Channel<Int>(Channel.BUFFERED)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        with(RecipeListFragmentView.inflate(layoutInflater, container, false)) {
            binding = this
            viewModel = recipeViewModel
            lifecycleOwner = viewLifecycleOwner
            actionHandler = this@RecipeListFragment
            adapter = RecipeAdapter(this@RecipeListFragment)
            return root
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // observe single event
        recipeViewModel.partialState
            .onEach { Logger.d("partial")}
            .launchIn(lifecycleScope)

        intents()
            .onEach { recipeViewModel.processIntent(it) }
            .launchIn(lifecycleScope)

        binding.rvRecipesList.layoutManager = object : LinearLayoutManager(requireContext()) {
            override fun supportsPredictiveItemAnimations(): Boolean = false
        }
        paginator = RecyclerPaginator(binding.rvRecipesList,
            { recipeViewModel.isLoadMoreDisabled() },
            { page ->
                loadMoreChannel.safeOffer(page)
            })

    }

    private fun intents() = merge(
        flowOf(RecipeListActions.Initial),
        binding.swipeRefreshLayout.refreshes().map { RecipeListActions.Refresh },
        loadMoreChannel.consumeAsFlow().map { RecipeListActions.LoadMore(it) }
    )

    override fun openRecipe(item: Recipe) {

    }

    override fun retry() {
        loadMoreChannel.safeOffer(paginator.currentPage)
    }
}