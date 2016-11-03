package leoneo7.bealawyer.helper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import leoneo7.bealawyer.R;
import leoneo7.bealawyer.edit.EditActivity;
import leoneo7.bealawyer.main.AboutAppActivity;
import leoneo7.bealawyer.main.EntryActivity;
import leoneo7.bealawyer.main.TagActivity;

/**
 * Created by ryouken on 2016/11/03.
 */

public class MenuHelper {
    public static void setupToolBar(Toolbar toolBar, ActionBar actionBar) {
        assert actionBar != null;
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);

        toolBar.setBackgroundColor(Color.rgb(255, 185, 0));
        toolBar.setTitleTextColor(Color.WHITE);
    }

    public static void onClickMenu(final Context context, NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_all:
                        Intent intent_progress = new Intent(context, EntryActivity.class);
                        context.startActivity(intent_progress);
                        break;
                    case R.id.menu_category:
                        Intent intent_category = new Intent(context, EditActivity.class);
                        context.startActivity(intent_category);
                        break;
                    case R.id.menu_tag:
                        Intent intent_tag = new Intent(context, TagActivity.class);
                        context.startActivity(intent_tag);
                        break;
                    case R.id.menu_about:
                        Intent intent_about = new Intent(context, AboutAppActivity.class);
                        context.startActivity(intent_about);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }
}
