package com.three38inc.app.picsmash;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */

public class NavigationDrawerFragment extends Fragment implements MenuListAdapter.ItemClickListener {

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    public static final String PREF_FILE_NAME = "testpref";
    public static final String KEY_USER_LEARNED_DRAWER = "user_learned_drawer";

    private View containerView;

    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;

    private RecyclerView menuListView;
    private MenuListAdapter menuAdapter;


    public NavigationDrawerFragment() {
        // Required empty public constructor
    }

    public List<MenuInfo> getData(){

        List<MenuInfo> data = new ArrayList<>();

        String[] headings = getResources().getStringArray(R.array.tabs);
        for(int i=0; i<headings.length; i++){
            MenuInfo current =  new MenuInfo();
            current.heading = headings[i];
            data.add(current);
        }
        return data;
    }

    @Override
    public void itemClicked(View view, int position) {
        mDrawerLayout.closeDrawer(containerView);
        //Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_SHORT).show();
        MainActivity.changePage(position);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUserLearnedDrawer = Boolean.valueOf( readFromPreferences( getActivity(), KEY_USER_LEARNED_DRAWER, "false" ) );
        if(savedInstanceState != null) {
            mFromSavedInstanceState = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        menuListView = (RecyclerView) layout.findViewById(R.id.menuList);
        menuAdapter = new MenuListAdapter(getActivity(),getData());
        menuAdapter.setItemClickListener(this);
        menuListView.setAdapter(menuAdapter);
        menuListView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return layout;
    };


    public void setUp( int fragementID, DrawerLayout drawerLayout, final Toolbar toolBar ) {


        containerView = getActivity().findViewById(fragementID);
        mDrawerLayout = drawerLayout;

        mDrawerToggle = new ActionBarDrawerToggle( getActivity(), drawerLayout, toolBar, R.string.drawer_open, R.string.drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if(!mUserLearnedDrawer)
                {
                    mUserLearnedDrawer = true;
                    saveToPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, "true");
                }
                getActivity().invalidateOptionsMenu();
                //Toast.makeText(getActivity(), "open", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
                //Toast.makeText(getActivity(), "close", Toast.LENGTH_SHORT).show();

            }

        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        if(!mUserLearnedDrawer && !mFromSavedInstanceState){
            mDrawerLayout.openDrawer(containerView);
            saveToPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, "true");
            Intent intent = new Intent(MyApplication.getAppContext(), IntroActivity.class);
            startActivity(intent);
        }


        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

    }

    public static void saveToPreferences(Context context, String preferenceName, String preferenceValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences( PREF_FILE_NAME, Context.MODE_PRIVATE );
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName, preferenceValue);
        editor.apply();
    }

    public static String readFromPreferences(Context context, String preferenceName, String defaultValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences( PREF_FILE_NAME, Context.MODE_PRIVATE );
        return sharedPreferences.getString(preferenceName,defaultValue);
    }


}
