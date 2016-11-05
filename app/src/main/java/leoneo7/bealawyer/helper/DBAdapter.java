package leoneo7.bealawyer.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.ByteArrayOutputStream;

/**
 * Created by ryouken on 2016/11/01.
 */
public class DBAdapter {

    static final String DATABASE_NAME = "bealaywer.db";
    static final int DATABASE_VERSION = 3;

    public static final String ENTRIES = "entries";
    public static final String ENTRY_ID = "entry_id";
    public static final String TITLE = "title";
    public static final String IMAGE = "image";
    public static final String NUMBERING = "numbering";
    public static final String REPEAT = "repeat";
    public static final String DATE = "date";


    protected final Context context;
    protected DatabaseHelper dbHelper;
    protected SQLiteDatabase db;

    public DBAdapter(Context context){
        this.context = context;
        dbHelper = new DatabaseHelper(this.context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION); }

        @Override public void onCreate(SQLiteDatabase db) {
            db.execSQL("PRAGMA foreign_keys=ON;");
            db.execSQL( "CREATE TABLE entries ("
                    + "entry_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "title TEXT NOT NULL,"
                    + "image TEXT,"
                    + "numbering TEXT,"
                    + "repeat INTEGER NOT NULL,"
                    + "date INTEGER NOT NULL UNIQUE);");
        }

        @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("PRAGMA foreign_keys = ON;");
            db.execSQL("DROP TABLE IF EXISTS " + ENTRIES);
            onCreate(db);
        }
    }

    public DBAdapter open() {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        dbHelper.close();
    }

    public Cursor getEntries(){
        Log.d("getEntries", "-------------------");
        String sql = "SELECT * from entries";
        return db.rawQuery(sql, null);
    }

    public Cursor getEntriesByDate(){
        String sql = "SELECT * from entries order by date desc;";
        return db.rawQuery(sql, null);
    }

    public Cursor getNewEntryId(){
        String sql = "SELECT entry_id from entries WHERE entry_id = (select max(entry_id) from entries);";
        return db.rawQuery(sql, null);
    }

    public void saveEntry(String title, String image, String numbering, int repeat, long date){
        Log.d("DBAsaveEntry", "---------------");
        ContentValues values = new ContentValues();
        values.put(TITLE, title);
        values.put(IMAGE, image);
        values.put(NUMBERING, numbering);
        values.put(REPEAT, repeat);
        values.put(DATE, date);
        db.insertOrThrow(ENTRIES, null, values);
    }

    public void updateEntry(int id, String title, String image, String numbering, int repeat, long date){
        ContentValues values = new ContentValues();
        values.put(TITLE, title);
        values.put(IMAGE, image);
        values.put(NUMBERING, numbering);
        values.put(REPEAT, repeat);
        values.put(DATE, date);
        db.update(ENTRIES, values, "entry_id = " + String.valueOf(id), null);
    }

    public byte[] changeBitmapToByte(Bitmap bitmap) {
        if (bitmap == null) return null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)) {
            byte[] bytes = byteArrayOutputStream.toByteArray();
            return bytes;
        }
        return null;
    }

}