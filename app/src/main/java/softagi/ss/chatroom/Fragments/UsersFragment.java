package softagi.ss.chatroom.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.samar.chatroom.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import softagi.ss.chatroom.data.model.RoomModel;
import softagi.ss.chatroom.data.model.UserModel;

public class UsersFragment extends Fragment
{
    View view;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    DividerItemDecoration dividerItemDecoration;
    FirebaseRecyclerAdapter<UserModel, roomsViewHolder> firebaseRecyclerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.users_fragment, container, false);
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

        displayRooms();
    }

    private void displayRooms()
    {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Users")
                .limitToLast(50);

        FirebaseRecyclerOptions<UserModel> options =
                new FirebaseRecyclerOptions.Builder<UserModel>()
                        .setQuery(query, UserModel.class)
                        .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<UserModel, roomsViewHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull final roomsViewHolder holder, int position, @NonNull final UserModel model)
            {
                final String key = getRef(position).getKey();

                holder.BindPlaces(model);
            }

            @NonNull
            @Override
            public roomsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.user_item, parent, false);
                return new roomsViewHolder(view);
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public class roomsViewHolder extends RecyclerView.ViewHolder
    {
        TextView name;
        CircleImageView circleImageView;
        LinearLayout linearLayout;

        roomsViewHolder(View itemView)
        {
            super(itemView);

            name = itemView.findViewById(R.id.user_name);
            circleImageView = itemView.findViewById(R.id.user_profile);
            linearLayout = itemView.findViewById(R.id.user_item);
        }

        void BindPlaces(final UserModel userModel)
        {
            name.setText(userModel.getFullname());

            Picasso.get()
                    .load(userModel.getImageUrl())
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
