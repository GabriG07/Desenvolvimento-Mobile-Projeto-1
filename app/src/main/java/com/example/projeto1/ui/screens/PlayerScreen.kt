package com.example.projeto1.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projeto1.R
import com.example.projeto1.ui.components.PortraitCassetteCover
import com.example.projeto1.ui.components.AppBottomBar
import com.example.projeto1.ui.theme.ColorBackground
import com.example.projeto1.ui.theme.ColorGold
import com.example.projeto1.ui.theme.ColorSurfaceDim
import com.example.projeto1.ui.theme.ColorTextPrimary
import com.example.projeto1.ui.theme.ColorTextSecondary
import com.example.projeto1.ui.viewmodel.MusicViewModel
import com.example.projeto1.ui.viewmodel.PlaylistViewModel

@Composable
fun PlayerScreen(
    onBack: () -> Unit,
    onOpenLyrics: () -> Unit,
    onHome: () -> Unit,
    onLibrary: () -> Unit,
    onChatsPage: () -> Unit,
    onSettings: () -> Unit,
    musicViewModel: MusicViewModel = viewModel(factory = MusicViewModel.Factory),
    playlistViewModel: PlaylistViewModel = viewModel(factory = PlaylistViewModel.Factory)
) {
    val track by musicViewModel.player.currentTrack.collectAsState()
    val isPlaying by musicViewModel.player.isPlaying.collectAsState()
    val progress by musicViewModel.player.progress.collectAsState()
    val playlists by playlistViewModel.playlists.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = ColorBackground,
        bottomBar = {
            AppBottomBar(
                currentRoute = "player",
                onSkipBack = { musicViewModel.previous() },
                onHome = onHome,
                onLibrary = onLibrary,
                onSettings = onSettings,
                track = null,
                isPlaying = null,
                onTogglePlay = null,
                onClick = null
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBackIosNew,
                        modifier = Modifier.size(32.dp),
                        contentDescription = stringResource(R.string.action_back),
                        tint = ColorTextPrimary
                    )
                }

                Column(
                    modifier = Modifier.fillMaxSize().padding(top = 56.dp, bottom = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    PortraitCassetteCover(
                        coverUrl = track?.coverUrl,
                        modifier = Modifier.fillMaxWidth(0.65f)
                    )
                }

                IconButton(
                    onClick = onChatsPage,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.chat_icon),
                        modifier = Modifier.size(40.dp),
                        contentDescription = null,
                    )
                }
            }

            // Informações e controles do player
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ColorSurfaceDim)
                    .padding(PaddingValues(horizontal = 24.dp, vertical = 24.dp)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (track == null) {
                    Text(
                        text = stringResource(R.string.player_no_song),
                        color = ColorTextSecondary,
                        textAlign = TextAlign.Center
                    )
                } else {
                    Text(
                        text = stringResource(R.string.player_now_playing),
                        color = ColorTextSecondary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = track!!.title,
                        color = ColorTextPrimary,
                        style = MaterialTheme.typography.headlineMedium,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = track!!.artist,
                        color = ColorTextSecondary,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(Modifier.height(16.dp))

                    LinearProgressIndicator(
                        progress = progress.coerceIn(0f, 1f),
                        color = ColorGold,
                        trackColor = ColorTextSecondary.copy(alpha = 0.3f),
                        modifier = Modifier.fillMaxWidth().height(4.dp)
                    )

                    Spacer(Modifier.height(16.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        IconButton(onClick = { musicViewModel.previous() }) {
                            Icon(
                                imageVector = Icons.Filled.SkipPrevious,
                                contentDescription = stringResource(R.string.player_previous),
                                tint = ColorTextPrimary,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                        IconButton(
                            onClick = { musicViewModel.togglePlay() },
                            modifier = Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(50))
                                .background(ColorSurfaceDim)
                        ) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                                contentDescription = stringResource(
                                    if (isPlaying) R.string.player_pause else R.string.player_play
                                ),
                                tint = ColorTextPrimary,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                        IconButton(onClick = { musicViewModel.next() }) {
                            Icon(
                                imageVector = Icons.Filled.SkipNext,
                                contentDescription = stringResource(R.string.player_next),
                                tint = ColorTextPrimary,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Button(
                            onClick = onOpenLyrics,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ColorGold,
                                contentColor = ColorBackground
                            )
                        ) {
                            Text(stringResource(R.string.player_view_lyrics))
                        }
                        Button(
                            onClick = { showAddDialog = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ColorSurfaceDim,
                                contentColor = ColorTextPrimary
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(stringResource(R.string.playlist_add_to))
                        }
                    }
                }
            }
        }
    }

    // Dialog de adicionar musica em playlist
    val currentTrack = track
    if (showAddDialog && currentTrack != null) {
        AddToPlaylistDialog(
            playlists = playlists,
            onDismiss = { showAddDialog = false },
            onPlaylistChosen = { playlist ->
                playlistViewModel.addTrackToPlaylist(playlist.id, currentTrack)
                showAddDialog = false
            }
        )
    }
}

@Composable
private fun AddToPlaylistDialog(
    playlists: List<com.example.projeto1.data.db.PlaylistEntity>,
    onDismiss: () -> Unit,
    onPlaylistChosen: (com.example.projeto1.data.db.PlaylistEntity) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = ColorSurfaceDim,
        title = {
            Text(
                text = stringResource(R.string.playlist_add_to),
                color = ColorTextPrimary
            )
        },
        text = {
            if (playlists.isEmpty()) {
                Text(
                    text = stringResource(R.string.playlist_add_no_playlists),
                    color = ColorTextSecondary
                )
            } else {
                Column {
                    playlists.forEach { p ->
                        Surface(
                            color = androidx.compose.ui.graphics.Color.Transparent,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onPlaylistChosen(p) }
                                .padding(vertical = 12.dp)
                        ) {
                            Text(
                                text = p.name,
                                color = ColorTextPrimary,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            androidx.compose.material3.TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(R.string.action_cancel),
                    color = ColorGold
                )
            }
        }
    )
}
