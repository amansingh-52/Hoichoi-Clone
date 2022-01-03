package com.example.hoichoihome.ui.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hoichoihome.repo.MainRepository
import com.example.hoichoihome.ui.main.intent.MainIntent
import com.example.hoichoihome.ui.main.viewstate.ViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import java.lang.Exception

class MainViewModel(
    private val repository: MainRepository
) : ViewModel() {

    val intent = Channel<MainIntent>(Channel.UNLIMITED)

    private val _state = MutableStateFlow<ViewState>(ViewState.Idle)
    val state get() = _state

    init {
        handleState()
    }

    private fun handleState() {
        viewModelScope.launch(Dispatchers.Main) {
            intent.consumeAsFlow().collect {
                when (it) {
                    is MainIntent.FetchHomeData -> fetchHomeData()
                }
            }
        }
    }

    private fun fetchHomeData() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = ViewState.Loading
            _state.value = try {
                ViewState.HomePageResponses(repository.getData())
            } catch (e: Exception) {
                ViewState.Error(e.localizedMessage)
            }
        }
    }

}