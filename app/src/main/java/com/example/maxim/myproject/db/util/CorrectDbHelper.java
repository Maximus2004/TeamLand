package com.example.maxim.myproject.db.util;

import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Pair;

import com.example.maxim.myproject.db.FilterAppsResultListener;
import com.example.maxim.myproject.model.AppModel;
import com.example.maxim.myproject.model.AppSection;
import com.example.maxim.myproject.ui.activity.LeastMainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.maxim.myproject.ui.activity.LeastMainActivity.isSort;

/**
 * Обновленная версия DbHelper'а
 */
public class CorrectDbHelper {

    private static final String CAT_APPLICATIONS = "applications";
    // таблица для других классов/activity
    public static DataSnapshot dataSnapshot;
    // не канонически правильная форма паттерна Singleton, но пользоваться можно
    // больше подробностей тут: https://refactoring.guru/ru/design-patterns/singleton
    // у sInstance префикс "s" – потому что есть "static"
    // volatile - переменная, изменяясь из одного потока, изменяется и во всех остальных - единая переменная
    private static volatile CorrectDbHelper sInstance = null;
    private List<AppModel> mAppsList = null;
    // у mDatabase префикс "m" – потому что он является частью класса – "member"
    private static DatabaseReference mDatabase;

    // общий "слушатель" событий обновления списка заявок
    private ValueEventListener appsListener = null;
    // список отложенных "запросов" фильтраций заявок
    private List<Pair<AppSection, FilterAppsResultListener>> mDelayedFilterRequests = new ArrayList<>();

    /**
     * Конструктор делаем приватным, чтобы его нельзя было вызвать БЕЗ вызова getInstance.
     *
     * @see #getInstance() – получать экземпляр класса только через этот метод
     */
    private CorrectDbHelper() {
        // инициализация базы данных
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public static DatabaseReference getDatabase() {
        return mDatabase;
    }

    /**
     * Главная точка "входа" в класс. Возвращает ЕДИНСТВЕННЫЙ экземпляр класса,
     * (а если его еще нет, то сначала инициализирует его)
     * который можно использовать по всему приложению без дополнительной инциализации
     *
     * @return экземпляр класса CorrectDbHelper
     * <p>
     * Единый экземляр класса для всего приложения
     */
    public static CorrectDbHelper getInstance() {
        if (sInstance == null)
            sInstance = new CorrectDbHelper();

        return sInstance;
    }

    /**
     * Загрузка списка заявок
     */
    public void loadApps() {
        DatabaseReference appsTable = mDatabase;
        // подписываемся на обновления списка заявок
        appsTable.addValueEventListener(getAppsListener());
    }

    /**
     * Запрос на отфильтрованный по секции список
     * <p>
     * если данных еще нет (список всех заявок пуст), то добавляем в очередь
     *
     * @param section  – секции {@link AppSection}
     * @param listener – результат обновления списка
     */
    public void getAppsFilteredBySection(AppSection section, FilterAppsResultListener listener) {
        if (mAppsList == null)
            mDelayedFilterRequests.add(Pair.create(section, listener));
        else
            filterAppsBySection(section, listener);
    }

    /**
     * Фильтруем список заявок.
     * <p>
     * Не очень понятна эта процедура
     */
    private void filterAppsBySection(AppSection section, FilterAppsResultListener listener) {
        if (mAppsList == null) throw new IllegalStateException("Сначала загрузи список заявок!");

        List<AppModel> filteredApps = new ArrayList<>();
        List<AppModel> tempAllAps = new ArrayList<>(mAppsList);

        for (AppModel app : tempAllAps) {
            if (section == AppSection.ALL) {
                // хотите все, держите все
                filteredApps.add(app);
            }
            if (
                // если фильтр "ДРУГИЕ"
                    (section == AppSection.OTHER && checkIfSectionInOther(app.section))
                            // не очень понятен смысл последнего условия
                            || app.section.equals(section.getName())
                    ) {
                filteredApps.add(app);
            }
        }

        listener.onFilterAppsResult(filteredApps);
    }

    /**
     * Проверяем, что заявка не входит в одну из популярных секций
     *
     * @param appSection - секция конкретной заявки
     * @return true – если заявка не входит в список популярных, false – если входит
     */
    private boolean checkIfSectionInOther(String appSection) {
        return !(
                appSection.equals(AppSection.APPS.getName())
                        || appSection.equals(AppSection.BUSINESS.getName())
                        || appSection.equals(AppSection.GAMES.getName())
                        || appSection.equals(AppSection.INTERNET.getName())
                        || appSection.equals(AppSection.SITES.getName())
        );
    }

    /**
     * Создаем новый или используем существующий слушатель
     *
     * @return ValueEventListener
     * <p>
     * Также реализован Singleton (одиночка)
     */
    private ValueEventListener getAppsListener() {
        if (appsListener == null)
            appsListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot table) {
                    // при изменении данных происходит следующее
                    DataSnapshot appTable = table.child(CAT_APPLICATIONS);
                    Log.d("AppsList-onDataChange", "Изменился набор данных в списке заявок");
                    // создаем "временный" список
                    List<AppModel> newList = new ArrayList<>();
                    // todo переделать на List<AppModel> data = ArrayList<>(((HashMap<String, AppModel>) appTable.getValue()).values);
                    // тогда сможем убрать проверки
                    for (DataSnapshot appSnapShot : appTable.getChildren()) {
                        // пропускаем "пустые", если есть и
                        // maxId (проверка на тип Long), пока база не почищена
                        if (!appSnapShot.exists() || appSnapShot.getValue().getClass() == Long.class)
                            continue;

                        // заполняем новыми данными
                        AppModel app = appSnapShot.getValue(AppModel.class);
                        newList.add(app);
                    }

                    // создаем список, если еще не создан или обновляем его
                    if (mAppsList == null)
                        mAppsList = new ArrayList<>();
                    else
                        mAppsList.clear();
                    mAppsList.addAll(newList);

                    // проверяем отложенные запросы и выполняем их
                    if (!mDelayedFilterRequests.isEmpty()) {
                        for (Pair<AppSection, FilterAppsResultListener> pair : mDelayedFilterRequests) {
                            filterAppsBySection(pair.first, pair.second);
                        }
                        // очищаем очередь
                        mDelayedFilterRequests.clear();
                    }

                    // заносим всю таблицу в поля этого класса, чтобы потом ей пользоваться
                    dataSnapshot = table;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("AppsList-onCanceled", "Изменился набор данных в списке заявок", databaseError.toException());
                }
            };

        return appsListener;
    }

    /**
     * Необходимо зачищать фоновое обновление списка заявок при уходе с экрана
     */
    public void clearAppsListener() {
        DatabaseReference appsTable = mDatabase.child("applications");
        appsTable.removeEventListener(getAppsListener());
    }

    public DatabaseReference getDescription(String userId) {
        return mDatabase.child("users").child(userId).child("description");
    }
}