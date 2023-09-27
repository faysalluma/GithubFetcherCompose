package com.groupec.githubfetchercompose.presentation.utils

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.groupec.githubfetchercompose.presentation.theme.Green
import com.groupec.githubfetchercompose.R

@Composable
fun ClickButton(onClick: ()->Unit, text : String) {
    MaterialTheme {
        Button(
            onClick = { onClick() },
            colors = ButtonDefaults.buttonColors(backgroundColor = Green, contentColor = Color.White),
            modifier = Modifier.padding(top = 14.dp),
            // Uses ButtonDefaults.ContentPadding by default
            /*  contentPadding = PaddingValues(
                  start = 12.dp,
                  top = 12.dp,
                  end = 20.dp,
                  bottom = 12.dp
              )*/
        ) {
            Text(text = text)
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            // Inner content including an icon and a text label
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_arrow_forward_24),
                contentDescription = null,
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
        }
    }

}

@Composable
fun SampleButton(onClick: ()->Unit, text : String) {
    MaterialTheme {
        Button(
            onClick = { onClick() },
            colors = ButtonDefaults.buttonColors(backgroundColor = Green, contentColor = Color.White),
        ) {
            Text(text = text)
        }
    }
}

@Composable
fun MyPrimaryButton(onClick: ()->Unit, text : String) {
    MaterialTheme {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onClick() },
            colors = ButtonDefaults.buttonColors(backgroundColor = Green, contentColor = Color.White),
            // Uses ButtonDefaults.ContentPadding by default
            contentPadding = PaddingValues(16.dp)
        ) {
            Text(text = text, fontSize = 16.sp)
        }
    }
}

