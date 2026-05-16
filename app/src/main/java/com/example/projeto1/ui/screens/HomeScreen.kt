package com.example.projeto1.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.projeto1.R
import com.example.projeto1.data.Track
import com.example.projeto1.ui.components.BannerCassette
import com.example.projeto1.ui.components.MiniPlayer
import com.example.projeto1.ui.components.AppBottomBar
import com.example.projeto1.ui.theme.ColorBackground
import com.example.projeto1.ui.theme.ColorSurface
import com.example.projeto1.ui.theme.ColorTextPrimary
import com.example.projeto1.ui.theme.ColorTextSecondary
import com.example.projeto1.ui.viewmodel.MusicViewModel
import com.example.projeto1.ui.viewmodel.PlaylistViewModel
import com.example.projeto1.data.db.PlaylistEntity

@Composable
fun HomeScreen(
    onOpenPlayer: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenLibrary: () -> Unit,
    onOpenPlaylist: (Long) -> Unit,
    musicViewModel: MusicViewModel = viewModel(factory = MusicViewModel.Factory),
    playlistViewModel: PlaylistViewModel = viewModel(factory = PlaylistViewModel.Factory)
) {
    val query by musicViewModel.query.collectAsState()
    val ui by musicViewModel.searchUi.collectAsState()
    val currentTrack by musicViewModel.player.currentTrack.collectAsState()
    val isPlaying by musicViewModel.player.isPlaying.collectAsState()
    val lastPlayedWithCover by playlistViewModel.lastPlayedWithCover.collectAsState()
    val keyboard = LocalSoftwareKeyboardController.current

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
                    currentRoute = "home",
                    onSkipBack = { musicViewModel.previous() },
                    onHome = { /* onde estamos */},
                    onLibrary = onOpenLibrary,
                    onSettings = onOpenSettings
                )
            }
        }
    ) { padding ->
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(padding).fillMaxSize()
        ) {
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.app_name),
                        color = ColorTextPrimary,
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = onOpenSettings) {
                        Icon(
                            imageVector = Icons.Filled.MoreHoriz,
                            contentDescription = stringResource(R.string.action_more),
                            tint = ColorTextPrimary
                        )
                    }
                }
            }

            item {
                OutlinedTextField(
                    value = query,
                    onValueChange = musicViewModel::onQueryChanged,
                    placeholder = { Text(stringResource(R.string.home_search_hint)) },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = {
                        musicViewModel.search()
                        keyboard?.hide()
                    }),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = ColorSurface,
                        unfocusedContainerColor = ColorSurface,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedTextColor = ColorTextPrimary,
                        unfocusedTextColor = ColorTextPrimary,
                        cursorColor = ColorTextPrimary,
                        focusedPlaceholderColor = ColorTextSecondary,
                        unfocusedPlaceholderColor = ColorTextSecondary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // última playlist tocada e "Weekly picks" só mostrados quando o usuário não está buscando
            val isSearching = query.isNotBlank() || ui.loading || ui.results.isNotEmpty()

            if (!isSearching) {
                item {
                    FeaturedCard(
                        lastPlayedWithCover = lastPlayedWithCover,
                        onPlaylistClick = onOpenPlaylist,
                        onCreateClick = onOpenLibrary
                    )
                }

                item {
                    Text(
                        text = stringResource(R.string.home_section_picks),
                        color = ColorTextPrimary,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            when {
                ui.loading -> item {
                    Box(modifier = Modifier.fillMaxWidth().height(80.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                ui.error -> item {
                    Text(
                        text = stringResource(R.string.home_search_error),
                        color = ColorTextSecondary
                    )
                }
                ui.results.isEmpty() && query.isNotBlank() -> item {
                    Text(
                        text = stringResource(R.string.home_no_results),
                        color = ColorTextSecondary
                    )
                }
                else -> itemsIndexed(ui.results) { index, track ->
                    TrackRow(track = track, onClick = {
                        //Coloca o resultado da busca em uma fila, para podermos navegar com os botões de próximo/anterior
                        musicViewModel.playFromList(ui.results, index)
                        onOpenPlayer()
                    })
                }
            }
        }
    }
}


@Composable
private fun FeaturedCard(
    lastPlayedWithCover: Pair<PlaylistEntity, String?>?,
    onPlaylistClick: (Long) -> Unit,
    onCreateClick: () -> Unit
) {
    // Cores possíveis
    val palette = remember {
        listOf(
            Color(0xFF7A1414),
            Color(0xFF4B2E83),
            Color(0xFF1F3A8A),
            Color(0xFF6B3F1B),
            Color(0xFF1F5F3F)
        )
    }

    if (lastPlayedWithCover != null) {
        val (playlist, firstTrackCover) = lastPlayedWithCover
        val tint = palette[(playlist.id % palette.size).toInt().coerceAtLeast(0)]
        Surface(
            color = ColorSurface,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clickable { onPlaylistClick(playlist.id) }
        ) {
            Box {
                BannerCassette(
                    coverUrl = firstTrackCover,
                    modifier = Modifier.fillMaxSize(),
                    fallbackColor = tint
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
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.home_featured_continue),
                        color = ColorTextSecondary,
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        text = playlist.name,
                        color = ColorTextPrimary,
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    } else {
        //Quando nenhuma playlist foi tocada ainda, sugere que o usuário crie uma
        Surface(
            color = ColorSurface,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clickable { onCreateClick() }
        ) {
            Box {
                BannerCassette(
                    coverUrl = null,
                    modifier = Modifier.fillMaxSize(),
                    fallbackColor = palette[0]
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.home_featured_empty_title),
                        color = ColorTextPrimary,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = stringResource(R.string.home_featured_empty_subtitle),
                        color = ColorTextSecondary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
private fun TrackRow(track: Track, onClick: () -> Unit) {
    Surface(
        color = ColorSurface,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(ColorBackground)
            ) {
                if (track.coverUrl != null) {
                    AsyncImage(model = track.coverUrl, contentDescription = null, modifier = Modifier.fillMaxSize())
                }
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = track.title,
                    color = ColorTextPrimary,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = track.artist,
                    color = ColorTextSecondary,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
