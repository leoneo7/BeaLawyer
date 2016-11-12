package leoneo7.bealawyer.recycler

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet

/**
 * Created by ryouken on 2016/11/03.
 */

class EntryRecyclerView(context: Context, attrs: AttributeSet) : RecyclerView(context, attrs) {

    init {
        setRecyclerAdapter(context)
    }

    fun setRecyclerAdapter(context: Context) {
        layoutManager = LinearLayoutManager(context)
        adapter = EntryRecyclerAdapter(context)
    }
}