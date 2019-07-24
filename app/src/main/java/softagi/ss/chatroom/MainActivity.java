package softagi.ss.chatroom;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import softagi.ss.chatroom.Fragments.MyRooms;
import softagi.ss.chatroom.Fragments.RoomsFragment;
import softagi.ss.chatroom.Fragments.UsersFragment;

import com.samar.chatroom.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
{
    TabLayout tabLayout;
    ViewPager viewPager;
    Toolbar toolbar;
    CircleImageView circleImageView;
    FragmentPagerAdapter fragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewpager);
        toolbar = findViewById(R.id.toolbar);
        circleImageView = findViewById(R.id.profile_picture);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager())
        {
            Fragment [] fragments = new Fragment[]
                    {
                            new RoomsFragment(),
                            new UsersFragment(),
                            new MyRooms()
                    };

            String [] names = new String[]
                    {
                            "ROOMS",
                            "USERS",
                            "MY ROOMS"
                    };

            @Override
            public Fragment getItem(int position)
            {
                return fragments [position];
            }

            @Override
            public int getCount()
            {
                return fragments.length;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position)
            {
                return names [position];
            }
        };

        viewPager.setAdapter(fragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.search :
                Toast.makeText(getApplicationContext(), "search clicked", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addroom(View view)
    {
        startActivity(new Intent(getApplicationContext(), AddRoomActivity.class));
    }

    public void profile(View view)
    {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
    }
}
