package dev.wxlf.cftbinlist.data.entities

import com.google.gson.annotations.SerializedName


data class NumberEntity (

  @SerializedName("length" ) var length : Int?     = null,
  @SerializedName("luhn"   ) var luhn   : Boolean? = null

)