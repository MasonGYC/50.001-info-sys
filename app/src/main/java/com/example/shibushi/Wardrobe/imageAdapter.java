package com.example.shibushi.Wardrobe;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.shibushi.R;
import com.nostra13.universalimageloader.utils.ImageSizeUtils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class imageAdapter extends RecyclerView.Adapter<imageAdapter.imageViewHolder> {
    Context context;
    LayoutInflater inflater;
    Model.DataSource dataSource;
    int width;

    public imageAdapter(Context context, Model.DataSource dataSource, int width){
        this.width = width;
        this.context = context;
        this.dataSource = dataSource;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public imageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View imgView = inflater.inflate(R.layout.card_wd_image_layout, parent, false);
        return new imageViewHolder(imgView, width);
    }

    @Override
    public void onBindViewHolder(@NonNull imageViewHolder holder, int position) {
        String url_s = this.dataSource.get(position).url;

        ExecutorService executor;
        executor = Executors.newSingleThreadExecutor();
        final Handler handler = new Handler(Looper.myLooper());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                final Container<Bitmap> cBitmap = new Container<Bitmap>();
                final Container<String> cUri = new Container<>();
                try {
                    URI uri = new URI(url_s);
                    URL url = new URL(url_s);
                    Bitmap bitmap = UtilsFetchBitmap.getBitmap(url);
                    cBitmap.set(bitmap);
                    cUri.set(url_s);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (cBitmap.get() != null) {
                            Picasso.get().load(cUri.get()).into(holder.imageView);
//                            holder.imageView.setImageBitmap(cBitmap.get());
                            holder.imageView.setMaxWidth(width);
//                            holder.imageView.setMaxHeight(width);
//                            holder.imageViewLayout.setMinimumHeight(width);
//                            holder.imageViewLayout.setMinimumWidth(width);
                            executor.shutdown();
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.dataSource.count();
    }

    public static class imageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        LinearLayout imageViewLayout;
        public imageViewHolder(@NonNull View itemView, int width) {
            super(itemView);
            imageView = itemView.findViewById(R.id.wardrobe_image);
//            imageViewLayout = itemView.findViewById(R.id.wardrobe_image);
//            imageView.setMinimumWidth(width);
//            imageView.setMaxWidth(width);
        }
    }

    class Container<T>{
        T value;
        Container(){this.value=null;}
        void set(T x){this.value=x;}
        T get(){return this.value;}
    }
}