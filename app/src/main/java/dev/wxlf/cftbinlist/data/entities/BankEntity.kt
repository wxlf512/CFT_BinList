package dev.wxlf.cftbinlist.data.entities

import com.google.gson.annotations.SerializedName


data class BankEntity (

  @SerializedName("name"  ) var name  : String? = null,
  @SerializedName("url"   ) var url   : String? = null,
  @SerializedName("phone" ) var phone : String? = null,
  @SerializedName("city"  ) var city  : String? = null

)