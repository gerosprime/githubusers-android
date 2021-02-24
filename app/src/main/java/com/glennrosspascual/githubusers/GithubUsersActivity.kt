package com.glennrosspascual.githubusers

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.glennrosspascual.githubusers.databinding.*
import com.glennrosspascual.githubusers.model.GithubUser
import com.glennrosspascual.githubusers.model.Result
import com.glennrosspascual.githubusers.model.repository.PostNoteResult
import com.glennrosspascual.githubusers.viewmodel.GithubUsersListViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.android.AndroidInjection
import retrofit2.HttpException
import javax.inject.Inject

class GithubUsersActivity : AppCompatActivity() {

    private var twoPane: Boolean = false

    @Inject
    lateinit var viewModelFactory : ViewModelProvider.Factory
    private lateinit var viewModel: GithubUsersListViewModel

    private lateinit var viewBinding : ActivityItemListBinding
    private lateinit var viewBindingContent : ContentGithubuserListBinding
    private lateinit var viewBindingError : ContentErrorBinding
    private lateinit var viewBindingLoading : ContentLoadingBinding

    private val onClickListener = object : GithubUserItemClickListener {
        override fun onUserItemClicked(githubUser: GithubUser, index: Int)
                = openDetailScreen(githubUser, index)
    }

    private lateinit var searchAdapter : GithubItemsAdapter
     private var mainAdapter = GithubItemsAdapter(listOf(), onClickListener)

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        viewBinding = ActivityItemListBinding.inflate(layoutInflater)
        viewBinding.run {
            viewBindingContent = activityItemListContent
            viewBindingError = activityItemListContentError
            viewBindingLoading = activityItemListContentLoading
        }

        with(viewBindingContent) {
            contentGithubListRecyclerview.addItemDecoration(
                DividerItemDecoration(
                    this@GithubUsersActivity,
                            DividerItemDecoration.VERTICAL)
            )
        }

        with(viewBindingError.contentErrorButton) {
            text = getText(R.string.error_button_retry)
            setOnClickListener { viewModel.loadGithubUsers(0) }
        }

        setContentView(viewBinding.root)

        viewModel = ViewModelProvider(this, viewModelFactory)[GithubUsersListViewModel::class.java]


        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = title

        // The detail container view will be present only in the
        // large-screen layouts (res/values-w900dp).
        // If this view is present, then the
        // activity should be in two-pane mode.
        twoPane = findViewById<NestedScrollView>(R.id.item_detail_container) != null

        searchAdapter = GithubItemsAdapter(listOf(), onClickListener)
        mainAdapter = GithubItemsAdapter(listOf(), onClickListener)
        viewBindingContent.contentGithubListRecyclerview.adapter = mainAdapter

