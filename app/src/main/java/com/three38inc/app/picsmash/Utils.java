package com.three38inc.app.picsmash;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.io.File;
import java.io.FileOutputStream;

import br.com.goncalves.pugnotification.notification.PugNotification;

/**
 * Created by Jobith on 8/16/2015.
 */
public class Utils {

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static boolean createDirIfNotExists(String path) {
        boolean ret = true;

        File file = new File(Environment.getExternalStorageDirectory(), path);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Log.d("ABC", "Problem creating Image folder");
                ret = false;
            }
        }
        return ret;
    }

    public static void shareImg(View v,TextView file){

        String fileName = (String)file.getText();

        if(createDirIfNotExists("Picsmash App")) {
            View content = v;
            content.setDrawingCacheEnabled(true);

            Bitmap bitmap = content.getDrawingCache();
            File root = Environment.getExternalStorageDirectory();
            File cachePath = new File(root.getAbsolutePath() + "/Picsmash App/"+fileName+".jpg");
            try {
                cachePath.createNewFile();
                FileOutputStream ostream = new FileOutputStream(cachePath);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                ostream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Intent mediaScanIntent = new Intent(
                        Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(cachePath); //out is your output file
                mediaScanIntent.setData(contentUri);
                MyApplication.getAppContext().sendBroadcast(mediaScanIntent);
            } else {
                MyApplication.getAppContext().sendBroadcast(new Intent(
                        Intent.ACTION_MEDIA_MOUNTED,
                        Uri.parse("file://"
                                + Environment.getExternalStorageDirectory())));
            }

            Intent share = new Intent(Intent.ACTION_SEND);
            share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            share.setType("image/*");
            share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(cachePath));
            Intent new_intent = Intent.createChooser(share, "Share Picsmash Awesomeness via");
            new_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            MyApplication.getAppContext().startActivity(new_intent);

        }

    }

    public static void addFav(String name, String category, String url, String courtesy){
        MyApplication.getWritableDatabase().add(name,category,url,courtesy);
        Toast.makeText(MyApplication.getAppContext(),"Picsmash added to fav!",Toast.LENGTH_SHORT).show();
    }

    public static void delFav(String name, String category, String url, String courtesy){
        MyApplication.getWritableDatabase().del(name, category, url, courtesy);
        Toast.makeText(MyApplication.getAppContext(),"Picsmash removed to fav!",Toast.LENGTH_SHORT).show();
    }

    public static void saveImg(View v,TextView file, String url){

        final InterstitialAd mInterstitialAd;
        mInterstitialAd = new InterstitialAd(MyApplication.getAppContext());
        mInterstitialAd.setAdUnitId("ca-app-pub-8677264540224431/7945109101");
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mInterstitialAd.loadAd(adRequest);
        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                mInterstitialAd.show();
            }
        });
        String fileName = (String)file.getText();
        if(createDirIfNotExists("Picsmash App")) {
            View content = v;
            content.setDrawingCacheEnabled(true);

            Bitmap bitmap = content.getDrawingCache();
            File root = Environment.getExternalStorageDirectory();
            File cachePath = new File(root.getAbsolutePath() + "/Picsmash App/"+fileName+".jpg");
            try {
                cachePath.createNewFile();
                FileOutputStream ostream = new FileOutputStream(cachePath);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                ostream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Intent mediaScanIntent = new Intent(
                        Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(cachePath); //out is your output file
                mediaScanIntent.setData(contentUri);
                MyApplication.getAppContext().sendBroadcast(mediaScanIntent);
            } else {
                MyApplication.getAppContext().sendBroadcast(new Intent(
                        Intent.ACTION_MEDIA_MOUNTED,
                        Uri.parse("file://"
                                + Environment.getExternalStorageDirectory())));
            }

            Intent share = new Intent(Intent.ACTION_SEND);
            share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            share.setType("image/*");
            share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(cachePath));
            Intent new_intent = Intent.createChooser(share, "Share Picsmash Awesomeness via");
            new_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            Intent i=new Intent();
//            i.setAction(Intent.ACTION_VIEW);
//            i.setDataAndType(Uri.fromFile(cachePath), "image/*");
            PendingIntent pendingIntent;
            pendingIntent =  PendingIntent.getActivity(MyApplication.getAppContext(), 0, new_intent, 0);
            //MyApplication.getAppContext().startActivity(new_intent);

            PugNotification.with(MyApplication.getAppContext())
                    .load()
                    .title("Picsmash")
                    .autoCancel(true)
                    .message("Picsmash Saved to Gallery!")
                    .button(R.drawable.abc_ic_menu_share_mtrl_alpha, "share", pendingIntent)
                    .smallIcon(R.drawable.logo_small)
                    .largeIcon(bitmap)
                    .simple()
//                    .custom()
//                    .setImageLoader(PixieFragment.this)
//                    .background(url)
//                    .setPlaceholder(R.drawable.photo)
                    .build();
        }

    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
                Toast.makeText(MyApplication.getAppContext(),"Cache Cleared successfully !",Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }


}
