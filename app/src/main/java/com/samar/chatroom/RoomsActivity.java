package com.samar.chatroom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;
import com.samar.chatroom.data.model.RoomModel;
import com.samar.chatroom.data.model.UserModel;

import java.util.ArrayList;
import java.util.List;


public class RoomsActivity extends AppCompatActivity {

    Toolbar toolbar;
    FloatingActionButton addRoom;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    List<RoomModel> list;
    PresentAdapter presentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rooms);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Return");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        list = new ArrayList<>();

        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        presentAdapter = new PresentAdapter(list);

        recyclerView.setAdapter(presentAdapter);
        addRoom.setOnClickListener(view -> {

            Intent intent = new Intent(getApplicationContext(),friendActivity.class);
            startActivity(intent);
            presentAdapter.notifyDataSetChanged();

        });



    }

    public class PresentAdapter extends RecyclerView.Adapter<PresentAdapter.PresentViewHolder>
    {
        List<RoomModel> list;

        PresentAdapter(List<RoomModel> list)
        {
            this.list = list;
        }

        @NonNull
        @Override
        public PresentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
        {
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.room_item, viewGroup, false);
            return new PresentViewHolder(view);
        }


        public void onBindViewHolder(@NonNull PresentViewHolder presentViewHolder, int i)
        {
            final String room_name = list.get(i).getName();
            final String image = list.get(i).getImageURL();

            presentViewHolder.room_name.setText(room_name);
            presentViewHolder.room_image.setImageURI(Uri.parse(image));
        }

        public int getItemCount()
        {
            return list.size();
        }

        class PresentViewHolder extends RecyclerView.ViewHolder
        {
            TextView room_name;
            ImageView room_image;

            PresentViewHolder(@NonNull View itemView)
            {
                super(itemView);

                room_name = itemView.findViewById(R.id.room_name);
                room_image = itemView.findViewById(R.id.room_image);
            }
        }
    }
}


