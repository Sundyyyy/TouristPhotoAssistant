package com.example.touristphotoassistant.ui.launcher;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.touristphotoassistant.MainActivity;
import com.example.touristphotoassistant.R;
import com.example.touristphotoassistant.ui.helper.ApplicationSettings;

@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {
    private static final String TAG = SplashScreenActivity.class.getSimpleName();
    private Context mContext = ApplicationSettings.getContext();

    private View mContentView;
    private static final int UI_DELAY = 3500;
    private Activity _activity;
    private final Handler mActivityHandler = new Handler();

//    private boolean isPermissionNeeded(){
//        boolean result = false;
//        for(String permission : PermissionActivity.listPermissions) {
//            if (ContextCompat.checkSelfPermission(SplashScreenActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
//                // Check for Rationale Option
//                if (!ActivityCompat.shouldShowRequestPermissionRationale(SplashScreenActivity.this, permission))
//                    result = true;
//            }
//        }
//        return result;
//    }

    private void runActivity() {
        ApplicationSettings.runTOPActivity(MainActivity.class, getWindow(), _activity);

/*        if(Account.Helper.getDefaultAccount().getIsPasswordSaved()){
            runTOPActivity(MainActivity.class, getWindow(), _activity);
        } else {
            runTOPActivity(LoginActivity.class, getWindow(), _activity);
        }
*/
    }

    private final Runnable mActivityRunnable = new Runnable() {
        @Override
        public void run() {
            runActivity();
/*
            if (BaseSupportTool.isPermissionNeeded(SplashScreenActivity.this)) {
                BaseSupportTool.grandPermissions(SplashScreenActivity.this);
            } else {
                runActivity();
            }
*/
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        _activity = this;
        TextView splashTextViewFamily = findViewById(R.id.splash_app_family_name);
        splashTextViewFamily.setText(getString(R.string.app_family_name));
        TextView splashTextViewSubFamily = findViewById(R.id.splash_app_subfamily_name);
        splashTextViewSubFamily.setText(getString(R.string.app_subfamily_name));

        TextView splashVersionView = findViewById(R.id.splash_version);
        //splashVersionView.setText(BuildConfig.VERSION_NAME);

        /*
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
*/
        mContentView = findViewById(R.id.fullscreen_content);
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        activityActions();
    }



    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    private void activityActions() {
        mActivityHandler.removeCallbacks(mActivityRunnable);
        mActivityHandler.postDelayed(mActivityRunnable, UI_DELAY);
    }

/*
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case BaseSupportTool.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                if (BaseSupportTool.onRequestPermissionsResult(permissions, grantResults)) {
                    //Access granted
                    if(!LocalDB.isExistDB()) {
                        LocalDB.initLocalDB(ApplicationSettings.getContext(), LocalDB.DB_NAME);
                    }
                    runActivity();
                } else {
                    //Access denied
                    finish();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

 */
}

