package com.example.projeto1.ui.screens

import android.R.attr.fontWeight
import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projeto1.R
import com.example.projeto1.data.Contact
import com.example.projeto1.data.Message
import com.example.projeto1.data.contatos
import com.example.projeto1.ui.components.PortraitCassetteCover
import com.example.projeto1.ui.components.AppBottomBar
import com.example.projeto1.ui.components.ChatComponent
import com.example.projeto1.ui.theme.ColorBackground
import com.example.projeto1.ui.theme.ColorGold
import com.example.projeto1.ui.theme.ColorSurface
import com.example.projeto1.ui.theme.ColorSurfaceDim
import com.example.projeto1.ui.theme.ColorTextPrimary
import com.example.projeto1.ui.theme.ColorTextSecondary
import com.example.projeto1.ui.viewmodel.ChatViewModel
import com.example.projeto1.ui.viewmodel.MusicViewModel
import com.example.projeto1.ui.viewmodel.PlaylistViewModel
import java.time.LocalDateTime


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatsPage(
    onBack: () -> Unit,
    onNewChat: () -> Unit,
    onGoToContact: () -> Unit,
    onHome: () -> Unit,
    onLibrary: () -> Unit,
    onSettings: () -> Unit,
    musicViewModel: MusicViewModel = viewModel(factory = MusicViewModel.Factory),
    chatViewModel: ChatViewModel = viewModel(factory = ChatViewModel.Factory),
    playlistViewModel: PlaylistViewModel = viewModel(factory = PlaylistViewModel.Factory)
) {
    val query by musicViewModel.query.collectAsState()
    val ui by musicViewModel.searchUi.collectAsState()
    val currentTrack by musicViewModel.player.currentTrack.collectAsState()
    val isPlaying by musicViewModel.player.isPlaying.collectAsState()
    val lastPlayedWithCover by playlistViewModel.lastPlayedWithCover.collectAsState()
    val keyboard = LocalSoftwareKeyboardController.current

    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = ColorBackground,
        bottomBar = {
            Column() {
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.add_chat_button),
                        modifier = Modifier.size(64.dp).clickable(onClick = onNewChat),
                        contentDescription = null,
                    )
                }
                AppBottomBar(
                    currentRoute = "player",
                    onSkipBack = { musicViewModel.previous() },
                    onHome = onHome,
                    onLibrary = onLibrary,
                    onSettings = onSettings,
                    track = currentTrack,
                    isPlaying = isPlaying,
                    onTogglePlay = { musicViewModel.togglePlay() },
                    onClick = onBack
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(top= (32.dp))
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
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxSize()
                ) {
                    Text(
                        text = stringResource(R.string.chats_title),
                        color = ColorTextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center
                    )
                }
                Column(
                    modifier = Modifier.fillMaxSize().padding(top = 56.dp, bottom = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    OutlinedTextField(
                        value = query,
                        onValueChange = musicViewModel::onQueryChanged,
                        placeholder = { Text(stringResource(R.string.chat_search)) },
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
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp)
                    )
                    Column(
                        modifier = Modifier.verticalScroll(rememberScrollState())
                    ) {
                        contatos.forEach {
                            contato -> ChatComponent(
                                contato= contato,
                                onClick = {
                                    chatViewModel.setContato(contato);
                                    onGoToContact()
                                }
                                )
                        }
                        Spacer(modifier = Modifier.height(160.dp))
                    }
                }
            }

        }
    }
}