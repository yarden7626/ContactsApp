package com.example.contacts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.MyViewHolder> {

    Context context;
    Context context2;
    ArrayList<CardModel> models;

    public CardAdapter(Context context, Context context2, ArrayList<CardModel>  models) {
        this.context = context;
        this.context2=context2;
        this.models = models;
    }


    @NonNull
    @Override
    public CardAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_layout,parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.text.setText(models.get(position).text);
        holder.text2.setText(models.get(position).text2);
        holder.iv.setImageResource(models.get(position).image);

    }

    @Override
    public int getItemCount() {
        return models.size();
    }
    static  public  class  MyViewHolder extends  RecyclerView.ViewHolder{
        TextView text;
        TextView text2;
        ImageView iv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //this is like OnCreate
            text= itemView.findViewById(R.id.textView);
            text2= itemView.findViewById(R.id.textView2);
            iv = itemView.findViewById(R.id.imageView2);
        }
    }
}
