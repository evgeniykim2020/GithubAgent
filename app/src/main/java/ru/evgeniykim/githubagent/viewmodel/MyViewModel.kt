package ru.evgeniykim.githubagent.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.evgeniykim.githubagent.model.userrepo.UserRepoModel
import ru.evgeniykim.githubagent.network.RetrofitModule
import ru.evgeniykim.githubagent.network.api.State

class MyViewModel: ViewModel() {
    private val retrofit = RetrofitModule.apiService
    private var _gainedData = MutableLiveData<State<UserRepoModel>>()
    val gainedData: LiveData<State<UserRepoModel>>
        get() = _gainedData


    fun getRepos(name:String) {
        viewModelScope.launch {
            try {
                _gainedData.postValue(State.Loading)

                val response = retrofit.getUserRepo(name)

                if(response.isSuccessful) {
                    _gainedData.postValue(State.Success(response.body()))

                } else {
                    _gainedData.postValue(State.Error(error = null, message = response.message()))
                }
            } catch (e: Throwable) {
                _gainedData.postValue(State.Error(e, message = e.message.toString()))
            }
        }
    }

}