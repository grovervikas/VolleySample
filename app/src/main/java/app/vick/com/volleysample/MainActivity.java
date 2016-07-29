package app.vick.com.volleysample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG     =       MainActivity.class.getSimpleName();
    private Button getRestButton;
    public static final int REQUEST_TIMEOUT_MS = 10000;
    private ArrayList<geometry> mArrayList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar =   (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getRestButton   =   (Button)  findViewById(R.id.getRestButton);
        getRestButton.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.getRestButton:{
                final ListenableFuture<JSONObject> getRestaurantListenable = jsonRequestWithGet(ServiceConfig.URL);
                Futures.addCallback(getRestaurantListenable, new FutureCallback<JSONObject>() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        Log.v(TAG,result.toString());
                        mArrayList = new ArrayList<geometry>();
                        try{
                            JSONArray jsonArray = result.getJSONArray("results");
                            for(int i = 0 ;i<jsonArray.length();i++){
                                geometry geometry = new Gson().fromJson(jsonArray.getJSONObject(i).toString(),geometry.class);
                                mArrayList.add(geometry);
                            }
                            Toast.makeText(MainActivity.this,"Data Geted From Service",Toast.LENGTH_SHORT).show();
                            Log.v(TAG,jsonArray.length()+"");
                        }catch (Exception e){
                            Toast.makeText(MainActivity.this,"There is some problem while getting restarorent",Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.v(TAG,t.toString());
                    }
                });
                break;
            }
        }
    }



    // json request with get
    public static ListenableFuture<JSONObject> jsonRequestWithGet(final String url) {

        final SettableFuture<JSONObject> jsonRequestGetSettable = SettableFuture.create();

        JsonObjectRequest jsonObjectRequestWithGet = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                jsonRequestGetSettable.set(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                jsonRequestGetSettable.setException(error);
                Log.v(TAG, "Exception: "+error);
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map headerMap = new HashMap();
                return headerMap;
            }
        };
        jsonObjectRequestWithGet.setPriority(Request.Priority.IMMEDIATE);
        jsonObjectRequestWithGet.setShouldCache(false);
        jsonObjectRequestWithGet.setRetryPolicy(new DefaultRetryPolicy(REQUEST_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyApplication.getInstance().getRequestQueue().add(jsonObjectRequestWithGet);
        return jsonRequestGetSettable;
    }

}
