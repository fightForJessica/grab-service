package com.jessi.grabservice.ui

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jessi.grabservice.model.TabModel
import com.jessi.grabservice.ui.page.ControlPage
import com.jessi.grabservice.ui.page.RequestPage
import com.jessi.grabservice.ui.page.ResponsePage
import com.jessi.grabservice.ui.theme.ThemeManager
import com.jessi.grabservice.viewmodel.MainViewModel
import com.jessi.grabservice.viewmodel.MainViewModelFactory

@Composable
@Preview
fun MainPreview() {
    Box(Modifier.fillMaxSize().background(Color.Gray)) {
        Main(PaddingValues.Zero)
    }
}

@Composable
fun Main(paddingValues: PaddingValues) {
    val viewModel = viewModel<MainViewModel>(factory = MainViewModelFactory())

    // tab 信息
    val tabList = listOf<TabModel>()
    var selectIndex by remember { mutableIntStateOf(0) }

    // 标题文本
    val titleText by remember(selectIndex) {
        mutableStateOf(tabList[selectIndex].tabName)
    }

    val pageState = rememberPagerState(
        initialPage = selectIndex,
        pageCount = { tabList.size }
    )

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
            state = pageState
        ) { pageIndex ->
            when (pageIndex) {
                0 -> {
                    ControlPage()
                }
                1 -> {
                    RequestPage()
                }
                2 -> {
                    ResponsePage()
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
                            bitmap = model.icon.toBitmap().asImageBitmap(),
                            contentDescription = null
                        )
                    }
                )
            }
        }
    }

}