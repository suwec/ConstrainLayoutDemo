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
    private val spanCount = 1
    private val spanCountMore = 3
    private val spanSize = 8
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
        if (layoutManager != null) {
            val isOneColumn = layoutManager!!.spanCount == spanCount
            layoutManager!!.spanCount = if (isOneColumn) spanCountMore else spanCount
            adapter!!.setSpanCount(if (isOneColumn) spanCountMore else spanCount)
            adapter!!.notifyItemRangeChanged(0, adapter!!.itemCount)
            layoutManager!!.invalidateSpanAssignments()
            val resId = if (!isOneColumn) R.drawable.ic_dashboard_black_24dp else R.drawable.ic_format_line_spacing_black_24dp
            item.icon = resources.getDrawable(resId, null)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initGui() {
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView!!.setHasFixedSize(true)
        adapter = RecyleAdapter(this, spanCountMore, spanSize)
        layoutManager = StaggeredGridLayoutManager(spanCountMore, StaggeredGridLayoutManager.VERTICAL)
        layoutManager!!.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        recyclerView!!.addItemDecoration(SpaceItemDecoration(spanSize))
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.adapter = adapter
        recyclerView!!.itemAnimator = DefaultItemAnimator()
    }

    inner class SpaceItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            outRect.left = space
            outRect.right = space
            outRect.top = space
            outRect.bottom = space
        }
    }
}
