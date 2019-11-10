package com.example.maxim.myproject.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;

import com.example.maxim.myproject.R;
import com.example.maxim.myproject.model.AppSection;
import com.example.maxim.myproject.ui.fragment.AppsListFragment;
import com.example.maxim.myproject.db.util.CorrectDbHelper;

import java.util.ArrayList;
import java.util.List;

public class LeastMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main_activity);

        init();
    }

    private void init() {
        // загружаем "таблицу" заявок
        CorrectDbHelper.getInstance().loadApps();

        // настраиваем ViewPager
        ViewPager viewPager = findViewById(R.id.main_view_pager);
        FragmentStatePagerAdapter fragmentsAdapter = getFragmentsAdapter();
        viewPager.setAdapter(fragmentsAdapter);

        // настраиваем табы для ViewPager, переключение которых будет менять фрагменты
        TabLayout tabLayout = findViewById(R.id.main_tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }

    /**
     * Создаем адаптер фрагментов для ViewPager
     *
     * @return FragmentStatePagerAdapter
     */
    private FragmentStatePagerAdapter getFragmentsAdapter() {
        // создаем фрагменты, в которых будут списки по секциям
        // список состоит из пар: название вкладки и фрагмент
        final List<Pair<String, AppsListFragment>> fragments = new ArrayList<>();
        fragments.add(Pair.create(AppSection.ALL.getName(), AppsListFragment.newInstance(AppSection.ALL)));
        fragments.add(Pair.create(AppSection.APPS.getName(), AppsListFragment.newInstance(AppSection.APPS)));
        fragments.add(Pair.create(AppSection.BUSINESS.getName(), AppsListFragment.newInstance(AppSection.BUSINESS)));
        fragments.add(Pair.create(AppSection.INTERNET.getName(), AppsListFragment.newInstance(AppSection.INTERNET)));
        fragments.add(Pair.create(AppSection.SITES.getName(), AppsListFragment.newInstance(AppSection.SITES)));
        fragments.add(Pair.create(AppSection.GAMES.getName(), AppsListFragment.newInstance(AppSection.GAMES)));
        fragments.add(Pair.create(AppSection.OTHER.getName(), AppsListFragment.newInstance(AppSection.OTHER)));

        // создаем адаптер на основе списка
        return new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position).second;
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return fragments.get(position).first;
            }
        };
    }

    @Override
    protected void onStop() {
        // очищаем подписку на обновления "таблицы" заявок
        CorrectDbHelper.getInstance().clearAppsListener();
        super.onStop();
    }
}
