package com.example.snapchatclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChooseUserActivity extends AppCompatActivity {

    ListView chooseUserListView;
    ArrayList<String> emails=new ArrayList<String>();
    ArrayList<String> keys=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_user);
        chooseUserListView= findViewById(R.id.chooseUserListView);

        final ArrayAdapter adapter= new ArrayAdapter(this,android.R.layout.simple_list_item_1,emails);
        chooseUserListView.setAdapter(adapter);
        FirebaseDatabase.getInstance().getReference().child("users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
               String email= dataSnapshot.child("email").getValue().toString();
               emails.add(email);
               keys.add(dataSnapshot.getKey());
               adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        chooseUserListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String,String> HashMap=new HashMap<String, String>();
                HashMap.put("from", FirebaseAuth.getInstance().getCurrentUser().getEmail());
                HashMap.put("imageName", getIntent().getStringExtra("imageName"));
                HashMap.put("imageURL",getIntent().getStringExtra("imageURL"));
                HashMap.put("message",getIntent().getStringExtra("message"));

                FirebaseDatabase.getInstance().getReference().child("users").child(keys.get(position)).child("snaps").push().setValue(HashMap);

                Intent intent=new Intent(ChooseUserActivity.this,SnapsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}
