package com.example.projeto1.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projeto1.R
import com.example.projeto1.ui.components.AppBottomBar
import com.example.projeto1.ui.theme.ColorBackground
import com.example.projeto1.ui.theme.ColorTextPrimary
import com.example.projeto1.ui.theme.ColorTextSecondary
import com.example.projeto1.ui.viewmodel.MusicViewModel

@Composable
fun LyricsScreen(
    onBack: () -> Unit,
    onHome: () -> Unit,
    onLibrary: () -> Unit,
    onSettings: () -> Unit,
    musicViewModel: MusicViewModel = viewModel(factory = MusicViewModel.Factory)
) {
    val ui by musicViewModel.lyricsUi.collectAsState()
    val track by musicViewModel.player.currentTrack.collectAsState()

    LaunchedEffect(track?.deezerId) {
        musicViewModel.loadLyricsForCurrentTrack()
    }

    Scaffold(
        containerColor = ColorBackground,
        bottomBar = {
            AppBottomBar(
                currentRoute = "lyrics",
                onSkipBack = { musicViewModel.previous() },
                onHome = onHome,
                onLibrary = onLibrary,
                onSettings = onSettings
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            Box(modifier = Modifier.padding(8.dp)) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.action_back),
                        tint = ColorTextPrimary
                    )
                }
            }

            track?.let {
                Text(
                    text = it.title,
                    color = ColorTextPrimary,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Text(
                    text = it.artist,
                    color = ColorTextSecondary,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            Box(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                contentAlignment = Alignment.TopStart
            ) {
                when {
                    ui.loading -> Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Text(
                            text = stringResource(R.string.lyrics_loading),
                            color = ColorTextSecondary,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                    ui.error -> Column {
                        Text(
                            text = stringResource(R.string.lyrics_error),
                            color = ColorTextSecondary
                        )
                        if (!ui.errorMessage.isNullOrBlank()) {
                            Text(
                                text = ui.errorMessage!!,
                                color = ColorTextSecondary,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                    ui.notFound -> Text(
                        text = stringResource(R.string.lyrics_not_found),
                        color = ColorTextSecondary
                    )
                    ui.lyrics != null -> Text(
                        text = ui.lyrics!!,
                        color = ColorTextPrimary,
                        modifier = Modifier.verticalScroll(rememberScrollState()).padding(PaddingValues(bottom = 16.dp))
                    )
                }
            }
        }
    }
}