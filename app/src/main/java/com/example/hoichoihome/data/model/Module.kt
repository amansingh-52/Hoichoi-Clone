package com.example.hoichoihome.data.model

data class Module(
    val contentData: List<ContentData>?,
    val contentType: String?,
    val hasNext: Boolean?,
    val hideTags: Boolean?,
    val id: String?,
    val moduleType: String?,
    val schedulingMap: SchedulingMap?,
    val settings: Settings?,
    val title: String?
)