        viewBindingContent.contentGithubListRecyclerview.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if(recyclerView.adapter ==  mainAdapter) {
                        loadMoreIfScrollAtBottom(recyclerView)
                    }
                    super.onScrolled(recyclerView, dx, dy)
                }
            })

        observeViewModels()
        if (savedInstanceState == null) {
            viewModel.loadGithubUsers(0)
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater = menuInflater
        inflater.inflate(R.menu.activity_github_users, menu)

        val menuItem = menu?.findItem(R.id.action_search)
        (menuItem?.actionView as SearchView).apply {
            // Assumes current activity is the searchable activity
            setIconifiedByDefault(true)
            setOnCloseListener { attachMainAdapter() }
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?) = performSearch(query ?: "")
                override fun onQueryTextChange(newText: String?): Boolean = true
            })
        }

        return true

    }

    private fun attachSearchAdapter() : Boolean {
        with(viewBindingContent) {
            contentGithubListRecyclerview.adapter = searchAdapter
        }

        with(viewModel) {
            itemsLiveData.removeObservers(this@GithubUsersActivity)
            searchResultLiveData.observe(this@GithubUsersActivity, { searchUsersStateChanged(it) })
        }

        return true
    }

    private fun attachMainAdapter() : Boolean {
        with(viewBindingContent) {
            contentGithubListRecyclerview.adapter = mainAdapter
        }
        with(viewModel) {
            searchResultLiveData.removeObservers(this@GithubUsersActivity)
            itemsLiveData.observe(this@GithubUsersActivity, { githubUsersLoadStateChanged(it) })
        }
        return true
    }

    private fun isInSearchMode() : Boolean {
        with(viewBindingContent) {
            return contentGithubListRecyclerview.adapter == searchAdapter
        }
    }

    private fun performSearch(query : String) : Boolean {
        attachSearchAdapter()
        viewModel.searchOnUser(query)
        return true
    }

    private fun observeViewModels() {
        viewModel.itemsLiveData.observe(this, { githubUsersLoadStateChanged(it) })
        viewModel.noteResultLiveData.observe(this, { postNoteStateChanged(it) })
        viewModel.searchResultLiveData.observe(this, {
            searchUsersStateChanged(it)
        })
    }

    private fun searchUsersStateChanged(result: Result<List<GithubUser>>) {
        if (!isInSearchMode()) {
            return
        }
        when(result) {
            is Result.Loading -> { seachUsersLoading() }
            is Result.Success -> { searchUsersSuccess(result.data) }
            is Result.Error -> { searchUsersError(result.throwable) }
            is Result.Loaded -> {}
        }

    }

    private fun seachUsersLoading() {
        githubUsersLoading(true)
    }

    private fun searchUsersError(error: Throwable) {
        with(viewBindingError) {
            contentErrorMessage.text = getString(R.string.search_generic_error)
        }
    }

    private fun searchUsersSuccess(items: List<GithubUser>) {
        githubUsersLoading(false)
        showGithubItems(false)
        showGithubItems(true)
        searchAdapter.githubUsers = items
        searchAdapter.notifyDataSetChanged()
    }

    private fun postNoteStateChanged(result: Result<PostNoteResult>) {
        when(result) {
            is Result.Loading -> {  }
            is Result.Success -> { postNoteSuccessful(result.data) }
            is Result.Error -> { postNoteError(result.throwable) }
            is Result.Loaded -> {}
        }
    }

    private fun postNoteSuccessful(result: PostNoteResult) {
        val adapter = viewBindingContent.contentGithubListRecyclerview.adapter as GithubItemsAdapter?
        adapter?.run {
            if (result.listIndex < adapter.itemCount && result.listIndex > -1) {
                notifyItemChanged(result.listIndex, result)
            }
        }
    }

    private fun githubUsersNextItemsLoaded(data: List<GithubUser>) {
        showGithubItemsMoreLoading(false)
        showGithubItemsError(false)

        // Reuse and update the adapter
        val adapter = viewBindingContent.contentGithubListRecyclerview.adapter as GithubItemsAdapter?
        adapter?.run {
            githubUsers = data
            notifyDataSetChanged()
        }

    }

    private fun githubItemsLoadMoreError(error: Throwable) {
        showGithubItemsMoreLoading(false)
        Snackbar.make(viewBinding.activityItemListCoordinator,
            R.string.load_more_error, Snackbar.LENGTH_SHORT).show()
    }

    private fun showGithubItemsMoreLoading(show: Boolean) {
        viewBindingContent.contentGithubListLoadmoreProgress.visibility =
            if (show) View.VISIBLE else View.INVISIBLE
    }

    private fun githubUsersLoadStateChanged(result : Result<List<GithubUser>>) {

        when (viewBindingContent.contentGithubListRecyclerview.adapter?.itemCount) {
            0 -> {
                when(result) {
                    is Result.Loading -> { githubUsersLoading(true) }
                    is Result.Loaded -> { githubUsersLoaded(result.data) }
                    is Result.Success -> { githubUsersLoadComplete(result.data) }
                    is Result.Error -> { postNoteError(result.throwable) }
                }
            }
             else -> {
                 when(result) {
                     is Result.Loading -> { showGithubItemsMoreLoading(true) }
                     is Result.Loaded -> githubUsersNextItemsLoaded(result.data)
                     is Result.Success -> { githubUsersNextItemLoadComplete() }
                     is Result.Error -> { githubItemsLoadMoreError(result.throwable) }
                 }
             }
        }

    }

    private fun githubUsersNextItemLoadComplete() {
        showGithubItemsMoreLoading(false)
    }

    private fun githubUsersLoadComplete(items : List<GithubUser>) {
        githubUsersLoading(false)
        showGithubItems(true)
        showGithubItemsError(false)
        showGithubItemsMoreLoading(false)
        // Reuse and update the adapter
        val adapter = viewBindingContent.contentGithubListRecyclerview.adapter as GithubItemsAdapter?
        adapter?.run {
            githubUsers = items
            notifyDataSetChanged()
        }
    }

    private fun githubUsersLoading(show: Boolean) {
        showGithubItemsError(false)
        showGithubItems(false)
        viewBindingLoading.root.visibility = if (show) View.VISIBLE else View.INVISIBLE
    }

    private fun showGithubItems(show : Boolean) {
        viewBindingContent.contentGithubListRecyclerview.visibility = if (show) View.VISIBLE else View.INVISIBLE
    }

    private fun githubUsersLoaded(items : List<GithubUser>) {

        viewModel.updatePageSize(
            resources.getDimension(R.dimen.list_item_height).toInt(),
            getContentHeight())

        val adapter = viewBindingContent.contentGithubListRecyclerview.adapter as GithubItemsAdapter

        if (adapter.itemCount == 0) {
            githubUsersLoading(false)
            showGithubItemsError(false)
            showGithubItemsMoreLoading(true)
            showGithubItems(true)
        }
        // Reuse and update the adapter
        adapter.run {
            githubUsers = items
            notifyDataSetChanged()
        }

    }

    private fun getContentHeight(): Int {
        return viewBindingContent.root.height
    }

    private fun loadMoreIfScrollAtBottom(recyclerView: RecyclerView) {

        val layoutManager = recyclerView.layoutManager as LinearLayoutManager

        val totalItemCount = layoutManager.itemCount
        val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

        if (totalItemCount - 1 <= lastVisibleItem) {
            val adapter = recyclerView.adapter as GithubItemsAdapter?
            val currentItems = adapter?.githubUsers ?: listOf()
            val lastItem = if (currentItems.isEmpty()) 0
                else currentItems[currentItems.size - 1].id
            viewModel.loadGithubUsers(lastItem, currentItems)
        }

    }

    private fun postNoteError(error : Throwable) {
        with(viewBindingError) {
            when (error) {
                is HttpException -> {
                    contentErrorMessage.text = error.localizedMessage
                }
                else -> {
                    contentErrorMessage.text = getString(R.string.technical_error_message)
                }
            }
        }

        githubUsersLoading(false)
        showGithubItems(false)
        showGithubItemsError(true)
    }

    private fun showGithubItemsError(show : Boolean) {
        viewBindingError.root.visibility = if (show) View.VISIBLE else View.INVISIBLE
    }

    private fun openDetailScreen(githubUser: GithubUser, index: Int) {
        val loginId = githubUser.login
        if (twoPane) {
            val fragment = GithubUserDetailFragment.createInstance(loginId, index)
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.item_detail_container, fragment)
                .commit()
        } else {
            val intent = Intent(this@GithubUsersActivity,
                GithubUserDetailActivity::class.java).apply {
                putExtra(GithubUserDetailFragment.ARGUMENT_USER_LOGIN, loginId)
                putExtra(GithubUserDetailFragment.ARGUMENT_USER_LIST_INDEX, index)
            }
            startActivity(intent)
        }
    }

}