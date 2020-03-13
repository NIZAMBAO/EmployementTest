package com.jasbir.communication_channels;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jasbir.communication_channels.Adapter.SampleAdapter;
import com.jasbir.communication_channels.model.SamplePojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AddRelatedList extends AppCompatActivity {
    private Button button;
    private EditText editText;
    private Button create_own,connect_multiple;
    private RecyclerView recyclerView;
    private SampleAdapter sampleAdapter;
    private DatabaseReference reference;
    private List<SamplePojo> mList;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_related_list);

        mList = new ArrayList<>();

        editText = findViewById(R.id.edit_1);

        //readchannel
        readChannel();

        //search user
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                searchUser(charSequence.toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        connect_multiple = findViewById(R.id.connect_multiple);
        create_own = findViewById(R.id.create);

        //opens dialog for create own
        create_own.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opendialog();

            }
        });


        connect_multiple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });








    }
        // searching channels
    private void searchUser(String s) {

        Query query = FirebaseDatabase.getInstance().getReference("sample").orderByChild("name")
                .startAt(s)
                .endAt(s+"\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    SamplePojo samplePojo = snapshot.getValue(SamplePojo.class);

                    mList.add(samplePojo);

                }

                sampleAdapter = new SampleAdapter(getApplicationContext(),mList);
                recyclerView.setAdapter(sampleAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void opendialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddRelatedList.this);

        View v = getLayoutInflater().inflate(R.layout.dialog_layout, null);

        editText = v.findViewById(R.id.create_yourown);
        button = v.findViewById(R.id.submit_button);


        builder.setView(v);
        AlertDialog dialog = builder.create();
        dialog.show();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

                HashMap<String,Object> hashMap = new HashMap<>();



                String s = editText.getText().toString();
                hashMap.put("name",s);

                reference.child("Related_channel").push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    }
                });






            }
        });


    }

    private void readChannel(){


        reference = FirebaseDatabase.getInstance().getReference("sample");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    SamplePojo samplePojo = snapshot.getValue(SamplePojo.class);

                    mList.add(samplePojo);

                }

                sampleAdapter = new SampleAdapter(getApplicationContext(),mList);
                recyclerView.setAdapter(sampleAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
