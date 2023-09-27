package com.groupec.githubfetchercompose.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.groupec.githubfetchercompose.presentation.utils.SingleClickIconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBarWithArrow(
    title: String,
    pressOnBack: () -> Unit
) {
    TopAppBar(
        title = { Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        ) },
        navigationIcon = {
            SingleClickIconButton(
                onClick = {
                    pressOnBack()
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        }
    )
}