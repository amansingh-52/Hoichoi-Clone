package com.example.hoichoihome.data.model

data class CreditBlock(
    val containsHollywoodCelebrities: Boolean?,
    val containsTVCelebrities: Boolean?,
    val credits: List<Credit>?,
    val title: String?
)