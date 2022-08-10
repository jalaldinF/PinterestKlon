package uz.example.apppinterest.model.search

import uz.example.apppinterest.model.homephoto.Urls

data class Result(
    val id:String,
    val color:String,
    val description:String,
    val urls: Urls
)
