package softagi.ss.chatroom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.samar.chatroom.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import softagi.ss.chatroom.Fragments.MyRooms;
import softagi.ss.chatroom.data.model.ChatModel;
import softagi.ss.chatroom.data.model.RoomModel;
import softagi.ss.chatroom.data.model.UserModel;

public class ChatActivity extends AppCompatActivity
{
    String ROOM;
    EditText chat_field;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    //FirebaseRecyclerAdapter<ChatModel, chatViewHolder> firebaseRecyclerAdapter;
    List<ChatModel> chatModels;

    String name,id,image,i;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ROOM = getIntent().getStringExtra("room_key");

        chat_field = findViewById(R.id.chat_field);
        recyclerView = findViewById(R.id.recyclerview);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        databaseReference.keepSynced(true);

        i = FirebaseAuth.getInstance().getCurrentUser().getUid();
        getData(i);
        layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        chatModels = new ArrayList<>();

        displayChats(ROOM);
    }

    private void displayChats(String room)
    {
        databaseReference.child("ChatDetails").child(room).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    ChatModel chatModel = dataSnapshot1.getValue(ChatModel.class);
                    chatModels.add(chatModel);
                }

                chatAdapter chatAdapter = new chatAdapter(chatModels);
                recyclerView.setAdapter(chatAdapter);
                recyclerView.scrollToPosition(chatModels.size() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

    public void sendchat(View view)
    {
        String m = chat_field.getText().toString();

        if (!m.isEmpty())
        {
            ChatModel chatModel = new ChatModel(m,id,image,name);
            String key = databaseReference.child("ChatDetails").child(ROOM).push().getKey();
            databaseReference.child("ChatDetails").child(ROOM).child(key).setValue(chatModel);

            chat_field.setText("");
        }
    }

    public void getData(String userID)
    {
        databaseReference.child("Users").child(userID).addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                UserModel userModel = dataSnapshot.getValue(UserModel.class);

                name = userModel.getFullname();
                id = userModel.getUserid();
                image = userModel.getImageUrl();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

    /*private void displayChat(String room)
    {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("ChatDetails")
                .child(room)
                .limitToLast(50);

        FirebaseRecyclerOptions<ChatModel> options =
                new FirebaseRecyclerOptions.Builder<ChatModel>()
                        .setQuery(query, ChatModel.class)
                        .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ChatModel, chatViewHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull final chatViewHolder holder, int position, @NonNull final ChatModel model)
            {
                final String key = getRef(position).getKey();

                holder.BindPlaces(model);
            }

            @NonNull
            @Override
            public chatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.chat_item, parent, false);
                return new chatViewHolder(view);
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }*/

    class chatAdapter extends RecyclerView.Adapter<chatAdapter.chatViewHolder>
    {
        List<ChatModel> list;

        chatAdapter(List<ChatModel> list)
        {
            this.list = list;
        }

        @NonNull
        @Override
        public chatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.chat_item, parent, false);
            return new chatViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull chatViewHolder holder, int position)
        {
            ChatModel chatModel = list.get(position);
            holder.BindPlaces(chatModel);
        }

        @Override
        public int getItemCount()
        {
            return list.size();
        }

        class chatViewHolder extends RecyclerView.ViewHolder
        {
            TextView name,chat,name2,chat2;
            CircleImageView circleImageView,circleImageView2;
            LinearLayout my_lin,other_lin;

            chatViewHolder(View itemView)
            {
                super(itemView);

                name = itemView.findViewById(R.id.chat_name);
                name2 = itemView.findViewById(R.id.chat_name2);
                chat = itemView.findViewById(R.id.chat_text);
                chat2 = itemView.findViewById(R.id.chat_text2);
                circleImageView = itemView.findViewById(R.id.chat_image);
                circleImageView2 = itemView.findViewById(R.id.chat_image2);
                my_lin = itemView.findViewById(R.id.my_message_lin);
                other_lin = itemView.findViewById(R.id.other_message_lin);
            }

            void BindPlaces(final ChatModel chatModel)
            {
                /*if (chatModel.getId().equals(i))
                {
                    my_lin.setVisibility(View.VISIBLE);

                    name.setText(chatModel.getName());
                    chat.setText(chatModel.getMessage());
                    Picasso.get()
                            .load(chatModel.getImage())
                            .placeholder(R.drawable.chat)
                            .error(R.drawable.chat)
                            .into(circleImageView);
                } else if (!chatModel.getId().equals(i))
                {
                    other_lin.setVisibility(View.VISIBLE);

                    name2.setText(chatModel.getName());
                    chat2.setText(chatModel.getMessage());
                    Picasso.get()
                            .load(chatModel.getImage())
                            .placeholder(R.drawable.chat)
                            .error(R.drawable.chat)
                            .into(circleImageView2);
                }*/
                my_lin.setVisibility(View.VISIBLE);

                name.setText(chatModel.getName());
                chat.setText(chatModel.getMessage());
                Picasso.get()
                        .load(chatModel.getImage())
                        .placeholder(R.drawable.chat)
                        .error(R.drawable.chat)
                        .into(circleImageView);
            }
        }
    }
}