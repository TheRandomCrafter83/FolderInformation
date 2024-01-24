package com.coderzf1.folderinformation

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class MainActivityViewModel:ViewModel() {

    var chosenUriState = MutableStateFlow(CurrentFolderState(null))
        private set

    fun setUri(uri:Uri){
        chosenUriState.update {
            it.copy(currentUri = uri)
        }
    }


}