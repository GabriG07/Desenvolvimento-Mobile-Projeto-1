package com.example.projeto1.navigation

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.projeto1.data.UserModel
import com.example.projeto1.ui.OnBoardingEvents
import com.example.projeto1.ui.screens.ChatScreen
import com.example.projeto1.ui.screens.ChatsPage
import com.example.projeto1.ui.screens.HomeScreen
import com.example.projeto1.ui.screens.LibraryScreen
import com.example.projeto1.ui.screens.LyricsScreen
import com.example.projeto1.ui.screens.NewChatScreen
import com.example.projeto1.ui.screens.PlayerScreen
import com.example.projeto1.ui.screens.PlaylistDetailScreen
import com.example.projeto1.ui.screens.SettingsScreen
import com.example.projeto1.ui.screens.SignInScreen
import com.example.projeto1.ui.viewmodel.ChatViewModel
import com.example.projeto1.ui.viewmodel.MusicViewModel
import com.example.projeto1.ui.viewmodel.OnboardingViewModel

object Routes {
    const val SIGNIN = "signIn"
    const val HOME = "home"
    const val PLAYER = "player"
    const val CHATS = "chats"
    const val NEWCHAT = "newChat"
    const val CHAT = "chat"
    const val LYRICS = "lyrics"
    const val LIBRARY = "library"
    const val SETTINGS = "settings"
    const val PLAYLIST_DETAIL = "playlist/{id}"
    fun playlistDetail(id: Long) = "playlist/$id"
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavGraph(onboardingViewModel: OnboardingViewModel, navController: NavHostController = rememberNavController(), activity: Activity) {
    val musicViewModel: MusicViewModel = viewModel(factory = MusicViewModel.Factory)
    val chatViewModel: ChatViewModel = viewModel(factory = ChatViewModel.Factory)

    var lastLoggedUser: String = ""

    var sharedPreferences: SharedPreferences = activity.getSharedPreferences( "SHARED_PREF", Context.MODE_PRIVATE)

    lastLoggedUser = sharedPreferences.getString("mobileNumber", "").toString()
    if (lastLoggedUser != "") {
        onboardingViewModel.action(OnBoardingEvents.LoginClick(
            UserModel(userName = "", mobileNumber = lastLoggedUser),
            status = { status ->
                if (status)  navController.navigate(Routes.HOME)
            },
            sharedPreferences = sharedPreferences,
            ))
    }

    NavHost(
        navController = navController,
        startDestination = Routes.SIGNIN
    ) {

        composable(Routes.SIGNIN) {
            SignInScreen(
                onHome = { navController.navigate(Routes.HOME) },
                onboardingViewModel::action,
                sharedPreferences= sharedPreferences
                )
        }

        composable(Routes.HOME) {
            HomeScreen(
                onOpenPlayer = { navController.navigate(Routes.PLAYER) },
                onOpenSettings = { navController.navigate(Routes.SETTINGS) },
                onOpenLibrary = { navController.navigate(Routes.LIBRARY) },
                onChatsPage = { navController.navigate(Routes.CHATS) },
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
                onChatsPage = { navController.navigate(Routes.CHATS) },
                musicViewModel = musicViewModel
            )
        }

        composable(Routes.CHATS) {
            ChatsPage(
                onBack = { navController.popBackStack() },
                onHome = { navController.goBackTo(Routes.HOME) },
                onNewChat = {navController.navigate(Routes.NEWCHAT)},
                onLibrary = { navController.navigate(Routes.LIBRARY) },
                onSettings = { navController.navigate(Routes.SETTINGS) },
                onGoToContact = { navController.navigate(Routes.CHAT) },
                chatViewModel = chatViewModel,
                musicViewModel = musicViewModel
            )
        }

        composable(Routes.NEWCHAT) {
            NewChatScreen(
                onBack = { navController.popBackStack() },
                onHome = { navController.goBackTo(Routes.HOME) },
                onLibrary = { navController.navigate(Routes.LIBRARY) },
                onSettings = { navController.navigate(Routes.SETTINGS) },
                onGoToContact = { navController.navigate(Routes.CHAT) },
                chatViewModel = chatViewModel,
                musicViewModel = musicViewModel
            )
        }

        composable(Routes.CHAT) {
            ChatScreen(
                onBack = { navController.popBackStack() },
                onHome = { navController.goBackTo(Routes.HOME) },
                onLibrary = { navController.navigate(Routes.LIBRARY) },
                onSettings = { navController.navigate(Routes.SETTINGS) },
                chatViewModel = chatViewModel,
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
                onboardingViewModel = onboardingViewModel,
                onSignIn = { navController.navigate(Routes.SIGNIN) {
                    popUpTo(0)
                } },
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