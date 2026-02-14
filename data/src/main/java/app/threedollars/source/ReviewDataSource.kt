package app.threedollars.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import app.threedollars.data.model.ContentsModel
import app.threedollars.data.response.StoreReviewResponse
import app.threedollars.network.NetworkService
import java.text.SimpleDateFormat
import java.util.*

internal class ReviewDataSource(
    private val networkService: NetworkService,
    private val storeId: String,
    private val sort: String
) : PagingSource<String, StoreReviewResponse.StoreReview>() {
    override suspend fun load(params: LoadParams<String>): LoadResult<String, StoreReviewResponse.StoreReview> {
        return try {
            val nextCursor = params.key

            val response =
                networkService.getStoreReviews(
                    storeId = storeId,
                    sort = sort,
                    size = params.loadSize,
                    cursor = nextCursor
                ).body()
            LoadResult.Page(
                data = response?.data?.contents?.filter { it.status != "DELETED" } ?: emptyList(),
                prevKey = null,
                nextKey = response?.data?.cursor?.nextCursor
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<String, StoreReviewResponse.StoreReview>): String? {
        return state.anchorPosition?.let { state.closestPageToPosition(it)?.nextKey }
    }
}