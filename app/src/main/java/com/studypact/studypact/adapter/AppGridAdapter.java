package com.studypact.studypact.adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.studypact.studypact.R;
import com.studypact.studypact.appinstance.AppInstance;
import static com.studypact.studypact.util.Util.setText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arvind on 11/12/2016.
 */

public class AppGridAdapter extends RecyclerView.Adapter<AppGridAdapter.AppViewHolder>{

    List<ResolveInfo> pkgAppsList = null;
    List<ResolveInfo> selectedApps = new ArrayList<>();

    public AppGridAdapter(){
        AppInstance mInstance = AppInstance.getInstance();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
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
                } else {
                    view.setBackgroundColor(view.getResources().getColor(android.R.color.white));
                    selectedApps.remove((ResolveInfo) view.getTag());
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
