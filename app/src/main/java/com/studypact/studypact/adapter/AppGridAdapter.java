package com.studypact.studypact.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.Image;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.studypact.studypact.R;
import com.studypact.studypact.activity.HomeActivity;
import com.studypact.studypact.appinstance.AppInstance;
import com.studypact.studypact.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import static com.studypact.studypact.util.Util.setText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arvind on 11/12/2016.
 */

public class AppGridAdapter extends RecyclerView.Adapter<AppGridAdapter.AppViewHolder>{
    private static final String TAG = "APPGRIDADAP";
    List<ResolveInfo> pkgAppsList = null;
    List<ResolveInfo> selectedApps = new ArrayList<>();
    FloatingActionButton mFab = null;

    public AppGridAdapter(final FloatingActionButton fab) {
        final AppInstance mInstance = AppInstance.getInstance();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        this.mFab = fab;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PackageManager pm = view.getContext().getPackageManager();
                JSONArray array = new JSONArray();
                try {
                    for (ResolveInfo app : selectedApps) {
                        array.put(app.activityInfo.applicationInfo.packageName);
                    }
                    Util.putIntoStore("locked_apps", array.toString());
                    Util.putBooleanFromStore("is_setup", true);
                    Intent intent = new Intent(mInstance, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    mInstance.startActivity(intent);
                } catch (Exception e) {
                    Util.showToast("Error in adding please try later");
                    Log.e(TAG, "Exception in adding banned apps", e);
                }
            }
        });

        //TODO :: implement caching
        pkgAppsList = mInstance.getPackageManager().queryIntentActivities( mainIntent, 0);
    }

    @Override
    public AppViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(AppInstance.getInstance(), R.layout.app_grid_item, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!selectedApps.contains((ResolveInfo) view.getTag())){
                    selectedApps.add((ResolveInfo) view.getTag());
                    view.setBackgroundColor(view.getResources().getColor(R.color.colorAccent));
                    if (selectedApps.size() == 1) {
                        mFab.setVisibility(View.VISIBLE);
                    }
                } else {
                    view.setBackgroundColor(view.getResources().getColor(android.R.color.white));
                    selectedApps.remove((ResolveInfo) view.getTag());
                    if (selectedApps.size() == 0) {
                        mFab.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
        return new AppViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AppViewHolder holder, int position) {
        ResolveInfo appInfo = pkgAppsList.get(position);
        PackageManager pm = AppInstance.getInstance().getPackageManager();
        setText(holder.getTextView(), appInfo.loadLabel(pm).toString());
        holder.getImageView().setImageDrawable(appInfo.loadIcon(pm));
        holder.getImageView().setTag(pkgAppsList.get(position));
        if (selectedApps.contains(appInfo)) {
            holder.mView.setBackgroundColor(holder.mView.getResources().getColor(R.color.colorAccent));
        } else {
            holder.mView.setBackgroundColor(holder.mView.getResources().getColor(android.R.color.white));
        }
        holder.mView.setTag(appInfo);
    }

    @Override
    public int getItemCount() {
        return pkgAppsList == null ? 0 : pkgAppsList.size();
    }

    static class AppViewHolder extends RecyclerView.ViewHolder{
        private View mView;
        TextView tView;
        ImageView iView;

        public AppViewHolder(View view){
            super(view);
            mView = view;
            iView = (ImageView) view.findViewById(R.id.grid_img);
            tView = (TextView) view.findViewById(R.id.grid_name);
        }

        TextView getTextView(){
            return tView;
        }

        ImageView getImageView() {
            return iView;
        }
    }
}
