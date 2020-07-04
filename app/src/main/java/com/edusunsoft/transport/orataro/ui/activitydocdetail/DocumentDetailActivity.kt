package com.edusunsoft.transport.orataro.ui.activitydocdetail

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import com.edusunsoft.transport.orataro.BuildConfig
import com.edusunsoft.transport.orataro.R
import com.edusunsoft.transport.orataro.base.BaseActivity
import com.edusunsoft.transport.orataro.databinding.DocDetailActivityBinding
import com.edusunsoft.transport.orataro.ui.activitylicense.LDActivity
import kotlinx.android.synthetic.main.app_toolbar_layout.*
import java.net.URLConnection


class DocumentDetailActivity : BaseActivity() {

    private val mTag = LDActivity::class.java.simpleName
    private lateinit var docDetailActivityBinding: DocDetailActivityBinding

    companion object {

        var DocumentTitle: String = "docTitle"
        var DocumentImageUrl: String = "docImageurl"

        fun getInstance(activity: Activity, documentPath: String, documentTitle: String): Intent {
            val intent = Intent(activity, DocumentDetailActivity::class.java)
            intent.putExtra(DocumentTitle, documentTitle)
            intent.putExtra(DocumentImageUrl, documentPath)
            return intent
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        docDetailActivityBinding = DataBindingUtil.setContentView(this, R.layout.doc_detail_activity)

        setToolbar(docDetailActivityBinding.toolbar, intent.extras.getString(DocumentTitle), true)
        rl_dummy.visibility = View.GONE

        docDetailActivityBinding.webView.setBackgroundColor(Color.parseColor("#FF4081"))

        docDetailActivityBinding.webView.settings.loadWithOverviewMode = true
        docDetailActivityBinding.webView.settings.useWideViewPort = true
        docDetailActivityBinding.webView.settings.builtInZoomControls = true
        docDetailActivityBinding.webView.settings.javaScriptEnabled = true
        docDetailActivityBinding.webView.loadUrl(BuildConfig.IMAGE_URL + intent.extras.getString(DocumentImageUrl))

    }

}
