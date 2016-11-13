package leoneo7.bealawyer.edit

import android.app.Dialog
import android.content.Intent
import android.graphics.Point
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import butterknife.ButterKnife
import butterknife.bindView
import leoneo7.bealawyer.R
import leoneo7.bealawyer.base.Entry
import leoneo7.bealawyer.helper.Const.Companion.ENTRY
import leoneo7.bealawyer.helper.Const.Companion.ENTRY_ID
import leoneo7.bealawyer.helper.DBAdapter
import leoneo7.bealawyer.helper.MenuHelper
import leoneo7.bealawyer.main.EntryActivity



/**
 * Created by ryouken on 2016/11/05.
 */

class ViewActivity : AppCompatActivity() {

    val mNavigationView: NavigationView by bindView(R.id.navigation_view)
    val mDrawerLayout: DrawerLayout by bindView(R.id.drawer_layout)
    val mToolBar: Toolbar by bindView(R.id.tool_bar)
    val titleText: TextView by bindView(R.id.title)
    val imageView: ImageView by bindView(R.id.imageView)
    val categoryText: TextView by bindView(R.id.category_text)
    val deleteButton: Button by bindView(R.id.deleteButton)
    val numberingText: TextView by bindView(R.id.numbering)
    val editButton: FloatingActionButton by bindView(R.id.editButton)

    private var entry: Entry? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view)
        ButterKnife.bind(this)

        setupToolBar()
        MenuHelper.onClickMenu(this, mNavigationView)

        setupData()
        setupListener()
    }

    private fun setupData() {
        entry = intent.getParcelableExtra(ENTRY)
        val image = entry!!.image

        val uri: Uri?
        if (image != null)
            uri = Uri.parse(image)
        else
            uri = null

        titleText.text = entry!!.title
        imageView.setImageURI(uri)
        numberingText.text = entry!!.numbering
        categoryText.text = entry!!.categoryName
    }

    private fun setupListener() {
        editButton.setOnClickListener(editButtonListener)
        deleteButton.setOnClickListener(deleteButtonListener)
        imageView.setOnClickListener(imageViewListener)
    }

    val editButtonListener = View.OnClickListener {
        val intent = Intent(this, EditActivity::class.java)
        if (entry!!.entryId == -1) getNewEntryId()
        intent.putExtra(ENTRY, entry)
        startActivity(intent)
    }

    val deleteButtonListener = View.OnClickListener {
        AlertDialog.Builder(this)
                .setTitle(R.string.delete_confirm)
                .setPositiveButton("OK") { dialog, whichButton ->
                    deleteEntry(entry!!)
                    val intent = Intent(this, EntryActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                }
                .setNegativeButton("キャンセル") { dialog, whichButton -> }
                .show()
    }

    val imageViewListener = View.OnClickListener {
        val view = ImageView(this)
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        view.setImageBitmap(bitmap)
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width = size.x

        val factor = (width / bitmap.width).toFloat()
        view.scaleType = ImageView.ScaleType.FIT_CENTER
        val dialog = Dialog(this)
        dialog.window.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(view)
        dialog.window.setLayout((bitmap.width * factor).toInt(), (bitmap.height * factor).toInt())
        dialog.show()
    }

    private fun deleteEntry(entry: Entry) {
        val dbAdapter = DBAdapter(this)
        dbAdapter.open()
        dbAdapter.deleteEntry(entry.entryId)
        dbAdapter.close()
    }

    private fun setupToolBar() {
        setSupportActionBar(mToolBar)
        val actionBar = supportActionBar
        MenuHelper.setupToolBar(mToolBar, actionBar, "")
    }

    private fun getNewEntryId() {
        val dbAdapter = DBAdapter(this)
        dbAdapter.open()
        val cursor = dbAdapter.newEntryId
        startManagingCursor(cursor)
        if (cursor.moveToFirst()) {
            do {
                entry!!.entryId = cursor.getInt(cursor.getColumnIndex(ENTRY_ID))
            } while (cursor.moveToNext())
        }
        stopManagingCursor(cursor)
        dbAdapter.close()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                mDrawerLayout.openDrawer(GravityCompat.START)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
