package com.example.parkingappproject.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.parkingappproject.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class UserRepository {

    private  final String TAG = this.getClass().getCanonicalName();
    private  final String COLLECTION_NAME = "users";
    private final FirebaseFirestore db;
    public User newUserInfo = new User();

    public MutableLiveData<String> signInStatus = new MutableLiveData<String>();
    public MutableLiveData<String> userExistStatus = new MutableLiveData<String>();
    public MutableLiveData<String> loggedInUserID = new MutableLiveData<String>();

    public  UserRepository(){
        db = FirebaseFirestore.getInstance();
    }

    public void addUser(User user){
        try {
            db.collection(COLLECTION_NAME)
                    .add(user)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "Document added with ID : " + documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "Error adding document to the store " + e);
                        }
                    });

        }catch (Exception ex){
            Log.e(TAG, ex.toString());
            Log.e(TAG, ex.getLocalizedMessage());
        }
    }

    public void getUser(String email, String password){

        this.signInStatus.postValue("LOADING");

        try{
            db.collection(COLLECTION_NAME)
                    .whereEqualTo("email", email)
                    // .whereEqualTo("password",password)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
//                                for (QueryDocumentSnapshot document : task.getResult()){
//                                    Log.d(TAG, document.getId() + "---" + document.getData());
//                                    if (document.toObject(User.class).getPassword().equals(password)){
//                                        Log.d(TAG, "Successful Login");
//                                        signInStatus.postValue("SUCCESS");
//                                    }else{
//                                        Log.d(TAG, "Unsuccessful Login");
//                                        signInStatus.postValue("FAILURE");
//                                    }
//                                }

                                if (task.getResult().getDocuments().size() != 0){
                                    if(task.getResult().getDocuments().get(0).toObject(User.class).getPassword().equals(password)){
                                        signInStatus.postValue("SUCCESS");


                                        //get the id of the current user logged in
                                        loggedInUserID.postValue(task.getResult().getDocuments().get(0).getId());
                                        Log.d(TAG, "Logged in user document ID: " +loggedInUserID);
                                    }
                                    else {
                                        signInStatus.postValue("FAILURE");
                                    }
                                }
                                else{
                                    signInStatus.postValue("FAILURE");
                                }


                            }else{
                                Log.e(TAG, "Error fetching document" + task.getException());
                                signInStatus.postValue("FAILURE");
                            }
                        }
                    });
        }catch (Exception ex){
            Log.e(TAG, ex.toString());
            Log.e(TAG, ex.getLocalizedMessage());
            signInStatus.postValue("FAILURE");
        }
    }

    public void checkUser(String email){

        this.userExistStatus.postValue("LOADING");

        try{
            db.collection(COLLECTION_NAME)
                    .whereEqualTo("email", email)
                    // .whereEqualTo("password",password)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){

                                if (task.getResult().getDocuments().size() != 0){
                                    userExistStatus.postValue("EXIST");
                                }
                                else{
                                    userExistStatus.postValue("NOT EXIST");
                                }


                            }else{
                                Log.e(TAG, "Error fetching document" + task.getException());
                                userExistStatus.postValue("NOT EXIST");
                            }
                        }
                    });
        }catch (Exception ex){
            Log.e(TAG, ex.toString());
            Log.e(TAG, ex.getLocalizedMessage());
            userExistStatus.postValue("NOT EXIST");
        }
    }

    public void updateUser(User user){
        try {
            db.collection(COLLECTION_NAME)
                    .document(loggedInUserID.getValue())
                    .update(
                        "name", user.getName(),
                            "password", user.getPassword(),
                            "contactNumber", user.getContactNumber(),
                            "carPlateNumber", user.getCarPlateNumber()
                    ).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Log.d(TAG, "Document updated Successfully ");
                    }
                    else{
                        Log.d(TAG, "Error Updating Document");
                    }
                    
                }
            });

        }catch (Exception ex){
            Log.e(TAG, ex.toString());
            Log.e(TAG, ex.getLocalizedMessage());
        }
    }

    public User getUpdateUserInfo(String userID){

        db.collection(COLLECTION_NAME)
                .document(userID)
                .get()
               .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                   @Override
                   public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                       if (task.isSuccessful()){

                           DocumentSnapshot documentSnapshot = task.getResult();
                           if(documentSnapshot !=null){

                            newUserInfo = documentSnapshot.toObject(User.class);


                           }

                       }
                       else{

                       }
                   }
               });
        
        return newUserInfo;
    }

    public void deleteUser(String userID){
        db.collection(COLLECTION_NAME)
                .document(userID)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.e(TAG, "Document deleted successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Failure deleting document");
                    }
                });
    }
    
    
    
}
