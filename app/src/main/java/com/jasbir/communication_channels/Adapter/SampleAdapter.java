package com.jasbir.communication_channels.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jasbir.communication_channels.R;
import com.jasbir.communication_channels.model.SamplePojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SampleAdapter extends RecyclerView.Adapter<SampleAdapter.Viewholder> {

    private Context mContext;
    private List<SamplePojo> mList;


    public SampleAdapter(Context mContext, List<SamplePojo> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.samplelist_channelitem,parent,false);

        return  new SampleAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, final int position) {

        holder.checkBox.setText(mList.get(position).getName());
       holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
               if(b){
                   DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Related_channel");

                   HashMap<String,Object> hashMap = new HashMap<>();
                   hashMap.put("name",mList.get(position).getName());
                   reference.push().setValue(hashMap);
               }
           }
       });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        public TextView textView;
        public CheckBox checkBox;

        public Viewholder(@NonNull View itemView) {
            super(itemView);


            checkBox = itemView.findViewById(R.id.checkbox);
        }
    }
}
