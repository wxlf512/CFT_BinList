package dev.wxlf.cftbinlist.domain.usecases

import dev.wxlf.cftbinlist.domain.repositories.BINInfoRepository

class FetchBINInfoUseCase(private val binInfoRepository: BINInfoRepository) {
    suspend fun execute(bin: String) = binInfoRepository.fetchBINInfo(bin)
}