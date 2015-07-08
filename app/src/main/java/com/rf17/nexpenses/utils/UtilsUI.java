package com.rf17.nexpenses.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.rf17.nexpenses.NexpensesApplication;
import com.rf17.nexpenses.activities.AboutActivity;
import com.rf17.nexpenses.R;
import com.rf17.nexpenses.activities.SettingsActivity;
import com.rf17.nexpenses.adapters.AppAdapter;
import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.Calendar;

public class UtilsUI {
    // Load Settings
    private static AppPreferences appPreferences;

    public static int darker (int color, double factor) {
        int a = Color.alpha(color);
        int r = Color.red(color);
        int g = Color.green( color );
        int b = Color.blue(color);

        return Color.argb(a, Math.max((int) (r * factor), 0), Math.max((int) (g * factor), 0), Math.max((int) (b * factor), 0));
    }

    public static Drawer setNavigationDrawer (Activity activity, final Context context, Toolbar toolbar, final AppAdapter appAdapter, final AppAdapter appSystemAdapter, final AppAdapter appFavoriteAdapter, final RecyclerView recyclerView) {
        int header;
        appPreferences = NexpensesApplication.getAppPreferences();

        if (getDayOrNight() == 1) {
            header = R.drawable.header_day;
        } else {
            header = R.drawable.header_night;
        }

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(activity)
                .withHeaderBackground(header)
                .build();

        return new DrawerBuilder()
                .withActivity(activity)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .withStatusBarColor(UtilsUI.darker(appPreferences.getPrimaryColorPref(), 0.8))
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(context.getResources().getString(R.string.action_lancamentos)).withIcon(FontAwesome.Icon.faw_money),
                        new PrimaryDrawerItem().withName(context.getResources().getString(R.string.action_categorias)).withIcon(FontAwesome.Icon.faw_tags),
                        new PrimaryDrawerItem().withName(context.getResources().getString(R.string.action_graficos)).withIcon(FontAwesome.Icon.faw_area_chart),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName(context.getResources().getString(R.string.action_apoie_desenvolvimento)).withIcon(FontAwesome.Icon.faw_heart),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName(context.getResources().getString(R.string.action_settings)).withIcon(FontAwesome.Icon.faw_cog).withCheckable(false),
                        new SecondaryDrawerItem().withName(context.getResources().getString(R.string.action_about)).withIcon(FontAwesome.Icon.faw_info_circle).withCheckable(false)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        switch (position) {
                            case 0:
                                recyclerView.setAdapter(appAdapter);
                                break;
                            case 1:
                                recyclerView.setAdapter(appSystemAdapter);
                                break;
                            case 3:
                                recyclerView.setAdapter(appFavoriteAdapter);
                                break;
                            case 5:
                                Log.v("", "Apoie o Desenvolvimento");
                                break;
                            case 6:
                                context.startActivity(new Intent(context, SettingsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                break;
                            case 7://Ajuda e Feedback
                                context.startActivity(new Intent(context, AboutActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                break;
                            default:
                                break;
                        }

                        return false;
                    }
                }).build();

    }

    public static int getDayOrNight() {
        int actualHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        if (actualHour >= 8 && actualHour < 19) {
            return 1;
        } else {
            return 0;
        }
    }

}