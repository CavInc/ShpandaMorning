package tk.cavink.shandamorning.ui.activites;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;


import tk.cavink.shandamorning.R;
import tk.cavink.shandamorning.data.managers.DataManager;
import tk.cavink.shandamorning.ui.dialogs.ThemeDialogs;
import tk.cavink.shandamorning.ui.fragments.AlarmListFragment;


public class MainActivity extends AppCompatActivity implements View.OnClickListener,ThemeDialogs.ThemeDialogListener {
    private static final int REQUEST_READ_PERMISSION = 654;
    private static final int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 434;

    private DataManager mDataManager;
    private ImageButton mThemeButton;
    private boolean useTheme = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mDataManager = DataManager.getInstance();

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        setTheme(mDataManager.getPrefManager().getThemeId());
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);


        mThemeButton =  findViewById(R.id.theme_change_bt);
        mThemeButton.setOnClickListener(this);

        if (mDataManager.getPrefManager().isFirstStart()) {
            Intent intent = new Intent(this,StartActivity.class);
            startActivity(intent);
            finish();
        }
        //mDataManager.getPrefManager().setFirstStart(true);

        viewFragment(new AlarmListFragment(),"ALARM_LIST");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_READ_PERMISSION);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + this.getPackageName()));
                startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.theme_change_bt) {
            if (useTheme) {
                mThemeButton.setImageResource(R.drawable.ic_arroy_up_white);
                ThemeDialogs dialogs = new ThemeDialogs();
                dialogs.show(getSupportFragmentManager(),"THEME_DIALOG");
                //dialogs.getDialog().getWindow().setGravity(Gravity.TOP | Gravity.LEFT | Gravity.RIGHT);
            } else {
                mThemeButton.setImageResource(R.drawable.ic_arroy_down_white);
            }
            useTheme = !useTheme;
        }
    }

    // устанавливаем фрагмент в контейнер
    public void viewFragment(Fragment fragment, String tag){
        FragmentTransaction trz = getSupportFragmentManager().beginTransaction();
        trz.replace(R.id.container,fragment,tag);
        trz.commit();
    }

    public void changeVisibleThemeButton(boolean status){
        if (status) {
            mThemeButton.setVisibility(View.VISIBLE);
        } else {
            mThemeButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSelect(int theme) {
        //TODO установка темы
        mThemeButton.setImageResource(R.drawable.ic_arroy_down_white);
        useTheme = !useTheme;
        mDataManager.getPrefManager().setThemeId(theme);
        recreate();
    }
}
