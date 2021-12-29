package com.example.hoichoihome.data.model

data class ContentData(
    val categories: List<Category>?,
    val creditBlocks: List<CreditBlock>?,
    val gist: Gist?,
    val id: String?,
    val images: Images?,
    val monetizationModels: List<MonetizationModel>?,
    val plans: List<Plan>?,
    val pricing: Pricing?,
    val seasons: List<Season>?,
    val seo: SeoX?,
    val showDetails: ShowDetails?,
    val site: String?,
    val tags: List<Tag>?
)