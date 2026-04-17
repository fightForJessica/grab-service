package com.jessi.grabservice.ui.detail.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jessi.grabservice.R
import com.jessi.grabservice.model.HttpRsp
import com.jessi.grabservice.ui.DividerLine
import com.jessi.grabservice.ui.cardBackground
import com.jessi.grabservice.ui.theme.ThemeManager
import com.jessi.grabservice.utils.orNone

@Composable
fun ResponseContentItemLayout(response: HttpRsp) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .cardBackground(ThemeManager.colorTheme.cardBackgroundColor)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier.size(20.dp),
                painter = painterResource(R.drawable.ic_request_detail_content),
                contentDescription = null,
                colorFilter = ColorFilter.tint(ThemeManager.colorTheme.defaultFilterColor)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.response_detail_content),
                fontSize = 18.sp,
                color = ThemeManager.colorTheme.globalText,
                fontWeight = FontWeight.W500
            )
        }

        DividerLine(horizontalPadding = 0.dp)

        // 响应状态码
        SingleTextContentItem(
            titleText = stringResource(R.string.response_code),
            contentText = response.rspStatus.orNone()
        )

        // HTTP 版本
        SingleTextContentItem(
            titleText = stringResource(R.string.http_version),
            contentText = response.httpVersion.orNone()
        )

        // 请求头
        if (response.formatHead != null && response.formatHead.size >= 2) {
            val headerList = response.formatHead.subList(1, response.formatHead.size).map {
                val splitIndex = it.indexOf(": ")
                if (splitIndex < 0) return@map "" to it
                return@map it.substring(
                    0,
                    splitIndex
                ) to it.substring(
                    splitIndex + 2,
                    it.length
                )
            }
            for (header in headerList) {
                SingleTextContentItem(
                    titleText = header.first,
                    contentText = header.second
                )
            }
        }
    }

}