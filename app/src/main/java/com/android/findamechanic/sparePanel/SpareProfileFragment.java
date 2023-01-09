package com.android.findamechanic.sparePanel;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.findamechanic.R;


public class SpareProfileFragment extends Fragment {

    Button postAd;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_spare_profile, null);
        getActivity().setTitle("Profile");

        postAd = v.findViewById(R.id.sparePostAd);

        postAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getContext(), Spare_PostAd.class));
            }
        });

        return v;
    }
}
