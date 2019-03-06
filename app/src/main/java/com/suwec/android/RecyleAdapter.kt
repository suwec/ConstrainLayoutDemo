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
    private val imgList = ArrayList<Int>() //图片地址列表

    private//        float density = dm.density;
    //        int height = dm.heightPixels;
    val screenWidth: Int //屏幕宽度
        get() {
            val resources = context.resources
            val dm = resources.displayMetrics
            return dm.widthPixels
        }

    init { //初始化图片，重复加载多次显示更多图片
        for (i in 0..5)
            initData(context)
    }

    fun setSpanCount(spanCount: Int) { //设置列数
        this.spanCount = spanCount
    }

    private fun initData(context: Context) {
        for (i in 1..19) { //加载图片资源地址
            imgList.add(context.resources.getIdentifier("pic$i", "drawable", context.packageName))
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder { //初始化Item布局
        val inflate = LayoutInflater.from(context).inflate(R.layout.activity_main, viewGroup, false)
        return MyViewHolder(inflate)
    }

    fun getImageWidthHeight(resId: Int): Float { //初始化图片宽高
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
        Glide.with(context).load(resourceId).into(viewHolder.imageView) //使用Glide加载图片
        viewHolder.constraintLayout.setOnClickListener(object : View.OnClickListener {
            private var isDelete = true
            override fun onClick(v: View) { //点击图片删除当前点击的图片
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
