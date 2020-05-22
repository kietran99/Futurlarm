package com.se68.rraptor.futurlarm.Class;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class FirebaseHelper {
    private DatabaseReference userDataRef;
    private DatabaseReference futurlarmRef;
    private StorageReference myStorageRef;
    private User userData;
    private FuturlarmList futurlarms;

    public FirebaseHelper() {
        userDataRef = FirebaseDatabase.getInstance().getReference("Users");
        futurlarmRef = FirebaseDatabase.getInstance().getReference("Futurlarms");
        myStorageRef = FirebaseStorage.getInstance().getReference("avatars");
    }

    public interface DataStatus{
        void dataLoaded(User userData);
        void dataLoaded(FuturlarmList futurlarms);
        void dataInserted();
        void dataUpdated();
        void dataDeleted();
    }

    public void getUserData(final String email, final DataStatus dataStatus){
        userDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    if (ds.getValue(User.class).getEmail().equals(email)) {
                        userData = new User();
                        userData = ds.getValue(User.class);
                        break;
                    }
                }
                dataStatus.dataLoaded(userData);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getFuturlarms(final String UId, final DataStatus dataStatus){
        futurlarmRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds_user : dataSnapshot.getChildren()){
                    if (ds_user.getKey().equals(UId)){
                        futurlarms = new FuturlarmList();
                        for (DataSnapshot ds_futurlarm : ds_user.getChildren()){
                            futurlarms.getList().add(ds_futurlarm.getValue(Futurlarm.class));
                        }
                        break;
                    }
                }
                dataStatus.dataLoaded(futurlarms);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void createUser(final Context context, Uri uri, final User user,
                              final String Uid, final DataStatus dataStatus){
        //Upload avatar
        if (uri == null){
            Toast.makeText(context, "No file selected", Toast.LENGTH_SHORT).show();
            return;
        }
        
        final StorageReference fileRef = myStorageRef.child(System.currentTimeMillis() + "." + ImageChooser.getFileExtension(context, uri));

        fileRef.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileRef.getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
            		                @Override
            		                public void onSuccess(Uri uri) {
                                        userData = user;
                                        userData.setAvatar(uri.toString());
                                        uploadUser(context, Uid, dataStatus);
           		                    }
        	                    })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void uploadUser(final Context context, final String Uid, final DataStatus dataStatus){
        userDataRef.child(Uid).setValue(userData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.dataInserted();
                    }
                })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
