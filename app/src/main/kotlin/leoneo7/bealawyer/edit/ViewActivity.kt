package leoneo7.bealawyer.edit

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
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
         editButton.setOnClickListener {
             val intent = Intent(this, EditActivity::class.java)
             if (entry!!.entryId == -1) getNewEntryId()
             intent.putExtra(ENTRY, entry)
             startActivity(intent)
         }
    }

    private fun setupToolBar() {
        setSupportActionBar(mToolBar)
        val actionBar = supportActionBar
        MenuHelper.setupToolBar(mToolBar, actionBar)
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
