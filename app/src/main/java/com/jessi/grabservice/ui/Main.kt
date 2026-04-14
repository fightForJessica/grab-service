package com.jessi.grabservice.ui

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jessi.grabservice.R
import com.jessi.grabservice.model.TabModel
import com.jessi.grabservice.ui.page.CONTROL_PAGE_NAME
import com.jessi.grabservice.ui.page.ControlPage
import com.jessi.grabservice.ui.page.IControlPageCallback
import com.jessi.grabservice.ui.page.REQUEST_PAGE_NAME
import com.jessi.grabservice.ui.page.RESPONSE_PAGE_NAME
import com.jessi.grabservice.ui.page.RequestPage
import com.jessi.grabservice.ui.page.ResponsePage
import com.jessi.grabservice.ui.theme.ThemeManager
import com.jessi.grabservice.viewmodel.MainViewModel
import com.jessi.grabservice.viewmodel.MainViewModelFactory
import kotlinx.coroutines.launch

interface IMainContentCallback: IControlPageCallback

@Composable
fun Main(
    context: Context,
    paddingValues: PaddingValues,
    callback: IMainContentCallback
) {
    val viewModel = viewModel<MainViewModel>(factory = MainViewModelFactory())
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
    var hideStatusContent by remember { mutableStateOf(false) }

    val pageState = rememberPagerState(
        initialPage = selectIndex,
        pageCount = { tabList.size }
    )
    LaunchedEffect(pageState.currentPage) {
        if (pageState.currentPage != selectIndex) {
            selectIndex = pageState.currentPage
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ThemeManager.colorTheme.backgroundColor)
            .padding(paddingValues)
    ) {

        // title
        AnimatedVisibility(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth()
                .background(ThemeManager.colorTheme.backgroundColor),
            visible = !hideStatusContent,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            Text(
                text = titleText,
                color = ThemeManager.colorTheme.globalText,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
        
        // pagerContainer
        HorizontalPager(
            modifier = Modifier.fillMaxSize().weight(1f),
            state = pageState,
            beyondViewportPageCount = 2,
            userScrollEnabled = true
        ) { pageIndex ->
            when (pageIndex) {
                0 -> {
                    ControlPage(context, viewModel, callback)
                }
                1 -> {
                    RequestPage(context)
                }
                2 -> {
                    ResponsePage(context)
                }
            }
        }

        // tabLayout
        AnimatedVisibility(
            modifier = Modifier.fillMaxWidth(),
            visible = !hideStatusContent,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            TabRow(
                modifier = Modifier.fillMaxWidth(),
                containerColor = ThemeManager.colorTheme.backgroundColor,
                selectedTabIndex = selectIndex
            ) {
                tabList.forEachIndexed { index, model ->
                    Tab(
                        selected = index == selectIndex,
                        onClick = {
                            // 点击对应 tab 切换到目标 pager
                            if (index != selectIndex) {
                                scope.launch {
                                    pageState.animateScrollToPage(index)
                                }
                            }
                        },
                        text = {
                            Text(
                                text = model.tabName,
                                fontSize = 14.sp,
                                color = ThemeManager.colorTheme.globalText
                            )
                        },
                        icon = {
                            Image(
                                modifier = Modifier.size(24.dp),
                                painter = painterResource(model.iconDrawableRes),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(ThemeManager.colorTheme.defaultFilterColor)
                            )
                        }
                    )
                }
            }
        }
    }

}