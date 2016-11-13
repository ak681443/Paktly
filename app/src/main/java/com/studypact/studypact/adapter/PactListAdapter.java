package com.studypact.studypact.adapter;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.util.TimeUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.studypact.studypact.R;
import com.studypact.studypact.appinstance.AppInstance;
import com.studypact.studypact.util.Util;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.studypact.studypact.util.Util.setText;

/**
 * Created by Arvind on 11/12/2016.
 */

public class PactListAdapter extends RecyclerView.Adapter<PactListAdapter.PactViewHolder>{

    private static final String TAG = "PACTLISTADAP";

    JSONObject pacts = null;
    int pact_length = 0;

    public PactListAdapter(JSONObject pacts){
        AppInstance mInstance = AppInstance.getInstance();
        if(pacts != null){
            this.pacts = pacts;
        }
    }

    @Override
    public PactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(AppInstance.getInstance(), R.layout.pact_list_item, null);
        return new PactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PactViewHolder holder, int position) {
        try {
            JSONObject pactInfo = pacts.getJSONArray("pacts").getJSONObject(position);
            setText(holder.getTextView(), pactInfo.getString("name"));
            String userName = pactInfo.getString("user_name");
            userName = Util.getInitials(userName);
            Drawable icon = TextDrawable.builder()
                    .buildRound(userName, ColorGenerator.MATERIAL.getColor(userName));
            holder.getImageView().setImageDrawable(icon);
            holder.getDurationView().setText(Util.getDateDifference(pactInfo.getLong("end_time"), pactInfo.getLong("start_time")));
        }catch (Exception e){
            Log.e(TAG, "exception in pact adapter", e);
        }
    }

    @Override
    public int getItemCount() {
        return pacts == null ? 0 : pact_length;
    }

    static class PactViewHolder extends RecyclerView.ViewHolder{
        private View mView;
        TextView nametView, durationtView;
        ImageView imageView;

        public PactViewHolder(View view){
            super(view);
            mView = view;
            imageView = (ImageView) view.findViewById(R.id.pact_mate);
            nametView = (TextView) view.findViewById(R.id.pact_name);
            durationtView = (TextView) view.findViewById(R.id.pact_duration);
        }

        TextView getTextView(){
            return nametView;
        }

        ImageView getImageView() {
            return imageView;
        }

        TextView getDurationView(){return durationtView;}
    }

    public void setPactList(JSONObject list){
        try {
            if (list != null) {
                this.pacts = list;
                pact_length = this.pacts.has("pacts") ? this.pacts.getJSONArray("pacts").length() : pact_length;
                notifyDataSetChanged();
            }
        }catch (Exception e){
            Log.e(TAG, "exception in set pact list", e);
        }
    }

}
