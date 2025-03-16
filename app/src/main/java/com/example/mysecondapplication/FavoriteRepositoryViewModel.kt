package com.example.mysecondapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo.ApolloClient
import com.example.mysecondapplication.model.Repository
import com.example.mysecondapplication.model.RepositoryOwner
import com.example.mysecondapplication.resolver.ContentResolverHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteRepositoryViewModel @Inject constructor(
    private val contentResolverHelper: ContentResolverHelper,
    private val apolloClient: ApolloClient
) : ViewModel() {

    private val _reposData: MutableLiveData<List<Repository>> by lazy {
        MutableLiveData<List<Repository>>()
    }

    val reposData: LiveData<List<Repository>> = _reposData

    fun updateFavoriteRepositories() {
        viewModelScope.launch {
            val repoIds = contentResolverHelper.allFavoriteRepositories
            if (repoIds.isEmpty()) {
                //todo notify user
                return@launch
            }

            val response = apolloClient.query(ReposQuery(repoIds)).execute()
            val data = response.data
            val nodes = data?.nodes.orEmpty()

//            val name = nodes[0]?.onRepository?.name
//            val ownerLogin = nodes[0]?.onRepository?.owner?.login
//            val ownerAvaUrl = nodes[0]?.onRepository?.owner?.avatarUrl

            val repos = nodes.filterNotNull()
                .filter { it.onRepository != null }
                .map {
                    val r = it.onRepository!!
                    Repository(
                        r.databaseId!!.toLong(),
                        r.name,
                        r.url.toString(),
                        RepositoryOwner(
                            r.owner.login,
                            r.owner.avatarUrl.toString(),
                        )
                    )
                }

            //val favorites = repositoryDao.findFavoritesByIds(ids)
            _reposData.value = repos //items.toModelList(favorites.map { it.id })
            //repos.value = items.toViewModelList(favorites)
        }

//        gitHubApiClient.search(query, 10).enqueue(object : Callback<GitHubRepositoryResponse> {
//            override fun onFailure(call: Call<GitHubRepositoryResponse>, t: Throwable) {
//                val x = 45435
//                //todo
//            }
//
//            override fun onResponse(
//                call: Call<GitHubRepositoryResponse>,
//                response: Response<GitHubRepositoryResponse>
//            ) {
////                adapter = MyMovieAdapter(baseContext, response.body() as MutableList<Movie>)
////                adapter.notifyDataSetChanged()
////                recyclerMovieList.adapter = adapter
//
//                if (response.isSuccessful) {
//                    val body = response.body()
//                    val items = body?.items.orEmpty()
//                    //var totalCount = body?.totalCount
//
//                    val ids = items.map { it.id }.toList()
//
//                    viewModelScope.launch {
//                        val favorites = repositoryDao.findFavoritesByIds(ids)
//                        repos.value = items.toModelList(favorites.map { it.id })
//                        //repos.value = items.toViewModelList(favorites)
//                    }
//
//                    //repoService.eraseAndAddRepos(items.orEmpty())
//                    //adapter.data = items.orEmpty().toViewModelList()
//
//                } else {
//                    //todo
//                }
//                //dialog.dismiss()
//            }
//        })
    }

    fun removeFromFavorites(repository: Repository) {
        //todo remove from current model
        _reposData.value = (_reposData.value ?: emptyList()) - repository

        contentResolverHelper.removeFromFavorites(repository.id)
    }
}