package com.marcinmejner.czytnikreddit

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.marcinmejner.czytnikreddit.model.Post
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.assist.FailReason
import com.nostra13.universalimageloader.core.assist.ImageScaleType
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener

class CustomListAdapter(context: Context, val resource: Int, posts: ArrayList<Post>) : ArrayAdapter<Post>(context, resource, posts) {
    private val TAG = "CustomListAdapter"

    private var lastPosition = -1


    inner class ViewHolder {

         var title: TextView? = null
         var author: TextView? = null
         var date_updated: TextView? = null
         var mProgressBar: ProgressBar? = null
         var thumbnailURL: ImageView? = null
    }

    init {
        setupImageLoader()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {

        var convertView = convertView

        var title = getItem(position).title
        var imgUrl = getItem(position).thumbnailURL
        var author = getItem(position).author
        var date_updated = getItem(position).date_updated


        try {


            //create the view result for showing the animation
            val result: View

            //ViewHolder object
            val holder: ViewHolder

            if (convertView == null) {
                val inflater = LayoutInflater.from(context)
                convertView = inflater.inflate(resource, parent, false)
                holder = ViewHolder()
                holder.title = convertView!!.findViewById<View>(R.id.cardTitle) as TextView
                holder.thumbnailURL = convertView.findViewById<View>(R.id.cardImage) as ImageView
                holder.author = convertView.findViewById<View>(R.id.cardAuthor) as TextView
                holder.date_updated = convertView.findViewById<View>(R.id.cardUpdated) as TextView
                holder.mProgressBar = convertView.findViewById<View>(R.id.cardProgressDialog) as ProgressBar

                result = convertView

                convertView.tag = holder
            } else {
                holder = convertView.tag as ViewHolder
                result = convertView
            }

            lastPosition = position

            holder.title!!.text = title
            holder.author!!.text = author
            holder.date_updated!!.text = date_updated


            val imageLoader = ImageLoader.getInstance()

            val defaultImage = context.resources.getIdentifier("@drawable/reddit_default", null, context.packageName)


            val options = DisplayImageOptions.Builder().cacheInMemory(true)
                    .cacheOnDisc(true).resetViewBeforeLoading(true)
                    .showImageForEmptyUri(defaultImage)
                    .showImageOnFail(defaultImage)
                    .showImageOnLoading(defaultImage).build()


            imageLoader.displayImage(imgUrl, holder.thumbnailURL, options, object : ImageLoadingListener {
                override fun onLoadingStarted(imageUri: String, view: View) {
                    holder.mProgressBar!!.visibility = View.VISIBLE
                }

                override fun onLoadingFailed(imageUri: String, view: View, failReason: FailReason) {
                    holder.mProgressBar!!.visibility = View.GONE
                }

                override fun onLoadingComplete(imageUri: String, view: View, loadedImage: Bitmap) {
                    holder.mProgressBar!!.visibility = View.GONE
                }

                override fun onLoadingCancelled(imageUri: String, view: View) {
                    holder.mProgressBar!!.visibility = View.GONE
                }

            })

            return convertView
        } catch (e: IllegalArgumentException) {
            Log.e(TAG, "getView: IllegalArgumentException: " + e.message)
            return convertView
        }






    }

    private fun setupImageLoader() {
        // UIL Setup
        val defaultOptions = DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(FadeInBitmapDisplayer(300)).build()

        val config = ImageLoaderConfiguration.Builder(
                context)
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build()

        ImageLoader.getInstance().init(config)
    }
}