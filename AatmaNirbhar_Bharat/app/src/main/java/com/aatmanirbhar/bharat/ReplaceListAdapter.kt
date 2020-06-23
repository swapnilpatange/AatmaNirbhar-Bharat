package com.aatmanirbhar.bharat

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.replace_list_item.view.*
import android.content.Intent
import android.net.Uri


class ReplaceListAdapter : RecyclerView.Adapter<ReplaceListAdapter.ReplaceViewHolder> {

    lateinit var replacements: ArrayList<Replacement>
    var activity: Activity?

    constructor(replacements: ArrayList<Replacement>? = null, activity: Activity? = null) : super() {
        this.replacements = replacements!!
        this.activity = activity
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ReplaceViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.replace_list_item, null)
        return ReplaceViewHolder(view)
    }

    override fun getItemCount(): Int {
        return replacements!!.size
    }

    override fun onBindViewHolder(replaceViewHolder: ReplaceViewHolder, position: Int) {
        replaceViewHolder.bind(replacements[position], activity)

    }

    class ReplaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(replacement: Replacement, activity: Activity?) {
            itemView.replacename.text = replacement.appname
            Glide.with(activity).load(replacement.replaceimage).into(itemView.replaceImage)
            itemView.setOnClickListener {
                activity?.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(replacement.path)
                    )
                )
            }
        }
    }
}