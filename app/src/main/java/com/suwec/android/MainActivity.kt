package com.suwec.android

import android.graphics.Rect
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View

class MainActivity : AppCompatActivity() {
    private var recyclerView: RecyclerView? = null
    private val spanCount = 1 //每行列数为1
    private val spanCountMore = 3 //每行列数为3
    private val spanSize = 8 //每列之间的间距
    private var layoutManager: StaggeredGridLayoutManager? = null
    private var adapter: RecyleAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        initGui()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.navigation, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (layoutManager != null) { //点击菜单时切换列数，3列切换成一列显示大图，再点重新3列瀑布流
            val isOneColumn = layoutManager!!.spanCount == spanCount
            layoutManager!!.spanCount = if (isOneColumn) spanCountMore else spanCount
            adapter!!.setSpanCount(if (isOneColumn) spanCountMore else spanCount) //设置列数
            adapter!!.notifyItemRangeChanged(0, adapter!!.itemCount)
            layoutManager!!.invalidateSpanAssignments()
            val resId = if (!isOneColumn) R.drawable.ic_dashboard_black_24dp else R.drawable.ic_format_line_spacing_black_24dp
            item.icon = resources.getDrawable(resId, null)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initGui() {
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView!!.setHasFixedSize(true) //避免RecyclerView重新计算大小
        adapter = RecyleAdapter(this, spanCountMore, spanSize)
        layoutManager = StaggeredGridLayoutManager(spanCountMore, StaggeredGridLayoutManager.VERTICAL)
        layoutManager!!.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE //避免滑动加载时跳动
        recyclerView!!.addItemDecoration(SpaceItemDecoration(spanSize)) //设置间距
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.adapter = adapter
        recyclerView!!.itemAnimator = DefaultItemAnimator() //设置默认加载新Item动画
    }

    inner class SpaceItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() { //切换之后重新设置间距
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            outRect.left = space
            outRect.right = space
            outRect.top = space
            outRect.bottom = space
        }
    }
}
