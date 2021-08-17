package tk.cavink.shandamorning.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import tk.cavink.shandamorning.R;
import tk.cavink.shandamorning.ui.activites.MainActivity;
import tk.cavink.shandamorning.utils.ConstantManager;

/**
 * Created by cav on 16.08.21.
 */

public class ThemeDialogs extends DialogFragment implements View.OnClickListener {
    private ThemeDialogListener mThemeDialogListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mThemeDialogListener = (MainActivity) getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME,android.R.style.Theme_Holo_Light_Panel);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        WindowManager.LayoutParams p = getDialog().getWindow().getAttributes();
        p.width = ViewGroup.LayoutParams.MATCH_PARENT;
        p.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE;
        p.y = 150;
        getDialog().getWindow().setAttributes(p);
        View v = inflater.inflate(R.layout.theme_dialog,container,false);
        v.setElevation(12);

        v.findViewById(R.id.theme_green).setOnClickListener(this);
        v.findViewById(R.id.theme_velvet).setOnClickListener(this);
        v.findViewById(R.id.theme_blue).setOnClickListener(this);
        v.findViewById(R.id.theme_dark).setOnClickListener(this);
        v.findViewById(R.id.theme_gray).setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View view) {
        if (mThemeDialogListener != null){
            switch (view.getId()){
                case R.id.theme_green:
                    mThemeDialogListener.onSelect(ConstantManager.THEME_GREEN);
                    break;
                case R.id.theme_velvet:
                    mThemeDialogListener.onSelect(ConstantManager.THEME_VELVET);
                    break;
                case R.id.theme_blue:
                    mThemeDialogListener.onSelect(ConstantManager.THEME_BLUE);
                    break;
                case R.id.theme_dark:
                    mThemeDialogListener.onSelect(ConstantManager.THEME_DARK);
                    break;
                case R.id.theme_gray:
                    mThemeDialogListener.onSelect(ConstantManager.THEME_GRAY);
                    break;
            }
        }
        dismiss();
    }


    public interface ThemeDialogListener {
        void onSelect(int theme);
    }

}
