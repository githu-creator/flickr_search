package com.example.flickrsearch

data class FlickrResponse(
    val items: List<ImageItem>
)

data class ImageItem(
    val title: String,
    val media: Media,
    val author: String,
    val description: String,
    val published: String
)

data class Media(
    val m: String // URL to the image
)
