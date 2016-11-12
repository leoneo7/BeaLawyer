package leoneo7.bealawyer.helper

import android.app.*
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import leoneo7.bealawyer.R
import leoneo7.bealawyer.base.Category
import leoneo7.bealawyer.edit.EditActivity
import leoneo7.bealawyer.helper.Const.Companion.CATEGORY_ID
import leoneo7.bealawyer.helper.Const.Companion.CATEGORY_NAME
import leoneo7.bealawyer.helper.Const.Companion.COUNT
import java.util.*

/**
 * Created by ryouken on 2016/11/07.
 */

class CategoryDialogFragment : DialogFragment() {

    private var adapter: DialogListAdapter? = null
    private var textView: TextView? = null
    private val list = ArrayList<Category>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // ダイアログ設定
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(getString(R.string.category_choose))

        getAllCategory(activity)

        val inflater = LayoutInflater.from(activity)
        val layout = inflater.inflate(R.layout.category_listview, activity.findViewById(R.id.category_listview_layout) as ViewGroup?)

        adapter = DialogListAdapter(activity.applicationContext, 0, list)
        val listView = layout.findViewById(R.id.category_listview) as ListView
        listView.adapter = adapter

        listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val category = list[position]
            Log.d("onItemClick", category.name)
            submit(category)
            adapter!!.notifyDataSetChanged()
        }

        builder.setView(layout)
        return builder.create()
    }

    internal fun submit(category: Category) {
        val activity = activity
        if (activity == null) {
            dismiss()
            return
        }

        if (activity is EditActivity) {
            Log.d("instanceof", "------------")
            activity.setCategory(category)
        }

        dismiss()
    }

    inner class DialogListAdapter(context: Context, resource: Int, objects: List<Category>) : ArrayAdapter<Category>(context, resource, objects) {
        private val inflater: LayoutInflater

        init {
            inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }

        override fun getView(position: Int, view: View?, parent: ViewGroup): View {
            var view = view
            if (view == null) view = inflater.inflate(android.R.layout.simple_list_item_1, null)
            textView = view!!.findViewById(android.R.id.text1) as TextView
            textView!!.text = list[position].name
            textView!!.setTextColor(Color.BLACK)
            return view
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

    companion object {

        fun newInstance(target: Fragment, requestCode: Int): CategoryDialogFragment {
            val fragment = CategoryDialogFragment()
            fragment.setTargetFragment(target, requestCode)

            val args = Bundle()
            fragment.arguments = args

            return fragment
        }
    }

}

