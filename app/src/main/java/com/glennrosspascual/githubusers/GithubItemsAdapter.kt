package com.glennrosspascual.githubusers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.glennrosspascual.githubusers.model.GithubUser
import com.glennrosspascual.githubusers.model.repository.PostNoteResult

class GithubItemsAdapter(var githubUsers: List<GithubUser>,
                         var onClickListener: GithubUserItemClickListener) :
    RecyclerView.Adapter<GithubUserViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GithubUserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.viewholder_github_item, parent, false)
        return GithubUserViewHolder(view, onClickListener)
    }

    override fun onBindViewHolder(holder: GithubUserViewHolder, position: Int, payloads: MutableList<Any>) {
        val reverse = (position + 1) % 4 == 0
        holder.bind(githubUsers[position], reverse, position)
    }

    override fun onBindViewHolder(holder: GithubUserViewHolder, position: Int) {
        val reverse = (position + 1) % 4 == 0
        holder.bind(githubUsers[position], reverse, position)
    }

    override fun getItemCount() = githubUsers.size

}