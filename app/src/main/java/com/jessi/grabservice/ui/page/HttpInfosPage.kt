package com.jessi.grabservice.ui.page

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jessi.grabservice.R
import com.jessi.grabservice.ui.theme.ThemeManager

@Composable
fun <T> HttpInfosPage(
    infoList: List<T>,
    emptyText: String,
    itemKey: (index: Int, item: T) -> Any,
    itemContent: @Composable (index: Int, item: T) -> Unit,
    onDeleteFabClick: () -> Unit
) {
    val listState = rememberLazyListState()

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
                    modifier = Modifier.fillMaxSize(),
                    state = listState
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
            modifier = Modifier.padding(bottom = 24.dp, end = 24.dp).align(Alignment.BottomEnd),
            visible = infoList.isNotEmpty(),
            enter = fadeIn(tween()),
            exit = fadeOut(tween())
        ) {
            FloatingActionButton(
                onClick = onDeleteFabClick
            ) {
                Image(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(R.drawable.ic_delete),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(ThemeManager.colorTheme.deleteFloatingButtonBackgroundColor)
                )
            }
        }
    }
}