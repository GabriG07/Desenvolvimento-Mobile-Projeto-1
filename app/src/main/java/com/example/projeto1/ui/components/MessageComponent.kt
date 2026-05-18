package com.example.projeto1.ui.components

import androidx.compose.material3.Card
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
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
import com.example.projeto1.ui.theme.ColorCardBlack
import com.example.projeto1.ui.theme.ColorCardWhite
import com.example.projeto1.ui.theme.ColorDarkBackground
import com.example.projeto1.ui.theme.ColorSurface
import com.example.projeto1.ui.theme.ColorTextPrimary
import com.example.projeto1.ui.theme.ColorTextSecondary

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MessageComponent(
    modifier: Modifier = Modifier,
    content: String,
    time: String,
    senderIsOther: Boolean,
) {

    Surface(
        color = ColorBackground
    ) {
                Row(
                    horizontalArrangement = if (!senderIsOther) Arrangement.End else Arrangement.Start,
                    modifier = Modifier.padding(
                        top = 8.dp, bottom = 8.dp,
                        start = if (!senderIsOther) 48.dp else 16.dp,
                        end = if (senderIsOther) 48.dp else 16.dp,
                        ).fillMaxHeight().fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.fillMaxHeight()
                    ) {
//                        Text(
//                            text = contato.name,
//                            color = ColorTextPrimary,
//                            style = MaterialTheme.typography.titleMedium,
//                            fontSize = 18.sp,
//                            maxLines = 1,
//                            overflow = TextOverflow.Ellipsis
//                        )
//                        Spacer(modifier = Modifier.height(8.dp))

                        Card(
                            modifier = Modifier.wrapContentHeight().wrapContentWidth(),
                            shape = RoundedCornerShape(
                                bottomStart = if (!senderIsOther) 24.dp else 0.dp,
                                bottomEnd = if (senderIsOther) 24.dp else 0.dp,
                                topStart = 24.dp,
                                topEnd = 24.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (!senderIsOther) ColorCardWhite else ColorCardBlack,
                            ),
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = (24.dp), vertical = (8.dp))
                            ) {
                                Column() {
                                    Text(
                                        text = content,
                                        fontSize = 16.sp,
                                        color = if (!senderIsOther) ColorCardBlack else ColorCardWhite,
                                        style = MaterialTheme.typography.bodyMedium,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End,
                                        ) {
                                        Text(
//                                text = "${contato.lastMessage.time.hour}:${contato.lastMessage.time.minute}",
//                                            "10:00",
                                            text = time,
                                            color = if (!senderIsOther) ColorCardBlack else ColorCardWhite,
                                            style = MaterialTheme.typography.bodyMedium,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }

                                }
//                                Spacer(Modifier.weight(1f))

                            }
                        }

                    }
                }


    }
}