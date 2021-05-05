package br.com.jcamelo.appfotos.view;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.button.MaterialButton;

import br.com.jcamelo.appfotos.R;
import br.com.jcamelo.appfotos.model.AbstractFragment;

public class HomeFragment extends AbstractFragment {

    private MaterialButton newPhoto;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);



        if (view != null) {
            newPhoto = view.findViewById(R.id.button_new);


            newPhoto.setOnClickListener(v -> {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                UserFragment userFragment = new UserFragment();
                fragmentTransaction.addToBackStack("home");
                fragmentTransaction.replace(R.id.main_frame_layout, userFragment);
                fragmentTransaction.commit();

            });

        }

        return view;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_home;
    }
}
