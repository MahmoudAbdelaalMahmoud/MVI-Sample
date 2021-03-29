package com.mahmoud.mvisample.data.remote


import com.google.gson.annotations.SerializedName

data class ResponseDto(@SerializedName("next")
                       val next: String ,
                       @SerializedName("previous")
                       val previous: String ,
                       @SerializedName("count")
                       val count: Int ,
                       @SerializedName("results")
                       val results: List<RecipeDto>?)


data class RecipeDto(@SerializedName("date_updated")
                     val dateUpdated: String ,
                     @SerializedName("rating")
                     val rating: Int ,
                     @SerializedName("description")
                     val description: String ,
                     @SerializedName("title")
                     val title: String ,
                     @SerializedName("long_date_added")
                     val longDateAdded: Int ,
                     @SerializedName("featured_image")
                     val featuredImage: String ,
                     @SerializedName("source_url")
                     val sourceUrl: String ,
                     @SerializedName("date_added")
                     val dateAdded: String ,
                     @SerializedName("long_date_updated")
                     val longDateUpdated: Int ,
                     @SerializedName("publisher")
                     val publisher: String ,
                     @SerializedName("ingredients")
                     val ingredients: List<String>?,
                     @SerializedName("pk")
                     val pk: Int )


