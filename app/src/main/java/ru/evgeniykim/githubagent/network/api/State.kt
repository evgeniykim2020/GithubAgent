package ru.evgeniykim.githubagent.network.api

sealed class State<out R> {
    data class Success<out T>(val data: T? = null) : State<T>()
    data class Error(val error: Throwable? = null, val message: String) : State<Nothing>()
    data object Loading : State<Nothing>()
}