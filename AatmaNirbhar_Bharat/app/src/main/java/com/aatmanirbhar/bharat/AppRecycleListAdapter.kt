package com.aatmanirbhar.bharat

import android.app.Activity
import android.content.DialogInterface
import android.graphics.Rect
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.app_list_item.view.*
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.support.v7.app.AlertDialog
import kotlinx.android.synthetic.main.aatmanirbhar_score_item.view.*


class AppRecycleListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {


    lateinit var otherApps: ArrayList<OtherApps>
    var activity: Activity? = null
    var AATMA_NIRBHAR_SCORE_ITEM = 0
    var aatmanirbhar: Float
    var dialogShown: Boolean = false

    constructor(
        otherApps: ArrayList<OtherApps>,
        activity: Activity,
        aatmanirbhar: Float
    ) {
        this.otherApps = otherApps
        this.activity = activity
        this.aatmanirbhar = aatmanirbhar
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == AATMA_NIRBHAR_SCORE_ITEM) {

            val view: View = LayoutInflater.from(parent.context).inflate(R.layout.aatmanirbhar_score_item, null)
            return AatmaNirbharViewHolder(view)
        } else {
            val view: View = LayoutInflater.from(parent.context).inflate(R.layout.app_list_item, null)
            return AppRecycleViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return otherApps!!.size + 1
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        if (viewHolder is AatmaNirbharViewHolder) {
            viewHolder.bindItems(aatmanirbhar, otherApps.size, activity, dialogShown)
            dialogShown = true
        } else if (viewHolder is AppRecycleViewHolder)
            viewHolder.bindItems(otherApps[position - 1], activity)
    }

    class AppRecycleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(otherapps: OtherApps?, activity: Activity?) {
            if (otherapps?.isinstall == 0) {
                itemView.appname.visibility = View.GONE
                itemView.appimage.visibility = View.GONE
                itemView.uninstall.visibility = View.GONE
                itemView.divider1.visibility = View.GONE

            } else {
                itemView.appname.visibility = View.VISIBLE
                itemView.appimage.visibility = View.VISIBLE
                itemView.uninstall.visibility = View.VISIBLE
                itemView.divider1.visibility = View.VISIBLE
                itemView.appname.text = otherapps?.name
                Glide.with(activity).load(otherapps?.image).into(itemView.appimage)
                itemView.uninstall.setOnClickListener {
                    val packageUri = Uri.parse("package:" + otherapps?.packagename)
                    val uninstallIntent = Intent(Intent.ACTION_DELETE, packageUri)
                    activity?.startActivityForResult(uninstallIntent, 400)
                }
            }

            itemView.otherappsHead.text = "Indian Alternatives to " + otherapps?.name
            if (!(itemView.replaceList.adapter is ReplaceListAdapter)) {
                val replaceListAdapter: ReplaceListAdapter = ReplaceListAdapter(otherapps?.replacement, activity)
                itemView.replaceList.adapter = replaceListAdapter
                itemView.replaceList.layoutManager =
                        LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                itemView.replaceList.addItemDecoration(HorizontalSpaceItemDecoration(25))

            }
        }
    }

    override fun getItemViewType(position: Int): Int {

        if (position == 0)
            return AATMA_NIRBHAR_SCORE_ITEM
        else
            return position
    }

    class HorizontalSpaceItemDecoration(val horizontalSpace: Int) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(
            outRect: Rect, view: View, parent: RecyclerView,
            state: RecyclerView.State
        ) {
            outRect.right = horizontalSpace
        }
    }

    class AatmaNirbharViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var pStatus: Float = 0f
        private val handler = Handler()

        var dialogShown = false
        fun bindItems(
            aatmanirbhar: Float,
            count: Int,
            activity: Activity?,
            mdialogShown: Boolean
        ) {
            this.dialogShown = mdialogShown
            var max: Float = (aatmanirbhar / count) * 100f
            Thread(Runnable {
                while (pStatus <= max) {
                    handler.post(Runnable {
                        itemView.progressBar.progress = pStatus.toInt()
                        itemView.txtProgress.setText(pStatus.toString() + " %")

                    })
                    try {
                        Thread.sleep(50)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }

                    pStatus++
                }

                activity?.runOnUiThread({
                    if (activity != null && !activity.isFinishing) {
                        if (pStatus > max && !this.dialogShown) {
                            this.dialogShown = true
                            AlertDialog.Builder(activity!!)
                                .setTitle("Aatmanirbhar Score")
                                .setMessage(
                                    (if ((count - aatmanirbhar).toInt() == 0) "Great!! " else "") + ("You are " + (pStatus - 1) +
                                            "% Aatmanirbhar. You have " + (count - aatmanirbhar).toInt() + if ((count - aatmanirbhar).toInt() == 1) " chinese app in your phone.  Explore Indian alternatives to famous chinese apps."
                                    else " chinese apps in your phone. Explore Indian alternatives to famous chinese apps.")
                                )

                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                // The dialog is automatically dismissed when a dialog button is clicked.
                                .setPositiveButton(
                                    android.R.string.yes,
                                    DialogInterface.OnClickListener { dialog, which ->
                                        // Continue with delete operation
                                    })
                                .show()
                        }
                    }
                })


            }).start()
        }
    }


}