package com.example.maxim.myproject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import java.util.Locale;

public class CatFragment extends Fragment {
    public static final String ARG_CAT_NUMBER = "cat_number";
    Main2Activity main = new Main2Activity();
    public CatFragment() {
        // Для фрагмента требуется пустой конструктор
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment, container, false);
        int i = getArguments().getInt(ARG_CAT_NUMBER);
        // имена котов на англ. для нахождения имен файлов
        String catName = main.mCatTitles[i];

        String catNameTitle = main.mCatTitles[i];

        int imageId = getResources().getIdentifier(catName.toLowerCase(Locale.ROOT),
                "drawable", getActivity().getPackageName());
        ((ImageView) rootView.findViewById(R.id.imageViewCat)).setImageResource(imageId);
        getActivity().setTitle(catNameTitle);
        return rootView;
    }
}
