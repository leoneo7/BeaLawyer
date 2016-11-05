package leoneo7.bealawyer.main;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;

import leoneo7.bealawyer.base.Entry;
import leoneo7.bealawyer.helper.DBAdapter;

/**
 * Created by ryouken on 2016/11/03.
 */

public class CardRecyclerView extends RecyclerView {

    List<Entry> list = new ArrayList<>();

    public CardRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAllEntry(context);
        setRecyclerAdapter(context);
    }

    public void setRecyclerAdapter(Context context){
        setLayoutManager(new LinearLayoutManager(context));
        setAdapter(new CardRecyclerAdapter(context, list));
    }

    private void getAllEntry(Context context) {
        list.clear();
        DBAdapter dbAdapter = new DBAdapter(context);
        dbAdapter.open();
        Cursor cursor = dbAdapter.getEntriesByDate();
        ((Activity) context).startManagingCursor(cursor);
        if (cursor.moveToFirst()) {
            do {
                Entry entry = new Entry(
                        cursor.getInt(cursor.getColumnIndex(DBAdapter.ENTRY_ID)),
                        cursor.getString(cursor.getColumnIndex(DBAdapter.TITLE)),
                        cursor.getString(cursor.getColumnIndex(DBAdapter.IMAGE)),
                        cursor.getString(cursor.getColumnIndex(DBAdapter.NUMBERING)),
                        cursor.getLong(cursor.getColumnIndex(DBAdapter.DATE)),
                        cursor.getString(cursor.getColumnIndex(DBAdapter.REPEAT)));
                list.add(entry);
            } while (cursor.moveToNext());
        }
        ((Activity) context).stopManagingCursor(cursor);
        dbAdapter.close();
    }
}