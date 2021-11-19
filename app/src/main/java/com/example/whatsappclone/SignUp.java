package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.whatsappclone.Model.Users;
import com.example.whatsappclone.databinding.ActivitySignUpBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {
 ActivitySignUpBinding binding;
private FirebaseAuth auth;
FirebaseDatabase database;
ProgressDialog progressDialog;
GoogleSignInClient  mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();
        progressDialog = new ProgressDialog(SignUp.this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("We are creating your account");

        binding=ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        auth=FirebaseAuth.getInstance(); //we took the instance of firebaseAuth
        database=FirebaseDatabase.getInstance(); //we took the instance of firebaseDatabase

        //now setting onClickListener on signUp button
        binding.SignUpbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog.show();
                // do the authentication work here
                auth.createUserWithEmailAndPassword(binding.email.getText().toString(), binding.password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                            Users user=new Users(binding.username.getText().toString(),binding.email.getText().toString(),
                                    binding.password.getText().toString());

                            String id=task.getResult().getUser().getUid();
                            database .getReference().child("Users").child(id).setValue(user);
                            Toast.makeText(SignUp.this,"User created successfully",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(SignUp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            // now we will use completeListener,for that we have to use user models
            }
        });
        binding.tvAlreadyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SignUp.this,SignIn.class);
                startActivity(intent);
            }
        });


    }


}