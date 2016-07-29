package app.vick.com.volleysample;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


/**
 * @author vikas.grover
 *         Volley Application Class
 */
public class VolleyApplication extends Application {

    public static final String TAG = VolleyApplication.class.getSimpleName();
    private static VolleyApplication mVolleyApplication;
    private RequestQueue mRequestQueue;


    @Override
    public void onCreate() {
        super.onCreate();
        mVolleyApplication = this;
    }

    //return application instance, thread safe
    public static synchronized VolleyApplication getInstance() {
        return mVolleyApplication;
    }


    //get request queue
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }


    //add request to queue
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    //add request to queue
    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    //cancel pending request
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

}


