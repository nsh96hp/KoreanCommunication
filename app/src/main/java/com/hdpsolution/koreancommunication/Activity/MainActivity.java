package com.hdpsolution.koreancommunication.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.arlib.floatingsearchview.util.Util;
import com.hdpsolution.koreancommunication.Activity.lockscreen.LockScreenSetting;
import com.hdpsolution.koreancommunication.Adapter.SearchResultsListAdapter;
import com.hdpsolution.koreancommunication.R;
import com.hdpsolution.koreancommunication.Utils.KUtils;
import com.hdpsolution.koreancommunication.data.DataFav;
import com.hdpsolution.koreancommunication.data.DatabaseHelper;
import com.hdpsolution.koreancommunication.data.KoreanSuggestion;
import com.hdpsolution.koreancommunication.data.Korean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private FloatingSearchView mSearchView;
    private String mLastQuery = "";
    private boolean mIsDarkSearchTheme = false;
    private RecyclerView mSearchResultsList;
    private SearchResultsListAdapter mSearchResultsAdapter;

    private ArrayList<Korean> arrKR;
    private ArrayList<Korean> arrFAV;
    private ArrayList<Korean> resultsTemp;
    private ArrayList<Integer> fnum;
    private int temp;

    private int language = 0;
    private int keyFav = 0;
    private SharedPreferences preLanguage;
    private SharedPreferences.Editor editLanguage;

    private DatabaseHelper db;
    private DataFav dbf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        findID();
        mSearchView = findViewById(R.id.floating_search_view);
        mSearchView.attachNavigationDrawerToMenuButton(drawer);

        preLanguage = getSharedPreferences(KUtils.LANGUAGE_KEY, MODE_PRIVATE);
        language = preLanguage.getInt(KUtils.L_KEY, 0);
        keyFav = preLanguage.getInt(KUtils.F_KEY, 0);

        setupResultsList();
        handleSearch();
    }

    private void handleSearch() {
        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {
                final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage(getResources().getString(R.string.waitsearching));
                progressDialog.setCancelable(false);
                progressDialog.show();

                if (!oldQuery.equals("") && newQuery.equals("")) {
                    mSearchView.clearSuggestions();
                    resultsTemp.clear();
                    mSearchResultsAdapter.swapData(resultsTemp);
                    progressDialog.dismiss();
                } else {
                    resultsTemp = new ArrayList<>();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            switch (language) {
                                case 0:
                                    for (int i = 0; i < arrKR.size(); i++) {
                                        if (arrKR.get(i).getTextVN().toString().toUpperCase().indexOf(newQuery.toUpperCase()) >= 0) {
                                            resultsTemp.add(arrKR.get(i));
                                            if (resultsTemp.size() > KUtils.MAX_LIST_SIZE_SEARCH) {
                                                progressDialog.dismiss();
                                                return;
                                            }
                                        }
                                    }
                                    break;
                                case 1:
                                    for (int i = 0; i < arrKR.size(); i++) {
                                        if (arrKR.get(i).getTextCH().toString().toUpperCase().indexOf(newQuery.toUpperCase()) >= 0) {
                                            resultsTemp.add(arrKR.get(i));
                                            if (resultsTemp.size() > KUtils.MAX_LIST_SIZE_SEARCH) {
                                                progressDialog.dismiss();
                                                return;
                                            }
                                        }
                                    }
                                    break;
                                case 2:
                                    for (int i = 0; i < arrKR.size(); i++) {
                                        if (arrKR.get(i).getTextEN().toString().toUpperCase().indexOf(newQuery.toUpperCase()) >= 0) {
                                            resultsTemp.add(arrKR.get(i));
                                            if (resultsTemp.size() > KUtils.MAX_LIST_SIZE_SEARCH) {
                                                progressDialog.dismiss();
                                                return;
                                            }
                                        }
                                    }
                                    break;
                            }

                            if (resultsTemp.size() < 30) {
                                for (int i = 0; i < arrKR.size(); i++) {
                                    if (arrKR.get(i).getTextKR().toString().toUpperCase().indexOf(newQuery.toUpperCase()) >= 0) {
                                        temp = 0;
                                        for (int j = 0; j < resultsTemp.size(); j++) {
                                            if (resultsTemp.get(j).get_id() == arrKR.get(i).get_id()) {
                                                temp = 1;
                                            }
                                        }
                                        if (temp == 0) {
                                            resultsTemp.add(arrKR.get(i));
                                            if (resultsTemp.size() > KUtils.MAX_LIST_SIZE_SEARCH) {
                                                progressDialog.dismiss();
                                                return;
                                            }
                                        }
                                    }
                                }

                                for (int i = 0; i < arrKR.size(); i++) {
                                    if (arrKR.get(i).getPinyin().toString().toUpperCase().indexOf(newQuery.toUpperCase()) >= 0) {
                                        temp = 0;
                                        for (int j = 0; j < resultsTemp.size(); j++) {
                                            if (resultsTemp.get(j).get_id() == arrKR.get(i).get_id()) {
                                                temp = 1;
                                            }
                                        }
                                        if (temp == 0) {
                                            resultsTemp.add(arrKR.get(i));
                                            if (resultsTemp.size() > KUtils.MAX_LIST_SIZE_SEARCH) {
                                                progressDialog.dismiss();
                                                return;
                                            }
                                        }
                                    }
                                }
                            }
                            progressDialog.dismiss();
                        }
                    }).start();
                    mSearchResultsAdapter.swapData(resultsTemp);
                }
                Log.e("ON SEARCH", "SUGGESTION");
            }
        });


        mSearchView.setOnBindSuggestionCallback(new SearchSuggestionsAdapter.OnBindSuggestionCallback() {
            @Override
            public void onBindSuggestion(View suggestionView, ImageView leftIcon, TextView textView, SearchSuggestion item, int itemPosition) {
                Log.e("ON BLIND", "onBlindSuggestionClicked()");

            }
        });
        mSearchView.setOnHomeActionClickListener(new FloatingSearchView.OnHomeActionClickListener() {
            @Override
            public void onHomeClicked() {

                Log.e("HOME SEARCH", "onHomeClicked()");
            }
        });


        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {
                Log.e("CLICK SEARCH", "onSuggestionClicked()");
            }

            @Override
            public void onSearchAction(String query) {
                mLastQuery = query;
                Log.e("ACTION SEARCH", "onSearchAction()" + query);
            }
        });

        mSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
                Log.e("setOnFocus", "onFocus()");
            }

            @Override
            public void onFocusCleared() {
                mSearchView.setSearchBarTitle(mLastQuery);
                Log.e("onFocusCleared", "onFocusCleared()");
            }
        });

        mSearchView.setOnSuggestionsListHeightChanged(new FloatingSearchView.OnSuggestionsListHeightChanged() {
            @Override
            public void onSuggestionsListHeightChanged(float newHeight) {
                mSearchResultsList.setTranslationY(newHeight);
            }
        });



    }

    private ImageButton main_fav, main_General_Conversation, main_Greetings, main_Numbers, main_datetime, main_Directions_Places, main_Transportation, main_Accommodation, main_Eating_Out, main_Shopping, main_Colours, main_Cities_Provinces,
            main_Countries, main_Tourist_Attractions, main_Family, main_Dating, main_Emergency, main_Feeling_Sick;

    public void findID() {
        mSearchResultsList = findViewById(R.id.search_results_list);

        main_fav = findViewById(R.id.main_fav);
        main_General_Conversation = findViewById(R.id.main_General_Conversation);
        main_Greetings = findViewById(R.id.main_Greetings);
        main_Numbers = findViewById(R.id.main_Numbers);
        main_datetime = findViewById(R.id.main_datetime);
        main_Directions_Places = findViewById(R.id.main_Directions_Places);
        main_Transportation = findViewById(R.id.main_Transportation);
        main_Accommodation = findViewById(R.id.main_Accommodation);
        main_Eating_Out = findViewById(R.id.main_Eating_Out);
        main_Shopping = findViewById(R.id.main_Shopping);
        main_Colours = findViewById(R.id.main_Colours);
        main_Cities_Provinces = findViewById(R.id.main_Cities_Provinces);
        main_Countries = findViewById(R.id.main_Countries);
        main_Tourist_Attractions = findViewById(R.id.main_Tourist_Attractions);
        main_Family = findViewById(R.id.main_Family);
        main_Dating = findViewById(R.id.main_Dating);
        main_Emergency = findViewById(R.id.main_Emergency);
        main_Feeling_Sick = findViewById(R.id.main_Feeling_Sick);

        main_fav.setOnClickListener(this);
        main_General_Conversation.setOnClickListener(this);
        main_Greetings.setOnClickListener(this);
        main_Numbers.setOnClickListener(this);
        main_datetime.setOnClickListener(this);
        main_Directions_Places.setOnClickListener(this);
        main_Transportation.setOnClickListener(this);
        main_Accommodation.setOnClickListener(this);
        main_Eating_Out.setOnClickListener(this);
        main_Shopping.setOnClickListener(this);
        main_Colours.setOnClickListener(this);
        main_Cities_Provinces.setOnClickListener(this);
        main_Countries.setOnClickListener(this);
        main_Tourist_Attractions.setOnClickListener(this);
        main_Family.setOnClickListener(this);
        main_Dating.setOnClickListener(this);
        main_Emergency.setOnClickListener(this);
        main_Feeling_Sick.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_fav:
                handleCategory(0);
                break;
            case R.id.main_General_Conversation:
                handleCategory(1);
                break;
            case R.id.main_Greetings:
                handleCategory(2);
                break;
            case R.id.main_Numbers:
                handleCategory(3);
                break;
            case R.id.main_datetime:
                handleCategory(4);
                break;
            case R.id.main_Directions_Places:
                handleCategory(5);
                break;
            case R.id.main_Transportation:
                handleCategory(6);
                break;
            case R.id.main_Accommodation:
                handleCategory(7);
                break;
            case R.id.main_Eating_Out:
                handleCategory(8);
                break;
            case R.id.main_Shopping:
                handleCategory(9);
                break;
            case R.id.main_Colours:
                handleCategory(10);
                break;
            case R.id.main_Cities_Provinces:
                handleCategory(11);
                break;
            case R.id.main_Countries:
                handleCategory(12);
                break;
            case R.id.main_Tourist_Attractions:
                handleCategory(13);
                break;
            case R.id.main_Family:
                handleCategory(14);
                break;
            case R.id.main_Dating:
                handleCategory(15);
                break;
            case R.id.main_Emergency:
                handleCategory(16);
                break;
            case R.id.main_Feeling_Sick:
                handleCategory(17);
                break;

        }
    }

    private void handleCategory(int category_id) {
        Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
        intent.putExtra(KUtils.CATEGORY_KEY, category_id);
        startActivity(intent);
    }

    private void setupResultsList() {
        db = new DatabaseHelper(MainActivity.this, getFilesDir().getAbsolutePath());
        try {
            db.prepareDataBase();
        } catch (IOException io) {
            throw new Error("Unable to create Database");
        }
        arrKR = new ArrayList<>();
        arrKR = db.getAllKorean();

        dbf=new DataFav(MainActivity.this);
        arrFAV=new ArrayList<>();
        if(keyFav==0){
            for (int i=0;i<arrKR.size();i++){
                dbf.Add_Data(arrKR.get(i));
                arrFAV.add(arrKR.get(i));

                preLanguage = getSharedPreferences(KUtils.LANGUAGE_KEY, MODE_PRIVATE);
                editLanguage = preLanguage.edit();
                editLanguage.putInt(KUtils.F_KEY,1);
                editLanguage.commit();
            }
        }else {
            arrFAV=dbf.GetAllFAV();
        }

        Log.e("FAVVVVVV",arrKR.get(0).toString()+"");

        mSearchResultsAdapter = new SearchResultsListAdapter(language,MainActivity.this,arrFAV);
        mSearchResultsList.setAdapter(mSearchResultsAdapter);
        mSearchResultsList.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        mSearchResultsAdapter.setItemsOnClickListener(new SearchResultsListAdapter.OnItemClickListener() {
            @Override
            public void onClick(Korean korean, int position) {
                mSearchResultsAdapter.Touch(position);
            }
        });
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (resultsTemp != null) {
                if (resultsTemp.size() > 0) {
                    mSearchView.setSearchText("");
                    resultsTemp.clear();
                    mSearchResultsAdapter.swapData(resultsTemp);
                }else {
                    super.onBackPressed();
                }
            } else {
                super.onBackPressed();
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_language:
                handleLanguage();
                break;
            case R.id.nav_translate:
                break;
            case R.id.nav_screen_lock:
                Intent intent=new Intent(MainActivity.this, LockScreenSetting.class);
                startActivity(intent);
                break;
            case R.id.nav_share:
                break;
            case R.id.nav_rate:
                break;
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void handleLanguage() {
        final Dialog dialog = new Dialog(MainActivity.this, R.style.mydialogstyle);
        dialog.setContentView(R.layout.dialog_language);

        preLanguage = getSharedPreferences(KUtils.LANGUAGE_KEY, MODE_PRIVATE);
        editLanguage = preLanguage.edit();


        ImageButton language_vn, language_ch, language_en;
        language_vn = dialog.findViewById(R.id.language_vn);
        language_ch = dialog.findViewById(R.id.language_ch);
        language_en = dialog.findViewById(R.id.language_en);

        language_vn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                language = 0;
                editLanguage.putInt(KUtils.L_KEY, language);
                editLanguage.commit();

                finish();
                startActivity(getIntent());
                dialog.dismiss();
            }
        });

        language_ch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                language = 1;
                editLanguage.putInt(KUtils.L_KEY, language);
                editLanguage.commit();

                finish();
                startActivity(getIntent());
                dialog.dismiss();
            }
        });

        language_en.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                language = 2;
                editLanguage.putInt(KUtils.L_KEY, language);
                editLanguage.commit();

                finish();
                startActivity(getIntent());
                dialog.dismiss();
            }
        });
        dialog.show();
    }


}
