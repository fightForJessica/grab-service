package com.jessi.grabservice.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jessi.grabservice.R
import com.jessi.grabservice.model.TabModel
import com.jessi.grabservice.ui.theme.ThemeManager

@Composable
fun BottomTabLayout(
    modifier: Modifier = Modifier,
    tabList: List<TabModel>,
    visible: Boolean,
    selectIndex: Int,
    onItemClick: (Int) -> Unit
) {
    AnimatedVisibility(
        modifier = modifier.fillMaxWidth()
            .height(dimensionResource(R.dimen.tab_layout_height)),
        visible = visible,
        enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }),
        exit = fadeOut() + slideOutVertically(targetOffsetY = { it / 2 })
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
                            onItemClick(index)
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