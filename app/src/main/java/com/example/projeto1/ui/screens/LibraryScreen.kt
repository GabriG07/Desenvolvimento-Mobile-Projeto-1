package com.example.projeto1.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projeto1.R
import com.example.projeto1.data.db.PlaylistEntity
import com.example.projeto1.ui.components.BannerCassette
import com.example.projeto1.ui.components.MiniPlayer
import com.example.projeto1.ui.components.AppBottomBar
import com.example.projeto1.ui.theme.ColorBackground
import com.example.projeto1.ui.theme.ColorGold
import com.example.projeto1.ui.theme.ColorTextPrimary
import com.example.projeto1.ui.theme.ColorTextSecondary
import com.example.projeto1.ui.viewmodel.MusicViewModel
import com.example.projeto1.ui.viewmodel.PlaylistViewModel

private val cardColors = listOf(
    Color(0xFF7A1414),
    Color(0xFF6E5A8C),
    Color(0xFF42627B),
    Color(0xFF7A4F25),
    Color(0xFF3F6B4E)
)

@Composable
fun LibraryScreen(
    onPlaylistClick: (Long) -> Unit,
    onOpenPlayer: () -> Unit,
    onHome: () -> Unit,
    onSettings: () -> Unit,
    playlistViewModel: PlaylistViewModel = viewModel(factory = PlaylistViewModel.Factory),
    musicViewModel: MusicViewModel = viewModel(factory = MusicViewModel.Factory)
) {
    val playlists by playlistViewModel.playlists.collectAsState()
    val currentTrack by musicViewModel.player.currentTrack.collectAsState()
    val isPlaying by musicViewModel.player.isPlaying.collectAsState()

    var showCreate by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf("") }

    Scaffold(
        containerColor = ColorBackground,
        bottomBar = {
            Column {
                MiniPlayer(
                    track = currentTrack,
                    isPlaying = isPlaying,
                    onTogglePlay = { musicViewModel.togglePlay() },
                    onClick = onOpenPlayer
                )
                AppBottomBar(
                    currentRoute = "library",
                    onSkipBack = { musicViewModel.previous() },
                    onHome = onHome,
                    onLibrary = { },
                    onSettings = onSettings
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreate = true },
                containerColor = ColorGold,
                contentColor = ColorBackground
            ) {
                Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.library_create_playlist))
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, top = 16.dp)
            ) {
                Text(
                    text = stringResource(R.string.library_title),
                    color = ColorTextPrimary,
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.weight(1f)
                )
            }

            if (playlists.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize().padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.library_empty),
                        color = ColorTextSecondary,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(playlists, key = { it.id }) { p ->
                        PlaylistCard(
                            playlist = p,
                            color = cardColors[(p.id % cardColors.size).toInt().coerceAtLeast(0)],
                            playlistViewModel = playlistViewModel,
                            onClick = { onPlaylistClick(p.id) }
                        )
                    }
                }
            }
        }
    }

    if (showCreate) {
        AlertDialog(
            onDismissRequest = { showCreate = false },
            title = { Text(stringResource(R.string.library_new_playlist_title)) },
            text = {
                OutlinedTextField(
                    value = newName,
                    onValueChange = { newName = it },
                    placeholder = { Text(stringResource(R.string.library_new_playlist_hint)) },
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    playlistViewModel.createPlaylist(newName)
                    newName = ""
                    showCreate = false
                }) { Text(stringResource(R.string.action_create)) }
            },
            dismissButton = {
                TextButton(onClick = { showCreate = false }) { Text(stringResource(R.string.action_cancel)) }
            }
        )
    }
}

@Composable
private fun PlaylistCard(
    playlist: PlaylistEntity,
    color: Color,
    playlistViewModel: PlaylistViewModel,
    onClick: () -> Unit
) {
    val coverFlow = remember(playlist.id) { playlistViewModel.firstCoverOf(playlist.id) }
    val firstCover by coverFlow.collectAsState(initial = null)

    Surface(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clickable(onClick = onClick)
    ) {
        Box(modifier = Modifier.background(color)) {
            BannerCassette(
                coverUrl = firstCover ?: playlist.coverUrl,
                modifier = Modifier.fillMaxSize(),
                fallbackColor = color
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            0f to Color.Transparent,
                            0.55f to Color.Transparent,
                            1f to Color.Black.copy(alpha = 0.7f)
                        )
                    )
            )
            Text(
                text = playlist.name,
                color = ColorTextPrimary,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            )
        }
    }
}