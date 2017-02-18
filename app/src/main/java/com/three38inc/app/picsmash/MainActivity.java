package com.three38inc.app.picsmash;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.balysv.materialripple.MaterialRippleLayout;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.pushbots.push.Pushbots;
import com.squareup.picasso.Picasso;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;
import me.tatarka.support.job.JobInfo;
import me.tatarka.support.job.JobScheduler;


public class MainActivity extends ActionBarActivity implements MaterialTabListener {

    private Toolbar toolbar;
    private static ViewPager pager;
    MaterialTabHost materialTab;

    String[] tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Pushbots.sharedInstance().init(this);


        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        pager = (ViewPager) findViewById(R.id.viewPager);
        pager.setOffscreenPageLimit(6);
        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);

        materialTab = (MaterialTabHost) this.findViewById(R.id.materialTab);

        pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                materialTab.setSelectedNavigationItem(position);

            }
        });

        for (int i = 0; i < pagerAdapter.getCount(); i++) {
            materialTab.addTab(
                    materialTab.newTab()
                            .setText(pagerAdapter.getPageTitle(i))
                            .setTabListener(this)
            );
        }



        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        NavigationDrawerFragment drawerFragment;

        drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);

        ImageView imgMenu = (ImageView) findViewById(R.id.imageView);
        Picasso.with(this)
                .load("http://pixie.three38inc.com/images/picsmash.jpg")
                .placeholder(R.drawable.photo)
                .into(imgMenu);

        MaterialRippleLayout.on(imgMenu)
                .rippleColor(Color.BLACK)
                .rippleOverlay(true)
                .rippleAlpha(0.2f)
                .rippleDiameterDp(10)
                .rippleDuration(350)
                .rippleFadeDuration(75)
                .rippleDelayClick(true)
                .create();

    }

    public static void changePage(int index){
        pager.setCurrentItem(index);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id)
        {
            case R.id.action_share:
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            break;
            case R.id.action_about:
                startActivity(new Intent(MyApplication.getAppContext(), AboutActivity.class));
            break;
            case R.id.action_clear:
                Utils.deleteCache(this);
            break;
            case R.id.action_help:
                finish();
                startActivity(new Intent(MyApplication.getAppContext(), IntroActivity.class));
            break;
            case R.id.action_fav:
                startActivity(new Intent(MyApplication.getAppContext(), FavouriteActivity.class));
            break;

        }


        return super.onOptionsItemSelected(item);
    }
////////////////////////////////////////////////////////////
    @Override
    public void onTabSelected(MaterialTab materialTab) {
        pager.setCurrentItem(materialTab.getPosition());
    }

    @Override
    public void onTabReselected(MaterialTab materialTab) {

    }

    @Override
    public void onTabUnselected(MaterialTab materialTab) {

    }
////////////////////////////////////////////////////////////////
    public class MyPagerAdapter extends FragmentStatePagerAdapter{


        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            tabs = getResources().getStringArray(R.array.tabs);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment =null;
            switch(position){
                case 0:  fragment = PixieFragment.newInstance("life","");  break;
                case 1:  fragment = PixieFragment.newInstance("logical","");  break;
                case 2:  fragment = PixieFragment.newInstance("dyk","");  break;
                case 3:  fragment = PixieFragment.newInstance("special","");  break;
                case 4:  fragment = PixieFragment.newInstance("minion","");  break;
                case 5:  fragment = PixieFragment.newInstance("memes","");  break;
                case 6:  fragment = PixieFragment.newInstance("malayalam","");  break;
                default: fragment = MyFragment.getInstnace(position);        break;
            }
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }

        @Override
        public int getCount() {
            return tabs.length;
        }
    }

    public static class MyFragment extends Fragment{
        private TextView textView;


        public static MyFragment getInstnace(int position){
            MyFragment myFragment = new MyFragment();
            Bundle args = new Bundle();
            args.putInt("position",position);
            myFragment.setArguments(args);
            return myFragment;
        }

        @Override

        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
            View layout = inflater.inflate(R.layout.my_fragment_layout, container, false);
            textView = (TextView) layout.findViewById(R.id.position);
            Bundle bundle = getArguments();
            if(bundle != null){
                textView.setText("The page is "+((int)bundle.getInt("position")+1));
            }
            return layout;
        }
    }
}
