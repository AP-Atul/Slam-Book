package com.friends.tfrndz.fragments;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.friends.tfrndz.R;
import com.friends.tfrndz.adapter.QuestionAdapter;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;


public class FragmentForm extends Fragment {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ImageView image1, loading;

    private Button submit;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private String userId;
    public Uri uploadUri;

    private ObjectAnimator rotate;
    private static final int PICK_IMAGE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_form, container, false);

        submit = view.findViewById(R.id.submit);
        image1 = view.findViewById(R.id.image1);
        loading = view.findViewById(R.id.loading);

        rotate = ObjectAnimator.ofFloat(loading, "rotation", 0f, 180f);
        rotate.setRepeatCount(100);
        rotate.setDuration(500);

        recyclerView = view.findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new QuestionAdapter(getActivity());
        recyclerView.setAdapter(adapter);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Slam/" + userId);
        storageReference = FirebaseStorage.getInstance().getReference("images/ " + userId);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToDB();
            }
        });

        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rotate.start();
                loading.setVisibility(View.VISIBLE);

                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                } else{
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Please Select Image"), PICK_IMAGE);
                }
            }
        });

        return view;
    }

    private void addToDB() {
        String[] answers = QuestionAdapter.getAnswers();
        for(int i = 0; i < answers.length; i++){
            databaseReference.child(String.valueOf(i)).setValue(answers[i]);
        }
        Toast.makeText(getContext(), "All your secrets are now shared with Tanuja \uD83D\uDE08 !", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uploadUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uploadUri);
                image1.setImageBitmap(bitmap);
                uploadImage(uploadUri, "image1");
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage(Uri uploadUri, final String imageKey) {
        final StorageReference ref = storageReference.child(imageKey);
        UploadTask uploadTask = ref.putFile(uploadUri);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserImages/"+ userId + "/" + imageKey);
                    databaseReference.setValue(downloadUri.toString());

                    loading.setVisibility(View.GONE);
                } else {
                    Toast.makeText(getContext(), "Error uploading image" + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                    loading.setVisibility(View.GONE);
                }
            }
        });
    }
}
