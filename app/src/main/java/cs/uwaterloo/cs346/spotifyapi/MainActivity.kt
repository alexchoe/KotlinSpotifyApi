package cs.uwaterloo.cs346.spotifyapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import cs.uwaterloo.cs346.spotifyapi.ui.theme.SpotifyAPITheme

import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val builtAPI = SpotifyApiHandler()
        val displayHandler = DisplayHandler()

        setContent {

            SpotifyAPITheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        api = builtAPI,
                        display = displayHandler,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(api: SpotifyApiHandler, display: DisplayHandler, modifier: Modifier = Modifier) {
    var result by remember { mutableStateOf(mutableListOf<List<String>>()) }
    var loading by remember { mutableStateOf(true) } // To manage loading state
    var buttonClick by remember { mutableStateOf(0) }

    LaunchedEffect(buttonClick) {
        // Call your suspend function to fetch data
        api.buildSearchApi()
        result.clear()
        var data = api.randomSong()
        result.add(display.displayPlaylistResults(data))
        data = api.randomSong()
        result.add(display.displayPlaylistResults(data))
        loading = false // Set loading to false once data fetching is done
    }
    Column(modifier = Modifier.fillMaxSize().padding(top = 50.dp)) {
        Button(onClick = {buttonClick++}, modifier = Modifier.align(Alignment.CenterHorizontally)) {Text(text = "New Songs")}
        if (!loading) {
            Text(
                text = result[0][0] + ", " + result[0][1] + ", " + result[0][2],
                modifier = Modifier.padding(top = 50.dp)
            )
            AsyncImage(
                model = result[0][3],
                contentDescription = "Album Image",
                modifier = Modifier.fillMaxWidth(0.5f).align(Alignment.CenterHorizontally)
            )
            Text(
                text = result[1][0] + ", " + result[1][1] + ", " + result[1][2],
                modifier = Modifier.padding(top = 50.dp)
            )
            AsyncImage(
                model = result[1][3],
                contentDescription = "Album Image",
                modifier = Modifier.fillMaxWidth(0.5f).align(Alignment.CenterHorizontally)
            )
        }
    }
}