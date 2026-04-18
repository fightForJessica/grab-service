package com.jessi.grabservice.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.Toast
import com.jessi.grabservice.model.HttpReq
import com.jessi.grabservice.model.HttpRsp
import top.sankokomi.wirebare.kernel.interceptor.http.HttpRequest
import top.sankokomi.wirebare.kernel.interceptor.http.HttpResponse
import top.sankokomi.wirebare.kernel.interceptor.http.HttpSession

private var toast: Toast? = null
fun Context.showToast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    toast?.cancel()
    toast = Toast.makeText(this, msg, duration)
    toast?.show()
}

fun Context.getAppIconDrawable(packageName: String): Drawable {
    return packageManager.getApplicationIcon(packageName)
}

val HttpRequest.id: String get() = "req|${requestTime}|${sequence}"

val HttpResponse.id: String get() = "rsp|${requestTime}|${sequence}"

fun HttpSession.toHttpReq(): HttpReq {
    return HttpReq(
        id = request.id,
        requestTime = request.requestTime,
        sourceProcessUid = tcpSession.sourceProcessUid,
        sourcePort = request.sourcePort,
        sourcePkgName = request.sourcePkgName,
        destinationAddress = request.destinationAddress,
        destinationPort = request.destinationPort,
        method = request.method,
        isHttps = request.isHttps,
        httpVersion = request.httpVersion,
        host = request.host,
        path = request.path,
        originHead = request.originHead,
        formatHead = request.formatHead,
        url = request.url,
    )
}

fun HttpSession.toHttpRsp(): HttpRsp {
    return HttpRsp(
        id = response.id,
        requestTime = response.requestTime,
        sourceProcessUid = tcpSession.sourceProcessUid,
        sourcePort = response.sourcePort,
        destinationAddress = response.destinationAddress,
        destinationPort = response.destinationPort,
        url = response.url,
        isHttps = response.isHttps,
        httpVersion = response.httpVersion,
        rspStatus = response.rspStatus,
        originHead = response.originHead,
        formatHead = response.formatHead,
        host = response.host,
        contentType = response.contentType,
        contentEncoding = response.contentEncoding,
    )
}

fun String?.orNone(): String {
    return this ?: "none"
}