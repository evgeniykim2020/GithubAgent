package ru.evgeniykim.githubagent.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.evgeniykim.githubagent.databinding.ResultItemBinding
import ru.evgeniykim.githubagent.model.userrepo.UserRepoModel
import ru.evgeniykim.githubagent.model.userrepo.UserRepoModelItem

class MyAdapter(val listener: AdapterListener): RecyclerView.Adapter<MyAdapter.MainViewHolder>() {
    inner class MainViewHolder(val binding: ResultItemBinding): RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<UserRepoModelItem>() {
        override fun areItemsTheSame(
            oldItem: UserRepoModelItem,
            newItem: UserRepoModelItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: UserRepoModelItem,
            newItem: UserRepoModelItem
        ): Boolean {
            return oldItem.id == newItem.id
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding = ResultItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainViewHolder(binding)
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val item = differ.currentList[position]

        holder.binding.name.text = item.name

        holder.binding.openButton.setOnClickListener {
            listener.openRepo(item.html_url)
        }

        holder.binding.downloadButton.setOnClickListener {
            listener.loadRepo(item.owner.login, item.name, item.html_url)
        }
    }
}

interface AdapterListener {
    fun openRepo(url: String)
    fun loadRepo(owner: String, repoName: String, url: String)
}