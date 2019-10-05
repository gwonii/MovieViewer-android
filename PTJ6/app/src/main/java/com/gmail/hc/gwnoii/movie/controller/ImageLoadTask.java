package com.gmail.hc.gwnoii.movie.controller;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.net.URL;

public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

    private String urlStr = null;
    private byte[] imageData = null;
    private ImageView imageView;

    public ImageLoadTask(String url, ImageView imageView) {
        this.urlStr = url;
        this.imageView = imageView;
    }

    public ImageLoadTask(byte[] imageData,ImageView imageView) {
        this.imageData = imageData;
        this.imageView = imageView;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Bitmap doInBackground(Void... voids) {
        Bitmap bitmap = null;

        try {
            URL url = new URL(urlStr);
            bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);

        imageView.setImageBitmap(bitmap);
        imageView.invalidate();
    }

}
