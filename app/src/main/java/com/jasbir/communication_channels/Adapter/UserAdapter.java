package com.jasbir.communication_channels.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jasbir.communication_channels.R;
import com.jasbir.communication_channels.model.Related_ch;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.Viewholder> {

    private Context mContext;
    private List<Related_ch> mUsers;
    private List<String> Userid;

    public UserAdapter(Context mContext, List<Related_ch> mUsers) {
        this.mContext = mContext;
        this.mUsers = mUsers;
        Userid = new ArrayList<>();
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_item, parent, false);

        return new UserAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, final int position) {

        Related_ch related_ch = mUsers.get(position);

        holder.textView.setText(related_ch.getName());
        holder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // deleting itmes from related channel

                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Related_channel");

                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot==null) return;

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                            Userid.add(snapshot.getKey());

                        }
                        reference.child(Userid.get(position)).removeValue();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        } );





    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{

        public TextView textView;
        public TextView icon;

        public Viewholder(@NonNull View itemView) {

            super(itemView);
            textView = itemView.findViewById(R.id.textview);
            icon = itemView.findViewById(R.id.close);


        }
    }
}
