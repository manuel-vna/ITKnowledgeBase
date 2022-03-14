package com.example.itkbproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.itkbproject.databinding.UpdateDeleteEntryBinding;


public class UpdateDeleteEntryFragment extends Fragment  {

    private UpdateDeleteEntryBinding binding;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = UpdateDeleteEntryBinding.inflate(inflater,container,false);
        return binding.getRoot();

    }


}
