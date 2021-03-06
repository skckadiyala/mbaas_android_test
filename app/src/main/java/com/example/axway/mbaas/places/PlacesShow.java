/**
 * Axway Platform SDK
 * Copyright (c) 2017 by Axway, Inc. All Rights Reserved.
 * Proprietary and Confidential - This source code is not for redistribution
 */

package com.example.axway.mbaas.places;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.axway.mbaas_preprod.SdkClient;
import com.axway.mbaas_preprod.SdkException;
import com.axway.mbaas_preprod.apis.PlacesAPI;
import com.example.axway.mbaas.R;
import com.example.axway.mbaas.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class PlacesShow extends Activity {
	private static PlacesShow currentActivity;
	private String placeId;

	private TextView textView;
	private Button removeButton1;
	private Button updateButton2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.places_show);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		currentActivity = this;

		textView = (TextView) findViewById(R.id.places_show_text_view);
		textView.setMovementMethod(new ScrollingMovementMethod());

		Intent intent = getIntent();
		placeId = intent.getStringExtra("place_id");

		removeButton1 = (Button) findViewById(R.id.places_show_remove_button1);
		removeButton1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(currentActivity, PlacesRemove.class);
				intent.putExtra("place_id", placeId);
				startActivity(intent);
			}
		});

		updateButton2 = (Button) findViewById(R.id.places_show_update_button2);
		updateButton2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(currentActivity, PlacesUpdate.class);
				intent.putExtra("place_id", placeId);
				startActivity(intent);
			}
		});
        new showPlaces().execute();

	}

    private class showPlaces extends AsyncTask<Void, Void, JSONObject> {
        HashMap<String, Object> data = new HashMap<String, Object>();

        private SdkException exceptionThrown = null;
        JSONObject successResponse;

        @Override
        protected JSONObject doInBackground(Void... voids) {

            try {
                successResponse = new PlacesAPI(SdkClient.getInstance()).placesShow(placeId, null, null, null);
            } catch (SdkException e) {
                exceptionThrown = e;
            }
            return successResponse;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            if (exceptionThrown == null) {
                try {
                    if (exceptionThrown == null && successResponse.getJSONObject("meta").get("status").toString().equalsIgnoreCase("ok")) {
                        textView.setText(successResponse.toString(4));

                    } else {
                        Utils.handleSDKException(exceptionThrown, currentActivity);
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

//    // Create dictionary of parameters to be passed with the request
//    HashMap<String, Object> data = new HashMap<String, Object>();
//		data.put("place_id", placeId);
//
//		try {
//        APSPlaces.show(data, new APSResponseHandler() {
//
//            @Override
//            public void onResponse(final APSResponse e) {
//                if (e.getSuccess()) {
//                    try {
//                        textView.setText(new JSONObject(e.getResponseString()).toString(4));
//                    } catch (APSCloudException e1) {
//                        Utils.handleException(e1, currentActivity);
//                    } catch (JSONException e1) {
//                        Utils.handleException(e1, currentActivity);
//                    }
//                } else {
//                    Utils.handleErrorInResponse(e, currentActivity);
//                }
//            }
//
//            @Override
//            public void onException(APSCloudException e) {
//                Utils.handleException(e, currentActivity);
//            }
//
//        });
//    } catch (APSCloudException e) {
//        Utils.handleException(e, currentActivity);
//    }

    @Override
	protected void onDestroy() {
		currentActivity = null;
		super.onDestroy();
	}
}


