package softagi.ss.chatroom.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.samar.chatroom.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import softagi.ss.chatroom.ChatActivity;
import softagi.ss.chatroom.data.model.RoomModel;

public class MyRooms extends Fragment
{
    View view;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    DividerItemDecoration dividerItemDecoration;
    FirebaseRecyclerAdapter<RoomModel, roomsViewHolder> firebaseRecyclerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.my_rooms_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerview);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        databaseReference.keepSynced(true);

        layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);

        displayRooms(FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    private void displayRooms(String id)
    {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("RoomsChat")
                .child(id)
                .limitToLast(50);

        FirebaseRecyclerOptions<RoomModel> options =
                new FirebaseRecyclerOptions.Builder<RoomModel>()
                        .setQuery(query, RoomModel.class)
                        .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<RoomModel, roomsViewHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull final roomsViewHolder holder, int position, @NonNull final RoomModel model)
            {
                final String key = getRef(position).getKey();

                holder.BindPlaces(model);

                holder.rippleLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), ChatActivity.class);
                        intent.putExtra("room_key",key);
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public roomsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.room_item, parent, false);
                return new roomsViewHolder(view);
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public class roomsViewHolder extends RecyclerView.ViewHolder
    {
        TextView name;
        CircleImageView circleImageView;
        MaterialRippleLayout rippleLayout;

        roomsViewHolder(View itemView)
        {
            super(itemView);

            name = itemView.findViewById(R.id.room_name);
            circleImageView = itemView.findViewById(R.id.room_image);
            rippleLayout = itemView.findViewById(R.id.room_mrl);
        }

        void BindPlaces(final RoomModel roomModel)
        {
            name.setText(roomModel.getName());

            Picasso.get()
                    .load(roomModel.getImageURL())
                    .placeholder(R.drawable.chat)
                    .error(R.drawable.chat)
                    .into(circleImageView);
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();

        if (firebaseRecyclerAdapter != null)
        {
            firebaseRecyclerAdapter.startListening();
        }
    }

    @Override
    public void onStop()
    {
        super.onStop();

        if (firebaseRecyclerAdapter != null)
        {
            firebaseRecyclerAdapter.stopListening();
        }
    }
}
