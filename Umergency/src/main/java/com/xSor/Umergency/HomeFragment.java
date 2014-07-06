package com.xSor.Umergency;

import android.app.Fragment;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

public class HomeFragment extends Fragment implements OnRefreshListener {

    private PullToRefreshLayout mPullToRefreshLayout;
    // Testing

    // Temporary URL/DIV Settings
    //String RowanUrl = "http://rowan.edu/";
    //String divClassOrID = "div#emergency";

    /*String RowanUrl = "http://xsorcreations.com";
    String divClassOrID = "div#copyright > p";*/

    public HomeFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        //new grabEmergencyText().execute();
        //RelativeLayout rLayout = (RelativeLayout) rootView;
        Resources res = getResources(); //resource handle
        Drawable drawable = res.getDrawable(R.drawable.rowan_bg); //new Image that was added to the res folder
        rootView.setBackground(drawable);


        mPullToRefreshLayout = (PullToRefreshLayout) rootView.findViewById(R.id.ptr_layout);
        ActionBarPullToRefresh.from(getActivity())
                .allChildrenArePullable()
                .listener(this)
                .setup(mPullToRefreshLayout);

        // Set background, this slows down the app dramatically..must be fixed.
        //setBackground();

        return rootView;
    }

    @Override
    public void onRefreshStarted(View view) {
        /**
         * Simulate Refresh with 4 seconds sleep
         */
        Toast.makeText(getActivity(), "pulled", Toast.LENGTH_LONG).show();
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(4);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);

                // Notify PullToRefreshLayout that the refresh has finished
                mPullToRefreshLayout.setRefreshComplete();
            }
        }.execute();
    }
  /* protected void setBackground() {
        //ImageView mainBackground = (ImageView) getView().findViewById(R.id.mainBackground);
       //mainBackground.setImageResource(R.drawable.rowan_bg);
        //mainBackground.setScaleType(ImageView.ScaleType.CENTER_CROP);
       RelativeLayout rLayout = (RelativeLayout) getView().findViewById(R.id.mainBackground);
       Resources res = getResources(); //resource handle
       Drawable drawable = res.getDrawable(R.drawable.rowan_bg); //new Image that was added to the res folder
       rLayout.setBackground(drawable);
       //rLayout.setBackgroundDrawable(drawable);
    }*/

   /* private class grabEmergencyText extends AsyncTask<Void, Void, Void> {
        String iconText;
        int iconColor;
        String schoolStatus;
        String returnedMessage;

        /*@Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(getActivity().getApplicationContext());
            mProgressDialog.setTitle("Emergency Text Check");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        /*@Override
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
            TextView displaySchoolStatus = (TextView) getView().findViewById(R.id.tvStatus);
            TextView displayReturnedMessage = (TextView) getView().findViewById(R.id.tvMessage);
            IconTextView displayIconText = (IconTextView) getView().findViewById(R.id.tvIcon);

            displayIconText.setText(iconText);
            displayIconText.setTextColor(iconColor);
            displaySchoolStatus.setText(schoolStatus);
            displaySchoolStatus.setTextColor(iconColor);
            displayReturnedMessage.setText(returnedMessage);
            //Toast.makeText(MainActivity.this, emgcyText, Toast.LENGTH_LONG);
            //mProgressDialog.dismiss();
        }

    }*/

}