package com.mylivn.imagetestassignement.task;

import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.animation.DecelerateInterpolator;
import com.bel.lampa.CircleImageView;
import com.bel.lampa.CustomProgressBar;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;

public class ImageDownloader extends AsyncTask<String, Integer, Bitmap> {

    private final CircleImageView avatarView;
    private final int loader;
    private final Context context;
    private final CustomProgressBar mProgressBar;
    private ProgressDialog simpleWaitDialog;
    private ObjectAnimator anim;

    public ImageDownloader(Context context, CircleImageView avatarView, int loader, CustomProgressBar mProgressBar) {
        this.avatarView = avatarView;
        this.loader = loader;
        this.context = context;
        this.mProgressBar = mProgressBar;
        initAnim();
    }

    private void initAnim() {
        anim = ObjectAnimator.ofInt(mProgressBar, "progress", 0, 100);
        //anim.setDuration(15000);
        anim.setInterpolator(new DecelerateInterpolator());

    }

    @Override
    protected Bitmap doInBackground(String... param) {
        // TODO Auto-generated method stub
        String image_url = param[0];

        return downloadBitmap(image_url);
    }


    @Override
    protected void onPreExecute() {
        Log.i("Async-Example", "onPreExecute Called");

        avatarView.setImageResource(loader);
        anim.start();

    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // mProgressBar.setProgress(values[0],true);
            mProgressBar.setProgress(values[0]);
        }
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        Log.i("Async-Example", "onPostExecute Called");
        avatarView.setImageBitmap(result);
        //   simpleWaitDialog.dismiss();

        // anim.cancel();

    }


    private Bitmap downloadBitmap(String url) {
        // certification SSL


        // initilize the default HTTP client object
        final DefaultHttpClient client = new DefaultHttpClient();

        //forming a HttoGet request
        final HttpGet getRequest = new HttpGet(url);
        try {

            HttpResponse response = client.execute(getRequest);

            //check 200 OK for success
            final int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode != HttpStatus.SC_OK) {
                Log.i("ImageDownloader", "Error " + statusCode +
                        " while retrieving bitmap from " + url);
                return null;

            }

            final HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream inputStream = null;
                try {
                    // getting contents from the stream
                    inputStream = entity.getContent();

                    // decoding stream data back into image Bitmap that android understands
                    final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                    return bitmap;
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    entity.consumeContent();
                }
            }
        } catch (Exception e) {
            // You Could provide a more explicit error message for IOException
            getRequest.abort();
            Log.i("ImageDownloader", "Something went wrong while" +
                    " retrieving bitmap from " + url + e.toString());
        }

        return null;
    }
}
