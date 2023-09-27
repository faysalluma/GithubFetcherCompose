package com.groupec.githubfetchercompose.utils.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.groupec.githubfetchercompose.R
import com.groupec.githubfetchercompose.data.dto.ContributorDTO
import com.groupec.githubfetchercompose.data.dtole.BranchDTO


@Composable
fun BranchList (items: List<BranchDTO>) {
    LazyColumn {
        items(items) { item ->
            // Display your filtered items here
            Column (modifier = Modifier.fillMaxWidth() ){
                ListItem(
                    headlineContent = { Text(item.name) }
                )
            }
        }
    }
}

@Composable
fun ContributorList (items: List<ContributorDTO>, onclickItem: (ContributorDTO) -> Unit) {
    LazyColumn {
        items(items) { item ->
            // Display your filtered items here
            Column (modifier = Modifier
                .fillMaxWidth()
                .clickable { onclickItem(item) }) {
                ListItem(
                    headlineContent = { Text(item.login, fontWeight = FontWeight.Bold) },
                    trailingContent = {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(item.avatar_url)
                                .crossfade(true)
                                .build(),
                            placeholder = painterResource(R.drawable.baseline_account_box_24),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(50.dp)
                                .clip(CircleShape)
                                // .border(2.dp, Color.Gray, CircleShape),
                            // colorFilter = ColorFilter.tint(Color.Blue)
                        )
                    }
                )
                Divider()
            }
        }
    }
}