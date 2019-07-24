package softagi.ss.chatroom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.samar.chatroom.R;

import softagi.ss.chatroom.data.model.RoomModel;
import softagi.ss.chatroom.data.model.UserModel;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddRoomActivity extends AppCompatActivity
{
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    RecyclerView recyclerView;
    EditText room_name_field;
    CircleImageView circleImageView;
    TextView users_number;
    RecyclerView.LayoutManager layoutManager;
    DividerItemDecoration dividerItemDecoration;
    FirebaseRecyclerAdapter<UserModel, usersViewHolder> firebaseRecyclerAdapter;
    List<String> id;
    Uri photoPath;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);

        recyclerView = findViewById(R.id.recyclerview);
        room_name_field = findViewById(R.id.roomname_field);
        users_number = findViewById(R.id.users_number);
        circleImageView = findViewById(R.id.room_image);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        databaseReference.keepSynced(true);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child("images");

        layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        dividerItemDecoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);

        displayusers();

        circleImageView.setOnClickListener(view ->
        {
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON_TOUCH)
                    .setAspectRatio(1,1)
                    .start(AddRoomActivity.this);
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK)
            {
                if (result != null)
                {
                    photoPath = result.getUri();

                    Picasso.get()
                            .load(photoPath)
                            .placeholder(R.drawable.chat)
                            .error(R.drawable.chat)
                            .into(circleImageView);
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
            {
                Exception error = result.getError();
            }
        }
    }

    private void displayusers()
    {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Users")
                .limitToLast(50);

        FirebaseRecyclerOptions<UserModel> options =
                new FirebaseRecyclerOptions.Builder<UserModel>()
                        .setQuery(query, UserModel.class)
                        .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<UserModel, usersViewHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull final usersViewHolder holder, int position, @NonNull final UserModel model)
            {
                final String key = getRef(position).getKey();

                holder.BindPlaces(model);

                //holder.linearLayout.setBackgroundColor(model.isSelected() ? getResources().getColor(R.color.select) : Color.WHITE);

                id = new ArrayList<>();
                holder.linearLayout.setOnClickListener(view ->
                {
                    if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(key))
                    {
                        Toast.makeText(getApplicationContext(),"can't add yourself", Toast.LENGTH_SHORT).show();
                    } else
                        {
                            model.setSelected(!model.isSelected());
                            holder.linearLayout.setBackgroundColor(model.isSelected() ? getResources().getColor(R.color.select) : Color.WHITE);

                            if (model.isSelected())
                            {
                                id.add(model.getUserid());
                            } else if (!model.isSelected())
                            {
                                id.remove(id.size() - 1);
                            }
                            users_number.setText("users : " + id.size());
                        }
                });
            }

            @NonNull
            @Override
            public usersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.user_item, parent, false);
                return new usersViewHolder(view);
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public void doneroom(View view)
    {
        String name = room_name_field.getText().toString();

        if (name.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "enter room name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (photoPath == null)
        {
            Toast.makeText(getApplicationContext(), "select room picture", Toast.LENGTH_SHORT).show();
            return;
        }

        if (id.size() == 0)
        {
            Toast.makeText(getApplicationContext(), "select at least one donor", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog = new ProgressDialog(AddRoomActivity.this);
        progressDialog.setTitle("Create Room");
        progressDialog.setMessage("Please Wait Until Creating Room ...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);

        uploadImage(name);
    }

    private void uploadImage(String name)
    {
        UploadTask uploadTask;

        final StorageReference ref = storageReference.child("images/" + photoPath.getLastPathSegment());

        uploadTask = ref.putFile(photoPath);

        Task<Uri> urlTask = uploadTask.continueWithTask(task ->
        {
            if (!task.isSuccessful())
            {
                throw task.getException();
            }

            // Continue with the task to get the download URL
            return ref.getDownloadUrl();
        }).addOnCompleteListener(task ->
        {
            Uri downloadUri = task.getResult();

            String imageUrl = downloadUri.toString();
            addRoom(name,imageUrl);
        }).addOnFailureListener(exception ->
        {
            // Handle unsuccessful uploads
            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        });
    }

    private void addRoom(String name, String imageUrl)
    {
        RoomModel roomModel = new RoomModel(name,imageUrl,id);

        String key = databaseReference.child("Rooms").push().getKey();
        databaseReference.child("Rooms").child(key).setValue(roomModel);

        for (String i : id)
        {
            databaseReference.child("RoomsChat").child(i).child(key).setValue(roomModel);
        }

        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    public class usersViewHolder extends RecyclerView.ViewHolder
    {
        TextView name;
        CircleImageView circleImageView;
        LinearLayout linearLayout;

        usersViewHolder(View itemView)
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
