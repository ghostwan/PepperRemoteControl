package com.ghostwan.pepperremote.ui.launcher

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ghostwan.pepperremote.R
import com.ghostwan.pepperremote.SmartRobotServiceApplication.Companion.EXTRA_FOCUS_TOKEN
import com.ghostwan.pepperremote.data.model.App
import com.ghostwan.pepperremote.data.source.AppRepository
import com.ghostwan.pepperremote.data.source.AppRepositoryContract
import com.ghostwan.pepperremote.service.RobotService.Companion.KEY_FOCUSID
import com.ghostwan.pepperremote.ui.qrcode.CorruptedDataException
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_launcher.*
import timber.log.Timber


class LauncherActivity : AppCompatActivity(), LauncherContract.View {
    private val repository: AppRepositoryContract by lazy { AppRepository(packageManager) }
    private val presenter: LauncherContract.Presenter by lazy { LauncherPresenter(this, repository) }
    private val viewManager: RecyclerView.LayoutManager by lazy { LinearLayoutManager(this) }
    private val viewAdapter: AppAdapter by lazy { AppAdapter(presenter::startApplication, presenter::deleteApplication) }
    private var isNewActivity = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)

        appList.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        swipeView.setOnRefreshListener {
            presenter.fetchRoboticAppList()
        }

    }

    override fun onResume() {
        super.onResume()
        presenter.fetchRoboticAppList(isNewActivity)
        isNewActivity = false
    }

    override fun showLoadingIndicator() {
        swipeView.post { swipeView.isRefreshing = true }
    }

    override fun hideLoadingIndicator() {
        swipeView.post{ swipeView.isRefreshing = false }
    }

    override fun showAppList(apps: List<App>) {
        emptyList.visibility = View.GONE
        appList.visibility = View.VISIBLE
        viewAdapter.submitList(apps)
    }

    override fun showEmptyList() {
        appList.visibility = View.GONE
        emptyList.visibility = View.VISIBLE
    }

    override fun showApplication(app: App) {
        val launchIntent = packageManager.getLaunchIntentForPackage(app.id)
        launchIntent?.apply {
            putExtra(KEY_FOCUSID, intent.getStringExtra(EXTRA_FOCUS_TOKEN))
        }
        startActivity(launchIntent)
    }

    override fun showUninstallApplication(app: App) {
        val intent = Intent(Intent.ACTION_DELETE)
        intent.data = Uri.parse("package:${app.id}")
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    override fun showError(throwable: Throwable) {
        runOnUiThread {
            Timber.e(throwable)
            val errorID = when (throwable) {
                is CorruptedDataException -> R.string.error_qrcode_corrupted
                else -> R.string.unknown_error
            }
            Snackbar.make(swipeView, errorID, Snackbar.LENGTH_LONG).show()
        }
    }

}
