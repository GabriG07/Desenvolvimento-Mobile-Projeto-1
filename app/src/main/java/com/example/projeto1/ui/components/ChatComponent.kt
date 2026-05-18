package com.example.projeto1.ui.components

import android.R.attr.top
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.projeto1.data.Contact
import com.example.projeto1.ui.theme.ColorBackground
import com.example.projeto1.ui.theme.ColorSurface
import com.example.projeto1.ui.theme.ColorTextPrimary
import com.example.projeto1.ui.theme.ColorTextSecondary

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatComponent(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    contato: Contact,
) {

    Surface(
        modifier = modifier
            .fillMaxWidth().
            clickable(onClick = onClick)
            .height(80.dp),
        color = ColorBackground
    ) {
        OutlinedCard(
            border = BorderStroke(1.dp, ColorSurface),
            colors = CardDefaults.cardColors(
                containerColor = ColorBackground,
                ),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .background(ColorBackground),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(32.dp))
                            .background(ColorSurface)
                    ) {
                        if (contato.profilePicture != null) {
                            AsyncImage(
                                model = contato.profilePicture,
                                contentDescription = null,
                                contentScale = ContentScale.FillHeight
                                )
                        }
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = contato.name,
                            color = ColorTextPrimary,
                            style = MaterialTheme.typography.titleMedium,
                            fontSize = 18.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(

                        ) {
                            Text(
                                text = contato.lastMessage.message,
                                color = ColorTextSecondary,
                                style = MaterialTheme.typography.bodyMedium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(Modifier.weight(1f))
                            Text(
                                text = "${contato.lastMessage.time.hour}:${contato.lastMessage.time.minute}",
                                color = ColorTextSecondary,
                                style = MaterialTheme.typography.bodyMedium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }

    }
}