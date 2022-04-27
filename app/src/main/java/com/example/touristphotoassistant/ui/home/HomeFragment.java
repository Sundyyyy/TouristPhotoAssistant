package com.example.touristphotoassistant.ui.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
import com.example.touristphotoassistant.tensorflow.CameraActivity;
import com.example.touristphotoassistant.ui.helper.ApplicationSettings;
import com.example.touristphotoassistant.ui.photocard.RecyclerTouchListener;
import com.example.touristphotoassistant.ui.photocard.RecyclerviewAdapter;
import com.example.touristphotoassistant.ui.photocard.PhotoX;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private RecyclerView recyclerView;
    private RecyclerviewAdapter recyclerviewAdapter;
    private RecyclerTouchListener touchListener;
    private String textResult;

    private static final int CAMERA_REQUEST = 9999;
    private ImageView imageView;

    private List<PhotoX> photoList;
    private PhotoX newPhoto;

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
                newPhoto = null;
                onTakePhoto();
            }
        });
        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ApplicationSettings.runTOPActivity(CameraActivity.class, getActivity().getWindow(), getActivity());
                return true;
            }
        });


        recyclerView = binding.recyclerview;
        recyclerviewAdapter = new RecyclerviewAdapter(this.getContext()); //this
        photoList = ApplicationSettings.getPhotoList();
        recyclerviewAdapter.setPhotoList(photoList);
        recyclerView.setAdapter(recyclerviewAdapter);

        touchListener = new RecyclerTouchListener(getActivity(),recyclerView); //this
        touchListener
                .setClickable(new RecyclerTouchListener.OnRowClickListener() {
                    @Override
                    public void onRowClicked(int position) {
                        //Toast.makeText(ApplicationSettings.getContext(), photoList.get(position).getPhotoDesc(), Toast.LENGTH_SHORT).show();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setTitle("Additionally")
                                        .setMessage(photoList.get(position).getPhotoDesc())
                                        .setIcon(new BitmapDrawable(getResources(), photoList.get(position).getPhoto()))
                                        .setPositiveButton("Thanks!", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                // Закрываем окно
                                                dialog.cancel();
                                            }
                                        });
                                builder.show();
                            }
                        });
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
                                photoList.remove(position);
                                recyclerviewAdapter.setPhotoList(photoList);
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

            detectPhotoLabel(data);

        }
    }

    private void setTextResult(String text){
        if(newPhoto != null) {
            newPhoto.setDesc(text);
            photoList.add(newPhoto);
            recyclerviewAdapter.setPhotoList(photoList);
        }
    }

    private void detectPhotoLabel(Intent data){

        InputImage image = InputImage.fromBitmap((Bitmap) data.getExtras().get("data"), 0); //rotationDegree: Only 0, 90, 180, 270 are supported

        newPhoto = new PhotoX("",  (Bitmap) data.getExtras().get("data"));

        // To use default options:
        //ImageLabeler labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS);

        // Or, to set the minimum confidence required:
        ImageLabelerOptions options =
            new ImageLabelerOptions.Builder()
                .setConfidenceThreshold(0.7f) // >=70% вероятность совпадения
                .build();
        ImageLabeler labeler = ImageLabeling.getClient(options);


        labeler.process(image)
                .addOnSuccessListener(new OnSuccessListener<List<ImageLabel>>() {
                    @SuppressLint("DefaultLocale") //для String.format
                    @Override
                    public void onSuccess(List<ImageLabel> labels) {
                        // Task completed successfully
                        // ...
                        String _nl = "\r\n"; // переход на новую строку. Используем в result
                        String result ="";

                        if(labels.size() > 0){
                            int i = 1;
                            for (ImageLabel label : labels) {
                                String text = label.getText();
                                float confidence = label.getConfidence();
                                int index = label.getIndex();
                                result += String.format(Locale.getDefault(),"%.2f %s", confidence, text);
                                //result += String.format("%d. Name: %s; Confidence: %.2f; Index: %d", i++, text, confidence, index);
                                result += _nl;
                            }
                            result += _nl;
                            result += "powered by google.mlkit.vision";
                        } else {
                            result = "Information not found";
                        }
                        setTextResult(result);
                      }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Task failed with an exception
                        // ...
                        setTextResult(e.toString());
                    }
                });
        Toast.makeText(getActivity(),"Please wait a while",Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}