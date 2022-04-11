package com.example.shibushi.Outfits;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shibushi.R;

import java.util.List;

public class OutfitParentAdapter extends RecyclerView.Adapter<OutfitParentAdapter.OutfitParentViewHolder>{

    Context context;
    LayoutInflater inflater;
    List<OutfitChildModel.ChildDataSource> datas;

    public OutfitParentAdapter(Context context, List<OutfitChildModel.ChildDataSource> datas) {
        this.context = context;
        this.datas = datas;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public OutfitParentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View outfitParentView = inflater.inflate(R.layout.activity_viewoutfits_parent_main, parent, false);
        return new OutfitParentViewHolder(outfitParentView);
    }

    @Override
    public void onBindViewHolder(@NonNull OutfitParentViewHolder holder, int position) {

        OutfitChildAdapter outfitChildAdapter = new OutfitChildAdapter(context, datas.get(position));

        LinearLayoutManager parentlinearLayoutManager = new LinearLayoutManager(context);
        parentlinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        Log.i("onBindparentVH",holder.toString());
//        Log.i("onBindparentVH",holder.outfitParentRV.toString());
        holder.outfitParentRV.setLayoutManager(parentlinearLayoutManager);
        holder.outfitParentRV.setAdapter(outfitChildAdapter);
        holder.outfitParentRV.setVisibility(View.VISIBLE);
        holder.categoryTextView.setText(datas.get(position).get(position).name);
    }

    @Override
    public int getItemCount() {
        return this.datas.size();
    }

    public static class OutfitParentViewHolder extends RecyclerView.ViewHolder{
        RecyclerView outfitParentRV;
        TextView categoryTextView;
        OutfitParentViewHolder(View view){
            super(view);
            categoryTextView = view.findViewById(R.id.categoryTextView);
            outfitParentRV = view.findViewById(R.id.outfitParentRecyclerView);


        }
    }
}
