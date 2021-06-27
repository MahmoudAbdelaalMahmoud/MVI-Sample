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
import com.mahmoud.mvisample.presentation.ui.adapter.RecipeStateAdapter
import com.mahmoud.mvisample.presentation.ui.adapter.toViewState
import com.mahmoud.mvisample.util.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class RecipeListFragment : Fragment(), IActionHandler {
    lateinit var binding: RecipeListFragmentView
    private val recipeViewModel: RecipeViewModel by viewModels()
    private val adapter = RecipeAdapter(this@RecipeListFragment)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        with(RecipeListFragmentView.inflate(layoutInflater, container, false)) {
            binding = this
            binding.rvRecipesList.adapter =
                adapter.withLoadStateHeaderAndFooter(
                    header = RecipeStateAdapter(this@RecipeListFragment),
                    footer = RecipeStateAdapter(this@RecipeListFragment)
                )
            viewModel = recipeViewModel
            lifecycleOwner = viewLifecycleOwner
            actionHandler = this@RecipeListFragment
            return root
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // observe single event
        recipeViewModel.partialState
            .onEach { Logger.d("partial") }
            .launchIn(lifecycleScope)

        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.isRefreshing = false
            adapter.refresh()
        }
        adapter.addLoadStateListener { loadState ->
            binding.vs = loadState.toViewState(adapter.itemCount)
        }
        intents()
            .onEach { recipeViewModel.processIntent(it) }
            .launchIn(lifecycleScope)

        binding.rvRecipesList.layoutManager = object : LinearLayoutManager(requireContext()) {
            override fun supportsPredictiveItemAnimations(): Boolean = false
        }


    }

    private fun intents() = merge(
        flowOf(RecipeListActions.Initial)
    )

    override fun openRecipe(item: Recipe) {

    }

    override fun retry() {
        adapter.retry()
    }
}