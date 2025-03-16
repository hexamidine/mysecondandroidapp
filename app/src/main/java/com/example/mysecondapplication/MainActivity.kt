package com.example.mysecondapplication

//import com.example.mysecondapplication.service.NetworkService
import android.app.AlertDialog
import android.content.DialogInterface
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

    // Create a client
//    val apolloClient = ApolloClient.Builder()
//        .serverUrl("https://api.github.com/graphql")
//        .build()
//    val token = "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"
//    private val apolloClient = NetworkService.getInstance()
//        ?.getApolloClientWithTokenInterceptor(token)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContent {
//            MySecondApplicationTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
//                }
//            }
//        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val manager = LinearLayoutManager(this)

        adapter = RepoAdapter(
            repositoryActionListener = object : RepositoryActionListener {
                override fun onRepositoryOpen(repo: Repository) {
                    openUrl(repo.url)
                }

                override fun onRepositoryRemove(repo: Repository) {
//                    val alertDialog = AlertDialog.Builder(binding.root.context)
//                    alertDialog.apply {
//                        setTitle("Удалить из избранных")
//                        setMessage("Уверены?")
//                        setCancelable(true)
//                        setPositiveButton("Да") { _, _ ->
//                            model.removeFromFavorites(repo)
//                        }
//                        setNegativeButton("Нет") { dialog, _ ->
//                            dialog.cancel()
//                        }
//                    }.create().show()
                    showConfirmationDialog("Удалить из избранных") {
                        model.removeFromFavorites(repo)
                    }
                }
            }
        )

        binding.refreshBtn.setOnClickListener {
            model.updateFavoriteRepositories()
        }
//            val resolver = ContentResolverHelper(applicationContext)
//            val repoIds = resolver.allFavoriteRepositories
//
//            //val cnt = repoIds.size
//
//            //val ids = listOf("R_kgDOBJY7iA", "R_kgDOCqWC5w")
//
//            runBlocking {
//                withContext(Dispatchers.IO) {
//                    // Execute your query. This will suspend until the response is received.
//                    val response = apolloClient?.query(ReposQuery(repoIds))?.execute()
//                    val data = response?.data
//                    val nodes = data?.nodes.orEmpty()
//
//                    val name = nodes[0]?.onRepository?.name
//                    val ownerLogin = nodes[0]?.onRepository?.owner?.login
//                    val ownerAvaUrl = nodes[0]?.onRepository?.owner?.avatarUrl
//
//                    //val errors = response?.errors
//                }
//            }

        binding.itemsView.layoutManager = manager // Назначение LayoutManager для RecyclerView
        binding.itemsView.adapter = adapter // Назначение адаптера для RecyclerView

        val reposObserver = Observer<List<Repository>> { repos ->
            // Update the UI
            adapter.data = repos
        }

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        model.reposData.observe(this, reposObserver)

        model.updateFavoriteRepositories()


//
//
//
//        }
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

//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    MySecondApplicationTheme {
//        Greeting("Android")
//    }
//}