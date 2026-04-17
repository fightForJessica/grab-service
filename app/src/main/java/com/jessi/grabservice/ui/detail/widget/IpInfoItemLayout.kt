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
import com.jessi.grabservice.ui.DividerLine
import com.jessi.grabservice.ui.cardBackground
import com.jessi.grabservice.ui.theme.ThemeManager
import com.jessi.grabservice.utils.orNone
import top.sankokomi.wirebare.kernel.common.WireBareHelper
import top.sankokomi.wirebare.kernel.net.IPVersion

@Composable
fun IpInfoItemLayout(
    sourcePort: Short?,
    destinationAddress: String?,
    destinationPort: Short?
) {
    Column(
        modifier = Modifier.fillMaxSize()
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
                painter = painterResource(R.drawable.ic_ip_address),
                contentDescription = null,
                colorFilter = ColorFilter.tint(ThemeManager.colorTheme.defaultFilterColor)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.ip_address),
                fontSize = 18.sp,
                color = ThemeManager.colorTheme.globalText,
                fontWeight = FontWeight.W500
            )
        }

        DividerLine(horizontalPadding = 0.dp)

        val ipVersion = WireBareHelper.parseIpVersion(destinationAddress)
        val srcIp = when (ipVersion) {
            IPVersion.IPv4 -> "127.0.0.1"
            IPVersion.IPv6 -> "::1"
            else -> ""
        }
        val srcPort = sourcePort?.toUShort()?.toString().orNone()
        val destIp = destinationAddress.orNone()
        val destPort = destinationPort?.toUShort()?.toString().orNone()

        // IP 版本
        SingleTextContentItem(
            titleText = stringResource(R.string.ip_version),
            contentText = ipVersion?.versionName.orNone()
        )

        // 来源 IP
        SingleTextContentItem(
            titleText = stringResource(R.string.ip_source),
            contentText = "$srcIp:$srcPort"
        )

        // 目标 IP
        SingleTextContentItem(
            titleText = stringResource(R.string.ip_destination),
            contentText = "$destIp:$destPort"
        )
    }
}