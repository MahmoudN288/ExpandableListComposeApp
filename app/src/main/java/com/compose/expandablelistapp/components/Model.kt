package com.compose.expandablelistapp.components

data class ListOfObjects(val listOf: List<Objects>)
data class Objects(val group: String, val name: String, val rank: String, val points: String)