package com.nku.moviessearchclient

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalContext
import com.nku.moviessearchclient.data.Movie
import com.nku.moviessearchclient.service.MoviesAPI
import com.nku.moviessearchclient.ui.theme.MoviesClientTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen(isVisible: Boolean = false, movieResults: List<Movie> = listOf()) {
    val numTests = 99
    MoviesClientTheme {
        // A surface container using the 'background' color from the theme
        var moviesList: List<Movie> by remember { mutableStateOf(movieResults) }
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Column {
                SearchInput()
                Button(onClick = {
                    var totalTimes = mutableListOf<Int>()
                    for (i in 0..numTests) {
                        val startTime = System.currentTimeMillis()
                        moviesList = getSearchResults()
                        val endTime = System.currentTimeMillis()
                        val totalTime = endTime - startTime
                        totalTimes.add(totalTime.toInt())
                    }

                    var high = 0
                    var low = 0
                    var mean = 0.0
                    var count = 0
                    var total = 0

                    for (ts in totalTimes) {
                        count++
                        total += ts
                        if (ts > high) {
                            high = ts
                        }

                        if (ts < low || low == 0) {
                            low = ts
                        }
                    }

                    mean = total.toDouble() / count
                    Log.d("Main", String.format("Count: %d", count))
                    Log.d("Main", String.format("High: %d", high))
                    Log.d("Main", String.format("Low: %d", low))
                    Log.d("Main", String.format("Mean: %f", mean))

                    Log.d("Main", String.format("Results Visible: %s", true))
                }) {
                    Text("Search")
                }
                SearchResults(moviesList)
            }
        }
    }
}

@Composable
fun SearchResults(moviesList: List<Movie>) {
    if (movieList.isNotEmpty()) {
        LazyColumn {
            items(moviesList) { moviesList ->
                MovieCard(moviesList)
            }
        }
    }
}

@Composable
fun MovieCard(movie: Movie) {
    Text(text = movie.title)
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchInput() {
    val mContext = LocalContext.current
    var text by rememberSaveable { mutableStateOf("") }

    TextField (
        value = text,
        onValueChange = {
            text = it
        },
        label = {
            Text("Search")
        },
        modifier = Modifier.onKeyEvent { keyEvent ->
                if (keyEvent.key != Key.Enter) return@onKeyEvent false
                if (keyEvent.type == KeyEventType.KeyUp) {
                    getSearchResults()
                }
                true
            }
    )
}

@Composable
fun SearchButton() {
    Button(onClick = {
        getSearchResults()
        Log.d("Main", String.format("Results Visible: %s", true))
    }) {
        Text("Search")
    }
}

fun getSearchResults(): List<Movie> {
    val apiClient = MoviesAPI()
    runBlocking {
        val job = launch(Dispatchers.Default) {
            apiClient.searchMovies("Comedy")
        }
    }
    return listOf()
}
