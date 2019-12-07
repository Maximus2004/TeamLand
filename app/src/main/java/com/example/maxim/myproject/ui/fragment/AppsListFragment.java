package com.example.maxim.myproject.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.example.maxim.myproject.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.maxim.myproject.db.FilterAppsResultListener;
import com.example.maxim.myproject.db.util.CorrectDbHelper;
import com.example.maxim.myproject.model.AppModel;
import com.example.maxim.myproject.model.AppSection;

import java.util.List;

public class AppsListFragment extends Fragment {

    private final static String ARG_SECTION = "APP_SECTION";
    private AppsAdapter mAdapter;
    private static String userI;

    // создаём новый экземпляр фрагмента и передаём ему раздел, который ему нужно отобразить (который ему послал ViewPager)
    public static AppsListFragment newInstance(AppSection section, String userId) {
        Bundle bundle = new Bundle();
        userI = userId;
        bundle.putSerializable(ARG_SECTION, section);
        AppsListFragment fragment = new AppsListFragment();
        // создаём Bundle("category", "section"), далее добавляем его в аргументы, доступ к которым возможен loadDbData
        fragment.setArguments(bundle);
        return fragment;
    }

    // до создания фрагмента (находим view для фрагмента - что будет содержаться во фрагменте)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    // после создания
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // инициализируем адаптер для recycler
        mAdapter = new AppsAdapter(userI);
        // настраиваем список
        RecyclerView list = (RecyclerView) view;
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        list.setAdapter(mAdapter);
    }

    // каждый раз, когда фрагмент снова отображается
    @Override
    public void onResume() {
        super.onResume();
        // обновляем список при каждом отображении (почти ;)
        loadDbData();
    }

    /**
     * Запрашиваем отфильтрованный по секции список заявок
     */
    private void loadDbData() {
        // достаем секции из параметров
        final AppSection section = (AppSection) getArguments().getSerializable(ARG_SECTION);
        // запрашиваем отфильтрованный список
        // так как, загрузка общего списка заявок асинхронна, тут тоже запрашиваем асинхронно:
        // результат получаем в методе onFilterAppsResult
        CorrectDbHelper.getInstance()
                .getAppsFilteredBySection(
                        section,
                        new FilterAppsResultListener() {
                            @Override
                            public void onFilterAppsResult(List<AppModel> list) {
                                mAdapter.upDateData(list);
                            }
                        }
                );
    }

    public AppSection getSection() {
        return (AppSection) getArguments().getSerializable(ARG_SECTION);
    }
}
