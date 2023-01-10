package com.android.findamechanic.sparePanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.findamechanic.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;


public class Spare_PostAd extends AppCompatActivity {

    final int PIC_CROP = 1;
    private Bitmap bitmapsimg;
    ImageButton imageButton;
    Button postAd;

    TextInputLayout mTitle, mDescription, mPrice, mPhone;
    String title, description, price, phone;
    Uri imageUri;
    private Uri mCropImageUri;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference,dataa;
    FirebaseAuth Fauth;
    StorageReference ref;
    String spareId, randomUid, province, suburb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spare_post_ad);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        mTitle = findViewById(R.id.spareAdTitle);
        mDescription = findViewById(R.id.spareAdDescription);
        mPrice = findViewById(R.id.spareAdPrice);
        mPhone = findViewById(R.id.spareAdContact);
        postAd = findViewById(R.id.sparePostAd);
        Fauth = FirebaseAuth.getInstance();
        databaseReference = firebaseDatabase.getInstance().getReference("SpareAdDetails");

        try {
            String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            dataa = firebaseDatabase.getInstance().getReference("Spares").child(userid);
            dataa.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    Spares spares = snapshot.getValue(Spares.class);

                    province = spares.getProvince();
                    suburb = spares.getSuburb();
                    imageButton = (ImageButton) findViewById(R.id.image_upload);

                    imageButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selectimage();
                        }
                    });
                    postAd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            title = mTitle.getEditText().getText().toString().trim();
                            description = mDescription.getEditText().getText().toString().trim();
                            price = mPrice.getEditText().getText().toString().trim();
                            phone = mPhone.getEditText().getText().toString().trim();

                            if(isValid()){
                                uploadImage();
                            }
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }catch (Exception e){
            Log.e("Error: ",e.getMessage());
        }

    }


    public void selectimage(){

        Intent imgintent = new Intent();
        imgintent.setType("image/*");
        imgintent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(imgintent,PIC_CROP);

    }

    @Override
    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode  == PIC_CROP && resultCode==RESULT_OK &&  data != null){
            imageUri = data.getData();

            try {

                bitmapsimg = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);

                imageButton.setImageBitmap(bitmapsimg);


                imageButton.setVisibility(View.VISIBLE);


            } catch (IOException e){

            }
        }

    }

    private void uploadImage() {

        if(imageUri != null){
            final ProgressDialog progressDialog = new ProgressDialog(Spare_PostAd.this);
            progressDialog.setTitle("Uploading.....");
            progressDialog.show();
            randomUid = UUID.randomUUID().toString();
            ref = storageReference.child(randomUid);
            spareId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            ref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            SpareAdDetails info =
                                    new SpareAdDetails(title , description, price, phone, String.valueOf(uri),randomUid, spareId);
                            firebaseDatabase.getInstance().getReference("SpareAdDetails")
                                    .child(province).child(suburb)
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(randomUid)
                                    .setValue(info).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            progressDialog.dismiss();
                                            Toast.makeText(Spare_PostAd.this,"Ad Posted Successfully!",Toast.LENGTH_LONG).show();
                                        }
                                    });

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(Spare_PostAd.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded "+(int) progress+"%");
                    progressDialog.setCanceledOnTouchOutside(false);
                }
            });
        }

    }

    private boolean isValid() {
        mTitle.setErrorEnabled(false);
        mTitle.setError("");
        mDescription.setErrorEnabled(false);
        mDescription.setError("");
        mPrice.setErrorEnabled(false);
        mPrice.setError("");

        boolean isValidTitle=false,isValidDescription = false,isValidPrice=false,isValid=false;
        if(TextUtils.isEmpty(description)){
            mDescription.setErrorEnabled(true);
            mDescription.setError("Description is Required");
        }else{
            mDescription.setError(null);
            isValidDescription=true;
        }
        if(TextUtils.isEmpty(title)){
            mTitle.setErrorEnabled(true);
            mTitle.setError("Enter number of Plates or Items");
        }else{
            isValidTitle=true;
        }
        if(TextUtils.isEmpty(price)){
            mPrice.setErrorEnabled(true);
            mPrice.setError("Please Mention Price");
        }else{
            isValidPrice=true;
        }

        isValid = (isValidDescription && isValidTitle && isValidPrice)?true:false;
        return isValid;
    }

}