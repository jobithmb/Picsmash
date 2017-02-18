package com.three38inc.app.picsmash;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Jobith on 8/6/2015.
 */
public class PixieAdapter extends RecyclerView.Adapter<PixieAdapter.ViewHolderPixie> {

    private LayoutInflater layoutInflater;
    private ItemClickListener itemClickListener;
    private ArrayList<Pixie> pixieList = new ArrayList<>();

    public PixieAdapter(Context context){
        layoutInflater = LayoutInflater.from(context);
    }

    public void setPixieList(ArrayList<Pixie> pixieList){
        this.pixieList = pixieList;
        notifyItemRangeChanged(0,pixieList.size());
    }

    @Override
    public ViewHolderPixie onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.pixie_item, parent, false);
        ViewHolderPixie viewHolderPixie = new ViewHolderPixie(view);
        return viewHolderPixie;
    }

    @Override
    public void onBindViewHolder(ViewHolderPixie holder, int position) {
        Pixie currentPixie = pixieList.get(position);
        holder.pixieName.setText(currentPixie.getImgName());
        String imgUrl = currentPixie.getImgUrl();

//        AdRequest adRequest = new AdRequest.Builder().build();
//        holder.mAdView.loadAd(adRequest);

        Picasso.with(MyApplication.getAppContext())
                .load(imgUrl)
                .placeholder(R.drawable.photo)
                .into(holder.pixieImage);

        YoYo.with(Techniques.FadeIn)
                .duration(700)
                .playOn(holder.pixieCard);


    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    @Override
    public int getItemCount() {
        return pixieList.size();
    }

    class ViewHolderPixie extends RecyclerView.ViewHolder implements View.OnClickListener{

        private CardView pixieCard;
        private ImageView pixieImage;
        private TextView pixieName;
//        AdView mAdView;

        public ViewHolderPixie(View itemView) {
            super(itemView);

            pixieCard = (CardView) itemView.findViewById(R.id.pixieCard);
            pixieImage = (ImageView) itemView.findViewById(R.id.pixieImg);
            pixieName = (TextView) itemView.findViewById(R.id.pixieName);

//            mAdView = (AdView) itemView.findViewById(R.id.adViewPixie);


            pixieImage.setOnClickListener(this);
            MaterialRippleLayout.on(pixieImage)
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
        public void onClick(View v) {
            if(itemClickListener!=null){
                itemClickListener.itemClicked(v, getPosition());
            }
        }
    }
    public interface ItemClickListener{
        public void itemClicked(View view, int position);
    }

}
