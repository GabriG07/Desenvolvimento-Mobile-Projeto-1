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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.projeto1.R
import com.example.projeto1.ui.components.AppBottomBar
import com.example.projeto1.ui.theme.ColorBackground
import com.example.projeto1.ui.theme.ColorDivider
import com.example.projeto1.ui.theme.ColorError
import com.example.projeto1.ui.theme.ColorGold
import com.example.projeto1.ui.theme.ColorSurface
import com.example.projeto1.ui.theme.ColorTextPrimary
import com.example.projeto1.ui.theme.ColorTextSecondary
import com.example.projeto1.ui.theme.ColorTextTertiary

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onHome: () -> Unit,
    onLibrary: () -> Unit
) {
    var crossfade by remember { mutableStateOf(true) }
    var autoplay by remember { mutableStateOf(true) }
    var notifications by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = ColorBackground,
        bottomBar = {
            AppBottomBar(
                currentRoute = "settings",
                onSkipBack = {},
                onHome = onHome,
                onLibrary = onLibrary,
                onSettings = { }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding).fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Top bar com botão de voltar + titulo
            item {
                Box(modifier = Modifier.fillMaxWidth()) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.action_back),
                            tint = ColorTextPrimary
                        )
                    }
                    Text(
                        text = stringResource(R.string.settings_title),
                        color = ColorTextPrimary,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            // Card do perfil
            item { ProfileCard() }

            //Seção de reprodução
            item {
                SectionLabel(text = stringResource(R.string.settings_section_playback))
            }
            item {
                Surface(color = ColorSurface, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                    Column {
                        SettingRowChevron(
                            title = stringResource(R.string.settings_audio_quality),
                            subtitle = stringResource(R.string.settings_audio_quality_value)
                        )
                        Divider()
                        SettingRowValue(
                            title = stringResource(R.string.settings_equalizer),
                            value = stringResource(R.string.settings_equalizer_value),
                            valueColor = ColorGold
                        )
                        Divider()
                        SettingRowSwitch(
                            title = stringResource(R.string.settings_crossfade),
                            checked = crossfade,
                            onCheckedChange = { crossfade = it }
                        )
                        Divider()
                        SettingRowSwitch(
                            title = stringResource(R.string.settings_autoplay),
                            checked = autoplay,
                            onCheckedChange = { autoplay = it }
                        )
                    }
                }
            }

            // Seção geral
            item {
                SectionLabel(text = stringResource(R.string.settings_section_general))
            }
            item {
                Surface(color = ColorSurface, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                    Column {
                        SettingRowSwitch(
                            title = stringResource(R.string.settings_notifications),
                            checked = notifications,
                            onCheckedChange = { notifications = it }
                        )
                        Divider()
                        SettingRowChevron(
                            title = stringResource(R.string.settings_language),
                            subtitle = stringResource(R.string.settings_language_value)
                        )
                        Divider()
                        SettingRowChevron(
                            title = stringResource(R.string.settings_theme),
                            subtitle = stringResource(R.string.settings_theme_value)
                        )
                    }
                }
            }

            // Seção da conta
            item {
                SectionLabel(text = stringResource(R.string.settings_section_account))
            }
            item {
                Surface(color = ColorSurface, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                    Column {
                        SettingRowChevron(
                            title = stringResource(R.string.settings_about),
                            subtitle = null
                        )
                        Divider()
                        Text(
                            text = stringResource(R.string.settings_logout),
                            color = ColorError,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileCard() {
    Surface(color = ColorSurface, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(50))
                    .background(ColorGold),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "GH",
                    color = ColorBackground,
                    style = MaterialTheme.typography.titleLarge
                )
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.settings_user_name),
                    color = ColorTextPrimary,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = stringResource(R.string.settings_user_plan),
                    color = ColorTextSecondary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = ColorTextSecondary
            )
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        color = ColorTextTertiary,
        style = MaterialTheme.typography.labelSmall,
        modifier = Modifier.padding(horizontal = 4.dp)
    )
}

@Composable
private fun SettingRowChevron(title: String, subtitle: String?) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, color = ColorTextPrimary, style = MaterialTheme.typography.titleMedium)
            if (subtitle != null) {
                Text(text = subtitle, color = ColorTextSecondary, style = MaterialTheme.typography.bodySmall)
            }
        }
        Icon(
            imageVector = Icons.Filled.ChevronRight,
            contentDescription = null,
            tint = ColorTextSecondary
        )
    }
}

@Composable
private fun SettingRowValue(title: String, value: String, valueColor: androidx.compose.ui.graphics.Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Text(text = title, color = ColorTextPrimary, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
        Text(text = value, color = valueColor, style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.width(4.dp))
        Icon(
            imageVector = Icons.Filled.ChevronRight,
            contentDescription = null,
            tint = valueColor
        )
    }
}

@Composable
private fun SettingRowSwitch(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(text = title, color = ColorTextPrimary, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = ColorTextPrimary,
                checkedTrackColor = ColorGold,
                uncheckedThumbColor = ColorTextPrimary,
                uncheckedTrackColor = ColorDivider
            )
        )
    }
}

@Composable
private fun Divider() {
    HorizontalDivider(color = ColorDivider, thickness = 0.5.dp)
}