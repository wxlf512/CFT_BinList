package dev.wxlf.cftbinlist.data.entities

import com.google.gson.annotations.SerializedName


data class BINInfoEntity (

  @SerializedName("number"  ) var number  : NumberEntity?  = NumberEntity(),
  @SerializedName("scheme"  ) var scheme  : String?  = null,
  @SerializedName("type"    ) var type    : String?  = null,
  @SerializedName("brand"   ) var brand   : String?  = null,
  @SerializedName("prepaid" ) var prepaid : Boolean? = null,
  @SerializedName("country" ) var country : CountryEntity? = CountryEntity(),
  @SerializedName("bank"    ) var bank    : BankEntity?    = BankEntity()

)