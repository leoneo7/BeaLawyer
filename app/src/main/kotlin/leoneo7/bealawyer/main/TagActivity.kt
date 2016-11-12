package leoneo7.bealawyer.main

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.bindView
import leoneo7.bealawyer.R
import leoneo7.bealawyer.edit.EditActivity
import leoneo7.bealawyer.helper.MenuHelper

/**
 * Created by ryouken on 2016/11/01.
 */
class TagActivity : AppCompatActivity() {

    val mNavigationView: NavigationView by bindView(R.id.navigation_view)
    val mDrawerLayout: DrawerLayout by bindView(R.id.drawer_layout)
    val mToolBar: Toolbar by bindView(R.id.tool_bar)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.entry)
        ButterKnife.bind(this)

        setupToolBar()
        MenuHelper.onClickMenu(this, mNavigationView)
    }

    @OnClick(R.id.addButton)
    fun ClickAddButton() {
        val intent = Intent(this, EditActivity::class.java)
        startActivity(intent)
    }

    private fun setupToolBar() {
        setSupportActionBar(mToolBar)
        val actionBar = supportActionBar
        MenuHelper.setupToolBar(mToolBar, actionBar, getString(R.string.menu_tag))
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
