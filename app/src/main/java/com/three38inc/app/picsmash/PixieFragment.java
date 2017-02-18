package com.three38inc.app.picsmash;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.balysv.materialripple.MaterialRippleLayout;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import br.com.goncalves.pugnotification.interfaces.ImageLoader;
import br.com.goncalves.pugnotification.interfaces.OnImageLoadingCompleted;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PixieFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PixieFragment extends Fragment implements PixieAdapter.ItemClickListener, SwipeRefreshLayout.OnRefreshListener, ImageLoader{
    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mPressed;

    private RequestQueue requestQueue;
    private ArrayList<Pixie> pixieArrayList = new ArrayList<>();

    private RecyclerView listPixies;
    private PixieAdapter pixieAdapter;
    public SwipeRefreshLayout swipeRefreshLayout;
    private TextView errorText;
    private ImageView errorImg;
    private Button retryButton;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PixieFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PixieFragment newInstance(String param1, String param2) {
        PixieFragment fragment = new PixieFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    public PixieFragment() {
        // Required empty public constructor
    }



    public void makeRequest_1(){

        Log.d("ABC", "tags = " + mParam1);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET,
                "http://pixie.three38inc.com/php/getPixie.php?cat="+mParam1,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        swipeRefreshLayout.setVisibility(View.VISIBLE);
                        errorImg.setVisibility(View.GONE);
                        errorText.setVisibility(View.GONE);
                        errorText.setText("");
                        retryButton.setVisibility(View.GONE);
                        pixieArrayList = parseJSON_1(response);

                        pixieAdapter.setPixieList(pixieArrayList);
//                        pixieAdapter.notifyDataSetChanged();
                        Log.d("ABC","Loaded from nakeRequest_1");
                        if(swipeRefreshLayout.isRefreshing())
                        {
                            swipeRefreshLayout.setRefreshing(false);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(MyApplication.getAppContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
                        errorImg.setVisibility(View.VISIBLE);
                        errorText.setVisibility(View.VISIBLE);
                        errorText.setText("Sorry, Something went wrong!");
                        retryButton.setVisibility(View.VISIBLE);
                        swipeRefreshLayout.setVisibility(View.GONE);
                        if(swipeRefreshLayout.isRefreshing())
                        {
                            swipeRefreshLayout.setRefreshing(false);
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
                Log.d("ABC","array length = "+photoArray.length());
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("state_pixie",pixieArrayList);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        requestQueue = VolleySingleton.getInstance().getRequestQueue();

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pixie, container, false);
        errorImg = (ImageView) view.findViewById(R.id.errorImg);
        errorText = (TextView) view.findViewById(R.id.errorText);
        retryButton = (Button) view.findViewById(R.id.retryBtn);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ABC", "Retrying Now !!!");

                swipeRefreshLayout.setRefreshing(true);
                makeRequest_1();
                if(swipeRefreshLayout.isRefreshing())
                {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.first, R.color.second, R.color.third, R.color.fourth);
        listPixies = (RecyclerView) view.findViewById(R.id.listPixies);
        pixieAdapter = new PixieAdapter(getActivity());
        pixieAdapter.setItemClickListener(this);
        if(Utils.isTablet(MyApplication.getAppContext()))
        {
            listPixies.setLayoutManager(new StaggeredGridLayoutManager(3,1));
        }
        else
        {
            listPixies.setLayoutManager(new StaggeredGridLayoutManager(2,1));
        }

        listPixies.setAdapter(pixieAdapter);

        if(savedInstanceState!=null) {
            pixieArrayList=savedInstanceState.getParcelableArrayList("state_pixie");
            pixieAdapter.setPixieList(pixieArrayList);

        }
        else
        {
                makeRequest_1();
        }


        return view;
    }


    @Override
    public void itemClicked(View view, int position) {

        Pixie obj = pixieArrayList.get(position);
        final String name = obj.getImgName();
        final String category = obj.getImgCategory();
        final String courtesy = obj.getImgCourtesy();
        final String url = obj.getImgUrl();
        int color = R.color.secondary_text;


        if(MyApplication.getWritableDatabase().match(name,category,url,courtesy)) {
            color = R.color.fav;
        }



            boolean wrapInScrollView = true;
            final MaterialDialog materialDialog = new MaterialDialog.Builder(getActivity())
                    //.title(name)
                    .customView(R.layout.pop_up_layout, wrapInScrollView)
                    .positiveText("share")
                    .positiveColor(getResources().getColor(R.color.secondary_text))
                    .negativeText("save")
                    .negativeColor(getResources().getColor(R.color.secondary_text))
                    .neutralText("fav")
                    .neutralColor(getResources().getColor(color))
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
                            if(MyApplication.getWritableDatabase().match(name,category,url,courtesy))
                                Toast.makeText(MyApplication.getAppContext(),"Picsmash is already a fav!",Toast.LENGTH_SHORT).show();
                            else
                                Utils.addFav(name,category,url,courtesy);

                        }
                    })
                    .show();

//

            View customView = materialDialog.getCustomView();
            ImageView image = (ImageView) customView.findViewById(R.id.pixieImg);
            TextView textName = (TextView) customView.findViewById(R.id.pixieName);
            TextView textCourtesy = (TextView) customView.findViewById(R.id.pixieCourtesy);

//            AdView mAdView = (AdView) customView.findViewById(R.id.adViewPreview);
//            AdRequest adRequest = new AdRequest.Builder().build();
//            mAdView.loadAd(adRequest);


            Log.d("ABC", courtesy);
            textCourtesy.setText(courtesy);
            textName.setText(name);

            Picasso.with(getActivity())
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

    @Override
    public void onRefresh() {
        makeRequest_1();
        if(swipeRefreshLayout.isRefreshing())
        {
            swipeRefreshLayout.setRefreshing(false);
        }


    }

    private Target viewTarget;
    @Override
    public void load(String uri, final OnImageLoadingCompleted onCompleted) {
        viewTarget = getViewTarget(onCompleted);
        Picasso.with(getActivity()).load(uri).into(viewTarget);
    }

    @Override
    public void load(int imageResId, OnImageLoadingCompleted onCompleted) {
        viewTarget = getViewTarget(onCompleted);
        Picasso.with(getActivity()).load(imageResId).into(viewTarget);
    }

    private static Target getViewTarget(final OnImageLoadingCompleted onCompleted) {
        return new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                onCompleted.imageLoadingCompleted(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
    }

}
