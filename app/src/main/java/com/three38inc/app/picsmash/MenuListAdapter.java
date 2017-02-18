package com.three38inc.app.picsmash;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;

import java.util.Collections;
import java.util.List;

/**
 * Created by Jobith on 07-06-2015.
 */

public class MenuListAdapter extends RecyclerView.Adapter <MenuListAdapter.MenuListViewHolder> {

    private LayoutInflater inflater;
    private ItemClickListener itemClickListener;
    List <MenuInfo> data = Collections.emptyList();

    public MenuListAdapter(Context context, List <MenuInfo> data) {
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MenuListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.menu_item, parent, false);
         MenuListViewHolder holder = new MenuListViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MenuListViewHolder holder, int position) {
        MenuInfo current = data.get(position);
        holder.heading.setText(current.heading);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MenuListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView heading;

        public MenuListViewHolder(View itemView) {
            super(itemView);
            heading = (TextView) itemView.findViewById(R.id.menu_heading);
            heading.setOnClickListener(this);
            MaterialRippleLayout.on(heading)
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
