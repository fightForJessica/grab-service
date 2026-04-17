package com.jessi.grabservice.ui.detail

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jessi.grabservice.R
import com.jessi.grabservice.model.AppInfo
import com.jessi.grabservice.model.HttpReq
import com.jessi.grabservice.ui.detail.widget.AppInfoItemLayout
import com.jessi.grabservice.ui.detail.widget.SingleInfoItem
import com.jessi.grabservice.ui.detail.widget.DetailInfoStatusPage
import com.jessi.grabservice.ui.detail.widget.IpInfoItemLayout
import com.jessi.grabservice.ui.detail.widget.RequestContentItemLayout
import com.jessi.grabservice.ui.detail.widget.SSLInfoItemLayout
import com.jessi.grabservice.ui.theme.ThemeManager
import com.jessi.grabservice.utils.orNone
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

const val REQUEST_DETAIL_PAGE_NAME = "请求详情"

@Composable
fun RequestDetailPage(
    context: Context,
    appInfo: AppInfo,
    request: HttpReq?,
    onScrollStateUpdate: (Boolean) -> Unit
) {

    val listState = rememberLazyListState()

    LaunchedEffect(listState.isScrollInProgress) {
        onScrollStateUpdate(listState.isScrollInProgress)
    }

    DetailInfoStatusPage(
        info = request,
        invalidText = stringResource(R.string.request_empty)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            state = listState,
            contentPadding = PaddingValues(
                top = dimensionResource(R.dimen.title_bar_height),
                bottom = dimensionResource(R.dimen.tab_layout_height)
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 抓包应用
            item {
                AppInfoItemLayout(context, appInfo)
            }

            // 请求链接
            item {
                SingleInfoItem(
                    drawableRes = R.drawable.ic_request_url,
                    title = stringResource(R.string.request_url),
                    contentText = it.url.orNone(),
                    colorFilter = ColorFilter.tint(ThemeManager.colorTheme.defaultFilterColor)
                )
            }

            // 请求时间
            item {
                val requestTime = it.requestTime?.let { time ->
                    Instant.fromEpochMilliseconds(time).toLocalDateTime(
                        TimeZone.currentSystemDefault()
                    ).toString()
                }.orNone()
                SingleInfoItem(
                    drawableRes = R.drawable.ic_request_time,
                    title = stringResource(R.string.request_time),
                    contentText = requestTime,
                    colorFilter = ColorFilter.tint(ThemeManager.colorTheme.defaultFilterColor)
                )
            }

            // IP 地址
            item {
                IpInfoItemLayout(
                    sourcePort = it.sourcePort,
                    destinationAddress = it.destinationAddress,
                    destinationPort = it.destinationPort
                )
            }

            // SSL/TLS
            item {
                // todo
                SSLInfoItemLayout()
            }

            // 请求信息
            item {
                RequestContentItemLayout(it)
            }
        }
    }
}