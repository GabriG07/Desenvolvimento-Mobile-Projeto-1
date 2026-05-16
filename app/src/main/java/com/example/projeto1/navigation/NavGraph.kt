package com.example.projeto1.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.projeto1.ui.screens.HomeScreen
import com.example.projeto1.ui.screens.LibraryScreen
import com.example.projeto1.ui.screens.LyricsScreen
import com.example.projeto1.ui.screens.PlayerScreen
import com.example.projeto1.ui.screens.PlaylistDetailScreen
import com.example.projeto1.ui.screens.SettingsScreen
import com.example.projeto1.ui.viewmodel.MusicViewModel

object Routes {
    const val HOME = "home"
    const val PLAYER = "player"
    const val LYRICS = "lyrics"
    const val LIBRARY = "library"
    const val SETTINGS = "settings"
    const val PLAYLIST_DETAIL = "playlist/{id}"
    fun playlistDetail(id: Long) = "playlist/$id"
}

@Composable
fun AppNavGraph(navController: NavHostController = rememberNavController()) {
    val musicViewModel: MusicViewModel = viewModel(factory = MusicViewModel.Factory)

    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {
        composable(Routes.HOME) {
            HomeScreen(
                onOpenPlayer = { navController.navigate(Routes.PLAYER) },
                onOpenSettings = { navController.navigate(Routes.SETTINGS) },
                onOpenLibrary = { navController.navigate(Routes.LIBRARY) },
                onOpenPlaylist = { id -> navController.navigate(Routes.playlistDetail(id)) },
                musicViewModel = musicViewModel
            )
        }

        composable(Routes.PLAYER) {
            PlayerScreen(
                onBack = { navController.popBackStack() },
                onOpenLyrics = { navController.navigate(Routes.LYRICS) },
                onHome = { navController.goBackTo(Routes.HOME) },
                onLibrary = { navController.navigate(Routes.LIBRARY) },
                onSettings = { navController.navigate(Routes.SETTINGS) },
                musicViewModel = musicViewModel
            )
        }

        composable(Routes.LYRICS) {
            LyricsScreen(
                onBack = { navController.popBackStack() },
                onHome = { navController.goBackTo(Routes.HOME) },
                onLibrary = { navController.navigate(Routes.LIBRARY) },
                onSettings = { navController.navigate(Routes.SETTINGS) },
                musicViewModel = musicViewModel
            )
        }

        composable(Routes.LIBRARY) {
            LibraryScreen(
                onPlaylistClick = { id ->
                    navController.navigate(Routes.playlistDetail(id))
                },
                onOpenPlayer = { navController.navigate(Routes.PLAYER) },
                onHome = { navController.goBackTo(Routes.HOME) },
                onSettings = { navController.navigate(Routes.SETTINGS) },
                musicViewModel = musicViewModel
            )
        }

        composable(Routes.PLAYLIST_DETAIL) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toLongOrNull() ?: 0L
            PlaylistDetailScreen(
                playlistId = id,
                onBack = { navController.popBackStack() },
                onPlayTracks = { tracks, index ->
                    musicViewModel.playFromList(tracks, index)
                    navController.navigate(Routes.PLAYER)
                }
            )
        }

        composable(Routes.SETTINGS) {
            SettingsScreen(
                onBack = { navController.popBackStack() },
                onHome = { navController.goBackTo(Routes.HOME) },
                onLibrary = { navController.navigate(Routes.LIBRARY) }
            )
        }
    }
}

private fun NavHostController.goBackTo(route: String) {
    navigate(route) {
        popUpTo(route) { inclusive = false }
        launchSingleTop = true
    }
}