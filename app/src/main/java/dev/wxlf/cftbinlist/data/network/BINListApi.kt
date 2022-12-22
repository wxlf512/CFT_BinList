package dev.wxlf.cftbinlist.data.network

import dev.wxlf.cftbinlist.data.entities.BINInfoEntity
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface BINListApi {

    @Headers("Accept-Version: 3")
    @GET("./{BIN}")
    suspend fun loadBINInfo(@Path("BIN") bin: String) : BINInfoEntity
}