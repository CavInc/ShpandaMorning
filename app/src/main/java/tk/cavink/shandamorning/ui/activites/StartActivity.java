package tk.cavink.shandamorning.ui.activites;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.sql.DatabaseMetaData;

import tk.cavink.shandamorning.R;
import tk.cavink.shandamorning.data.managers.DataManager;

/**
 * Created by cav on 17.08.21.
 */

public class StartActivity extends AppCompatActivity implements View.OnClickListener{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_start);
        findViewById(R.id.start_bt).setOnClickListener(this);
        String sp = "Shpanda Morning <font color='black'>!</font>";
        ((TextView)findViewById(R.id.textView3)).setText(Html.fromHtml(sp), TextView.BufferType.SPANNABLE);
    }

    @Override
    public void onClick(View view) {
        DataManager.getInstance().getPrefManager().setFirstStart(false);
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
