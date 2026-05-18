package com.example.projeto1.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.projeto1.R
import com.example.projeto1.data.contatos
import com.example.projeto1.ui.components.AppBottomBar
import com.example.projeto1.ui.components.ChatComponent
import com.example.projeto1.ui.theme.ColorBackground
import com.example.projeto1.ui.theme.ColorDarkBackground
import com.example.projeto1.ui.theme.ColorGold
import com.example.projeto1.ui.theme.ColorGoldDim
import com.example.projeto1.ui.theme.ColorSurface
import com.example.projeto1.ui.theme.ColorTextPrimary
import com.example.projeto1.ui.theme.ColorTextSecondary
import com.example.projeto1.ui.viewmodel.ChatViewModel
import com.example.projeto1.ui.viewmodel.MusicViewModel
import com.example.projeto1.ui.viewmodel.PlaylistViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NewChatScreen(
    onBack: () -> Unit,
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
                        text = stringResource(R.string.new_chat_title),
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
                    Column(
                        modifier = Modifier.verticalScroll(rememberScrollState())
                    ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical= 8.dp, horizontal = 16.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .clip(RoundedCornerShape(32.dp))
                                        .background(ColorGold)
                                ) {
//                                    if (contato.profilePicture != null) {
//                                        AsyncImage(
//                                            model = contato.profilePicture,
//                                            contentDescription = null,
//                                            contentScale = ContentScale.FillHeight
//                                        )
//                                    }
                                    Column(
                                        modifier = Modifier.fillMaxHeight().fillMaxWidth(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Icon(Icons.Filled.People, contentDescription = null, modifier= Modifier.size(40.dp), tint = ColorDarkBackground)
                                    }
                                }
                                Spacer(Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = stringResource(R.string.new_group),
                                        color = ColorTextPrimary,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontSize = 18.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                            Text(
                                text = stringResource(R.string.your_contacts),
                                color = ColorTextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
                            )
                            contatos.forEach {
                                contato ->
                                ChatComponent(
                                    contato = contato,
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