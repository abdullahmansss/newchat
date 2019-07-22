package softagi.ss.chatroom;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.samar.chatroom.R;

import softagi.ss.chatroom.data.model.UserModel;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity
{
    private MaterialRippleLayout more;
    private Spinner spinner3;

    private Spinner spinner1,spinner2;
    String gender,blood_type,dis;
    public static TextView age , last_donate;

    static String age_txt = "",last_txt = "";

    CircleImageView profile;
    Uri photoPath;
    static int i = 0;

    ProgressDialog progressDialog;

    private FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    EditText first_name_field, last_name_field, email_field, password_field, mobile_field,disease_field;
    TextView login;
    String city;

    String first_name, last_name, email, password, mobile_number;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        first_name_field = findViewById(R.id.first_name);
        last_name_field = findViewById(R.id.last_name);
        email_field = findViewById(R.id.email);
        password_field = findViewById(R.id.password);
        mobile_field = findViewById(R.id.mobile);
        login = findViewById(R.id.login);
        disease_field = findViewById(R.id.disease_field);

        profile = findViewById(R.id.profile_picture);

        more = findViewById(R.id.more);
        spinner3 = findViewById(R.id.spinner3);

        age = findViewById(R.id.age_txt);
        last_donate = findViewById(R.id.donate_txt);

        spinner1 = findViewById(R.id.spinner1);
        spinner2 = findViewById(R.id.spinner2);

        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.gender));
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(arrayAdapter1);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                gender = String.valueOf(adapterView.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.blood_type));
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(arrayAdapter2);

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                blood_type = String.valueOf(adapterView.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        profile.setOnClickListener(view ->
        {
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON_TOUCH)
                    .setAspectRatio(1,1)
                    .start(RegisterActivity.this);
        });

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        databaseReference.keepSynced(true);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child("images");

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

       more.setOnClickListener(view ->
       {
           first_name = first_name_field.getText().toString();
           last_name = last_name_field.getText().toString();
           email = email_field.getText().toString();
           password = password_field.getText().toString();
           mobile_number = mobile_field.getText().toString();
           dis = disease_field.getText().toString();

           if (TextUtils.isEmpty(first_name))
           {
                Toast.makeText(getApplicationContext(), "Please enter first name...", Toast.LENGTH_LONG).show();
                return;
            }

            if (TextUtils.isEmpty(last_name))
            {
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

           if (city.equals("Select your city"))
           {
               Toast.makeText(this, "Select address firstly", Toast.LENGTH_SHORT).show();
               return;
           }

           if (gender.equals("Select gender"))
           {
               Toast.makeText(this, "Select gender firstly", Toast.LENGTH_SHORT).show();
               return;
           }

           if(blood_type.equals("Select your blood type"))
           {
               Toast.makeText(this, "Select your blood type firstly", Toast.LENGTH_SHORT).show();
               return;
           }

           if (age_txt.length() == 0)
           {
               Toast.makeText(this, "Select your birth date", Toast.LENGTH_SHORT).show();
               return;
           }

           if (last_txt.length() == 0)
           {
               Toast.makeText(this, "Select your last donation date", Toast.LENGTH_SHORT).show();
               return;
           }

           if (dis.isEmpty())
           {
               Toast.makeText(this, "Enter your previous diseases", Toast.LENGTH_SHORT).show();
               return;
           }

            progressDialog = new ProgressDialog(RegisterActivity.this);
            progressDialog.setTitle("Register");
            progressDialog.setMessage("Please Wait Until Creating Account ...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
            progressDialog.setCancelable(false);

           CreateAccount(first_name + " " + last_name , email,password,mobile_number,city,gender,blood_type,age_txt,last_txt,dis);
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

    private void uploadImage(String fullname,String email,String mobile, String city,String gender, String type, String age , String last , String dis)
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

            AddUser(fullname,email,mobile,city,gender,type,age,last,dis,imageUrl);
        }).addOnFailureListener(exception -> {
            // Handle unsuccessful uploads
            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        });
    }

    private void AddUser(String fullname,String email,String mobile, String city,String gender, String type, String age , String last , String dis, String image)
    {
        UserModel userModel = new UserModel(getUID(),fullname,email,mobile,city,gender,type,age,last,dis,image);
        databaseReference.child("Users").child(getUID()).setValue(userModel);
        progressDialog.dismiss();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    private void CreateAccount(String fullname,String email,String password,String mobile, String city,String gender, String type, String age , String last , String dis)
    {
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                    {
                        uploadImage(fullname,email,mobile,city,gender,type,age,last,dis);
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

    public void openBirthDialog(View view)
    {
        i = 1;
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void openDateDialog(View view)
    {
        i = 2;
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener
    {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState)
        {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day)
        {
            // Do something with the date chosen by the user
            String d = String.valueOf(day);
            int mon = month + 1;
            String m = String.valueOf(mon);
            String y = String.valueOf(year);

            if (i == 1)
            {
                age.setText(d + "/" + m + "/" + y);
                age_txt = d + "/" + m + "/" + y;
            } else if (i == 2)
            {
                last_donate.setText(d + "/" + m + "/" + y);
                last_txt = d + "/" + m + "/" + y;
            }
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null)
        {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
    }
}