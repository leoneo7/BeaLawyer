package leoneo7.bealawyer.edit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import leoneo7.bealawyer.R;
import leoneo7.bealawyer.helper.DBAdapter;
import leoneo7.bealawyer.helper.MenuHelper;

/**
 * Created by ryouken on 2016/11/01.
 */
public class EditActivity extends AppCompatActivity {

    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.tool_bar)
    Toolbar mToolBar;

    @BindView(R.id.title)
    EditText titleText;

    @BindView(R.id.numbering)
    EditText numberingText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit);
        ButterKnife.bind(this);

        setupToolBar();
        MenuHelper.onClickMenu(this, mNavigationView);
    }

    @OnClick(R.id.cameraButton)
    public void clickCameraButton() {
        Log.d("camera", "---------------");
    }

    @OnClick(R.id.galleryButton)
    public void clickGalleryButton() {
        Log.d("gallery", "---------------");
    }

    @OnClick(R.id.saveButton)
    public void clickSaveButton() {
        String title = titleText.getText().toString();
        String numbering = numberingText.getText().toString();
        Calendar calendar = Calendar.getInstance();
        long date = calendar.getTimeInMillis();

        saveEntry(title, numbering, date);
    }

    private void saveEntry(String title, String numbering, long date) {
        Log.d("saveEntry", "---------------");
        DBAdapter dbAdapter = new DBAdapter(this);
        dbAdapter.open();
        dbAdapter.saveEntry(title, null, numbering, 0, date);
        dbAdapter.close();
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
