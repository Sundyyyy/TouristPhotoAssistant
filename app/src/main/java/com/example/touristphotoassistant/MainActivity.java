package com.example.touristphotoassistant;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.example.touristphotoassistant.ui.helper.ApplicationSettings;
import com.example.touristphotoassistant.ui.photocard.RecyclerTouchListener;
import com.example.touristphotoassistant.ui.photocard.RecyclerviewAdapter;
import com.example.touristphotoassistant.ui.photocard.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import com.example.touristphotoassistant.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    public final static String[] listPermissions = {
            Manifest.permission.INTERNET,
            Manifest.permission.CAMERA
    };

    private RecyclerView recyclerView;
    private RecyclerviewAdapter recyclerviewAdapter;
    private RecyclerTouchListener touchListener;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        askForPermissions();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_user)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerviewAdapter = new RecyclerviewAdapter(this);

        final List<Task> taskList = new ArrayList<>();
        Task task = new Task("Buy Dress","Buy Dress at Shoppershop for coming functions");
        taskList.add(task);
        task = new Task("Go For Walk","Wake up 6AM go for walking");
        taskList.add(task);
        task = new Task("Office Work","Complete the office works on Time");
        taskList.add(task);
        task = new Task("watch Repair","Give watch to service center");
        taskList.add(task);
        task = new Task("Recharge Mobile","Recharge for 10$ to my **** number");
        taskList.add(task);
        task = new Task("Read book","Read android book completely");
        taskList.add(task);

        recyclerviewAdapter.setTaskList(taskList);
        recyclerView.setAdapter(recyclerviewAdapter);

        touchListener = new RecyclerTouchListener(this,recyclerView);
        touchListener
                .setClickable(new RecyclerTouchListener.OnRowClickListener() {
                    @Override
                    public void onRowClicked(int position) {
                        Toast.makeText(getApplicationContext(),taskList.get(position).getName(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onIndependentViewClicked(int independentViewID, int position) {

                    }
                })
                .setSwipeOptionViews(R.id.delete_task,R.id.edit_task)
                .setSwipeable(R.id.rowFG, R.id.rowBG, new RecyclerTouchListener.OnSwipeOptionsClickListener() {
                    @Override
                    public void onSwipeOptionClicked(int viewID, int position) {
                        switch (viewID){
                            case R.id.delete_task:
                                taskList.remove(position);
                                recyclerviewAdapter.setTaskList(taskList);
                                break;
                            case R.id.edit_task:
                                Toast.makeText(getApplicationContext(),"Edit Not Available",Toast.LENGTH_SHORT).show();
                                break;

                        }
                    }
                });
        recyclerView.addOnItemTouchListener(touchListener);

    }

    public static boolean isPermissionsAllowed(){
        Context staticContext = ApplicationSettings.getContext();

        for(String permission:listPermissions ) {
            if (ContextCompat.checkSelfPermission(staticContext, permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

    private boolean addPermission(List<String> permissionsList, String permission) {

        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option

            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permission))
                return false;
        }
        return true;
    }

    private boolean askForPermissions() {
        //Logs.addToLog(mContext,TAG,"grandPermissions() start");

        List<String> permissionsNeeded = new ArrayList<String>();
        final List<String> permissionsList = new ArrayList<String>();

        //перебераем права READ_PHONE_STATE, INTERNET
        for (String permission : listPermissions) {
            if (!addPermission(permissionsList, permission))
                permissionsNeeded.add(permission);
        }

        if (permissionsList.size() > 0) {
            ActivityCompat.requestPermissions(this, permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);

        }
        return isPermissionsAllowed();
    }

/*
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancelListener) {
        new AlertDialog.Builder(this)
                .setTitle("Permission Denied")
                .setMessage("Permission is denied, Please allow permissions from App Settings.")
                .setPositiveButton("App Settings", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        intent = Intent()
                        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        val uri = Uri.fromParts("package", getPackageName(), null)
                        intent.data = uri
                        startActivity(intent)
                    }
                }
                )
                .setNegativeButton("Cancel",null)
                .create()
                .show();
    }

 */
}