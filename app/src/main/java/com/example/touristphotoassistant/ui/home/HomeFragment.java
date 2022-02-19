package com.example.touristphotoassistant.ui.home;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.touristphotoassistant.R;
import com.example.touristphotoassistant.databinding.FragmentHomeBinding;
import com.example.touristphotoassistant.ui.helper.ApplicationSettings;
import com.example.touristphotoassistant.ui.photocard.RecyclerTouchListener;
import com.example.touristphotoassistant.ui.photocard.RecyclerviewAdapter;
import com.example.touristphotoassistant.ui.photocard.Task;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private RecyclerView recyclerView;
    private RecyclerviewAdapter recyclerviewAdapter;
    private RecyclerTouchListener touchListener;
    private String textResult;

    private static final int CAMERA_REQUEST = 9999;
    private ImageView imageView;

    private List<Task> taskList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        imageView = binding.imageView;

        FloatingActionButton fab = binding.fabTakePhoto;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getActivity(),"Take photo",Toast.LENGTH_SHORT).show();
                onTakePhoto();
            }
        });

        recyclerView = binding.recyclerview;
        recyclerviewAdapter = new RecyclerviewAdapter(this.getContext()); //this

        taskList = new ArrayList<>();
        /*
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
         */

        recyclerviewAdapter.setPhotoList(taskList);
        recyclerView.setAdapter(recyclerviewAdapter);

        touchListener = new RecyclerTouchListener(getActivity(),recyclerView); //this
        touchListener
                .setClickable(new RecyclerTouchListener.OnRowClickListener() {
                    @Override
                    public void onRowClicked(int position) {
                        Toast.makeText(ApplicationSettings.getContext(),taskList.get(position).getPhotoDesc(), Toast.LENGTH_SHORT).show();
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
                                recyclerviewAdapter.setPhotoList(taskList);
                                break;
                            case R.id.edit_task:
                                Toast.makeText(getActivity(),"Edit Not Available",Toast.LENGTH_SHORT).show();
                                break;

                        }
                    }
                });
        recyclerView.addOnItemTouchListener(touchListener);

        return root;
    }

    void onTakePhoto(){
        try {
            // Намерение для запуска камеры
            Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(captureIntent, CAMERA_REQUEST);
        } catch (ActivityNotFoundException e) {
            // Выводим сообщение об ошибке
            String errorMessage = "Ваше устройство не поддерживает съемку";
            Toast toast = Toast
                    .makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
        {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            detectPhotoLabel(data);

            recyclerviewAdapter.setPhotoList(taskList);
            imageView.setImageBitmap(photo);

        }
    }

    private void setTextResult(String text){
        textResult = text;
    }

    private void detectPhotoLabel(Intent data){

        InputImage image = InputImage.fromBitmap((Bitmap) data.getExtras().get("data"), 0); //rotationDegree: Only 0, 90, 180, 270 are supported
/*
        InputImage image = null;
        try {
            image = InputImage.fromFilePath(getContext(), data.getData());
        } catch (IOException e) {
            e.printStackTrace();
        }
*/
        // To use default options:
        ImageLabeler labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS);
        // Or, to set the minimum confidence required:
        // ImageLabelerOptions options =
        //     new ImageLabelerOptions.Builder()
        //         .setConfidenceThreshold(0.7f)
        //         .build();
        // ImageLabeler labeler = ImageLabeling.getClient(options);


        labeler.process(image)
                .addOnSuccessListener(new OnSuccessListener<List<ImageLabel>>() {
                    @Override
                    public void onSuccess(List<ImageLabel> labels) {
                        // Task completed successfully
                        // ...
                        textResult = "Success. List size: " + labels.size();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Task failed with an exception
                        // ...
                        textResult = e.toString();
                    }
                });

        Task task = new Task("", textResult, (Bitmap) data.getExtras().get("data"));
        taskList.add(task);
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}