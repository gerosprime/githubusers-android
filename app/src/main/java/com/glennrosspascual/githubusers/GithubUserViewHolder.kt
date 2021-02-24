package com.glennrosspascual.githubusers

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.request.RequestOptions
import com.glennrosspascual.githubusers.model.GithubUser

class GithubUserViewHolder(itemView: View,
                           var onClickListener: GithubUserItemClickListener)
    : RecyclerView.ViewHolder(itemView) {

    private val userName: TextView = itemView.findViewById(R.id.viewholder_item_list_content_user)
    private val details: TextView = itemView.findViewById(R.id.viewholder_item_list_content_secondary)

    private val notes: ImageView = itemView.findViewById(R.id.viewholder_item_list_notes)
    private val userAvatar: ImageView = itemView.findViewById(R.id.viewholder_item_list_image)

    private val requestOptionsReverse = RequestOptions.errorOf(R.drawable.ic_baseline_person_24)
        .transform(ReverseBitmapTransformation())
        .placeholder(R.drawable.ic_baseline_person_24)

    private val requestOptionsDefault = RequestOptions.errorOf(R.drawable.ic_baseline_person_24)
        .placeholder(R.drawable.ic_baseline_person_24)

    fun bind(user : GithubUser, reverseImage : Boolean = false, position: Int) {
        itemView.setOnClickListener {
            onClickListener.onUserItemClicked(user, position)
        }
        userName.text = user.login
        details.text = user.notes


        val notesString = user.notes ?: ""
        if (notesString.isEmpty()) {
            notes.setImageDrawable(null)
        } else {
            notes.setImageResource(R.drawable.ic_baseline_notes_24)
        }
        if (reverseImage) {
            Glide.with(itemView)
                .setDefaultRequestOptions(requestOptionsReverse)
                .load(user.avatarUrl).into(userAvatar)
        } else {
            Glide.with(itemView)
                .setDefaultRequestOptions(requestOptionsDefault)
                .load(user.avatarUrl).into(userAvatar)
        }

    }
}