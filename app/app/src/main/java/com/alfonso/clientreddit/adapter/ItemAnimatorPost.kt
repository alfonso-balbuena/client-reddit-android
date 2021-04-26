package com.alfonso.clientreddit.adapter

import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.alfonso.clientreddit.R

class ItemAnimatorPost : DefaultItemAnimator() {

    override fun animateRemove(holder: RecyclerView.ViewHolder?): Boolean {

        holder?.let {
            val animation = AnimationUtils.loadAnimation(it.itemView.context, R.anim.slide_left)
            it.itemView.startAnimation(animation)
        }
        dispatchRemoveFinished(holder)
        return true
    }
}