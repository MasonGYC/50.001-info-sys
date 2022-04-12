package com.example.shibushi.Outfits;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shibushi.Models.cClothing;
import com.example.shibushi.R;
import com.example.shibushi.Wardrobe.UtilsFetchBitmap;
import com.example.shibushi.Wardrobe.imageAdapter;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SingleOutfitAdapter extends RecyclerView.Adapter<SingleOutfitAdapter.SingleOutfitViewHolder>{

    Context context;
    LayoutInflater inflater;
    ArrayList<cClothing> datasource;


    public SingleOutfitAdapter(Context context, ArrayList<cClothing> datasource) {
        this.context = context;
        this.datasource = datasource;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public SingleOutfitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View contactView = inflater.inflate(R.layout.singleoutfit_cardview, parent, false);
        return new SingleOutfitViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleOutfitViewHolder holder, int position) {
        // imga_name -> uri
        String url_s = this.datasource.get(position).getImg_name();

        // bind image
        Log.i("imgname",this.datasource.get(position).getImg_name());

        ExecutorService executor;
        executor = Executors.newSingleThreadExecutor();
        final Handler handler = new Handler(Looper.myLooper());
        executor.execute(() -> {
            final imageAdapter.Container<Bitmap> cBitmap = new imageAdapter.Container<>();
            final imageAdapter.Container<String> cUri = new imageAdapter.Container<>();
            try {
                URL url = new URL(url_s);
                Bitmap bitmap = UtilsFetchBitmap.getBitmap(url);
                cBitmap.set(bitmap);
                cUri.set(url_s);
            } catch (IOException e) {
                e.printStackTrace();
            }

            handler.post(() -> {
                if (cBitmap.get() != null) {
                    Picasso.get().load(cUri.get()).resize(600,600).centerCrop().into(holder.outfitImageView);
                    holder.outfitImageView.setMaxWidth(600);
                    Log.i("post_imagename",cUri.get());
                    executor.shutdown();
                }
            });
        });



    }

    @Override
    public int getItemCount() {
        return this.datasource.size();
    }

    public static class SingleOutfitViewHolder extends RecyclerView.ViewHolder{
        ImageView outfitImageView;
        SingleOutfitViewHolder(View view){
            super(view);
            outfitImageView = view.findViewById(R.id.singleoutfitImageview);

        }
    }
}

