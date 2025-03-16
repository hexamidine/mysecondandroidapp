package com.example.mysecondapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.mysecondapplication.databinding.ItemRepoBinding
import com.example.mysecondapplication.model.Repository

interface RepositoryActionListener {
    fun onRepositoryOpen(repo: Repository)
    fun onRepositoryRemove(repo: Repository)
}

class RepoAdapter(
    private val repositoryActionListener: RepositoryActionListener
) : RecyclerView.Adapter<RepoAdapter.RepoViewHolder>() {

    var data: List<Repository> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRepoBinding.inflate(inflater, parent, false)

        binding.openBtn.setOnClickListener {
            repositoryActionListener.onRepositoryOpen(it.tag as Repository)
        }

        binding.deleteBtn.setOnClickListener {
            repositoryActionListener.onRepositoryRemove(it.tag as Repository)
        }

        return RepoViewHolder(binding)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        val repo = data[position]

        with(holder.binding) {
            openBtn.tag = repo
            deleteBtn.tag = repo

            repoNameTextView.text = repo.name
            repoAuthorLoginTextView.text = repo.owner.login
            repoAuthorIconImageView.load(repo.owner.avatarUrl)
        }
    }

    class RepoViewHolder(val binding: ItemRepoBinding) : RecyclerView.ViewHolder(binding.root)
}