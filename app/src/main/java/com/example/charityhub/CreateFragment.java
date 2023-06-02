package com.example.charityhub;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

public class CreateFragment extends Fragment {

    private static final int REQUEST_CODE_GALLERY = 1;
    private EditText editTextTitle;
    public EditText telephoneNumber;
    private EditText description;
    private EditText userTelegram;
    private ImageView imageView;
    private Uri selectedImageUri;
    private StorageReference storageRef;
    DatabaseReference mDatabase;

    private Button button;
    private boolean isButtonClickable = true;

    public CreateFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create, container, false);

        init(view);
        editTextTitle = view.findViewById(R.id.editTextTitle);
        imageView = view.findViewById(R.id.imageView);
        telephoneNumber = view.findViewById(R.id.userTelephoneNumberEditText);
        description = view.findViewById(R.id.editTextTextMultiLine);
        userTelegram = view.findViewById(R.id.userTelegramEditText);
        Button buttonUploadImage = view.findViewById(R.id.buttonUploadImage);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        buttonUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Создание Intent для выбора изображения из галереи
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            }
        });

        Button buttonAddPost = view.findViewById(R.id.buttonAddPost);
        buttonAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editTextTitle.getText().toString();
                String telephoneNumberText = telephoneNumber.getText().toString();
                String descriptionText = description.getText().toString();
                String userTelegramText = userTelegram.getText().toString();

                if (TextUtils.isEmpty(title) || selectedImageUri == null || TextUtils.isEmpty(telephoneNumberText)) {
                    Toast.makeText(getActivity(), "Заполните все поля", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(isButtonClickable) {

                    String filename = UUID.randomUUID().toString();
                    StorageReference imageRef = storageRef.child("images/" + filename);

                    imageRef.putFile(selectedImageUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String imageUrl = uri.toString();
                                            Announcement announcement = new Announcement(imageUrl, title, telephoneNumberText, descriptionText, userTelegramText);
                                            mDatabase.push().setValue(announcement);

                                            Context context = getContext();
                                            Intent intent = new Intent(getActivity(), AnnouncementDetailsActivity.class);

                                            intent.putExtra("telephoneNumberText", telephoneNumberText);

                                            Toast.makeText(getActivity(), "Объявление добавлено успешно", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), "Ошибка при загрузке изображения", Toast.LENGTH_SHORT).show();
                                }
                            });
                    isButtonClickable = false;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isButtonClickable = true;
                        }
                    }, 100000);
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_GALLERY && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.getData();

            imageView.setImageURI(selectedImageUri);
        }
    }

    public void init(View view){
        mDatabase = FirebaseDatabase.getInstance().getReference("Announcements");
        button = view.findViewById(R.id.buttonAddPost);
    }
}