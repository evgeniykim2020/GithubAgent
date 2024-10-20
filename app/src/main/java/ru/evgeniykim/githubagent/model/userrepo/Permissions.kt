package ru.evgeniykim.githubagent.model.userrepo

data class Permissions(
    val admin: Boolean,
    val pull: Boolean,
    val push: Boolean
)