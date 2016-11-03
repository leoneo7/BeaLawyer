package leoneo7.bealawyer.edit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import leoneo7.bealawyer.R;
import leoneo7.bealawyer.helper.DBAdapter;

/**
 * Created by ryouken on 2016/11/01.
 */
public class EditActivity extends AppCompatActivity {

    @BindView(R.id.title)
    EditText titleText;

    @BindView(R.id.numbering)
    EditText numberingText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.cameraButton)
    public void clickCameraButton() {
        Log.d("camera", "---------------");
    }

    @OnClick(R.id.galleryButton)
    public void clickGalleryButton() {
        Log.d("gallery", "---------------");
    }

//    @OnClick(R.id.saveButton)
    public void clickSaveButton() {
        String title = titleText.getText().toString();
        String numbering = numberingText.getText().toString();
        Calendar calendar = Calendar.getInstance();
        long date = calendar.getTimeInMillis();

        saveEntry(title, numbering, date);
    }

    private void saveEntry(String title, String numbering, long date) {
        DBAdapter dbAdapter = new DBAdapter(this);
        dbAdapter.open();
        dbAdapter.saveEntry(title, null, numbering, 0, date);
        dbAdapter.close();
    }

}
