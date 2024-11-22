package com.example.flickrsearch

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun MainScreen(viewModel: MainViewModel = viewModel()) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "imageGrid") {
        composable("imageGrid") {
            ImageGrid(viewModel = viewModel, onImageClick = { image ->
                navController.navigate(
                    "detailView/${image.title}/${image.description}/${image.author}/${image.published}/${image.media.m}"
                )
            })
        }
        composable(
            route = "detailView/{title}/{description}/{author}/{published}/{imageUrl}",
            arguments = listOf(
                navArgument("title") { type = NavType.StringType },
                navArgument("description") { type = NavType.StringType },
                navArgument("author") { type = NavType.StringType },
                navArgument("published") { type = NavType.StringType },
                navArgument("imageUrl") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title") ?: ""
            val description = backStackEntry.arguments?.getString("description") ?: ""
            val author = backStackEntry.arguments?.getString("author") ?: ""
            val published = backStackEntry.arguments?.getString("published") ?: ""
            val imageUrl = backStackEntry.arguments?.getString("imageUrl") ?: ""
            DetailView(title, description, author, published, imageUrl)
        }
    }
}

@Composable
fun ImageGrid(viewModel: MainViewModel, onImageClick: (ImageItem) -> Unit) {
    val images by viewModel.images.collectAsState()
    val loading by viewModel.loading.collectAsState()
    var query by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize()) {
        TextField(
            value = query,
            onValueChange = {
                query = it
                viewModel.searchImages(it)
            },
            label = { Text("Search") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        if (loading) {
            CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 128.dp),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(images.size) { index ->
                    val image = images[index]
                    ImageCard(image = image, onClick = { onImageClick(image) })
                }
            }
        }
    }
}

@Composable
fun ImageCard(image: ImageItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = 4.dp
    ) {
        Column(Modifier.padding(8.dp)) {
            Image(
                painter = rememberAsyncImagePainter(image.media.m),
                contentDescription = image.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = image.title, maxLines = 1, textAlign = TextAlign.Center, color = Color.Black)
        }
    }
}

@Composable
fun DetailView(title: String, description: String, author: String, published: String, imageUrl: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = rememberAsyncImagePainter(imageUrl),
            contentDescription = title,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Title: $title", style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Description: $description", style = MaterialTheme.typography.body1)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Author: $author", style = MaterialTheme.typography.body1)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Published: $published", style = MaterialTheme.typography.body1)
    }
}
