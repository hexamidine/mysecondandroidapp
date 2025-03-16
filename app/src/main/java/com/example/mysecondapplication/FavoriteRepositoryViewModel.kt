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
import kotlinx.coroutines.Dispatchers
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
                //todo notify user?
                return@launch
            }

            val response = apolloClient.query(ReposQuery(repoIds)).execute()
            val nodes = response.data?.nodes.orEmpty()

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

            _reposData.value = repos
        }
    }

    fun removeFromFavorites(repository: Repository) {
        _reposData.value = (_reposData.value ?: emptyList()) - repository

        viewModelScope.launch (Dispatchers.IO) {
            contentResolverHelper.removeFromFavorites(repository.id)
        }
    }
}