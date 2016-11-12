package leoneo7.bealawyer.main

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import butterknife.bindView
import leoneo7.bealawyer.R
import leoneo7.bealawyer.edit.EditActivity
import leoneo7.bealawyer.helper.Const
import leoneo7.bealawyer.helper.Const.Companion.CATEGORY_ID
import leoneo7.bealawyer.helper.MenuHelper

/**
 * Created by ryouken on 2016/11/01.
 */
class EntryActivity : AppCompatActivity() {

    val mNavigationView: NavigationView by bindView(R.id.navigation_view)
    val mDrawerLayout: DrawerLayout by bindView(R.id.drawer_layout)
    val mToolBar: Toolbar by bindView(R.id.tool_bar)
    val mAddButton: FloatingActionButton by bindView(R.id.addButton)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.entry)

        setupToolBar()
        MenuHelper.onClickMenu(this, mNavigationView)
        mAddButton.setOnClickListener(addButtonListener)
    }

    val addButtonListener = View.OnClickListener {
        val categoryId = intent.getIntExtra(Const.CATEGORY_ID, -1)
        val intent = Intent(baseContext, EditActivity::class.java)
        intent.putExtra(CATEGORY_ID, categoryId)
        startActivity(intent)
    }

    private fun setupToolBar() {
        setSupportActionBar(mToolBar)
        val actionBar = supportActionBar
        MenuHelper.setupToolBar(mToolBar, actionBar, getString(R.string.menu_all))
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
