package tk.cavink.shandamorning.ui.activites;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import tk.cavink.shandamorning.utils.Func;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private NumberPicker np1;
    private NumberPicker np2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        findViewById(R.id.startAlarm).setOnClickListener(this);

        np1 = findViewById(R.id.numberPicker1);
        np1.setMinValue(0);
        np1.setMaxValue(23);
        np2 = findViewById(R.id.numberPicker2);
        np2.setMinValue(0);
        np2.setMaxValue(59);

        Calendar c = Calendar.getInstance();
        np1.setValue(c.get(Calendar.HOUR_OF_DAY));
    }

    @Override
    public void onClick(View view) {
        int h = np1.getValue();
        int m = np2.getValue();

        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,h);
        c.set(Calendar.MINUTE,m);
        Log.d("MA",Func.dateToStr("yyyy-MM-dd HH:mm",c.getTime()));
        //Func.setAlarm(c.getTime());
        Func.setAlarmAM(this,c.getTime(),Func.ALARM_START);
    }

    // устанавливаем фрагмент в контейнер
    private void viewFragment(Fragment fragment, String tag){
        FragmentTransaction trz = getSupportFragmentManager().beginTransaction();
        trz.replace(R.id.container,fragment,tag);
        trz.commit();
    }

}
