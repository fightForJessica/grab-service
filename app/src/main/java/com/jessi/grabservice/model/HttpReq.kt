package com.jessi.grabservice.model

import androidx.compose.runtime.Stable

@Stable
data class HttpReq(
    val id: String,
    val requestTime: Long?,
    val sourceProcessUid: Int,
    val sourcePort: Short?,
    val sourcePkgName: String?,
    val destinationAddress: String?,
    val destinationPort: Short?,
    val method: String?,
    val isHttps: Boolean?,
    val httpVersion: String?,
    val host: String?,
    val path: String?,
    val originHead: String?,
    val formatHead: List<String>?,
    val url: String?,
)