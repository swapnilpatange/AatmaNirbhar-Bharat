package com.aatmanirbhar.bharat

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.pm.PackageManager
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import kotlinx.android.synthetic.main.activity_replace_app.*
import java.util.*
import kotlin.Comparator


class ReplaceAppActivity : AppCompatActivity() {

    //lateinit var otherApps: ArrayList<OtherApps>
    lateinit var completeList: ArrayList<OtherApps>
    var totalChineseApp: Float = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_replace_app)

        progressBar.setVisibility(View.VISIBLE)
        //otherApps = ArrayList()
        val mAndroidViewModel = ViewModelProviders.of(this@ReplaceAppActivity)
            .get(com.aatmanirbhar.bharat.AndroidViewModel::class.java)
        mAndroidViewModel.getApplist()?.observe(this, Observer<ApiResponse> { t: ApiResponse? ->

            progressBar.setVisibility(View.GONE)
            for (i in 0 until t?.appList!!.size) {
                if (appInstalledOrNot(t?.appList[i].packagename)) {
                    t?.appList[i].isinstall = 1
                    totalChineseApp++
                } else
                    t?.appList[i].isinstall = 0
            }
            completeList = t?.appList!!
            Collections.sort(completeList, object : Comparator<OtherApps> {
                override fun compare(otherApps1: OtherApps?, otherApps2: OtherApps?): Int {
                    if (otherApps1?.isinstall == 0 && otherApps2?.isinstall == 1) {
                        return 1
                    } else
                        return -1
                }

            })
            if (completeList.size > 0) {
                appList.visibility = View.VISIBLE
                noResult.visibility = View.GONE
                val appRecycleListAdapter: AppRecycleListAdapter =
                    AppRecycleListAdapter(completeList, this, (completeList.size - totalChineseApp))
                appList.adapter = appRecycleListAdapter
                appList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            } else {
                noResult.visibility = View.VISIBLE
                appList.visibility = View.GONE
            }
        })
        appInstalledOrNot("com.whatsapp");

    }

    private fun appInstalledOrNot(uri: String): Boolean {
        val pm = packageManager
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES)
            return true
        } catch (e: PackageManager.NameNotFoundException) {
        }

        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            400 -> {

                var temp: Float = 0f
                for (i in 0 until completeList.size) {
                    if (appInstalledOrNot(completeList[i].packagename)) {
                        completeList[i].isinstall = 1
                        temp++;
                    } else {
                        completeList[i].isinstall = 0
                    }
                }
                if (temp == totalChineseApp) {

                } else {
                    totalChineseApp = temp
                    if (completeList.size > 0) {
                        appList.visibility = View.VISIBLE
                        noResult.visibility = View.GONE
                        (appList.adapter as AppRecycleListAdapter).otherApps = completeList
                        (appList.adapter as AppRecycleListAdapter).aatmanirbhar = (completeList.size - totalChineseApp)
                        (appList.adapter as AppRecycleListAdapter).dialogShown = false
                        (appList.adapter as AppRecycleListAdapter).notifyDataSetChanged()
                    } else {
                        noResult.visibility = View.VISIBLE
                        appList.visibility = View.GONE
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
