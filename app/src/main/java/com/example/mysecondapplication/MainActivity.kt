package com.example.mysecondapplication

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mysecondapplication.adapter.RepoAdapter
import com.example.mysecondapplication.adapter.RepositoryActionListener
import com.example.mysecondapplication.databinding.ActivityMainBinding
import com.example.mysecondapplication.model.Repository
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: RepoAdapter

    val model: FavoriteRepositoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = RepoAdapter(
            repositoryActionListener = object : RepositoryActionListener {
                override fun onRepositoryOpen(repo: Repository) {
                    openUrl(repo.url)
                }

                override fun onRepositoryRemove(repo: Repository) {
                    showConfirmationDialog("Удалить из избранных") {
                        model.removeFromFavorites(repo)
                    }
                }
            }
        )

        binding.refreshBtn.setOnClickListener {
            model.updateFavoriteRepositories()
        }

        binding.itemsView.layoutManager = LinearLayoutManager(this)
        binding.itemsView.adapter = adapter

        val reposObserver = Observer<List<Repository>> { repos ->
            adapter.data = repos
        }

        model.reposData.observe(this, reposObserver)

        model.updateFavoriteRepositories()
    }

    private fun showConfirmationDialog(title: String, action: () -> Unit) {
        val alertDialog = AlertDialog.Builder(binding.root.context)
        alertDialog.apply {
            setTitle(title)
            setMessage("Уверены?")
            setCancelable(true)
            setPositiveButton("Да") { _, _ ->
                action.invoke()
            }
            setNegativeButton("Нет") { dialog, _ ->
                dialog.cancel()
            }
        }.create().show()
    }

    private fun openUrl(repoUrl: String) {
        var url = repoUrl
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://$url"
        }
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }
}
