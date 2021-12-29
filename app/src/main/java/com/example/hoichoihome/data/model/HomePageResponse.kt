package com.example.hoichoihome.data.model

import java.lang.StringBuilder

data class HomePageResponse(
    val categoryMap: Map<String , String>? = mapOf(),
    val id: String?,
    val metadataMap: MetadataMap?,
    val moduleCount: Int?,
    val moduleIds: List<String>?,
    val modules: List<Module>?
)