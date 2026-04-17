package com.jessi.grabservice.ui.detail.widget

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import com.jessi.grabservice.R
import com.jessi.grabservice.model.AppInfo
import com.jessi.grabservice.ui.DividerLine
import com.jessi.grabservice.ui.cardBackground
import com.jessi.grabservice.ui.theme.ThemeManager
import com.jessi.grabservice.utils.getAppIconDrawable

@Composable
fun AppInfoItemLayout(
    context: Context,
    appInfo: AppInfo
) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .cardBackground(ThemeManager.colorTheme.cardBackgroundColor)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier.size(20.dp),
                bitmap = context.getAppIconDrawable(appInfo.packageName).toBitmap().asImageBitmap(),
                contentDescription = null
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = appInfo.appName,
                fontSize = 18.sp,
                color = ThemeManager.colorTheme.globalText,
                fontWeight = FontWeight.W500
            )
        }

        Spacer(Modifier.height(4.dp))
        DividerLine(horizontalPadding = 0.dp)
        Spacer(Modifier.height(4.dp))

        // UID
        SingleTextContentItem(
            titleText = stringResource(R.string.uid),
            contentText = appInfo.uid.toString()
        )

        Spacer(Modifier.height(4.dp))

        // 应用程序名称
        SingleTextContentItem(
            titleText = stringResource(R.string.app_package_name),
            contentText = appInfo.packageName
        )

        Spacer(Modifier.height(4.dp))

        // 是否系统应用
        SingleTextContentItem(
            titleText = stringResource(R.string.system_app),
            contentText = if (appInfo.isSystemApp) "是" else "否"
        )
    }
}