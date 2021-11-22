package com.skillbox.ascentstrava.presentation.activities.list

import com.skillbox.ascentstrava.presentation.activities.data.ActivityItem

data class PagingState(
    val limit: Int,
    var loadingPage: Boolean,
    var pageCount: Int,
    val itemsList: List<ActivityItem>,
    var hasMore: Boolean,
    var isFirstLoad: Boolean
) {
    val offset
        get() = (pageCount - 1) * limit
}