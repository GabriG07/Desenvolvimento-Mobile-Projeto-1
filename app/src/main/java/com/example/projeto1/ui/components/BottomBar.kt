package com.example.projeto1.ui.components

import android.R.attr.top
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.projeto1.R
import com.example.projeto1.data.Track
import com.example.projeto1.ui.theme.ColorDarkBackground
import com.example.projeto1.ui.theme.ColorGold
import com.example.projeto1.ui.theme.ColorSurface
import com.example.projeto1.ui.theme.ColorTextPrimary

@Composable
fun AppBottomBar(
    currentRoute: String,
    onSkipBack: () -> Unit,
    onHome: () -> Unit,
    onLibrary: () -> Unit,
    onSettings: () -> Unit,
    track: Track?,
    isPlaying: Boolean?,
    onTogglePlay: (() -> Unit?)?,
    onClick: (() -> Unit?)?,
    modifier: Modifier = Modifier
) {

    var barHeight = 80;
    if (track != null) {
        barHeight = 160;
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(16.dp))
            .height(barHeight.dp),
        color = ColorSurface
    ) {
        Column() {
            if (onClick == null) {}
            else {
                MiniPlayer(
                    track = track,
                    isPlaying = isPlaying == true,
                    onTogglePlay = onTogglePlay as () -> Unit,
                    onClick = onClick as () -> Unit,
                    modifier = modifier,
                );
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ColorSurface),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BarButton(
                    icon = Icons.Filled.Settings,
                    contentDesc = stringResource(R.string.nav_skip_back),
                    active = currentRoute == "settings",
                    onClick = onSettings,
                )
                BarButton(
                    icon = Icons.Filled.Home,
                    contentDesc = stringResource(R.string.nav_home),
                    active = currentRoute == "home",
                    onClick = onHome
                )
                BarButton(
                    icon = Icons.Filled.Bookmark,
                    contentDesc = stringResource(R.string.nav_library),
                    active = currentRoute == "library",
                    onClick = onLibrary,
                )
            }
        }
    }
}

@Composable
private fun BarButton(
    icon: ImageVector,
    contentDesc: String,
    active: Boolean,
    onClick: () -> Unit,
) {
    Card(modifier = Modifier.size(height = (80.dp), width = (100.dp)), colors = CardDefaults.cardColors(
        containerColor = if (active) ColorDarkBackground else ColorSurface
    )) {
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center) {
            IconButton(
                onClick = onClick, modifier = Modifier.padding(top = (16.dp))) {
                Icon(
                    imageVector = icon,
                    modifier = Modifier.size(64.dp),
                    contentDescription = contentDesc,
                    tint = if (active) ColorGold else ColorTextPrimary
                )
            }

        }
    }
}