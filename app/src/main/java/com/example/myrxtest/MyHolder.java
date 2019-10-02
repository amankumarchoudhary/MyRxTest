package com.example.myrxtest;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyHolder extends RecyclerView.ViewHolder {

    TextView textView;

    public MyHolder(@NonNull View itemView) {
        super(itemView);

        textView=itemView.findViewById(R.id.textview);
    }
}
