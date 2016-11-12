package leoneo7.bealawyer.helper

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import leoneo7.bealawyer.base.Entry
import leoneo7.bealawyer.helper.Const.Companion.CATEGORIES
import leoneo7.bealawyer.helper.Const.Companion.CATEGORY_ID
import leoneo7.bealawyer.helper.Const.Companion.CATEGORY_NAME
import leoneo7.bealawyer.helper.Const.Companion.DATE
import leoneo7.bealawyer.helper.Const.Companion.ENTRIES
import leoneo7.bealawyer.helper.Const.Companion.ENTRY_ID
import leoneo7.bealawyer.helper.Const.Companion.IMAGE
import leoneo7.bealawyer.helper.Const.Companion.NUMBERING
import leoneo7.bealawyer.helper.Const.Companion.REPEAT_TIMES
import leoneo7.bealawyer.helper.Const.Companion.TAGMAPS
import leoneo7.bealawyer.helper.Const.Companion.TAGS
import leoneo7.bealawyer.helper.Const.Companion.TAG_ID
import leoneo7.bealawyer.helper.Const.Companion.TAG_NAME
import leoneo7.bealawyer.helper.Const.Companion.TITLE

/**
 * Created by ryouken on 2016/11/01.
 */
class DBAdapter(private val context: Context) {
    private var dbHelper: DatabaseHelper = DatabaseHelper(this.context)
    private var db: SQLiteDatabase = dbHelper.writableDatabase

    init { }

    private class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL("PRAGMA foreign_keys=ON;")
            db.execSQL("CREATE TABLE entries ("
                    + "entry_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "title TEXT NOT NULL,"
                    + "image TEXT,"
                    + "numbering TEXT,"
                    + "repeat_times INTEGER NOT NULL,"
                    + "date INTEGER NOT NULL,"
                    + "category_id INTEGER NOT NULL,"
                    + "FOREIGN KEY (category_id) REFERENCES categories(category_id)"
                    + ");")
            db.execSQL("CREATE TABLE categories ("
                    + "category_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "category_name TEXT NOT NULL);")
            db.execSQL("CREATE TABLE tags ("
                    + "tag_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "tag_name TEXT NOT NULL);")
            db.execSQL("CREATE TABLE tagmaps ("
                    + "tagmap_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "entry_id INTEGER NOT NULL,"
                    + "tag_id INTEGER NOT NULL,"
                    + "FOREIGN KEY (entry_id) REFERENCES entries(entry_id),"
                    + "FOREIGN KEY (tag_id) REFERENCES tags(tag_id)"
                    + ");")
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("PRAGMA foreign_keys = ON;")
            db.execSQL("DROP TABLE IF EXISTS " + ENTRIES)
            onCreate(db)
        }
    }

    fun open(): DBAdapter {
        return this
    }

    fun close() {
        dbHelper.close()
    }

    val entriesByDate: Cursor
        get() {
            val sql = "SELECT * from entries as e " +
                    "JOIN categories as c on e.category_id = c.category_id order by date desc;"
            return db.rawQuery(sql, null)
        }

    val newEntryId: Cursor
        get() {
            val sql = "SELECT entry_id from entries " +
                    "WHERE entry_id = (select max(entry_id) from entries);"
            return db.rawQuery(sql, null)
        }

    val categories: Cursor
        get() {
            val sql = "SELECT c.*, count(e.entry_id) as count " +
                    "FROM categories as c LEFT JOIN entries as e ON (c.category_id = e.category_id) " +
                    "GROUP BY c.category_id;"
            return db.rawQuery(sql, null)
        }

    fun getEntriesInCategory(categoryId: Int): Cursor {
        val sql = "SELECT * from entries as e " +
                "JOIN categories as c on e.category_id = c.category_id " +
                "WHERE e.category_id = " + categoryId.toString() + ";"
        return db.rawQuery(sql, null)
    }

    val tags: Cursor
        get() {
            val sql = "SELECT * from tags;"
            return db.rawQuery(sql, null)
        }

    fun getTagsByEntry(entryId: Int): Cursor {
        val sql = "SELECT * FROM tags " +
                "JOIN tagmaps ON tags.tag_id = tagmaps.tag_id " +
                "WHERE tagmaps.entry_id = " + entryId.toString() + ";"
        return db.rawQuery(sql, null)
    }

    fun saveEntry(entry: Entry) {
        Log.d("DBAsaveEntry", "---------------")
        val values = ContentValues()
        values.put(TITLE, entry.title)
        values.put(IMAGE, entry.image)
        values.put(NUMBERING, entry.numbering)
        values.put(REPEAT_TIMES, entry.repeat)
        values.put(DATE, entry.date)
        values.put(CATEGORY_ID, entry.categoryId)
        db.insertOrThrow(ENTRIES, null, values)
    }

    fun updateEntry(entry: Entry) {
        val values = ContentValues()
        values.put(TITLE, entry.title)
        values.put(IMAGE, entry.image)
        values.put(NUMBERING, entry.numbering)
        values.put(REPEAT_TIMES, entry.repeat)
        values.put(DATE, entry.date)
        values.put(CATEGORY_ID, entry.categoryId)
        db.update(ENTRIES, values, "entry_id = " + entry.entryId.toString(), null)
    }

    fun saveCategory(name: String) {
        Log.d("DBAsaveCategory", "---------------")
        val values = ContentValues()
        values.put(CATEGORY_NAME, name)
        db.insertOrThrow(CATEGORIES, null, values)
    }

    fun saveTag(name: String) {
        Log.d("DBAsaveTag", "---------------")
        val values = ContentValues()
        values.put(TAG_NAME, name)
        db.insertOrThrow(TAGS, null, values)
    }

    fun saveTagMap(entry_id: Int, tag_id: Int) {
        Log.d("DBAsaveTagMap", "---------------")
        val values = ContentValues()
        values.put(ENTRY_ID, entry_id)
        values.put(TAG_ID, tag_id)
        db.insertOrThrow(TAGMAPS, null, values)
    }

    companion object {
        internal val DATABASE_NAME = "bealaywer.db"
        internal val DATABASE_VERSION = 4
    }

}