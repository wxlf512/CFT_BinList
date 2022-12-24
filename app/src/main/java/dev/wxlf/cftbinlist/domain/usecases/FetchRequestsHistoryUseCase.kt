package dev.wxlf.cftbinlist.domain.usecases

import dev.wxlf.cftbinlist.domain.repositories.RequestsRepository

class FetchRequestsHistoryUseCase(private val requestsRepository: RequestsRepository) {
    suspend fun execute() = requestsRepository.fetchRequestsHistory()
}