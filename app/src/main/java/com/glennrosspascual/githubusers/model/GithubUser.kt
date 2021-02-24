package com.glennrosspascual.githubusers.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(indices = [Index(value = ["login"], unique = true)])
data class GithubUser(@PrimaryKey var recordId : Int? = null,
                      val id : Int,
                      val login : String,
                      var name : String? = "",
                      var followers : Int = 0, var following : Int = 0,
                      var company : String? = "", var blog : String? = "",
                      @ColumnInfo(defaultValue = "") var notes : String? = "",
                      @SerializedName("avatar_url")
                      @ColumnInfo(defaultValue = "") var avatarUrl : String? = "",
                      @ColumnInfo(defaultValue = "html_url") var htmlUrl : String = "") {
    fun copyDetailsFrom(user: GithubUser) {
        user.let {
            this.name = it.name
            this.following = it.following
            this.followers = it.followers
            this.company = it.company
            this.blog = it.blog
            this.notes = it.notes
            this.avatarUrl = it.avatarUrl
            this.htmlUrl = it.htmlUrl
        }
    }
}