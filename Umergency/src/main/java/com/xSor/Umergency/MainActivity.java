package com.xSor.Umergency;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.IconTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import com.joanzapata.android.iconify.Iconify;
import com.joanzapata.android.iconify.IconDrawable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import pac.xSor.navdrawer.adapter.NavDrawerListAdapter;
import pac.xSor.navdrawer.model.NavDrawerItem;
import pac.xSor.roundbitmap.RoundedDrawable;


public class MainActivity extends ActionBarActivity {

    // Temporary URL/DIV Settings
    String RowanUrl = "http://www.rowan.edu/emergency/";
    String divClassOrID = "div.EmergencyTitle";
    // String div2 = "div.EmergencyMessageTitle";

    /*String RowanUrl = "http://xsorcreations.com";
    String divClassOrID = "div#copyright > p";*/


    // Navigation Drawer
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    // nav drawer title
    private CharSequence mDrawerTitle;

    // used to store app title
    private CharSequence mTitle;

    // slide menu items
    private String[] navMenuTitles;

    // Checking Progress Bar
    ProgressDialog mProgressDialog;

    // Action Bar Menu Items
    private static final int MENU_REFRESH = Menu.FIRST;
    private static final int MENU_SETTINGS = Menu.FIRST + 1;


    // DP to device pixel
    private int dpToPx(int dp)
    {
        float density = getResources().getDisplayMetrics().density;
        return Math.round((float)dp * density);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load Main Activity
        setContentView(R.layout.activity_main);

        mTitle = mDrawerTitle = getTitle();

        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        // nav drawer icons from resources
        String[] navMenuIcons = getResources().getStringArray(R.array.nav_drawer_icons);

        // Define nav drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slider_menu);

        // Add List Header
        View drawerHeader = View.inflate(this, R.layout.nav_drawer_header, null);
        mDrawerList.addHeaderView(drawerHeader, "Header", false);


        //Rounded University Logo
        Bitmap universityLogo = BitmapFactory.decodeResource(getResources(), R.drawable.rowan_bg);
        Drawable roundedUniversityLogo = new RoundedDrawable(universityLogo, dpToPx(120));
        ImageView univLogo = (ImageView) findViewById(R.id.universityLogo);
        univLogo.setImageDrawable(roundedUniversityLogo);


        ArrayList<NavDrawerItem> navDrawerItems = new ArrayList<NavDrawerItem>();

        // adding nav drawer items to array

        // Home
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons[0]));
        // Updates
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons[1]));
        // Institution
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons[2]));
        // Settings
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons[3], true, "22"));
        // About
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons[4]));


        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        // setting the nav drawer list adapter
        NavDrawerListAdapter adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        mDrawerList.setAdapter(adapter);

        // enabling action bar app icon and behaving it as toggle button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ){
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(1);
        }
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        //new grabEmergencyText().execute();
    }

    /**
     * Slide menu item click listener
     * */

    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            Log.e("NavigationDrawer", "Pressed Option " + (position));
            displayView(position);
        }
    }

    /**
     * Displaying fragment view for selected nav drawer list item
     * */
    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        boolean pressed = false;
        switch (position) {
            case 1:
                fragment = new HomeFragment();
                pressed = true;
                break;
            case 2:
                //fragment = new UpdateFragment();
                break;
            case 3:
                //fragment = new InstitutionFragment();
                break;
            case 4:
                //fragment = new SettingsFragment();
                break;
            case 5:
                //fragment = new AboutFragment();
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, fragment).commit();

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position-1]);
            mDrawerLayout.closeDrawer(mDrawerList);

            if(pressed) {
                new grabEmergencyText().execute();
            }

        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        Drawable refreshDrawable = new IconDrawable(this, Iconify.IconValue.fa_refresh)
                .colorRes(R.color.white)
                .actionBarSize();

        Drawable settingsDrawable = new IconDrawable(this, Iconify.IconValue.fa_cog)
                .colorRes(R.color.white)
                .actionBarSize();

       menu.add(0, MENU_REFRESH, Menu.NONE, R.string.menu_refresh).setIcon(refreshDrawable).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
       menu.add(0, MENU_SETTINGS, Menu.NONE, R.string.menu_settings).setIcon(settingsDrawable).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        int id = item.getItemId();
        if (id == MENU_REFRESH) {
            new grabEmergencyText().execute();
            Toast.makeText(MainActivity.this, "Updating Status", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /***
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        //boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggle
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private class grabEmergencyText extends AsyncTask<Void, Void, Void> {
        String iconText;
        int iconColor;
        String schoolStatus;
        String returnedMessage;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setTitle("Updating Status");
            mProgressDialog.setMessage("Please Wait...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();


        }

        @Override
        protected Void doInBackground(Void...params) {

            try {
                Document doc = Jsoup.connect(RowanUrl).get();
                Elements divs = doc.select(divClassOrID);

                if(divs.isEmpty()) {
                    iconText = "{fa-check-circle}";
                    iconColor = getResources().getColor(R.color.Emerald);
                    schoolStatus = "Rowan University is operating under normal schedule.";
                    returnedMessage = "No emergency message found.";

                } else {
                    iconText = "{fa-exclamation-triangle}";
                    iconColor = getResources().getColor(R.color.GoldYellow);
                    schoolStatus = "Rowan University is closed!.";
                    returnedMessage = divs.text();

                }


            } catch (IOException e) {
                //Toast.makeText(e.getMessage(), Toast.LENGTH_LONG).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Set title into TextView
            TextView displaySchoolStatus = (TextView) findViewById(R.id.tvStatus);
            TextView displayReturnedMessage = (TextView) findViewById(R.id.tvMessage);
            IconTextView displayIconText = (IconTextView) findViewById(R.id.tvIcon);

            displayIconText.setText(iconText);
            displayIconText.setTextColor(iconColor);
            displaySchoolStatus.setText(schoolStatus);
            displaySchoolStatus.setTextColor(iconColor);
            displayReturnedMessage.setText(returnedMessage);
            //Toast.makeText(MainActivity.this, emgcyText, Toast.LENGTH_LONG);
            mProgressDialog.dismiss();
        }

    }
}

