package com.example.projeto1.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import coil.compose.AsyncImage
import com.example.projeto1.R

//Cassete mostrado no player
@Composable
fun PortraitCassetteCover(
    coverUrl: String?,
    modifier: Modifier = Modifier,
    fallbackColor: Color = Color(0xFF7A1414)
) {
    Box(
        modifier = modifier
            .aspectRatio(2f / 3f)
            .clip(RoundedCornerShape(8.dp))
            .background(fallbackColor)
    ) {
        // Imagem da capa da música por trás
        if (coverUrl != null) {
            AsyncImage(
                model = coverUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x33000000))
        )

        // "Furos" do cassete
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val r = w * 0.17f
            val cx = w / 2f

            val reelYs = listOf(h * 0.30f, h * 0.70f)
            for (cy in reelYs) {
                drawCircle(color = Color(0xFF0E1014), radius = r, center = Offset(cx, cy))

                drawCircle(
                    color = Color(0xFF2A2D36),
                    radius = r,
                    center = Offset(cx, cy),
                    style = Stroke(width = 2.5f)
                )

                drawCircle(
                    color = Color(0xFF050507),
                    radius = r * 0.28f,
                    center = Offset(cx, cy)
                )
            }
        }
    }
}

//Cassete exibido na página inicial, onde mostramos a última playlist tocada
@Composable
fun BannerCassette(
    coverUrl: String?,
    modifier: Modifier = Modifier,
    fallbackColor: Color = Color(0xFF7A1414)
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(fallbackColor)
    ) {
        // Background
        if (coverUrl != null) {
            AsyncImage(
                model = coverUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x55000000))
        )

        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val r = minOf(w, h) * 0.18f
            val cy = h * 0.45f
            val leftX = w * 0.33f
            val rightX = w * 0.67f

            val bandHeight = h * 0.40f
            drawRect(
                color = Color(0x55101218),
                topLeft = Offset(0f, cy - bandHeight / 2f),
                size = Size(w, bandHeight)
            )


//            for (cx in listOf(leftX, rightX)) {
//                drawCircle(color = Color(0xFF111319), radius = r, center = Offset(cx, cy))
//                drawCircle(
//                    color = Color(0xFF353841),
//                    radius = r,
//                    center = Offset(cx, cy),
//                    style = Stroke(width = 4f)
//                )
//
//                drawCircle(color = Color(0xFF050507), radius = r * 0.30f, center = Offset(cx, cy))
//            }
        }

        val imageModifier = Modifier.size(240.dp)

        Column(
            modifier = Modifier.padding(bottom = (24.dp))
        ){
            Row(
                Modifier.fillMaxWidth().wrapContentHeight(),
                horizontalArrangement = Arrangement.Center,
            ){
                Image(
                    painter = painterResource(R.drawable.tape_detail),
//                contentDescription = stringResource(id = R.string.dog_content_description)
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                    modifier = imageModifier,
                )
            }

        }
    }
}


// Cassete mostrado no mini player
@Composable
fun MiniCassette(
    coverUrl: String?,
    modifier: Modifier = Modifier,
    fallbackColor: Color = Color(0xFF7A1414),
    accentColor: Color = Color(0xFF9B1B1B)
) {

    Box(
        modifier = modifier
            .aspectRatio(1.6f)
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0xFF111319))
    ) {

        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val bandH = h * 0.10f
            drawRect(color = accentColor, topLeft = Offset(0f, 0f),
                size = androidx.compose.ui.geometry.Size(w, bandH))
            drawRect(color = accentColor, topLeft = Offset(0f, h - bandH),
                size = androidx.compose.ui.geometry.Size(w, bandH))
        }

        Box(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.70f)
                .align(Alignment.Center)
                .clip(RoundedCornerShape(3.dp))
                .background(fallbackColor)
        ) {
            if (coverUrl != null) {
                AsyncImage(
                    model = coverUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height
                val r = h * 0.18f
                val cy = h * 0.5f
                val leftX = w * 0.30f
                val rightX = w * 0.70f
                for (cx in listOf(leftX, rightX)) {
                    drawCircle(color = Color(0xCC000000), radius = r, center = Offset(cx, cy))
                    drawCircle(color = Color(0xFF202228), radius = r * 0.45f, center = Offset(cx, cy))
                }
            }
        }
    }
}