package com.marcinmejner.czytnikreddit.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.TextView
import com.marcinmejner.czytnikreddit.R
import com.marcinmejner.czytnikreddit.comments.Comment

class CommentsListAdapter(context: Context, val resource: Int, posts: ArrayList<Comment>) : ArrayAdapter<Comment>(context, resource, posts) {
    private val TAG = "CustomListAdapter"

    private var lastPosition = -1

    inner class ViewHolder {

         var comment: TextView? = null
         var author: TextView? = null
         var date_updated: TextView? = null
         var mProgressBar: ProgressBar? = null
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {

        var convertView = convertView

        var title = getItem(position).comment
        var author = getItem(position).author
        var date_updated = getItem(position).updated

        try {
            //create the view result for showing the animation
            val result: View

            //ViewHolder object
            val holder: ViewHolder

            if (convertView == null) {
                val inflater = LayoutInflater.from(context)
                convertView = inflater.inflate(resource, parent, false)
                holder = ViewHolder()
                holder.comment = convertView!!.findViewById<View>(R.id.Comment) as TextView
                holder.author = convertView.findViewById<View>(R.id.commentAuthor) as TextView
                holder.date_updated = convertView.findViewById<View>(R.id.commentUpdated) as TextView
                holder.mProgressBar = convertView.findViewById<View>(R.id.commentProgressBar) as ProgressBar

                result = convertView

                convertView.tag = holder
            } else {
                holder = convertView.tag as ViewHolder
                result = convertView
                holder.mProgressBar?.visibility = View.VISIBLE
            }

            lastPosition = position

            holder.comment!!.text = title
            holder.author!!.text = author
            holder.date_updated!!.text = date_updated
            holder.mProgressBar?.visibility = View.INVISIBLE

            return convertView
        } catch (e: IllegalArgumentException) {
            Log.e(TAG, "getView: IllegalArgumentException: " + e.message)
            return convertView
        }
    }
}