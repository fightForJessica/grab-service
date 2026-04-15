package com.jessi.grabservice.ui.page

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.jessi.grabservice.R
import com.jessi.grabservice.model.AppInfo
import com.jessi.grabservice.model.HttpRsp
import com.jessi.grabservice.ui.HttpInfoItemLayout
import com.jessi.grabservice.ui.Tag
import com.jessi.grabservice.ui.lowerHalfConerBackground
import com.jessi.grabservice.ui.theme.ThemeManager
import com.jessi.grabservice.ui.upperHalfConerBackground
import com.jessi.grabservice.utils.getAppIconDrawable
import com.jessi.grabservice.viewmodel.MainViewModel
import top.sankokomi.wirebare.kernel.interceptor.http.HttpHeaderParser

const val RESPONSE_PAGE_NAME = "响应列表"

interface IResponsePageCallback {
    fun onResponseDeleteClick()
    fun onResponseItemClick(response: HttpRsp, appInfo: AppInfo)
}

@Composable
fun ResponsePage(
    context: Context,
    viewModel: MainViewModel,
    callback: IResponsePageCallback
) {
    val responseList = viewModel.responseList
    HttpInfosPage(
        infoList = responseList,
        emptyText = stringResource(R.string.response_empty),
        itemKey = { index, response ->
            "info_page_response:${response.id}"
        },
        itemContent = { index, response ->
            val targetAppInfo = viewModel.appInfoList.find {
                it.uid == response.sourceProcessUid
            }
            require(targetAppInfo != null)
            val modifier = when (index) {
                0 -> {
                    Modifier.upperHalfConerBackground(ThemeManager.colorTheme.cardBackgroundColor)
                }
                responseList.size - 1 -> {
                    Modifier.lowerHalfConerBackground(ThemeManager.colorTheme.cardBackgroundColor)
                }
                else -> {
                    Modifier.background(ThemeManager.colorTheme.cardBackgroundColor)
                }
            }
            HttpInfoItemLayout(
                modifier = modifier,
                iconDrawable = context.getAppIconDrawable(targetAppInfo.packageName),
                url = response.url ?: response.destinationAddress,
                headText = if (HttpHeaderParser.isHttpVersion(response.httpVersion)) {
                    response.formatHead?.firstOrNull()
                } else {
                    null
                },
                onClick = {
                    callback.onResponseItemClick(response, targetAppInfo)
                },
                tagContent = {
                    if (response.isHttps == true) {
                        Tag(stringResource(R.string.common_ssl))
                    }
                    if (HttpHeaderParser.isHttpVersion(response.httpVersion)) {
                        Tag(response.rspStatus)
                    }
                    Tag(response.contentEncoding)
                    Tag(response.contentType)
                }
            )
        },
        onDeleteFabClick = {
            callback.onResponseDeleteClick()
        }
    )

}