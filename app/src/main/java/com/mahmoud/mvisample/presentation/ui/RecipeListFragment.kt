package com.mahmoud.mvisample.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mahmoud.mvisample.databinding.RecipeListFragmentView
import com.mahmoud.mvisample.domain.model.Recipe
import com.mahmoud.mvisample.domain.mvi.RecipeListActions
import com.mahmoud.mvisample.domain.mvi.RecipeListPartialState
import com.mahmoud.mvisample.domain.mvi.ViewState
import com.mahmoud.mvisample.presentation.vm.RecipeViewModel
import com.mahmoud.mvisample.presentation.ui.adapter.IActionHandler
import com.mahmoud.mvisample.presentation.ui.adapter.RecipeAdapter
import com.mahmoud.mvisample.util.*
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class RecipeListFragment : Fragment(), IActionHandler {
    private lateinit var paginator: RecyclerPaginator
    lateinit var binding: RecipeListFragmentView
    private val recipeViewModel: RecipeViewModel by viewModels()
    private val loadMorePublisher = PublishSubject.create<Int>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        with(RecipeListFragmentView.inflate(layoutInflater, container, false)) {
            binding = this
            viewState = recipeViewModel.initialState
            lifecycleOwner = viewLifecycleOwner
            actionHandler = this@RecipeListFragment
            adapter = RecipeAdapter(this@RecipeListFragment)
            return root
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // observe single event
        recipeViewModel.partialStatPublisher
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::render) {

            }
        recipeViewModel.states()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::render) { Logger.e(it) }

        intents().apply(recipeViewModel::processIntents)

        binding.rvRecipesList.layoutManager = object : LinearLayoutManager(requireContext()) {
            override fun supportsPredictiveItemAnimations(): Boolean = false
        }
        paginator = RecyclerPaginator(binding.rvRecipesList,
            loadMore = { page ->
                loadMorePublisher.onNext(page)
            })

    }

    private fun render(vs: ViewState) {
        paginator.isLoadmoreDisabled = { vs.isLoadMoreDisabled() }
        binding.viewState = vs
    }

    private fun render(partialState: RecipeListPartialState) {

    }

    private fun intents() = Observable.merge(
        Observable.just(RecipeListActions.Initial()),
        binding.swipeRefreshLayout.rxRefreshes().map { RecipeListActions.Refresh() },
        loadMorePublisher.map { RecipeListActions.LoadMore(it) }
    )

    override fun openRecipe(item: Recipe) {
    }

    override fun retry() {
        loadMorePublisher.onNext(paginator.currentPage)
    }
}