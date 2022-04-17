package com.example.touristphotoassistant.ui.about;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.touristphotoassistant.tensorflow.CameraActivity;
import com.example.touristphotoassistant.databinding.FragmentAboutBinding;
import com.example.touristphotoassistant.ui.helper.ApplicationSettings;

public class AboutFragment extends Fragment {

    private FragmentAboutBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AboutViewModel userViewModel =
                new ViewModelProvider(this).get(AboutViewModel.class);

        binding = FragmentAboutBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textAbout;
        userViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        Button activityCameraButton =  binding.buttonCameraActivity;
        activityCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplicationSettings.runTOPActivity(CameraActivity.class, getActivity().getWindow(), getActivity());
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}