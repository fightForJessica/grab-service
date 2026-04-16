package com.jessi.grabservice.ui.page

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jessi.grabservice.R
import com.jessi.grabservice.ui.cardBackground
import com.jessi.grabservice.ui.theme.ThemeManager

@Composable
fun <T> HttpInfosPage(
    infoList: List<T>,
    emptyText: String,
    listState: LazyListState,
    itemKey: (index: Int, item: T) -> Any,
    itemContent: @Composable (index: Int, item: T) -> Unit,
    onDeleteFabClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        AnimatedContent(
            modifier = Modifier.align(Alignment.Center),
            targetState = infoList.isEmpty(),
            transitionSpec = {
                fadeIn(tween()) togetherWith fadeOut(tween())
            }
        ) { empty ->
            if (empty) {
                Text(
                    text = emptyText,
                    fontSize = 18.sp,
                    color = ThemeManager.colorTheme.globalText,
                    fontWeight = FontWeight.W500
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    state = listState,
                    contentPadding = PaddingValues(top = 56.dp, bottom = 70.dp) // 标题栏高度 / 导航栏高度
                ) {
                    itemsIndexed(
                        items = infoList,
                        key = itemKey,
                        contentType = { _, _ -> "info_page_item" },
                    ) { index, item ->
                        itemContent(index, item)
                    }
                }
            }
        }

        // Fab
        AnimatedVisibility(
            modifier = Modifier.padding(bottom = 24.dp + 70.dp, end = 24.dp).align(Alignment.BottomEnd),
            visible = infoList.isNotEmpty() && !listState.isScrollInProgress,
            enter = fadeIn(tween()) + slideInHorizontally(tween()) { it / 2 },
            exit = fadeOut(tween()) + slideOutHorizontally(tween()) { it / 2 }
        ) {
            Box(
                modifier = Modifier.cardBackground(
                    color = ThemeManager.colorTheme.deleteFloatingButtonBackgroundColor,
                    radius = 16.dp
                ).clickable {
                    onDeleteFabClick()
                }.padding(16.dp)
            ) {
                Image(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(R.drawable.ic_delete),
                    contentDescription = null
                )
            }
        }
    }
}