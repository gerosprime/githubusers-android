package com.glennrosspascual.githubusers

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.glennrosspascual.githubusers.databinding.ActivityItemDetailBinding


class GithubUserDetailActivity : AppCompatActivity() {

    private lateinit var binding : ActivityItemDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.detailToolbar)

        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            val id = intent.getStringExtra(GithubUserDetailFragment.ARGUMENT_USER_LOGIN)
            val index = intent.getIntExtra(GithubUserDetailFragment.ARGUMENT_USER_LIST_INDEX, 0)
            val fragment = GithubUserDetailFragment.createInstance(id ?: "", index)

            supportFragmentManager.beginTransaction()
                    .add(R.id.item_detail_container, fragment)
                    .commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

}