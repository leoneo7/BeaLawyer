package leoneo7.bealawyer.recycler

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import leoneo7.bealawyer.R
import leoneo7.bealawyer.base.Category
import leoneo7.bealawyer.helper.Const.Companion.CATEGORY_ID
import leoneo7.bealawyer.helper.Const.Companion.CATEGORY_NAME
import leoneo7.bealawyer.helper.Const.Companion.COUNT
import leoneo7.bealawyer.helper.DBAdapter
import leoneo7.bealawyer.main.EntryActivity
import java.util.*

/**
 * Created by ryouken on 2016/11/03.
 */

class CategoryRecyclerAdapter(private val context: Context) : RecyclerView.Adapter<CategoryRecyclerAdapter.ViewHolder>() {

    private val list = ArrayList<Category>()

    init {
        getAllCategory(context)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(vh: ViewHolder, position: Int) {
        val category = list[position]

        vh.name.text = category.name
        vh.count.text = category.count.toString() + "個の答案"
        vh.layout.setOnClickListener {
            val intent = Intent(context, EntryActivity::class.java)
            intent.putExtra(CATEGORY_ID, category.id)
            intent.putExtra(CATEGORY_NAME, category.name)
            intent.putExtra(COUNT, category.count)
            context.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryRecyclerAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val v = layoutInflater.inflate(R.layout.category_recycler, parent, false)
        val viewHolder = ViewHolder(v)
        return viewHolder
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var name: TextView
        var count: TextView
        var layout: LinearLayout

        init {
            name = v.findViewById(R.id.name) as TextView
            count = v.findViewById(R.id.count) as TextView
            layout = v.findViewById(R.id.layout) as LinearLayout
        }
    }

    private fun getAllCategory(context: Context) {
        list.clear()
        val dbAdapter = DBAdapter(context)
        dbAdapter.open()
        val cursor = dbAdapter.categories
        (context as Activity).startManagingCursor(cursor)
        if (cursor.moveToFirst()) {
            do {
                val category = Category(
                        cursor.getInt(cursor.getColumnIndex(CATEGORY_ID)),
                        cursor.getString(cursor.getColumnIndex(CATEGORY_NAME)),
                        cursor.getInt(cursor.getColumnIndex(COUNT)))
                list.add(category)
            } while (cursor.moveToNext())
        }
        context.stopManagingCursor(cursor)
        dbAdapter.close()
    }
}