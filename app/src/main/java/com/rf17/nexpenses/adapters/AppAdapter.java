package com.rf17.nexpenses.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonFlat;
import com.rf17.nexpenses.NexpensesApplication;
import com.rf17.nexpenses.activities.AppActivity;
import com.rf17.nexpenses.AppInfo;
import com.rf17.nexpenses.R;
import com.rf17.nexpenses.activities.MainActivity;
import com.rf17.nexpenses.utils.AppPreferences;

import java.util.ArrayList;
import java.util.List;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.AppViewHolder> implements Filterable {
    // Load Settings
    private AppPreferences appPreferences;

    // AppAdapter variables
    private List<AppInfo> appList;
    private List<AppInfo> appListSearch;
    private Context context;

    public AppAdapter(List<AppInfo> appList, Context context) {
        this.appList = appList;
        this.context = context;
        this.appPreferences = NexpensesApplication.getAppPreferences();
    }

    @Override
    public int getItemCount() {
        return appList.size();
    }

    public void clear() {
        appList.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(AppViewHolder appViewHolder, int i) {
        AppInfo appInfo = appList.get(i);
        appViewHolder.vName.setText(appInfo.getName());
        appViewHolder.vApk.setText(appInfo.getAPK());
        appViewHolder.vIcon.setImageDrawable(appInfo.getIcon());

        setButtonEvents(appViewHolder, appInfo);

    }

    private void setButtonEvents(AppViewHolder appViewHolder, final AppInfo appInfo) {
        CardView cardView = appViewHolder.vCard;

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity activity = (Activity) context;

                Intent intent = new Intent(context, AppActivity.class);
                intent.putExtra("app_name", appInfo.getName());
                intent.putExtra("app_apk", appInfo.getAPK());
                intent.putExtra("app_version", appInfo.getVersion());
                intent.putExtra("app_source", appInfo.getSource());
                intent.putExtra("app_data", appInfo.getData());
                Bitmap bitmap = ((BitmapDrawable) appInfo.getIcon()).getBitmap();
                intent.putExtra("app_icon", bitmap);
                intent.putExtra("app_isSystem", appInfo.isSystem());

                context.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.fade_back);
            }
        });

    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                final FilterResults oReturn = new FilterResults();
                final List<AppInfo> results = new ArrayList<>();
                if (appListSearch == null) {
                    appListSearch = appList;
                }
                if (charSequence != null) {
                    if (appListSearch != null && appListSearch.size() > 0) {
                        for (final AppInfo appInfo : appListSearch) {
                            if (appInfo.getName().toLowerCase().contains(charSequence.toString())) {
                                results.add(appInfo);
                            }
                        }
                    }
                    oReturn.values = results;
                    oReturn.count = results.size();
                }
                return oReturn;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if (filterResults.count > 0) {
                    MainActivity.setResultsMessage(false);
                } else {
                    MainActivity.setResultsMessage(true);
                }
                appList = (ArrayList<AppInfo>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public AppViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View appAdapterView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.app_layout, viewGroup, false);
        return new AppViewHolder(appAdapterView);
    }

    public static class AppViewHolder extends RecyclerView.ViewHolder {
        protected TextView vName;
        protected TextView vApk;
        protected ImageView vIcon;
        protected ButtonFlat vExtract;
        protected ButtonFlat vShare;
        protected CardView vCard;

        public AppViewHolder(View v) {
            super(v);
            vName = (TextView) v.findViewById(R.id.txtName);
            vApk = (TextView) v.findViewById(R.id.txtApk);
            vIcon = (ImageView) v.findViewById(R.id.imgIcon);
            vExtract = (ButtonFlat) v.findViewById(R.id.btnExtract);
            vShare = (ButtonFlat) v.findViewById(R.id.btnShare);
            vCard = (CardView) v.findViewById(R.id.app_card);

        }
    }

}
