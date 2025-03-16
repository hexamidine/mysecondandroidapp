package com.example.mysecondapplication.adapter

import android.view.LayoutInflater
import android.view.View
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
        //val context = holder.itemView.context

        with(holder.binding) {
            openBtn.tag = repo
            deleteBtn.tag = repo

            repoNameTextView.text = repo.name
            repoAuthorLoginTextView.text = repo.owner.login
            repoAuthorIconImageView.load(repo.owner.avatarUrl)

//            val color =
//                if (repo.isInFavorites) R.color.red else R.color.gray // Цвет "сердца", если пользователь был лайкнут

//            nameTextView.text = person.name // Отрисовка имени пользователя
//            companyTextView.text = person.companyName // Отрисовка компании пользователя
//            likedImageView.setColorFilter( // Отрисовка цвета "сердца"
//                ContextCompat.getColor(context, color),
//                android.graphics.PorterDuff.Mode.SRC_IN
//            )
//            Glide.with(context).load(person.photo).circleCrop() // Отрисовка фотографии пользователя с помощью библиотеки Glide
//                .error(R.drawable.ic_person)
//                .placeholder(R.drawable.ic_person).into(imageView)
        }
    }

    class RepoViewHolder(val binding: ItemRepoBinding) : RecyclerView.ViewHolder(binding.root)

//    override fun onClick(v: View?) {
//        if (v != null && v.id == R.id.likedImageView) {
//            val repo = v.tag as Repository
//            repositoryActionListener.onRepositoryLike(repo)
//        }
//    }
}