package com.bel.lampa.loader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bel.lampa.AvatarView;
import com.bel.lampa.CircleImageView;
import com.bel.lampa.CustomProgressBar;
import com.bel.lampa.Utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.*;
import java.util.List;

public class Lamp {
    private static Lamp singleton;
    private final Context context;
    private String path;
    private int placeholder;
    private AvatarView avatarView;
    private int error_img;


    private int countCash;
    private int diskCash;
    private int cashedItems;
    private List<String> urls;

    // Get max available VM memory, exceeding this amount will throw an
    // OutOfMemory exception. Stored in kilobytes as LruCache takes an
    // int in its constructor.
    final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    // Use 1/8th of the available memory for this memory cache.
    final int cacheSize = maxMemory / 8;

    // final int cacheSize = diskCash / 8;
    private LruCache<String, Bitmap> memoryCache = new LruCache<String, Bitmap>(cacheSize) {
        @Override
        protected int sizeOf(String key, Bitmap bitmap) {
            // The cache size will be measured in kilobytes rather than
            // number of items.
            return bitmap.getByteCount() / 1024;
        }
    };

    public Lamp(Context context) {
        this.context = context;
    }

    public static Lamp with(Context context) {
        if (singleton == null) {
            synchronized (Lamp.class) {
                if (singleton == null) {
                    singleton = new Lamp(context);
                }
            }
        }
        return singleton;
    }

    public void load(String path) {
        if (Utils.isEmptyString(path)) {
            Toast.makeText(context, "Path must be not empty", Toast.LENGTH_LONG).show();
        } else {
            this.path = path;
        }
    }

    public void load(List<String> urls) {
        this.urls = urls;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public void placeholder(int placeholder) {
        this.placeholder = placeholder;
    }

    public void into(AvatarView avatarView) {
        this.avatarView = avatarView;
    }


    // the method where we launch  the  loading process
    public void rub() {
        if (Utils.isConnected(context)) {
            final String imageKey = "key" + path;

            final Bitmap bitmap = getBitmapFromMemCache(imageKey);
            if (bitmap != null) {
                // if Bitmap  is cashed no need to progress  progress bar
                // Just load bitmap in the Circle ImageView
                avatarView.getCircleImageView().setImageBitmap(bitmap);
            } else {
                new ImageDownloader(context, avatarView.getCircleImageView(), placeholder, avatarView.getProgressBar()).execute(path);
            }
        } else {
            Toast.makeText(context, "Please check your Connectivity to Internet", Toast.LENGTH_SHORT).show();
        }

    }

    public void error(int error_img) {
        this.error_img = error_img;
    }

    public void rub(int index) {
        path = urls.get(index);
        rub();
    }

    public void maxCountCash(int countCash) {
        this.countCash = countCash;
    }

    public void maxDiskCash(int diskCash) {
        memoryCache.resize(diskCash);
    }

    // add bitmap to memory cash using Key string
    private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            memoryCache.put(key, bitmap);
        }
    }

    // get bitmap from memory cash using Key
    public Bitmap getBitmapFromMemCache(String key) {
        return memoryCache.get(key);
    }

    public class ImageDownloader extends AsyncTask<String, Integer, Bitmap> {

        private final CircleImageView circleImageView;

        private final int loader;
        private final Context context;
        private final CustomProgressBar mProgressBar;

        public ImageDownloader(Context context, CircleImageView circleImageView, int loader, CustomProgressBar mProgressBar) {
            this.circleImageView = circleImageView;
            this.loader = loader;
            this.context = context;
            this.mProgressBar = mProgressBar;
        }


        @Override
        protected Bitmap doInBackground(String... param) {
            // TODO Auto-generated method stub
            String image_url = param[0];

            return downloadBitmap(image_url);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Log.i("valuesvalues", "" + values[0]);
            mProgressBar.setProgress(values[0]);

        }

        @Override
        protected void onPostExecute(Bitmap result) {
            circleImageView.setImageBitmap(result);
        }

        @Override
        protected void onPreExecute() {
            //   we add  the placeholder  here
            circleImageView.setImageResource(loader);
            circleImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        }

        private Bitmap drawableToBitmap(Drawable drawable) {
            Bitmap bitmap = null;

            if (drawable instanceof BitmapDrawable) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                if (bitmapDrawable.getBitmap() != null) {
                    return bitmapDrawable.getBitmap();
                }
            }

            if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
                bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth() * 2, drawable.getIntrinsicHeight() * 2, Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        }


        private Bitmap downloadBitmap(String url) {
            // certification SSL


            // initilize the default HTTP client object
            final DefaultHttpClient client = new DefaultHttpClient();

            //forming a HttoGet request
            final HttpGet getRequest = new HttpGet(url);
            try {

                HttpResponse execute = client.execute(getRequest);

                //check 200 OK for success
                final int statusCode = execute.getStatusLine().getStatusCode();

                if (statusCode != HttpStatus.SC_OK) {
                    Log.i("ImageDownloader", "Error " + statusCode +
                            " while retrieving bitmap from " + url);
                    return null;

                }

                final HttpEntity entity = execute.getEntity();
                if (entity != null) {
                    InputStream content = null;
                    String response = "";

                    try {
                        content = entity.getContent();

                        final Bitmap bitmap = BitmapFactory.decodeStream(content);


                        for (int i = 0; i < 101; i++) {
                            publishProgress(i);

                            try {
                                Thread.sleep(20);
                            } catch (InterruptedException ie) {
                                ie.printStackTrace();
                            }
                        }
                        if (cashedItems < countCash) {
                            addBitmapToMemoryCache("key" + path, bitmap);
                            cashedItems++;
                        }
                        return bitmap;
                    } finally {
                        if (content != null) {
                            content.close();
                        }
                        entity.consumeContent();
                    }
                }
            } catch (Exception e) {
                getRequest.abort();
            }

            Drawable drawable = context.getResources().getDrawable(error_img);

            Bitmap b = drawableToBitmap(drawable);
            return b;
        }
    }
}