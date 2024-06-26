package com.emrecura.easyshop.models

data class Post (
    val posts: List<PostElement>,
    val total: Long,
    val skip: Long,
    val limit: Long
)

data class PostElement (
    val id: Long,
    val title: String,
    val body: String,
    val tags: List<String>,
    val reactions: Reactions,
    val views: Long,
    val userID: Long
)

data class Reactions (
    val likes: Long,
    val dislikes: Long
)
