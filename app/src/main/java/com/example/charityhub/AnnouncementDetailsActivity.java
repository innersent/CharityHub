package com.example.charityhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

public class AnnouncementDetailsActivity extends AppCompatActivity {
    private ImageView mainAnnouncement;
    private ImageView participateButton;
    private TextView announcementTitle;
    private TextView participateCount;
    private TextView userTelephoneNumber;
    private TextView descriptionDetails;
    private ImageView userTelegramIcon;
    private DatabaseReference mDatabase;
    private ArrayList<String> keys;
    private String announcementId;
    private String userTelegramText;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement_details);
        init();
        getAnnouncement();
        getUrlAnnouncement();
    }

    public void init(){
        announcementId = getIntent().getStringExtra("selectedAnnouncementTelephoneNumber");
        mDatabase = FirebaseDatabase.getInstance().getReference("Announcements").child(announcementId);
        mainAnnouncement = findViewById(R.id.mainDetails);
        announcementTitle = findViewById(R.id.announcement_title_details);
        participateButton = findViewById(R.id.participate_button_details);
        participateCount = findViewById(R.id.participate_count_details);
        userTelephoneNumber = findViewById(R.id.user_telephone_number_details);
        descriptionDetails = findViewById(R.id.description_deatails);

        keys = new ArrayList<>();
    }

    public void getAnnouncement(){
        intent = getIntent();
//        Announcement selectedAnnouncement = intent.getParcelableExtra("selectedAnnouncement");
        Picasso.get().load(intent.getStringExtra("selectedAnnouncement")).into(mainAnnouncement);
        String title = intent.getStringExtra("selectedAnnouncementTitle");
        announcementTitle.setText(title);
        int count = intent.getIntExtra("selectedAnnouncementParticipateCount", 0);
        participateCount.setText(Integer.toString(count));
        String telephoneNumber = intent.getStringExtra("selectedAnnouncementTelephoneNumber");
        userTelephoneNumber.setText(telephoneNumber);
        String description = intent.getStringExtra("selectedAnnouncementDescription");
        descriptionDetails.setText(description);
        userTelegramText = intent.getStringExtra("selectedAnnouncementUserTelegram");
        userTelegramIcon = findViewById(R.id.telegram_icon_details);
    }

    public void getUrlAnnouncement(){
        mDatabase.child("userNumberTelephone").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String telephoneNumberText = snapshot.getValue(String.class);
                    userTelephoneNumber.setText(telephoneNumberText);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void openUserTelegram(View view){
        if(!userTelegramText.isEmpty()){
            String telegramUrl = "https://t.me/" + userTelegramText;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(telegramUrl));
            startActivity(intent);
        } else {
            Toast.makeText(AnnouncementDetailsActivity.this, "К сожалению автор объявления не оставил свой телеграм", Toast.LENGTH_SHORT).show();
        }
    }


    public void backToHome(View view){
        startActivity(new Intent(AnnouncementDetailsActivity.this, MainActivity.class));
    }

}