package leoneo7.bealawyer.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import leoneo7.bealawyer.R;
import leoneo7.bealawyer.edit.EditActivity;

/**
 * Created by ryouken on 2016/11/01.
 */
public class ListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.addButton)
    public void ClickAddButton() {
        Intent intent = new Intent(this, EditActivity.class);
        startActivity(intent);
    }
}
