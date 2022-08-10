package uz.example.apppinterest.model.relatedcollection

import uz.example.apppinterest.model.homephoto.Urls
data class SinglePhoto(
    val id: String,
    val urls: Urls,
    val likes: Long,
    val related_collections: RelatedCollections,
)
