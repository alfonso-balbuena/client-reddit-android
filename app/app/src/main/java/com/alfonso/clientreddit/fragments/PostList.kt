package com.alfonso.clientreddit.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.alfonso.clientreddit.R
import com.alfonso.clientreddit.adapter.ItemAnimatorPost
import com.alfonso.clientreddit.adapter.PostAdapter
import com.alfonso.clientreddit.adapter.PostListener
import com.alfonso.clientreddit.databinding.FragmentPostListBinding
import com.alfonso.clientreddit.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import timber.log.Timber

@AndroidEntryPoint
class PostList : Fragment() {

    private lateinit var binding : FragmentPostListBinding
    private val viewModelShared : MainViewModel by activityViewModels()
    private lateinit var animation: Animation

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_post_list,container,false)
        val adapter = PostAdapter(viewModelShared, PostListener {
            viewModelShared.selectPost(it)
        })
        animation = AnimationUtils.loadAnimation(context, R.anim.dismiss_all_recycler)
        binding.recyclerViewPosts.adapter = adapter
        binding.recyclerViewPosts.itemAnimator = SlideInLeftAnimator()

        binding.viewModel = viewModelShared

        viewModelShared.hasNext.observe(viewLifecycleOwner, {
            binding.nextButton.isEnabled = it
        })

        viewModelShared.hasPrevious.observe(viewLifecycleOwner,{
            binding.previousButton.isEnabled =it
        })

        viewModelShared.isLoading.observe(viewLifecycleOwner, {
            binding.swipeRefresh.isRefreshing = it
        })
        binding.swipeRefresh.setOnRefreshListener { viewModelShared.refresh() }

        viewModelShared.posts.observe(viewLifecycleOwner, Observer {
            it?.let {
                if(it.isNotEmpty()){
                    if(binding.recyclerViewPosts.alpha == 0f) {
                        binding.recyclerViewPosts.alpha = 1f
                    }
                    adapter.submitList(it)
                } else {
                    animation.setAnimationListener(object : Animation.AnimationListener {
                        override fun onAnimationStart(p0: Animation?) {
                        }

                        override fun onAnimationEnd(p0: Animation?) {
                            binding.recyclerViewPosts.alpha = 0f
                            adapter.submitList(it) //Clear list for adapter
                        }

                        override fun onAnimationRepeat(p0: Animation?) {
                        }

                    })
                    binding.recyclerViewPosts.startAnimation(animation)

                }

            }
        })

        return binding.root
    }

}