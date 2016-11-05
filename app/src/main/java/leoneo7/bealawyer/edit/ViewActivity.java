package leoneo7.bealawyer.edit;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import leoneo7.bealawyer.R;
import leoneo7.bealawyer.helper.DBAdapter;
import leoneo7.bealawyer.helper.MenuHelper;

/**
 * Created by ryouken on 2016/11/05.
 */

public class ViewActivity extends AppCompatActivity {

    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.tool_bar)
    Toolbar mToolBar;

    @BindView(R.id.title)
    TextView titleText;

    @BindView(R.id.imageView)
    ImageView imageView;

    @BindView(R.id.numbering)
    TextView numberingText;

    private int id;
    private String title;
    private String image;
    private String numbering;
    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String IMAGE = "image";
    private static final String NUMBERING = "numbering";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view);
        ButterKnife.bind(this);

        setupToolBar();
        MenuHelper.onClickMenu(this, mNavigationView);

        setupData();
    }

    private void setupData() {
        Intent intent = getIntent();

        id = intent.getIntExtra(ID, -1);
        title = intent.getStringExtra(TITLE);
        image = intent.getStringExtra(IMAGE);
        numbering = intent.getStringExtra(NUMBERING);
        Uri uri;
        if (image != null) uri = Uri.parse(image);
        else uri = null;

        titleText.setText(title);
        imageView.setImageURI(uri);
        numberingText.setText(numbering);
    }

    @OnClick(R.id.editButton)
    public void clickEditButton() {
        Intent intent = new Intent(this, EditActivity.class);
        if (id == -1) getNewEntryId();
        intent.putExtra(ID, id);
        intent.putExtra(TITLE, title);
        intent.putExtra(IMAGE, image);
        intent.putExtra(NUMBERING, numbering);
        startActivity(intent);
    }

    private void setupToolBar() {
        setSupportActionBar(mToolBar);
        final ActionBar actionBar = getSupportActionBar();
        MenuHelper.setupToolBar(mToolBar, actionBar);
    }

    private void getNewEntryId() {
        DBAdapter dbAdapter = new DBAdapter(this);
        dbAdapter.open();
        Cursor cursor = dbAdapter.getNewEntryId();
        startManagingCursor(cursor);
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(DBAdapter.ENTRY_ID));
            } while (cursor.moveToNext());
        }
        stopManagingCursor(cursor);
        dbAdapter.close();
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
