package cs.uwaterloo.cs346.spotifyapi


import com.adamratzman.spotify.SpotifyAppApi
import com.adamratzman.spotify.models.PagingObject
import com.adamratzman.spotify.models.PlaylistTrack
import com.adamratzman.spotify.models.SpotifyPublicUser
import com.adamratzman.spotify.models.SpotifySearchResult
import com.adamratzman.spotify.spotifyAppApi
import com.adamratzman.spotify.utils.Market

class SpotifyApiHandler {
    private val clientID = "7dfcb2f0bd064663aa2a44aee3277e1d"
    private val clientSecret = "19780c316e09451a95b5666032945a5a"
    private var api: SpotifyAppApi? = null

    init {

    }

    /// Pulls the developer ClientID and ClientSecret tokens provided
    /// by Spotify and builds them into an object that can easily
    /// call public Spotify APIs.
    suspend fun buildSearchApi() {
        api = spotifyAppApi(clientID, clientSecret).build()
    }

    // Performs Spotify database query for queries related to user information. Returns
    // the results as a SpotifyPublicUser object.
    suspend fun userSearch(userQuery: String): SpotifyPublicUser? {
        return api!!.users.getProfile(userQuery)
    }

    // Performs Spotify database query for queries related to track information. Returns
    // the results as a SpotifySearchResult object.
    suspend fun trackSearch(searchQuery: String): SpotifySearchResult {
        return api!!.search.searchAllTypes(searchQuery, limit = 10, offset = 1, market = Market.US)
    }

    suspend fun randomSong(): PagingObject<PlaylistTrack> {
        return api!!.playlists.getPlaylistTracks(playlist = "37i9dQZEVXbMDoHDwVN2tF", limit = 1, offset = kotlin.random.Random.nextInt(0, 49), market = Market.US)
    }

}
