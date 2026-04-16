package com.jessi.grabservice.ui.page

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.jessi.grabservice.R
import com.jessi.grabservice.model.AppInfo
import com.jessi.grabservice.model.HttpReq
import com.jessi.grabservice.ui.HttpInfoItemLayout
import com.jessi.grabservice.ui.Tag
import com.jessi.grabservice.ui.lowerHalfConerBackground
import com.jessi.grabservice.ui.theme.ThemeManager
import com.jessi.grabservice.ui.upperHalfConerBackground
import com.jessi.grabservice.utils.Logger
import com.jessi.grabservice.utils.getAppIconDrawable
import com.jessi.grabservice.viewmodel.MainViewModel
import top.sankokomi.wirebare.kernel.interceptor.http.HttpHeaderParser

const val REQUEST_PAGE_NAME = "请求列表"

interface IRequestPageCallback {
    fun onRequestItemClick(request: HttpReq, appInfo: AppInfo)
}

@Composable
fun RequestPage(
    context: Context,
    viewModel: MainViewModel,
    callback: IRequestPageCallback
) {
    val listState = rememberLazyListState()
    val requestList = viewModel.requestList

    LaunchedEffect(listState.isScrollInProgress) {
        Logger.i("RequestPage scrolling: ${listState.isScrollInProgress}")
        viewModel.setMainPageScrolling(listState.isScrollInProgress)
    }

    HttpInfosPage(
        infoList = requestList,
        emptyText = stringResource(R.string.request_empty),
        listState = listState,
        itemKey = { index, request ->
            "info_page_request:${request.id}"
        },
        itemContent = { index, request ->
            val targetAppInfo = viewModel.appInfoList.find {
                it.uid == request.sourceProcessUid
            }
            require(targetAppInfo != null)
            val modifier = when (index) {
                0 -> {
                    Modifier.upperHalfConerBackground(ThemeManager.colorTheme.cardBackgroundColor)
                }
                requestList.size - 1 -> {
                    Modifier.lowerHalfConerBackground(ThemeManager.colorTheme.cardBackgroundColor)
                }
                else -> {
                    Modifier.background(ThemeManager.colorTheme.cardBackgroundColor)
                }
            }
            HttpInfoItemLayout(
                modifier = modifier,
                iconDrawable = context.getAppIconDrawable(targetAppInfo.packageName),
                url = request.url ?: request.destinationAddress,
                headText = if (HttpHeaderParser.isHttpVersion(request.httpVersion)) {
                    request.formatHead?.firstOrNull()
                } else {
                    null
                },
                onClick = {
                    callback.onRequestItemClick(request, targetAppInfo)
                },
                tagContent = {
                    if (request.isHttps == true) {
                        Tag(stringResource(R.string.common_ssl))
                    }
                    if (HttpHeaderParser.isHttpVersion(request.httpVersion)) {
                        Tag(request.method)
                        Tag(request.httpVersion)
                    }
                }
            )
        },
        onDeleteFabClick = {
            viewModel.clearRequest()
        }
    )

}