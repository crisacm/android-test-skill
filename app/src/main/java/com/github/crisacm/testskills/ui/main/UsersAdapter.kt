package com.github.crisacm.testskills.ui.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.crisacm.testskills.data.database.models.User
import com.github.crisacm.testskills.databinding.ItemUserBinding

class UsersAdapter : RecyclerView.Adapter<UsersAdapter.ViewHolder>() {
    private var dataSet: MutableList<User> = mutableListOf()
    private var _onItemClickListener: ((User, Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersAdapter.ViewHolder = ViewHolder(
        ItemUserBinding.inflate(LayoutInflater.from(parent.context), null, false)
    )

    override fun onBindViewHolder(holder: UsersAdapter.ViewHolder, position: Int) {
        holder.bind(dataSet[position], position)
    }

    inner class ViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User, position: Int) {
            with(binding) {
                textName.text = user.name
                textPhone.text = user.phone
                textEmail.text = user.email

                card.setOnClickListener {
                    _onItemClickListener?.let { it(user, position) }
                }

                buttonPosts.setOnClickListener {
                    _onItemClickListener?.let { it(user, position) }
                }
            }
        }
    }

    override fun getItemCount(): Int = dataSet.size

    override fun getItemViewType(position: Int): Int = position

    override fun getItemId(position: Int): Long = position.toLong()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(mutableList: MutableList<User>) {
        dataSet = mutableList
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(callback: (User, Int) -> Unit) {
        _onItemClickListener = callback
    }
}