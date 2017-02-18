//package com.three38inc.app.picsmash;
//
//import android.graphics.Bitmap;
//import android.graphics.drawable.Drawable;
//import android.os.AsyncTask;
//import android.util.Log;
//
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.android.volley.toolbox.RequestFuture;
//import com.squareup.picasso.Picasso;
//import com.squareup.picasso.Target;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.TimeoutException;
//
//import br.com.goncalves.pugnotification.interfaces.ImageLoader;
//import br.com.goncalves.pugnotification.interfaces.OnImageLoadingCompleted;
//import me.tatarka.support.job.JobParameters;
//import me.tatarka.support.job.JobService;
//
///**
// * Created by Jobith on 8/9/2015.
// */
//public class PixieService extends JobService implements ImageLoader{
//    @Override
//    public boolean onStartJob(JobParameters jobParameters) {
//        new MyTask(this).execute(jobParameters);
//        Log.d("ABC","on startJob");
//        return true;
//    }
//
//    @Override
//    public boolean onStopJob(JobParameters jobParameters) {
//        return false;
//    }
//
//    private static class MyTask extends AsyncTask<JobParameters, Void, JobParameters>{
//
//        PixieService pixieService;
//        private RequestQueue requestQueue;
//        ArrayList<Pixie> listPixies;
//
//        MyTask(PixieService pixieService){
//            this.pixieService = pixieService;
//            requestQueue = VolleySingleton.getInstance().getRequestQueue();
//        }
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected JobParameters doInBackground(JobParameters... params) {
//            JSONObject response = makeRequest();
//            listPixies = parseJSON(response);
//            Log.d("ABC", "In AsyncTask");
//            MyApplication.getWritableDatabase().insertPixie(listPixies, true);
//
//            return params[0];
//        }
////TODO: check reScheduling part
//        @Override
//        protected void onPostExecute(JobParameters jobParameters) {
//            if(listPixies.isEmpty())
//                pixieService.jobFinished(jobParameters, true);
//            else
//                pixieService.jobFinished(jobParameters, false);
//
//        }
//
//        public JSONObject makeRequest(){
//            JSONObject response = null;
//            Log.d("ABC", "Making request!");
//
//            RequestFuture<JSONObject> requestFuture = RequestFuture.newFuture();
//
//            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET,
//                    "http://pixie.three38inc.com/php/getPixie.php", requestFuture, requestFuture);
//            requestQueue.add(jsonRequest);
//            try {
//                response = requestFuture.get(30000, TimeUnit.MILLISECONDS);
//            } catch (InterruptedException e) {
//                Log.d("ABC", "REQUEST ERROR = "+e.getMessage());
//            } catch (ExecutionException e) {
//                Log.d("ABC", "REQUEST ERROR = " + e.getMessage());
//            } catch (TimeoutException e) {
//                Log.d("ABC", "REQUEST ERROR = " + e.getMessage());
//            }
//            return response;
//        }
//
//        private ArrayList<Pixie> parseJSON(JSONObject response){
//            Log.d("ABC", "Making parseJSON!");
//            ArrayList<Pixie> listPixie = new ArrayList<>();
//            if(response != null && response.length()>0) {
//
//                try {
//                    JSONArray photoArray = response.getJSONArray("PIXIES");
//                    Log.d("ABC","array length = "+photoArray.length());
//                    for (int i = 0; i < photoArray.length(); i++) {
//                        JSONObject currentPixie = photoArray.getJSONObject(i);
//                        //data.append(currentPixie.getString("category")+"\n");
//                        String imgCategory = currentPixie.getString("category");
//                        String imgName = currentPixie.getString("name");
//                        String imgUrl = currentPixie.getString("url");
//                        String imgCourtsey = currentPixie.getString("courtesy");
//
//                        Pixie pixie = new Pixie(imgCategory, imgName, imgUrl, imgCourtsey);
//
//                        listPixie.add(pixie);
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//            else
//            {
//                Log.d("ABC", "parse JSON got null response from makerequest!");
//            }
//            return listPixie;
//
//        }
//    }
//
//    private Target viewTarget;
//    @Override
//    public void load(String uri, final OnImageLoadingCompleted onCompleted) {
//        viewTarget = getViewTarget(onCompleted);
//        Picasso.with(MyApplication.getAppContext()).load(uri).into(viewTarget);
//    }
//
//    @Override
//    public void load(int imageResId, OnImageLoadingCompleted onCompleted) {
//        viewTarget = getViewTarget(onCompleted);
//        Picasso.with(MyApplication.getAppContext()).load(imageResId).into(viewTarget);
//    }
//
//    private static Target getViewTarget(final OnImageLoadingCompleted onCompleted) {
//        return new Target() {
//            @Override
//            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                onCompleted.imageLoadingCompleted(bitmap);
//            }
//
//            @Override
//            public void onBitmapFailed(Drawable errorDrawable) {
//
//            }
//
//            @Override
//            public void onPrepareLoad(Drawable placeHolderDrawable) {
//
//            }
//        };
//    }
//}
