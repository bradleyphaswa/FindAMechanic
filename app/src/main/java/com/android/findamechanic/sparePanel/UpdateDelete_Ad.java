package com.android.findamechanic.sparePanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.findamechanic.R;
import com.android.findamechanic.SparePanel_BottomNavigation;
import com.android.findamechanic.UpdateAdModel;
import com.bumptech.glide.Glide;
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

import java.util.UUID;

public class UpdateDelete_Ad extends AppCompatActivity {

    TextInputLayout mTitle, mPrice, mDescription, mMobile;
    ImageButton imageButton;
    Uri imageUri;
    String title, description, price, spareId, dbUri, mobile, id, randomUid, province, suburb;
    Button updateAd, deleteAd;
    StorageReference ref, storageReference;
    FirebaseStorage storage;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference, data;
    FirebaseAuth mFirebaseAuth;
    private ProgressDialog mProgressDialog;
    final int PIC_CROP = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete_ad);

        mTitle = findViewById(R.id.updateTitle);
        mDescription = findViewById(R.id.updateDescription);
        mPrice = findViewById(R.id.updatePrice);
        mMobile = findViewById(R.id.updateContact);
        imageButton = findViewById(R.id.updateImage);
        updateAd = findViewById(R.id.update_ad);
        deleteAd = findViewById(R.id.delete_ad);
        id = getIntent().getStringExtra(SpareHomeAdapter.ADD_RANDOM_ID);

        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        data = mFirebaseDatabase.getInstance().getReference("Spares").child(userId);
        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Spares spare = snapshot.getValue(Spares.class);
                province = spare.getProvince();
                suburb = spare.getSuburb();

                updateAd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        title = mTitle.getEditText().getText().toString().trim();
                        description = mDescription.getEditText().getText().toString().trim();
                        price = mPrice.getEditText().getText().toString().trim();
                        mobile = mMobile.getEditText().getText().toString().trim();

                        if(isValid())  {

                            if(imageUri != null) {
                                uploadeImage();
                            }
                            else {
                                updateDesc(dbUri);
                            }
                            }
                        }

                });

                deleteAd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateDelete_Ad.this);
                        builder.setMessage("Are you sure you want to Delete this Meal");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mFirebaseDatabase.getInstance().getReference("SpareAdDetails").child(province).child(suburb)
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(id).removeValue();
                                AlertDialog.Builder ad = new AlertDialog.Builder(UpdateDelete_Ad.this);
                                ad.setMessage("Ad deleted successfully!");
                                ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        startActivity(new Intent(UpdateDelete_Ad.this, SparePanel_BottomNavigation.class));
                                    }
                                });
                                AlertDialog alert = ad.create();
                                alert.show();
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                });

                String useridd = FirebaseAuth.getInstance().getCurrentUser().getUid();
                mProgressDialog = new ProgressDialog(UpdateDelete_Ad.this);
                mDatabaseReference = FirebaseDatabase.getInstance().getReference("SpareAdDetails").child(province).child(suburb)
                        .child(useridd).child(id);
                mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UpdateAdModel updateAdModel = snapshot.getValue(UpdateAdModel.class);
                        mTitle.getEditText().setText(updateAdModel.getTitle());
                        mDescription.getEditText().setText(updateAdModel.getDescription());
                        mMobile.getEditText().setText(updateAdModel.getPhone());
                        mPrice.getEditText().setText(updateAdModel.getPrice());
                        Glide.with(UpdateDelete_Ad.this).load(updateAdModel.getImageUri()).into(imageButton);
                        dbUri = updateAdModel.getImageUri();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                mFirebaseAuth = FirebaseAuth.getInstance();
                mDatabaseReference = mFirebaseDatabase.getInstance().getReference("SpareAdDetails");
                storage = FirebaseStorage.getInstance();
                storageReference = storage.getReference();
                imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectimage();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateDesc(String bUri) {
        spareId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        SpareAdDetails info = new SpareAdDetails(title, description, mobile, price, bUri, id, spareId);
        mFirebaseDatabase.getReference("SpareAdDetails").child(province).child(suburb)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(id)
                .setValue(info).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mProgressDialog.dismiss();
                        Toast.makeText(UpdateDelete_Ad.this,"Ad Updates Successfully!",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void uploadeImage() {
        if(imageUri != null) {

            mProgressDialog.setTitle("Uploading....");
            mProgressDialog.show();
            randomUid = UUID.randomUUID().toString();
            ref = storageReference.child(randomUid);
            ref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                   ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                       @Override
                       public void onSuccess(Uri uri) {
                           updateDesc(String.valueOf(uri));
                       }
                   });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mProgressDialog.dismiss();
                    Toast.makeText(UpdateDelete_Ad.this,"Failed:"+e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100.0 * snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                    mProgressDialog.setMessage("Upload "+(int) progress+"%");
                    mProgressDialog.setCanceledOnTouchOutside(false);
                }
            });
        }
    }


    public void selectimage(){

        Intent imgintent = new Intent();
        imgintent.setType("image/*");
        imgintent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(imgintent,PIC_CROP);

    }

    private boolean isValid() {
        mTitle.setErrorEnabled(false);
        mTitle.setError("");
        mDescription.setErrorEnabled(false);
        mDescription.setError("");
        mPrice.setErrorEnabled(false);
        mPrice.setError("");
        mMobile.setErrorEnabled(false);
        mMobile.setError("");

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