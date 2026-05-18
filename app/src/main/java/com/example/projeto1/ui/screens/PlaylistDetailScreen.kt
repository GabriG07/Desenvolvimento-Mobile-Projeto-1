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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.projeto1.R
import com.example.projeto1.data.Track
import com.example.projeto1.data.db.TrackEntity
import com.example.projeto1.data.toDomain
import com.example.projeto1.ui.theme.ColorBackground
import com.example.projeto1.ui.theme.ColorError
import com.example.projeto1.ui.theme.ColorSurface
import com.example.projeto1.ui.theme.ColorTextPrimary
import com.example.projeto1.ui.theme.ColorTextSecondary
import com.example.projeto1.ui.viewmodel.PlaylistViewModel

@Composable
fun PlaylistDetailScreen(
    playlistId: Long,
    onBack: () -> Unit,
    onPlayTracks: (List<Track>, Int) -> Unit,
    playlistViewModel: PlaylistViewModel = viewModel(factory = PlaylistViewModel.Factory)
) {
    val tracksFlow = remember(playlistId) { playlistViewModel.loadDetail(playlistId) }
    val tracks: List<TrackEntity> by tracksFlow.collectAsState(initial = emptyList())
    val name by playlistViewModel.detailName.collectAsState()

    Scaffold(containerColor = ColorBackground) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBackIosNew,
                        modifier = Modifier.size(32.dp),
                        contentDescription = stringResource(R.string.action_back),
                        tint = ColorTextPrimary
                    )
                }
                Text(
                    text = name ?: "",
                    color = ColorTextPrimary,
                    style = MaterialTheme.typography.headlineMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f).padding(start = 8.dp)
                )
            }

            if (tracks.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize().padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.playlist_detail_empty),
                        color = ColorTextSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(tracks.size, key = { idx -> tracks[idx].rowId }) { idx ->
                        val entity = tracks[idx]
                        Surface(
                            color = ColorSurface,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    playlistViewModel.markPlaylistAsPlayed(playlistId)
                                    onPlayTracks(tracks.map { it.toDomain() }, idx)
                                }
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
                                    if (entity.coverUrl != null) {
                                        AsyncImage(
                                            model = entity.coverUrl,
                                            contentDescription = null,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }
                                }
                                Spacer(Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = entity.title,
                                        color = ColorTextPrimary,
                                        style = MaterialTheme.typography.titleMedium,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = entity.artistName,
                                        color = ColorTextSecondary,
                                        style = MaterialTheme.typography.bodyMedium,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                                IconButton(onClick = { playlistViewModel.removeTrack(entity.rowId) }) {
                                    Icon(
                                        imageVector = Icons.Filled.Delete,
                                        contentDescription = stringResource(R.string.action_delete),
                                        tint = ColorError
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}