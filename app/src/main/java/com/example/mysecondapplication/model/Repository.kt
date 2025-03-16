package com.example.mysecondapplication.model

data class Repository(
    var id: Long,
    val name: String,
    val url: String,
    val owner: RepositoryOwner,
    //var isInFavorites: Boolean,
)