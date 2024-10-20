package ru.evgeniykim.githubagent.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.evgeniykim.githubagent.data.Repos
import ru.evgeniykim.githubagent.databinding.ArchiveItemBinding

class ArchiveAdapter: RecyclerView.Adapter<ArchiveAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ArchiveItemBinding): RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Repos>() {
        override fun areItemsTheSame(oldItem: Repos, newItem: Repos): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Repos, newItem: Repos): Boolean {
            return oldItem.id == newItem.id
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ArchiveItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = differ.currentList[position]

        holder.binding.ownerName.text = item.userName
        holder.binding.repoName.text = item.repoName
    }
}