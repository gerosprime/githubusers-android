package com.glennrosspascual.githubusers

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.glennrosspascual.githubusers.databinding.ContentErrorBinding
import com.glennrosspascual.githubusers.databinding.ContentGithubuserDetailBinding
import com.glennrosspascual.githubusers.databinding.ContentLoadingBinding
import com.glennrosspascual.githubusers.databinding.FragmentGithubuserDetailBinding
import com.glennrosspascual.githubusers.model.GithubUser
import com.glennrosspascual.githubusers.model.Result
import com.glennrosspascual.githubusers.model.repository.PostNoteResult
import com.glennrosspascual.githubusers.viewmodel.GithubUserDetailViewModel
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class GithubUserDetailFragment : Fragment() {

    companion object {

        const val ARGUMENT_USER_LOGIN = "user_login"
        const val ARGUMENT_USER_LIST_INDEX = "user_index"

        fun createInstance(id : String, index : Int) : GithubUserDetailFragment {
            val arguments = Bundle()
            arguments.putString(ARGUMENT_USER_LOGIN, id)
            arguments.putInt(ARGUMENT_USER_LIST_INDEX, index)

            val instance = GithubUserDetailFragment()
            instance.arguments = arguments
            return instance
        }
    }

    private lateinit var binding : FragmentGithubuserDetailBinding
    private lateinit var bindingContent : ContentGithubuserDetailBinding
    private lateinit var bindingContentProgress : ContentLoadingBinding
    private lateinit var bindingContentError : ContentErrorBinding

    @Inject
    lateinit var factory : ViewModelProvider.Factory
    private lateinit var viewModel : GithubUserDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)

        val provider = ViewModelProvider(viewModelStore, factory)
        viewModel = provider.get(GithubUserDetailViewModel::class.java)

    }

    private fun getLoginString() = arguments?.getString(ARGUMENT_USER_LOGIN) ?: ""
    private fun getIndex() = arguments?.getInt(ARGUMENT_USER_LOGIN) ?: 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentGithubuserDetailBinding.inflate(inflater, container, false)
        bindingContent = binding.fragmentDetailContent
        bindingContentProgress = binding.fragmentDetailContentLoading
        bindingContentError = binding.fragmentDetailContentError

        with(bindingContent) {
            contentGithubuserDetailSave.setOnClickListener {
                val notes = contentGithubuserDetailNotes.editText?.text ?: ""
                viewModel.updateNotes(notes.toString(), getLoginString(), getIndex())
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.githubUserLiveData.observe(viewLifecycleOwner, {
            githubUserDetailLoadStateChanged(it)
        })
        viewModel.noteResultLiveData.observe(viewLifecycleOwner, {
            postUserNoteLoadStateChanged(it)
        })
    }

    private fun postUserNoteLoadStateChanged(result: Result<PostNoteResult>) {
        when (result) {
            is Result.Success -> {
                displayProgressIndicator(false)
                postUserNodeSuccess()
                displayContent(true)
            }
            is Result.Error -> {
                postUserNoteError()
            }
            is Result.Loading -> {
                postUserNoteAddProgress()
            }
        }
    }

    private fun postUserNoteAddProgress() {
        Toast.makeText(requireContext(), getString(R.string.post_user_note_progress), Toast.LENGTH_SHORT).show()
    }

    private fun postUserNodeSuccess() {
        Toast.makeText(requireContext(), R.string.post_user_note_success, Toast.LENGTH_SHORT).show()
    }

    private fun postUserNoteError() {
        Toast.makeText(requireContext(), getString(R.string.post_user_note_error), Toast.LENGTH_SHORT).show()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState == null) {
            val id = arguments?.getString(ARGUMENT_USER_LOGIN)

            if (id.isNullOrEmpty()) {
                throw IllegalArgumentException("Login name required")
            }

            viewModel.loadGithubUser(id)
        }
    }

    private fun githubUserDetailLoadStateChanged(result : Result<GithubUser>) {
        when (result) {
            is Result.Success -> {
                displayProgressIndicator(false)
                populateUserDetail(result.data)
                displayContent(true)
            }
            is Result.Error -> {
                displayErrorContent(true)
                displayContent(false)
                displayProgressIndicator(false)
                githubUserDetailLoadError(result.throwable)
            }
            is Result.Loading -> {
                displayProgressIndicator(true)
                displayContent(false)
                displayErrorContent(false)
            }
        }
    }

    private fun displayErrorContent(show: Boolean) {
        bindingContentError.apply {
            setVisibilities(if (show) View.VISIBLE else View.INVISIBLE, root)
        }
    }

    private fun githubUserDetailLoadError(error : Throwable) {
        bindingContentError.apply {
            contentErrorMessage.text = error.localizedMessage
        }
    }

    private fun displayProgressIndicator(show: Boolean) {
        bindingContentProgress.apply {
            setVisibilities(if (show) View.VISIBLE else View.INVISIBLE, root)
        }
    }

    private fun displayContent(show: Boolean) {
        bindingContent.apply {
            setVisibilities(if (show) View.VISIBLE else View.GONE, root)
        }
    }

    private fun populateUserDetail(data: GithubUser) {
        bindingContent.apply {
            contentGithubuserDetailFollowers.text = getString(R.string.followers_format, data.followers)
            contentGithubuserDetailFollowing.text = getString(R.string.following_format, data.following)
            contentGithubuserCompany.text = getString(R.string.company_format, data.company)
            contentGithubuserBlog.text = getString(R.string.blog_format, data.blog)
            Glide.with(this@GithubUserDetailFragment)
                .load(data.avatarUrl)
                .into(contentGithubuserDetailAvatar)
            contentGithubuserName.text = getString(R.string.name_format, data.name)

            val notes = data.notes ?: ""
            contentGithubuserDetailNotes.editText?.run {
                setText(notes)
            }
        }
    }

    private fun setVisibilities(visibility : Int, vararg views : View) {
        for (view in views) {
            view.visibility = visibility
        }
    }

}