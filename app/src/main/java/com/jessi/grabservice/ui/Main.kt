package com.jessi.grabservice.ui

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jessi.grabservice.R
import com.jessi.grabservice.model.TabModel
import com.jessi.grabservice.ui.page.CONTROL_PAGE_NAME
import com.jessi.grabservice.ui.page.ControlPage
import com.jessi.grabservice.ui.page.RequestPage
import com.jessi.grabservice.ui.page.ResponsePage
import com.jessi.grabservice.ui.theme.ThemeManager
import com.jessi.grabservice.utils.drawableResIdToBitmap
import com.jessi.grabservice.viewmodel.MainViewModel
import com.jessi.grabservice.viewmodel.MainViewModelFactory
import kotlinx.coroutines.launch

@Composable
@Preview
fun MainPreview() {
    Box(Modifier.fillMaxSize().background(Color.Gray)) {
//        Main(PaddingValues.Zero)
    }
}

@Composable
fun Main(
    context: Context,
    paddingValues: PaddingValues
) {
    val viewModel = viewModel<MainViewModel>(factory = MainViewModelFactory())
    val scope = rememberCoroutineScope()

    // tab 信息, todo fill
    val tabList = listOf(
        TabModel(CONTROL_PAGE_NAME, R.drawable.ic_control_page)
    )
    var selectIndex by remember { mutableIntStateOf(0) }

    // 标题文本
    val titleText by remember(selectIndex) {
        mutableStateOf(tabList[selectIndex].tabName)
    }

    val pageState = rememberPagerState(
        initialPage = selectIndex,
        pageCount = { tabList.size }
    )

    LaunchedEffect(pageState.currentPage) {
        if (pageState.currentPage != selectIndex) {
            selectIndex = pageState.currentPage
        }
    }

    ConstraintLayout(
        modifier = Modifier.fillMaxSize().padding(paddingValues)
    ) {

        // 约束控制
        val (title, container, tabLayout) = createRefs()

        // title
        Text(
            modifier = Modifier.fillMaxWidth()
                .constrainAs(title) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }.padding(vertical = 16.dp),
            text = titleText,
            color = ThemeManager.colorTheme.globalText,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        
        // pagerContainer
        HorizontalPager(
            modifier = Modifier.fillMaxWidth()
                .constrainAs(container) {
                    top.linkTo(title.bottom)
                    bottom.linkTo(tabLayout.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            state = pageState,
            beyondViewportPageCount = 2,
            userScrollEnabled = true
        ) { pageIndex ->
            when (pageIndex) {
                0 -> {
                    ControlPage(context)
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
        TabRow(
            modifier = Modifier.fillMaxWidth()
                .constrainAs(tabLayout) {
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
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
                            bitmap = context.drawableResIdToBitmap(model.iconDrawableRes),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(ThemeManager.colorTheme.defaultFilterColor)
                        )
                    }
                )
            }
        }
    }

}