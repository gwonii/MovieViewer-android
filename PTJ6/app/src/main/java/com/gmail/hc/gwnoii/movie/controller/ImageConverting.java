package com.gmail.hc.gwnoii.movie.controller;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Handler;

public class ImageConverting extends AsyncTask<Void, Void, byte[]> {

    private String urlStr = null;
    private byte[] imageData = null;

    public ImageConverting(String url) {
        this.urlStr = url;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected byte[] doInBackground(Void... voids) {
        Bitmap bitmap = null;

        if (urlStr != null) {

            try {
                URL url = new URL(urlStr);
                bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        assert bitmap != null;
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);

        return outputStream.toByteArray();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

}
