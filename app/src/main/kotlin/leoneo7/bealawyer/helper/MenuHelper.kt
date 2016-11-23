package leoneo7.bealawyer.helper

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.graphics.Color
import android.support.design.widget.NavigationView
import android.support.v7.app.ActionBar
import android.support.v7.widget.Toolbar
import leoneo7.bealawyer.R
import leoneo7.bealawyer.main.AboutAppActivity
import leoneo7.bealawyer.main.CategoryActivity
import leoneo7.bealawyer.main.EntryActivity

/**
 * Created by ryouken on 2016/11/03.
 */

object MenuHelper {
    fun setupToolBar(toolBar: Toolbar, actionBar: ActionBar?, title: String) {
        assert(actionBar != null)
        actionBar!!.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.title = title

        toolBar.setBackgroundColor(Color.rgb(255, 185, 0))
        toolBar.setTitleTextColor(Color.WHITE)
    }

    fun onClickMenu(context: Context, navigationView: NavigationView) {
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_all -> createIntent(context, EntryActivity::class.java)
                R.id.menu_category -> createIntent(context, CategoryActivity::class.java)
//                R.id.menu_tag -> createIntent(context, TagActivity::class.java)
                R.id.menu_about -> createIntent(context, AboutAppActivity::class.java)
                else -> {
                }
            }
            false
        }
    }

    private fun createIntent(context: Context, clazz: Class<*>) {
        val intent = Intent(context, clazz)
        intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP)
        context.startActivity(intent)
        (context as Activity).finish()
    }
}
