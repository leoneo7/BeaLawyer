package leoneo7.bealawyer.edit;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

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

    @BindView(R.id.imageView)
    ImageView imageView;

    @BindView(R.id.numbering)
    EditText numberingText;

    @BindView(R.id.cameraButton)
    Button cameraButton;

    @BindView(R.id.galleryButton)
    Button galleryButton;

    @BindView(R.id.layoutBox)
    LinearLayout layoutBox;

    private Uri mUri;
    private String path;
    private static final int REQUEST_CHOOSER = 1000;
    private int id = -1;
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
        setContentView(R.layout.edit);
        ButterKnife.bind(this);

        setupToolBar();
        MenuHelper.onClickMenu(this, mNavigationView);

        setupData();
    }

    private void setupData() {
        Intent intent = getIntent();

        id = intent.getIntExtra(ID, -1);
        title = intent.getStringExtra(TITLE);
        numbering = intent.getStringExtra(NUMBERING);
        titleText.setText(title);
        numberingText.setText(numbering);

        Uri uri;
        image = intent.getStringExtra(IMAGE);
        if (image != null) uri = Uri.parse(image);
        else uri = null;
        if (uri != null) {
            imageView.setImageURI(uri);
            imageView.setVisibility(View.VISIBLE);
            layoutBox.setVisibility(View.GONE);
        }

    }

    @OnClick(R.id.cameraButton)
    public void clickCameraButton() {
        Log.d("camera", "---------------");
        useCamera();
    }

    @OnClick(R.id.galleryButton)
    public void clickGalleryButton() {
        Log.d("gallery", "---------------");
        useGallery();
    }

    @OnClick(R.id.saveButton)
    public void clickSaveButton() {
        String title = titleText.getText().toString();
        if (title.isEmpty()) {
            showAlertDialog();
            return;
        }
        String numbering = numberingText.getText().toString();
        Calendar calendar = Calendar.getInstance();
        long date = calendar.getTimeInMillis();

        if (id == -1) {
            saveEntry(title, numbering, date);
        } else {
            updateEntry(id, title, numbering, date);
        }

        moveToView();
    }

    private void moveToView(){
        Intent intent = new Intent(this, ViewActivity.class);
        intent.putExtra(ID, id);
        intent.putExtra(TITLE, title);
        intent.putExtra(IMAGE, image);
        intent.putExtra(NUMBERING, numbering);
        this.startActivity(intent);
    }

    private void showAlertDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.no_title_error)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .show();
    }

    private void saveEntry(String title, String numbering, long date) {
        DBAdapter dbAdapter = new DBAdapter(this);
        dbAdapter.open();
        dbAdapter.saveEntry(title, path, numbering, 0, date);
        dbAdapter.close();
    }

    private void updateEntry(int id, String title, String numbering, long date) {
        DBAdapter dbAdapter = new DBAdapter(this);
        dbAdapter.open();
        dbAdapter.updateEntry(id, title, path, numbering, 0, date);
        dbAdapter.close();
    }

    private void useCamera() {
        String photoName;
        String title = titleText.getText().toString();
        if (title != null) photoName = "BeaLawyer/" + title + ".jpg";
        else               photoName = "BeaLawyer/" + System.currentTimeMillis() + ".jpg";

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, photoName);
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        mUri = getContentResolver()
                .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
        startActivityForResult(intentCamera, REQUEST_CHOOSER);
    }

    private void useGallery() {
        // ギャラリー用のIntent作成
        Intent intentGallery;
        if (Build.VERSION.SDK_INT < 19) {
            intentGallery = new Intent(Intent.ACTION_GET_CONTENT);
            intentGallery.setType("image/*");
        } else {
            intentGallery = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intentGallery.addCategory(Intent.CATEGORY_OPENABLE);
            intentGallery.setType("image/*");
        }
        startActivityForResult(intentGallery, REQUEST_CHOOSER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CHOOSER) {
            if (resultCode != RESULT_OK) {
                return;
            }

            Uri resultUri = (data.getData() != null ? data.getData() : mUri);
            if (resultUri == null) return;
            else path = resultUri.toString();

            MediaScannerConnection.scanFile(this, new String[]{resultUri.getPath()},
                    new String[]{"image/jpeg"}, null);

            imageView.setImageURI(resultUri);

            imageView.setVisibility(View.VISIBLE);
            layoutBox.setVisibility(View.GONE);
        }
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
