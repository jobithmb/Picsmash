package com.three38inc.app.picsmash;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.balysv.materialripple.MaterialRippleLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FavouriteActivity extends ActionBarActivity implements SwipeRefreshLayout.OnRefreshListener, PixieAdapter.ItemClickListener {

    private Toolbar toolbar;
    private RecyclerView listResults;
    private SwipeRefreshLayout swipeRefresh;
    private PixieAdapter pixieAdapter;
    private ArrayList<Pixie> pixieArrayList = new ArrayList<>();
    private String query;

    private ImageView errorImg;
    private TextView errorText;
    private Button retryButton;

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);

        requestQueue = VolleySingleton.getInstance().getRequestQueue();

        listResults = (RecyclerView) findViewById(R.id.listPixies_results);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh_results);
        swipeRefresh.setOnRefreshListener(this);
        swipeRefresh.setColorSchemeResources(R.color.first, R.color.second, R.color.third, R.color.fourth);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Favourites");

        errorImg = (ImageView) findViewById(R.id.errorImg);
        errorText = (TextView) findViewById(R.id.errorText);
        retryButton = (Button) findViewById(R.id.retryBtn);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        pixieAdapter = new PixieAdapter(this);
        pixieAdapter.setItemClickListener(this);
        if(Utils.isTablet(MyApplication.getAppContext()))
        {
            listResults.setLayoutManager(new StaggeredGridLayoutManager(3,1));
        }
        else
        {
            listResults.setLayoutManager(new StaggeredGridLayoutManager(2,1));
        }

        listResults.setAdapter(pixieAdapter);

        fetchResults();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }


    private void fetchResults() {
            //getSupportActionBar().setTitle("Search results for - "+query);
            //makeRequest_1(query);
        pixieArrayList = MyApplication.getWritableDatabase().readPixie();
        if(pixieArrayList.isEmpty()) {
            errorImg.setVisibility(View.VISIBLE);
            errorText.setVisibility(View.VISIBLE);
            errorText.setText("Sorry picsmashes found!");
            retryButton.setVisibility(View.VISIBLE);
            swipeRefresh.setVisibility(View.GONE);
        }
        else{
            swipeRefresh.setVisibility(View.VISIBLE);
            errorImg.setVisibility(View.GONE);
            errorText.setVisibility(View.GONE);
            errorText.setText("");
            retryButton.setVisibility(View.GONE);
            pixieAdapter.setPixieList(pixieArrayList);
        }
        if(swipeRefresh.isRefreshing())
        {
            swipeRefresh.setRefreshing(false);
        }



    }

    @Override
    public void onRefresh() {
        pixieArrayList = MyApplication.getWritableDatabase().readPixie();
        if(pixieArrayList.isEmpty()) {
            errorImg.setVisibility(View.VISIBLE);
            errorText.setVisibility(View.VISIBLE);
            errorText.setText("Sorry picsmashes found!");
            retryButton.setVisibility(View.VISIBLE);
            swipeRefresh.setVisibility(View.GONE);
        }
        else{
            swipeRefresh.setVisibility(View.VISIBLE);
            errorImg.setVisibility(View.GONE);
            errorText.setVisibility(View.GONE);
            errorText.setText("");
            retryButton.setVisibility(View.GONE);
            pixieAdapter.setPixieList(pixieArrayList);
        }
        if(swipeRefresh.isRefreshing())
        {
            swipeRefresh.setRefreshing(false);
        }

    }

    public void makeRequest_1(String query){
        Log.d("ABC", "tags = " + query);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET,
                "http://pixie.three38inc.com/php/getPixie.php?q="+query,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        swipeRefresh.setVisibility(View.VISIBLE);
                        errorImg.setVisibility(View.GONE);
                        errorText.setVisibility(View.GONE);
                        errorText.setText("");
                        retryButton.setVisibility(View.GONE);
                        pixieArrayList = parseJSON_1(response);

                        pixieAdapter.setPixieList(pixieArrayList);
                        Log.d("ABC","Loaded from makeRequest_1");
                        if(swipeRefresh.isRefreshing())
                        {
                            swipeRefresh.setRefreshing(false);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        errorImg.setVisibility(View.VISIBLE);
                        errorText.setVisibility(View.VISIBLE);
                        errorText.setText("Sorry no results found!");
                        retryButton.setVisibility(View.VISIBLE);
                        swipeRefresh.setVisibility(View.GONE);
                        if(swipeRefresh.isRefreshing())
                        {
                            swipeRefresh.setRefreshing(false);
                        }
                    }
                });
        requestQueue.add(jsonRequest);
    }

    private ArrayList<Pixie> parseJSON_1(JSONObject response){
        ArrayList<Pixie> listPixie = new ArrayList<>();
        if(response != null && response.length()>0) {

            try {
                JSONArray photoArray = response.getJSONArray("PIXIES");
                Log.d("ABC", "array length = " + photoArray.length());
                getSupportActionBar().setTitle(photoArray.length() + " results found");
                for (int i = 0; i < photoArray.length(); i++) {
                    JSONObject currentPixie = photoArray.getJSONObject(i);
                    String imgCategory = currentPixie.getString("category");
                    String imgName = currentPixie.getString("name");
                    String imgUrl = currentPixie.getString("url");
                    String imgCourtesy = currentPixie.getString("courtesy");
                    Pixie pixie = new Pixie(imgCategory, imgName, imgUrl, imgCourtesy);

                    listPixie.add(pixie);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return listPixie;

    }



    @Override
    public void itemClicked(View view, int position) {
        Pixie obj = pixieArrayList.get(position);
        final String name = obj.getImgName();
        final String category = obj.getImgCategory();
        final String courtesy = obj.getImgCourtesy();
        final String url = obj.getImgUrl();

        boolean wrapInScrollView = true;
        MaterialDialog materialDialog = new MaterialDialog.Builder(this)
                .customView(R.layout.pop_up_layout, wrapInScrollView)
                .positiveText("share")
                .positiveColor(getResources().getColor(R.color.secondary_text))
                .negativeText("save")
                .negativeColor(getResources().getColor(R.color.secondary_text))
                .neutralText("remove")
                .neutralColor(getResources().getColor(R.color.secondary_text))
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        Utils.shareImg(dialog.findViewById(R.id.pixieImg), (TextView) dialog.findViewById(R.id.pixieName));
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        Utils.saveImg(dialog.findViewById(R.id.pixieImg), (TextView) dialog.findViewById(R.id.pixieName), url);
                    }

                    @Override
                    public void onNeutral(MaterialDialog dialog) {
                        Utils.delFav(name, category, url, courtesy);
                        pixieArrayList = MyApplication.getWritableDatabase().readPixie();
                        if(pixieArrayList.isEmpty()) {
                            errorImg.setVisibility(View.VISIBLE);
                            errorText.setVisibility(View.VISIBLE);
                            errorText.setText("Sorry picsmashes found!");
                            retryButton.setVisibility(View.VISIBLE);
                            swipeRefresh.setVisibility(View.GONE);
                        }
                        else{
                            swipeRefresh.setVisibility(View.VISIBLE);
                            errorImg.setVisibility(View.GONE);
                            errorText.setVisibility(View.GONE);
                            errorText.setText("");
                            retryButton.setVisibility(View.GONE);
                            pixieAdapter.setPixieList(pixieArrayList);
                            pixieAdapter.notifyDataSetChanged();
                        }
                        if(swipeRefresh.isRefreshing())
                        {
                            swipeRefresh.setRefreshing(false);
                        }


                    }
                })
                .show();

//

        View customView = materialDialog.getCustomView();
        ImageView image = (ImageView) customView.findViewById(R.id.pixieImg);
        TextView textName = (TextView) customView.findViewById(R.id.pixieName);
        TextView textCourtesy = (TextView) customView.findViewById(R.id.pixieCourtesy);

        Log.d("ABC",courtesy);
        textCourtesy.setText(courtesy);
        textName.setText(name);

        Picasso.with(this)
                .load(url)
                .placeholder(R.drawable.photo)
                .into(image);

        MaterialRippleLayout.on(image)
                .rippleColor(Color.BLACK)
                .rippleOverlay(true)
                .rippleAlpha(0.2f)
                .rippleDiameterDp(10)
                .rippleDuration(350)
                .rippleFadeDuration(75)
                .rippleDelayClick(true)
                .create();
    }
}
