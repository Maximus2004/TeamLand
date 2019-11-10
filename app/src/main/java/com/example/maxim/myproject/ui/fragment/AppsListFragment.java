package com.example.maxim.myproject.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.maxim.myproject.R;
import com.example.maxim.myproject.db.FilterAppsResultListener;
import com.example.maxim.myproject.db.util.CorrectDbHelper;
import com.example.maxim.myproject.model.AppModel;
import com.example.maxim.myproject.model.AppSection;

import java.util.ArrayList;
import java.util.List;

public class AppsListFragment extends Fragment {

    private final static String ARG_SECTION = "APP_SECTION";
    private AppsAdapter mAdapter;

    public static AppsListFragment newInstance(AppSection section) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_SECTION, section);
        AppsListFragment fragment = new AppsListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // инициализируем адаптер
        mAdapter = new AppsAdapter();

        // настраиваем список
        RecyclerView list = (RecyclerView) view;
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        list.setAdapter(mAdapter);
    }

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

    /**
     * Простой адаптер списка заявок
     */
    class AppsAdapter extends RecyclerView.Adapter<AppsAdapter.AdapterViewHolder> {

        // локальная версия списка
        private List<AppModel> mApps = new ArrayList<>();

        /**
         * Обновление списка, при получение новых данных
         *
         * @param newList - новый список
         */
        void upDateData(List<AppModel> newList) {
            // обновляем данные
            mApps.clear();
            mApps.addAll(newList);

            // оповещаем адаптер об изменениях
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_adapter, parent, false);
            return new AdapterViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final AdapterViewHolder holder, int position) {
            AppModel app = mApps.get(position);
            holder.experience.setText(app.experience);
            holder.example.setText(app.example);
            holder.user.setText(app.creator);
            holder.appId.setText(app.appId);
            holder.mainName.setText(app.name);
            holder.ambition.setText(app.purpose);
        }

        @Override
        public int getItemCount() {
            return mApps.size();
        }

        @Override
        public int getItemViewType(int position) {
            return 0;
        }

        class AdapterViewHolder extends RecyclerView.ViewHolder {
            TextView mainName;
            TextView appId;
            TextView example;
            TextView ambition;
            TextView experience;
            TextView more;

            Button user;
            ImageButton star;

            AdapterViewHolder(View view) {
                super(view);
                mainName = view.findViewById(R.id.applName);
                appId = view.findViewById(R.id.applicationID);
                ambition = view.findViewById(R.id.writeAdout);
                experience = view.findViewById(R.id.expView);

                example = view.findViewById(R.id.exampView);

                more = view.findViewById(R.id.buttonMore);
                star = view.findViewById(R.id.imageButton0);
                user = view.findViewById(R.id.userBtn);
            }
        }
    }
}
