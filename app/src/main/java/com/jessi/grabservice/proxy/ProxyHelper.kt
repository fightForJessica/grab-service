package com.jessi.grabservice.proxy

import android.app.Activity
import android.net.VpnService
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.jessi.grabservice.utils.Logger
import com.jessi.grabservice.utils.showToast
import com.jessi.grabservice.viewmodel.MainViewModel
import top.sankokomi.wirebare.kernel.common.WireBare
import top.sankokomi.wirebare.kernel.interceptor.http.HttpSession
import top.sankokomi.wirebare.kernel.ssl.JKS
import top.sankokomi.wirebare.kernel.util.WireBareLogger

class ProxyHelper(
    private val activity: ComponentActivity,
    private val viewModel: MainViewModel
) {

    private companion object {
        const val MTU = 4096
    }

    private val vpnLaunchResultLauncher = activity.registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        object : ActivityResultCallback<ActivityResult> {
            override fun onActivityResult(result: ActivityResult) {
                if (result.resultCode == Activity.RESULT_OK) {
                    realStartGrabService()
                } else {
                    viewModel.enableGrab(false)
                    activity.showToast("启动失败，请检查是否授予VPN权限")
                    Logger.e("VPN 服务启动失败")
                }
            }
        }
    )

    val wireBareJKS by lazy {
        JKS(
            jksStream = { activity.assets.open("wirebare.jks") },
            alias = "wirebare",
            password = "wirebare".toCharArray(),
            algorithm = "RSA",
            type = "PKCS12",
            organization = "WB",
            organizationUnit = "WB"
        )
    }

    fun tryStartGrabService() {
        Logger.i("尝试启动抓取服务")
        val prepareIntent = VpnService.prepare(activity)
        if (prepareIntent != null) {
            vpnLaunchResultLauncher.launch(prepareIntent)
        } else {
            realStartGrabService()
        }
    }

    private fun realStartGrabService() {
        val selectAppPackageList = if (viewModel.filterSystemApp.value) {
            viewModel.appInfoList.filter { it.isSystemApp }
        } else {
            viewModel.appInfoList
        }.filter {
            it.isGrabSelected
        }.map {
            it.packageName
        }.filter {
            !it.isEmpty()
        }
        if (selectAppPackageList.isEmpty()) {
            Logger.i("没有选择目标抓取应用，不启动服务")
            activity.showToast("启动需要先选择抓包应用程序")
            return
        }
        viewModel.enableGrab(true)
        Logger.i("启动抓取服务, selectAppPackageList: $selectAppPackageList")
        startProxy(
            targetPackageNameArray = selectAppPackageList.toTypedArray(),
            onRequest = {
                viewModel.addRequest(it)
            },
            onResponse = {
                viewModel.addResponse(it)
            }
        )
    }

    fun stopGrabService() {
        Logger.i("停止抓取服务")
        viewModel.enableGrab(false)
        WireBare.stopProxy()
    }

    fun startProxy(
        targetPackageNameArray: Array<String>,
        onRequest: (HttpSession) -> Unit,
        onResponse: (HttpSession) -> Unit
    ) {
        WireBare.logLevel = WireBareLogger.Level.VERBOSE
        WireBare.startProxy {
            if (viewModel.enableSSL.value) {
                jks = wireBareJKS
            }
            mtu = MTU
            tcpProxyServerCount = 5
            ipv4ProxyAddress = "10.1.10.1" to 32
//            enableIPv6 = ProxyPolicyDataStore.enableIPv6.value
//            ipv6ProxyAddress = "a:a:1:1:a:a:1:1" to 128
            addRoutes("0.0.0.0" to 0, "::" to 0)
            addAllowedApplications(*targetPackageNameArray)
            addAsyncHttpInterceptor(
                listOf(
                    GrabHttpInterceptor.Factory(onRequest, onResponse)
                )
            )
        }
    }

}
