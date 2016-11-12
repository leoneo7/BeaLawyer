package leoneo7.bealawyer.main

import android.content.DialogInterface
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.InputType
import android.view.MenuItem
import android.widget.EditText
import butterknife.ButterKnife
import butterknife.bindView
import leoneo7.bealawyer.R
import leoneo7.bealawyer.helper.DBAdapter
import leoneo7.bealawyer.helper.MenuHelper

/**
 * Created by ryouken on 2016/11/01.
 */
class CategoryActivity : AppCompatActivity() {

    val mNavigationView: NavigationView by bindView(R.id.navigation_view)
    val mDrawerLayout: DrawerLayout by bindView(R.id.drawer_layout)
    val mToolBar: Toolbar by bindView(R.id.tool_bar)
    val addButton: FloatingActionButton by bindView(R.id.addButton)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.category)
        ButterKnife.bind(this)

        setupToolBar()
        MenuHelper.onClickMenu(this, mNavigationView)
        addButton.setOnClickListener { addCategory() }
    }

    private fun addCategory() {
        val editView = EditText(this)
        editView.inputType = InputType.TYPE_CLASS_TEXT

        AlertDialog.Builder(this)
                .setTitle(getString(R.string.category_name))
                .setView(editView)
                .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, whichButton ->
                    val name = editView.text
                    if (name.isEmpty()) return@OnClickListener
                    saveCategory(name.toString())
                })
                .setNegativeButton("キャンセル") { dialog, whichButton -> }
                .show()
    }

    private fun saveCategory(name: String) {
        val dbAdapter = DBAdapter(this)
        dbAdapter.open()
        dbAdapter.saveCategory(name)
        dbAdapter.close()
    }

    private fun setupToolBar() {
        setSupportActionBar(mToolBar)
        val actionBar = supportActionBar
        MenuHelper.setupToolBar(mToolBar, actionBar, getString(R.string.menu_category))
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
