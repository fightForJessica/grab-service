package com.jessi.grabservice.ui.detail

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.jessi.grabservice.R
import com.jessi.grabservice.model.AppInfo
import com.jessi.grabservice.model.HttpReq
import com.jessi.grabservice.model.HttpRsp
import com.jessi.grabservice.model.TabModel
import com.jessi.grabservice.ui.BottomTabLayout
import com.jessi.grabservice.ui.theme.ThemeManager
import kotlinx.coroutines.launch

@Composable
fun Detail(
    context: Context,
    paddingValues: PaddingValues,
    appInfo: AppInfo,
    request: HttpReq?,
    response: HttpRsp?,
    initPageName: String
) {
    // todo 加入 request 和 response 的联动
    var request by remember { mutableStateOf(request) }
    var response by remember { mutableStateOf(response) }

    val scope = rememberCoroutineScope()

    val tabList = mutableListOf<TabModel>()
    if (request != null) {
        tabList.add(
            TabModel(REQUEST_DETAIL_PAGE_NAME, R.drawable.ic_request_detail_page)
        )
    }
    if (response != null) {
        tabList.add(
            TabModel(RESPONSE_DETAIL_PAGE_NAME, R.drawable.ic_response_detail_page)
        )
    }

    var selectIndex by remember {
        mutableIntStateOf(tabList.indexOfFirst { it.tabName == initPageName })
    }

    val titleText by remember(selectIndex) {
        mutableStateOf(tabList[selectIndex].tabName)
    }

    var detailPageScrolling by remember { mutableStateOf(false) }

    val pageState = rememberPagerState(
        initialPage = selectIndex,
        pageCount = { tabList.size }
    )
    LaunchedEffect(pageState.currentPage) {
        if (pageState.currentPage != selectIndex) {
            selectIndex = pageState.currentPage
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
            .background(ThemeManager.colorTheme.backgroundColor)
            .padding(paddingValues)
    ) {

        // title
        AnimatedVisibility(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .zIndex(1f),
            visible = !detailPageScrolling,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            Text(
                modifier = Modifier.background(ThemeManager.colorTheme.backgroundColor)
                    .padding(vertical = 16.dp)
                    .height(24.dp)
                    .align(Alignment.Center),
                text = titleText,
                color = ThemeManager.colorTheme.globalText,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }

        // pagerContainer
        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            state = pageState,
            beyondViewportPageCount = 1,
            userScrollEnabled = true
        ) { pageIndex ->
            when (tabList[pageIndex].tabName) {
                REQUEST_DETAIL_PAGE_NAME -> {
                    RequestDetailPage(
                        context = context,
                        appInfo = appInfo,
                        request = request,
                        onScrollStateUpdate = {
                            detailPageScrolling = it
                        }
                    )
                }
                RESPONSE_DETAIL_PAGE_NAME -> {
                    ResponseDetailPage(
                        context = context,
                        appInfo = appInfo,
                        response = response,
                        onScrollStateUpdate = {
                            detailPageScrolling = it
                        }
                    )
                }
            }
        }

        BottomTabLayout(
            modifier = Modifier.align(Alignment.BottomCenter),
            tabList = tabList,
            visible = !detailPageScrolling,
            selectIndex = selectIndex,
            onItemClick = {
                scope.launch {
                    pageState.animateScrollToPage(it)
                }
            }
        )
    }
}