package com.emrecura.easyshop.models

data class UserUpdateRequest(
    val firstName: String?,
    val lastName: String?,
    val username: String?,
    val email: String?
)
