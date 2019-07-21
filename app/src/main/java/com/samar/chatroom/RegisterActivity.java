package com.samar.chatroom;


import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.samar.chatroom.data.model.UserModel;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.victor.loading.rotate.RotateLoading;


public class RegisterActivity extends AppCompatActivity {

    private MaterialRippleLayout more;
    private Spinner spinner3;

    ImageView profile;
    Uri photoPath;

    ProgressDialog progressDialog;

    RotateLoading rotateLoading;

    private FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    EditText first_name_field, last_name_field, email_field, password_field, mobile_field;
    TextView login;
    String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        first_name_field = findViewById(R.id.first_name);
        last_name_field = findViewById(R.id.last_name);
        email_field = findViewById(R.id.email);
        password_field = findViewById(R.id.password);
        mobile_field = findViewById(R.id.mobile);
        login = findViewById(R.id.login);

        profile = findViewById(R.id.profile_picture);

        more = findViewById(R.id.more);
        spinner3 = findViewById(R.id.spinner3);
        
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        databaseReference.keepSynced(true);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child("images");

        String first_name, last_name, email, password, mobile_number;
        first_name = first_name_field.getText().toString();
        last_name = last_name_field.getText().toString();
        email = email_field.getText().toString();
        password = password_field.getText().toString();
        mobile_number = mobile_field.getText().toString();

        ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.City));
        arrayAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(arrayAdapter3);
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                city = String.valueOf(adapterView.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        login.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(),Login.class);
            startActivity(intent);
        });

       more.setOnClickListener(view -> {

           if (TextUtils.isEmpty(first_name)) {
                Toast.makeText(getApplicationContext(), "Please enter first name...", Toast.LENGTH_LONG).show();
                return;
            }
            if (TextUtils.isEmpty(last_name)) {
                Toast.makeText(getApplicationContext(), "Please enter last name!", Toast.LENGTH_LONG).show();
                return;
            }

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getApplicationContext(), "Please enter email...", Toast.LENGTH_LONG).show();
                return;
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(getApplicationContext(), "Please enter password!", Toast.LENGTH_LONG).show();
                return;
            }

            if (TextUtils.isEmpty(mobile_number)) {
                Toast.makeText(getApplicationContext(), "Please enter mobile number...", Toast.LENGTH_LONG).show();
                return;
            }
            if (photoPath == null) {
                Toast.makeText(getApplicationContext(), "please add your picture", Toast.LENGTH_SHORT).show();
                return;
            }

            progressDialog = new ProgressDialog(RegisterActivity.this);
            progressDialog.setTitle("Register");
            progressDialog.setMessage("Please Wait Until Creating Account ...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
            progressDialog.setCancelable(false);

           rotateLoading.start();

           CreateAccount(email,password,first_name,last_name);

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
                            .into(profile);
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
            {
                Exception error = result.getError();
            }
        }
    }

    private void uploadImage(final String firstName,final String lastName, final String email)
    {
        UploadTask uploadTask;

        final StorageReference ref = storageReference.child("images/" + photoPath.getLastPathSegment());

        uploadTask = ref.putFile(photoPath);

        Task<Uri> urlTask = uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful())
            {
                throw task.getException();
            }

            // Continue with the task to get the download URL
            return ref.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            Uri downloadUri = task.getResult();

            String imageUrl = downloadUri.toString();

            AddUser(firstName,lastName,email,imageUrl);
            progressDialog.dismiss();

            Intent intent = new Intent(getApplicationContext(), MoreInfo.class);
            startActivity(intent);
            Toast.makeText(getApplicationContext(), "successfully", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(exception -> {
            // Handle unsuccessful uploads
            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        });
    }
    private void AddUser(String first_name, String last_name,String email, String imageurl)
    {
        UserModel userModel = new UserModel(first_name,last_name,email,null,null,null,null,null,null,null,null, imageurl);

        databaseReference.child("Users").child(getUID()).setValue(userModel);
    }

    private void CreateAccount(String email, String password, String firstName, String lastName)
    {
        email = email_field.getText().toString();
        firstName = first_name_field.getText().toString();
        lastName = last_name_field.getText().toString();

        String finalFirstName = firstName;
        String finalEmail = email;
        String finalLastName = lastName;
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                    {
                        uploadImage(finalFirstName, finalLastName, finalEmail);
                    } else
                    {
                        String error_message = task.getException().getMessage();
                        Toast.makeText(getApplicationContext(), error_message, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
    }

    private String getUID()
    {
        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        return id;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home :
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    }