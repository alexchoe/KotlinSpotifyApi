package cs.uwaterloo.cs346.spotifyapi

import com.adamratzman.spotify.models.PagingObject
import com.adamratzman.spotify.models.PlaylistTrack
import com.adamratzman.spotify.models.SpotifyPublicUser
import com.adamratzman.spotify.models.SpotifySearchResult

// Handles displaying and prettying of information
class DisplayHandler {
    init {

    }

    /// Basic option table to tell user what searches they can do.
    fun displaySearchOptions() {
        println()
        println("==================")
        println("(1) Artist Name")
        println("(2) Track Name")
        println("(3) User Profile")
        println("==================")
        println()
    }

    // Displays the user based search results
    fun displayUserSearchResults(searchResults: SpotifyPublicUser) {
        val parsedResults = parseUserSearchResults(searchResults)

        println(parsedResults)
    }

    // Displays the track based search results
    fun displayTrackSearchResults(searchResults: SpotifySearchResult): MutableList<Pair<String, String>> {
        val parsedResults = parseTrackSearchResults(searchResults)
        val resultList: MutableList<Pair<String, String>> = mutableListOf()
        //println()
        for (printedList in parsedResults) {
            // For some reason the first list is empty every time. So this
            // just skips it.
            if (printedList.isNullOrEmpty()) {
                continue
            }
            resultList.add(Pair(printedList[0], printedList[1]))
        }
        return resultList
    }

    fun displayPlaylistResults(searchResults: PagingObject<PlaylistTrack>): List<String> {
        return parsePlaylistResults(searchResults)

        /*val resultList: MutableList<List<String>> = mutableListOf()
        //println()
        for (printedList in parsedResults) {
            // For some reason the first list is empty every time. So this
            // just skips it.
            if (printedList.isNullOrEmpty()) {
                continue
            }
            //resultList.add(listOf(printedList[0], printedList[1], printedList[2]))
            resultList.add(printedList)
        }
        return resultList*/
    }

    // Parses through all the results of a track search and presents them in a way
    // that is digestible by a person.
    private fun parseTrackSearchResults(searchResults: SpotifySearchResult): List<List<String>> {
        var fullResultSet: MutableList<List<String>> = mutableListOf(listOf())

        for (t in searchResults.tracks!!.items) {
            var singleResultSet = mutableListOf<String>()
            singleResultSet.add(t.artists[0].name ?: "No Artist")
            singleResultSet.add(t.name)

            // API returns track playback time in milliseconds so this
            // converts it to the traditional m:ss display style.
            val timeInSeconds = (t.length/1000).toInt()
            val minutes = (timeInSeconds/60).toInt()
            val remainderSeconds = (timeInSeconds%60).toInt()
            val timeString = "$minutes:$remainderSeconds"
            singleResultSet.add(timeString)

            singleResultSet.add(t.externalUrls.spotify.toString())

            fullResultSet.add(singleResultSet)
        }
        return fullResultSet
    }

    // Parses through all the returned values from the API call into user
    // relevant information
    private fun parseUserSearchResults(searchResults: SpotifyPublicUser): List<String> {
        var userProfileInfo = mutableListOf<String>()

        // userProfileInfo.add(searchResults.displayName.toString())
        userProfileInfo.add(searchResults.id)
        userProfileInfo.add(searchResults.followers.total.toString())
        userProfileInfo.add(searchResults.externalUrls.spotify.toString())

        return userProfileInfo
    }

    private fun parsePlaylistResults(searchResults: PagingObject<PlaylistTrack>): List<String> {
        val t = searchResults.items[0]
        val singleResultSet = mutableListOf<String>()
        singleResultSet.add(t.track!!.asTrack!!.name)
        singleResultSet.add(t.track!!.asTrack!!.album.name)
        singleResultSet.add(t.track!!.asTrack!!.artists[0].name ?: "No Artist")
        singleResultSet.add(t.track!!.asTrack!!.album.images!![0].url)
        return singleResultSet
    }
}