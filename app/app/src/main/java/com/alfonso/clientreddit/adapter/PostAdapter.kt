package com.alfonso.clientreddit.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alfonso.clientreddit.models.DataPost
import com.alfonso.clientreddit.databinding.ItemPostBinding
import com.alfonso.clientreddit.viewModel.MainViewModel

class PostAdapter(val viewModel: MainViewModel) : ListAdapter<DataPost,PostAdapter.PostViewHolder>(PostDiffCallback())  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder.create(parent,viewModel)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }


    class PostViewHolder private constructor(private val binding : ItemPostBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : DataPost) {
            binding.post = item
            binding.executePendingBindings()
        }

        companion object {
            fun create(parent: ViewGroup,viewMod : MainViewModel) : PostViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemPostBinding.inflate(layoutInflater,parent,false)
                binding.viewModel = viewMod
                return PostViewHolder(binding)
            }
        }
    }
}

class PostDiffCallback : DiffUtil.ItemCallback<DataPost>() {
    override fun areItemsTheSame(oldItem: DataPost, newItem: DataPost): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DataPost, newItem: DataPost): Boolean {
        return oldItem == newItem
    }

}