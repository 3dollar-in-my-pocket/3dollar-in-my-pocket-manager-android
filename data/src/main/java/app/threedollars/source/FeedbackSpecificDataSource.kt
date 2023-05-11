package app.threedollars.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import app.threedollars.data.model.ContentsModel
import app.threedollars.network.NetworkService
import java.text.SimpleDateFormat
import java.util.*

class FeedbackSpecificDataSource(
    private val networkService: NetworkService,
    private val targetId: String
) : PagingSource<String, ContentsModel>() {
    override suspend fun load(params: LoadParams<String>): LoadResult<String, ContentsModel> {
        return try {
            val form = SimpleDateFormat("yyyy-MM-dd")
            val currentDate = form.format(Date(System.currentTimeMillis()))

            val next = params.key ?: currentDate
            val response = networkService.getFeedbackSpecific("BOSS_STORE", targetId, next, next).body()
            LoadResult.Page(
                data = response?.data?.contents ?: listOf(),
                prevKey = null,
                nextKey = response?.data?.cursor?.nextCursor
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<String, ContentsModel>): String? {
        return state.anchorPosition?.let { state.closestPageToPosition(it)?.nextKey }
    }
}