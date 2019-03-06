package com.suwec.android

import android.content.Context
import android.graphics.BitmapFactory
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import java.util.*

class RecyleAdapter(private val context: Context, private var spanCount: Int, private val spanSize: Int) : RecyclerView.Adapter<RecyleAdapter.MyViewHolder>() {
    private val imgList = ArrayList<Int>()

    private//        float density = dm.density;
    //        int height = dm.heightPixels;
    val screenWidth: Int
        get() {
            val resources = context.resources
            val dm = resources.displayMetrics
            return dm.widthPixels
        }

    init {
        for (i in 0..5)
            initData(context)
    }

    fun setSpanCount(spanCount: Int) {
        this.spanCount = spanCount
    }

    private fun initData(context: Context) {
        for (i in 1..19) {
            imgList.add(context.resources.getIdentifier("pic$i", "drawable", context.packageName))
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val inflate = LayoutInflater.from(context).inflate(R.layout.activity_main, viewGroup, false)
        return MyViewHolder(inflate)
    }

    fun getImageWidthHeight(resId: Int): Float {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        val bitmap = BitmapFactory.decodeResource(context.resources, resId, options) // 此时返回的bitmap为null
        val outWidth = options.outWidth.toFloat()
        val outHeight = options.outHeight.toFloat()
        return outHeight / outWidth
    }

    override fun onBindViewHolder(viewHolder: RecyleAdapter.MyViewHolder, i: Int) {
        val resourceId = imgList[i]
        val imageLayoutParams = viewHolder.imageView.layoutParams
        imageLayoutParams.width = screenWidth / spanCount - spanCount * spanSize//获取实际展示的图片宽度
        val imageWidthHeight = getImageWidthHeight(resourceId)
        imageLayoutParams.height = (imageLayoutParams.width * imageWidthHeight).toInt()//获取最终图片高度
        viewHolder.imageView.layoutParams = imageLayoutParams//应用高度到布局中
        Glide.with(context).load(resourceId).into(viewHolder.imageView)
        viewHolder.constraintLayout.setOnClickListener(object : View.OnClickListener {
            private var isDelete = true
            override fun onClick(v: View) {
                if (isDelete) {
                    isDelete = false
                    imgList.removeAt(i)
                    notifyItemRemoved(i)
                    notifyItemRangeChanged(i, imgList.size)
                    Thread(Runnable {
                        try {
                            Thread.sleep(300)
                            isDelete = true
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                        }
                    }).start()
                }
            }
        })
    }

    override fun getItemCount(): Int {
        return imgList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var constraintLayout: CardView
        val imageView: ImageView

        init {
            constraintLayout = itemView.findViewById(R.id.constrainLayout)
            imageView = itemView.findViewById(R.id.tv_hello)
        }
    }
}
