package com.github.crisacm.testskills.ui.posts

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.crisacm.testskills.data.database.models.Post
import com.github.crisacm.testskills.databinding.ItemPostBinding

class PostAdapter : RecyclerView.Adapter<PostAdapter.ViewHolder>() {
    private var dataSet: MutableList<Post> = mutableListOf()
    private var _onItemCLickListener: ((Post, Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostAdapter.ViewHolder = ViewHolder(
        ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    inner class ViewHolder(private val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post, position: Int) {
            with(binding) {
                textTitle.text = post.title
                textBody.text = post.body

                card.setOnClickListener {
                    _onItemCLickListener?.let { it(post, position) }
                }
            }
        }
    }

    override fun onBindViewHolder(holder: PostAdapter.ViewHolder, position: Int) {
        holder.bind(dataSet[position], position)
    }

    override fun getItemCount(): Int = dataSet.size

    override fun getItemViewType(position: Int): Int = position

    override fun getItemId(position: Int): Long = position.toLong()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(mutableList: MutableList<Post>) {
        dataSet = mutableList
        notifyDataSetChanged()
    }

    fun setOnItemCLickListener(callback: (Post, Int) -> Unit) {
        _onItemCLickListener = callback
    }
}