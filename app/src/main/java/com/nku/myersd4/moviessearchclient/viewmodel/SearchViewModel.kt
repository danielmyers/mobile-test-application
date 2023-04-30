package com.nku.moviessearchclient.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nku.moviessearchclient.data.Movie
import com.nku.moviessearchclient.getSearchResults
import kotlinx.coroutines.flow.*

class SearchViewModel : ViewModel() {

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _movies = MutableStateFlow(listOf<Movie>())
    val movies = searchText.combine(_movies) { text, movies ->
            if(text.isBlank()) {
                movies
            }
            else {
                getSearchResults()
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _movies.value
        )

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

}