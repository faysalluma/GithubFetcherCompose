package com.groupec.githubfetchercompose.utils.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.groupec.githubfetchercompose.data.dto.RepositoryDTO

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarListIn(items: List<String>, onclickItem: (String) -> Unit) {
    var text by remember { mutableStateOf("") } // Query for SearchBar
    var active by remember { mutableStateOf(false) } // Active state for SearchBar
    val filteredItems = items.filter { it.contains(text, ignoreCase = true) }

    SearchBar(modifier = Modifier.fillMaxWidth(),
        colors = SearchBarDefaults.colors(containerColor = Color.White),
        query = text,
        onQueryChange = {
            text = it
        },
        onSearch = {
            active = active // spread out search bar list content
        },
        active = active,
        onActiveChange = {
            active = it
        },
        placeholder = {
            Text(text = "Enter your query")
        },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search icon")
        },
        trailingIcon = {
            if (active) {
                Icon(
                    modifier = Modifier.clickable {
                        if (text.isNotEmpty()) {
                            text = ""
                        } else {
                            active = false
                        }
                    },
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close icon"
                )
            }
        }
    )
    {
        filteredItems.forEach {
            if (it.isNotEmpty()) {
                Box (modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onclickItem(it) }) {
                    Row (modifier = Modifier.padding(all = 14.dp)) {
                        Icon(imageVector = Icons.Default.History, contentDescription = null)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = it)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarListHistory(onclickItem: (String) -> Unit) {
    var text by remember { mutableStateOf("") } // Query for SearchBar
    var active by remember { mutableStateOf(false) } // Active state for SearchBar
    val searchHistoryList = remember { mutableStateListOf("") }

    // Search Bar
    SearchBar(modifier = Modifier.fillMaxWidth(),
        colors = SearchBarDefaults.colors(containerColor = Color.White),
        query = text,
        onQueryChange = {
            text = it
        },
        onSearch = {
            active = false
            if (text.isNotEmpty()) searchHistoryList.add(text)
            text=""
        },
        active = active, // spread out search bar list content
        onActiveChange = {
            active = it
        },
        placeholder = {
            Text(text = "Enter your query")
        },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search icon")
        },
        trailingIcon = {
            if (active) {
                Icon(
                    modifier = Modifier.clickable {
                        if (text.isNotEmpty()) {
                            text = ""
                        } else {
                            active = false
                        }
                    },
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close icon"
                )
            }
        }
    )
    {
        searchHistoryList.forEach {
            if (it.isNotEmpty()) {
                Box (modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onclickItem(it) }) {
                    Row (modifier = Modifier.padding(all = 14.dp)) {
                        Icon(imageVector = Icons.Default.History, contentDescription = null)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = it)
                    }
                }
            }
        }

        Text(
            modifier = Modifier
                .padding(all = 14.dp)
                .fillMaxWidth()
                .clickable {
                    searchHistoryList.clear()
                },
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            text = "Clear all history"
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarListOut(items: List<RepositoryDTO>, onclickItem: (RepositoryDTO) -> Unit) {
    var text by remember { mutableStateOf("") } // Query for SearchBar
    var active by remember { mutableStateOf(false) } // Active state for SearchBar
    val filteredItems = items.filter { it.full_name.contains(text, ignoreCase = true) }

    Column (modifier = Modifier
        .fillMaxSize()
        .padding(10.dp)){

        // Search Bar
        SearchBar(modifier = Modifier.fillMaxWidth(),
            // colors = SearchBarDefaults.colors(containerColor = Color.White),
            query = text,
            onQueryChange = {
                text = it
            },
            onSearch = {
                active = false
            },
            active = false, // spread out search bar list content
            onActiveChange = {
                active = it
            },
            placeholder = {
                Text(text = "Enter your query")
            },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search icon")
            },
            trailingIcon = {
                if (active) {
                    Icon(
                        modifier = Modifier.clickable {
                            if (text.isNotEmpty()) {
                                text = ""
                            } else {
                                active = false
                            }
                        },
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close icon"
                    )
                }
            }
        )
        {

        }

        Spacer(modifier = Modifier.height(8.dp))

        // List
        LazyColumn {
            items(filteredItems) { item ->
                // Display your filtered items here
                Column (modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onclickItem(item) }) {
                    ListItem(
                        headlineContent = { Text(item.full_name, fontWeight = FontWeight.Bold) },
                        supportingContent = {
                            item.description?.let { Text(it) }
                        },
                        /*leadingContent = {
                            Icon(
                                Icons.Filled.Favorite,
                                contentDescription = "Localized description",
                            )
                        },*/
                        trailingContent = {
                            Icon(
                                Icons.Filled.Star,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    )
                    Divider()
                }
            }
        }
    }
}