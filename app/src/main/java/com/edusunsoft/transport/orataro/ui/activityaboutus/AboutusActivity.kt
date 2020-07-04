package com.edusunsoft.transport.orataro.ui.activityaboutus

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.text.HtmlCompat
import androidx.databinding.DataBindingUtil
import com.edusunsoft.transport.orataro.BuildConfig
import com.edusunsoft.transport.orataro.R
import com.edusunsoft.transport.orataro.base.BaseViewModel
import com.edusunsoft.transport.orataro.databinding.ActivityAboutUsBinding
import com.edusunsoft.transport.orataro.dialog.LoadingDialog
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import kotlinx.android.synthetic.main.activity_about_us.*
import kotlinx.android.synthetic.main.activity_about_us.view.*


class AboutusActivity : YouTubeBaseActivity(), AboutusNavigator, YouTubePlayer.OnInitializedListener {

    private val mTag = AboutusActivity::class.java.simpleName

    private lateinit var mBinding: ActivityAboutUsBinding
    private lateinit var mViewModel: AboutUsViewModel

    companion object {
        fun getInstance(activity: Activity): Intent {
            return Intent(activity, AboutusActivity::class.java)
        }

        private const val RECOVERY_DIALOG_REQUEST = 1
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_about_us)
        mViewModel = AboutUsViewModel(this, this)

        val mBaseViewModel = BaseViewModel(this, this)
        mBaseViewModel.isBackShow.set(true)

        //remove dummy view
        mBinding.appToolbar.rl_dummy.visibility = View.GONE

        // set toolbar title
        mBinding.appToolbar.tv_activity_title.text = resources.getString(R.string.about_us)
        mBinding.ivBack.visibility = View.VISIBLE
        mBinding.mAboutUsViewModel = mViewModel

        mBinding.youtubeView.initialize(BuildConfig.DEVELOPER_KEY, this)

        val content = "ORATARO is a smart communication platform for students, parents and teachers with a real time update on Class Activity, Homework, Circular, Academic Calendar, Progress update and schoolgroups discussion for brainstorming and other project work within a class or at a school level. The super smart features of ORATARO will reinforce the scale of teacher and parent interaction and will influence more involvement of both in advancements of the child's education. The App allows the School to create content categories/sub-categories as and when they wish facilitate communication between Students, Parents and Teachers. This facility is not available among the other school App available on the Market. ORATARO supports all types of digital content including all document types e.g. PDF, Word, Excel, Videos and Photos."
        mBinding.text.text = content

//        setWebViewData()

    }

    private fun setWebViewData() {
        wv_about_us.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                Log.d(mTag, "Progress= {$newProgress}")
                LoadingDialog.showLoading(this@AboutusActivity)
            }
        }
        wv_about_us.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
            ): Boolean {
                Log.d(mTag, "OverrideUrlLoading")
                return true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                Log.d(mTag, "Finished")
                LoadingDialog.hideLoading()
            }
        }

        wv_about_us.settings.javaScriptEnabled = true
        wv_about_us.loadUrl("https://firstsiteguide.com/about-us/")
    }

    override fun onBackPressed() {
        super.onBackPressed()
//        supportFinishAfterTransition()
    }

    override fun onInitializationSuccess(provider: YouTubePlayer.Provider?, player: YouTubePlayer?, wasRestored: Boolean) {

        if (!wasRestored) {
            player?.cueVideo(BuildConfig.YOUTUBE_VIDEO_CODE)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            getYouTubePlayerProvider().initialize(BuildConfig.DEVELOPER_KEY, this)
        }
    }

    private fun getYouTubePlayerProvider(): YouTubePlayer.Provider {
        return findViewById<View>(R.id.youtube_view) as YouTubePlayerView
    }

    override fun onInitializationFailure(provider: YouTubePlayer.Provider?, errorReason: YouTubeInitializationResult) {
        if (errorReason?.isUserRecoverableError) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show()
        } else {
            val errorMessage = String.format(getString(R.string.error_player), errorReason.toString())
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        }

    }

    override fun onError(error: String) {

    }

    override fun onBack() {

        this.finish()

    }

}
