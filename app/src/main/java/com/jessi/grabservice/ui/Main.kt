package com.jessi.grabservice.ui

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
import androidx.compose.runtime.collectAsState
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
import com.jessi.grabservice.model.TabModel
import com.jessi.grabservice.proxy.ProxyHelper
import com.jessi.grabservice.ui.page.CONTROL_PAGE_NAME
import com.jessi.grabservice.ui.page.ControlPage
import com.jessi.grabservice.ui.page.IControlPageCallback
import com.jessi.grabservice.ui.page.IRequestPageCallback
import com.jessi.grabservice.ui.page.IResponsePageCallback
import com.jessi.grabservice.ui.page.REQUEST_PAGE_NAME
import com.jessi.grabservice.ui.page.RESPONSE_PAGE_NAME
import com.jessi.grabservice.ui.page.RequestPage
import com.jessi.grabservice.ui.page.ResponsePage
import com.jessi.grabservice.ui.theme.ThemeManager
import com.jessi.grabservice.viewmodel.MainViewModel
import kotlinx.coroutines.launch

interface IMainContentCallback: IControlPageCallback,
    IRequestPageCallback,
    IResponsePageCallback

@Composable
fun Main(
    context: Context,
    viewModel: MainViewModel,
    proxyHelper: ProxyHelper,
    paddingValues: PaddingValues,
    callback: IMainContentCallback
) {
    val scope = rememberCoroutineScope()

    // tab 信息
    val tabList = listOf(
        TabModel(CONTROL_PAGE_NAME, R.drawable.ic_control_page),
        TabModel(REQUEST_PAGE_NAME, R.drawable.ic_request_page),
        TabModel(RESPONSE_PAGE_NAME, R.drawable.ic_response_page)
    )
    var selectIndex by remember { mutableIntStateOf(0) }

    // 标题文本
    val titleText by remember(selectIndex) {
        mutableStateOf(tabList[selectIndex].tabName)
    }

    // 列表滚动时，隐藏标题和 tab
    val mainPageScrolling by viewModel.mainPageScrolling.collectAsState()

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
        modifier = Modifier
            .fillMaxSize()
            .background(ThemeManager.colorTheme.backgroundColor)
            .padding(paddingValues)
    ) {

        // title
        AnimatedVisibility(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .zIndex(1f),
            visible = !mainPageScrolling,
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
            beyondViewportPageCount = 2,
            userScrollEnabled = true
        ) { pageIndex ->
            when (pageIndex) {
                0 -> {
                    ControlPage(context, viewModel, proxyHelper, callback)
                }
                1 -> {
                    RequestPage(context, viewModel, callback)
                }
                2 -> {
                    ResponsePage(context, viewModel, callback)
                }
            }
        }

        // tabLayout
        BottomTabLayout(
            modifier = Modifier.align(Alignment.BottomCenter),
            tabList = tabList,
            visible = !mainPageScrolling,
            selectIndex = selectIndex,
            onItemClick = {
                scope.launch {
                    pageState.animateScrollToPage(it)
                }
            }
        )
    }

}