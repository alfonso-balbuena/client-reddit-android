package com.alfonso.clientreddit.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.alfonso.clientreddit.R
import com.alfonso.clientreddit.databinding.FragmentPostDetailBinding
import com.alfonso.clientreddit.viewModel.MainViewModel
import timber.log.Timber


class PostDetail : Fragment() {

    private lateinit var binding : FragmentPostDetailBinding
    private val viewModelShared : MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_post_detail,container,false)

        viewModelShared.postSelected.observe(viewLifecycleOwner, {
            viewModelShared.readPost(it)
            binding.post = it
            binding.executePendingBindings()
        })

        binding.imagePost.setOnClickListener {
            binding.post?.link?.let {
                val uri = "https://reddit.com$it"
                Timber.d("Opening... $uri")
                val webView = Intent(Intent.ACTION_VIEW)
                webView.data = Uri.parse(uri)
                startActivity(webView)
            }

        }
        return binding.root
    }

}