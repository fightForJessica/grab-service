package com.jessi.grabservice.model

import android.os.Parcelable
import androidx.compose.runtime.Stable
import com.jessi.grabservice.utils.id
import kotlinx.parcelize.Parcelize
import top.sankokomi.wirebare.kernel.interceptor.http.HttpSession

@Stable
@Parcelize
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
) : Parcelable
