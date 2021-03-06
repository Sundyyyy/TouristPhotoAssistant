package com.example.touristphotoassistant.ui.helper;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.view.Window;
import android.view.WindowManager;

import com.example.touristphotoassistant.ui.photocard.PhotoX;

import java.util.ArrayList;
import java.util.List;

//используется для быстрого определения переменной Context в любом месте программы и любом Class
//добавить м AndroidManifest
//<application
//           android:name=".ApplicationSettings"
// GitHub Token
//  9447668: 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
//  Sandyyy: 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
//далее вызов там где надо - ApplicationSettings.getContext()

public class ApplicationSettings extends Application {
    private static ApplicationSettings  instance;
    private static String ApplicationName;

    private static List<PhotoX> globalPhotoList;

    public ApplicationSettings() {
        globalPhotoList = new ArrayList<>();
        instance = this;
    }

    public static List<PhotoX> getPhotoList(){
        return globalPhotoList;
    }

    public static Context getContext() {
        return instance;
    }

    public static String getAppName(){
        return "Tourist Photo Assistant"; //.values/string
    }

/*
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "channel_name";
            String description = "channel_description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //createServiceNotificationChannel();
        createNotificationChannel();
    }
*/

    public static void runTOPActivity(Class<?> activityClass, Window window, Activity activity) {

        //TODO: Launcher. Пробуем setupWindowAnimations();
        WindowAnimation.setupWindowAnimations(window);
        //Fullscreen mode
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Context context = activity;
        Intent runningActivity = new Intent(context, activityClass);
        //runningActivity.addFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME); //FLAG_ACTIVITY_TASK_ON_HOME – таск для вызываемого Activity будет располагаться сразу после Home. Если из этого нового таска выходить кнопкой Назад, то попадешь не в предыдущий таск, а в Home.
        runningActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        runningActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity (runningActivity);
        activity.finish();
    }
}
