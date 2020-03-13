package com.jasbir.communication_channels;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jasbir.communication_channels.Adapter.UserAdapter;
import com.jasbir.communication_channels.model.Related_ch;

import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.util.Objects.hash;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Related_ch> user;
    private UserAdapter userAdapter;
    private TextView add,eye,edit,create;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        if(checkConn()){
            Toast.makeText(this, "No Internet please connect Internet", Toast.LENGTH_SHORT).show();
            
        }

        eye = findViewById(R.id.eye);
        edit = findViewById(R.id.edit);
        create = findViewById(R.id.create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Click on + on related chanel", Toast.LENGTH_SHORT).show();
            }
        });

        // write permission
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "write permission", Toast.LENGTH_SHORT).show();
            }
        });

        // read permision
        eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Read permission", Toast.LENGTH_SHORT).show();
            }
        });

        // add section to add related channel
        add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(),AddRelatedList.class));
            }
        });



        user = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        //  database related channel it contain all the channel that is related to the current channel

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Related_channel");



        // reading the channels in related section

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    Related_ch related_ch = snapshot.getValue(Related_ch.class);
                    user.add(related_ch);
                }

                userAdapter = new UserAdapter(getApplicationContext(),user);
                recyclerView.setAdapter(userAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        samplelistchannel();

    }

    // this is sample_channel list

    private  void  samplelistchannel(){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String,Object> hashMap = new HashMap<>();

        hashMap.put("name","Defaultchannel");



        reference.child("sample").push().setValue(hashMap);




    }

    //for checking network
    public boolean checkConn(){

        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        if(activeNetwork!=null)
            return false;
            // it will show that app is connected to network
        else
            return true;
        
//            Toast.makeText(this, "No internet", Toast.LENGTH_LONG).show();

    }



}
