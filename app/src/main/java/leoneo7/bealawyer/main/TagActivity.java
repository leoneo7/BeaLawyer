package leoneo7.bealawyer.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import leoneo7.bealawyer.R;
import leoneo7.bealawyer.edit.EditActivity;
import leoneo7.bealawyer.helper.MenuHelper;

/**
 * Created by ryouken on 2016/11/01.
 */
public class TagActivity extends AppCompatActivity {

    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.tool_bar)
    Toolbar mToolBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        ButterKnife.bind(this);

        setupToolBar();
        MenuHelper.onClickMenu(this, mNavigationView);
    }

    @OnClick(R.id.addButton)
    public void ClickAddButton() {
        Intent intent = new Intent(this, EditActivity.class);
        startActivity(intent);
    }

    private void setupToolBar() {
        setSupportActionBar(mToolBar);
        final ActionBar actionBar = getSupportActionBar();
        MenuHelper.setupToolBar(mToolBar, actionBar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
