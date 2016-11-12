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
import leoneo7.bealawyer.base.Entry
import leoneo7.bealawyer.edit.ViewActivity
import leoneo7.bealawyer.helper.Const.Companion.CATEGORY_ID
import leoneo7.bealawyer.helper.Const.Companion.CATEGORY_NAME
import leoneo7.bealawyer.helper.Const.Companion.DATE
import leoneo7.bealawyer.helper.Const.Companion.ENTRY
import leoneo7.bealawyer.helper.Const.Companion.ENTRY_ID
import leoneo7.bealawyer.helper.Const.Companion.IMAGE
import leoneo7.bealawyer.helper.Const.Companion.NUMBERING
import leoneo7.bealawyer.helper.Const.Companion.REPEAT_TIMES
import leoneo7.bealawyer.helper.Const.Companion.TITLE
import leoneo7.bealawyer.helper.DBAdapter
import java.util.*

/**
 * Created by ryouken on 2016/11/03.
 */

class EntryRecyclerAdapter(private val context: Context) : RecyclerView.Adapter<EntryRecyclerAdapter.ViewHolder>() {

    private val list = ArrayList<Entry>()
    val categoryId = (context as Activity).intent.getIntExtra(CATEGORY_ID, -1)

    init {
        if (categoryId == -1)
            getAllEntry(context)
        else
            getEntryInCategory(context, categoryId)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(vh: ViewHolder, position: Int) {
        val entry = list[position]

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = entry.date
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val date = calendar.get(Calendar.DATE)
        val dateString = String.format("%s/%s/%s", year, month + 1, date)

        vh.title.text = entry.title
        vh.date.text = dateString
        vh.repeat.text = entry.repeat.toString() + "回"
        vh.layout.setOnClickListener {
            val intent = Intent(context, ViewActivity::class.java)
            intent.putExtra(ENTRY, entry)
            context.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryRecyclerAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val v = layoutInflater.inflate(R.layout.entry_recycler, parent, false)
        val viewHolder = ViewHolder(v)
        return viewHolder
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var title: TextView
        var date: TextView
        var repeat: TextView
        var layout: LinearLayout

        init {
            title = v.findViewById(R.id.title) as TextView
            date = v.findViewById(R.id.date) as TextView
            repeat = v.findViewById(R.id.repeat) as TextView
            layout = v.findViewById(R.id.layout) as LinearLayout
        }
    }

    private fun getAllEntry(context: Context) {
        list.clear()
        val dbAdapter = DBAdapter(context)
        dbAdapter.open()
        val cursor = dbAdapter.entriesByDate
        (context as Activity).startManagingCursor(cursor)
        if (cursor.moveToFirst()) {
            do {
                val entry = Entry(
                        cursor.getInt(cursor.getColumnIndex(ENTRY_ID)),
                        cursor.getString(cursor.getColumnIndex(TITLE)),
                        cursor.getString(cursor.getColumnIndex(IMAGE)),
                        cursor.getString(cursor.getColumnIndex(NUMBERING)),
                        cursor.getInt(cursor.getColumnIndex(REPEAT_TIMES)),
                        cursor.getLong(cursor.getColumnIndex(DATE)),
                        cursor.getInt(cursor.getColumnIndex(CATEGORY_ID)),
                        cursor.getString(cursor.getColumnIndex(CATEGORY_NAME)))
                list.add(entry)
            } while (cursor.moveToNext())
        }
        context.stopManagingCursor(cursor)
        dbAdapter.close()
    }

    private fun getEntryInCategory(context: Context, categoryId: Int) {
        list.clear()
        val dbAdapter = DBAdapter(context)
        dbAdapter.open()
        val cursor = dbAdapter.getEntriesInCategory(categoryId)
        (context as Activity).startManagingCursor(cursor)
        if (cursor.moveToFirst()) {
            do {
                val entry = Entry(
                        cursor.getInt(cursor.getColumnIndex(ENTRY_ID)),
                        cursor.getString(cursor.getColumnIndex(TITLE)),
                        cursor.getString(cursor.getColumnIndex(IMAGE)),
                        cursor.getString(cursor.getColumnIndex(NUMBERING)),
                        cursor.getInt(cursor.getColumnIndex(REPEAT_TIMES)),
                        cursor.getLong(cursor.getColumnIndex(DATE)),
                        categoryId,
                        cursor.getString(cursor.getColumnIndex(CATEGORY_NAME)))
                list.add(entry)
            } while (cursor.moveToNext())
        }
        context.stopManagingCursor(cursor)
        dbAdapter.close()
    }

}