package tk.cavink.shandamorning.ui.activites;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.NumberPicker;

import java.util.Calendar;
import java.util.Date;

import tk.cavink.shandamorning.R;
import tk.cavink.shandamorning.ui.fragments.AlarmListFragment;
import tk.cavink.shandamorning.ui.fragments.SetAlarmFragment;
import tk.cavink.shandamorning.utils.Func;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_READ_PERMISSION = 654;
    private NumberPicker np1;
    private NumberPicker np2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        viewFragment(new AlarmListFragment(),"ALARM_LIST");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_READ_PERMISSION);
        }
    }

    @Override
    public void onClick(View view) {
        /*
        int h = np1.getValue();
        int m = np2.getValue();

        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,h);
        c.set(Calendar.MINUTE,m);
        Log.d("MA",Func.dateToStr("yyyy-MM-dd HH:mm",c.getTime()));
        */
        //Func.setAlarm(c.getTime());
        //Func.setAlarmAM(this,c.getTime(),Func.ALARM_START);
    }

    // устанавливаем фрагмент в контейнер
    public void viewFragment(Fragment fragment, String tag){
        FragmentTransaction trz = getSupportFragmentManager().beginTransaction();
        trz.replace(R.id.container,fragment,tag);
        trz.commit();
    }

}
