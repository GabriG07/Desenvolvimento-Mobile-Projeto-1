package com.example.projeto1.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.projeto1.R
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
    modifier: Modifier = Modifier
) {
    val onSettingsRoute = currentRoute == "settings"
    val thirdIcon: ImageVector = if (onSettingsRoute) Icons.Filled.Settings else Icons.Filled.Bookmark
    val thirdLabel = if (onSettingsRoute) R.string.nav_settings else R.string.nav_library
    val thirdAction: () -> Unit = if (onSettingsRoute) onSettings else onLibrary
    val thirdActive = currentRoute == "library" || currentRoute == "settings"

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(16.dp))
            .height(64.dp),
        color = ColorSurface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(ColorSurface),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BarButton(
                icon = Icons.Filled.SkipPrevious,
                contentDesc = stringResource(R.string.nav_skip_back),
                active = false,
                onClick = onSkipBack
            )
            BarButton(
                icon = Icons.Filled.Home,
                contentDesc = stringResource(R.string.nav_home),
                active = currentRoute == "home",
                onClick = onHome
            )
            BarButton(
                icon = thirdIcon,
                contentDesc = stringResource(thirdLabel),
                active = thirdActive,
                onClick = thirdAction
            )
        }
    }
}

@Composable
private fun BarButton(
    icon: ImageVector,
    contentDesc: String,
    active: Boolean,
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = icon,
            contentDescription = contentDesc,
            tint = if (active) ColorGold else ColorTextPrimary
        )
    }
}