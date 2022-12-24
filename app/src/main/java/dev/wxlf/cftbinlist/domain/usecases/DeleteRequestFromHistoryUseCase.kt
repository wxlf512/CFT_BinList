package dev.wxlf.cftbinlist.domain.usecases

import dev.wxlf.cftbinlist.data.entities.RequestEntity
import dev.wxlf.cftbinlist.domain.repositories.RequestsRepository

class DeleteRequestFromHistoryUseCase(private val requestsRepository: RequestsRepository) {
    suspend fun execute(requestEntity: RequestEntity) =
        requestsRepository.deleteRequest(requestEntity)
}