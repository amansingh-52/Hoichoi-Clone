package com.example.hoichoihome.ui.main.viewstate

import com.example.hoichoihome.data.model.HomePageResponse

sealed class ViewState {
    object Idle : ViewState()
    object Loading : ViewState()
    data class Error(val error : String?) : ViewState()
    data class HomePageResponses(val data : HomePageResponse) : ViewState()
}
