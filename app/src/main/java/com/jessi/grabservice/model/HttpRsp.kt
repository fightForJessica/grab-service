package com.jessi.grabservice.model

import androidx.compose.runtime.Stable
import com.jessi.grabservice.utils.id
import top.sankokomi.wirebare.kernel.interceptor.http.HttpSession

@Stable
data class HttpRsp(
    val id: String,
    val requestTime: Long?,
    val sourceProcessUid: Int,
    val sourcePort: Short?,
    val destinationAddress: String?,
    val destinationPort: Short?,
    val url: String?,
    val isHttps: Boolean?,
    val httpVersion: String?,
    val rspStatus: String?,
    val originHead: String?,
    val formatHead: List<String>?,
    val host: String?,
    val contentType: String?,
    val contentEncoding: String?,
)
