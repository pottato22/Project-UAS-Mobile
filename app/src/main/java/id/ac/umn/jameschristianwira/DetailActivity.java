package id.ac.umn.jameschristianwira;

import android.app.Notification;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;

public class DetailActivity extends AppCompatActivity {

    NotificationManagerCompat notificationManagerCompat;
    Characters character;
    ImageView photo;
    TextView tvFullname, tvCharname, tvGender, tvBirthday;

    @Override
    protected void onStart() {
        super.onStart();
//        getDataFromExtra();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getDataFromExtra();
        showData();

        FloatingActionButton fab = findViewById(R.id.fab);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_notifications, getApplicationContext().getTheme()));
        } else {
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_notifications));
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeToast("Character will be shown in notification");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
                        sendOnChannel(character.getRealname(), character.getCharName());
                    }
                }, 5000);

                //startService(intent1);
            }
        });
    }

    public void sendOnChannel(String realname, String charname) {
        String title = "Hi, I'm " + realname;
        String message = "I'm playing as " + charname + " in Game of Thrones";

        Notification notification = new NotificationCompat.Builder(getApplicationContext(), Notif.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_one)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();

        notificationManagerCompat.notify(1, notification);
    }

    public void getDataFromExtra() {
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            Characters temp = new Characters(
                    bundle.getString("charname"),
                    bundle.getString("realname"),
                    bundle.getString("birthday"),
                    bundle.getString("gender"),
                    getPhotoFromFile()
            );

            character = temp;
        } else character = null;
    }

    public Bitmap getPhotoFromFile() {
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(
                    getApplicationContext().openFileInput("temp")
            );

            return bitmap;
        } catch (FileNotFoundException e) {
            Log.e("READ FILE", "Read file error message: " + e.getMessage());
        }

        return null;
    }

    public void showData() {
        photo = findViewById(R.id.detail_photo);
        tvFullname = findViewById(R.id.detail_fullname);
        tvCharname = findViewById(R.id.detail_charname);
        tvGender = findViewById(R.id.detail_gender);
        tvBirthday = findViewById(R.id.detail_bod);

        photo.setImageBitmap(character.getPhoto());
        tvFullname.setText(": " + character.getRealname());
        tvCharname.setText(": " + character.getCharName());
        tvBirthday.setText(": " + character.getBirthday());
        tvGender.setText(": " + character.getGender());
    }

    public void makeToast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_about_me:
                Intent intent = new Intent(DetailActivity.this, AboutMe.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}


