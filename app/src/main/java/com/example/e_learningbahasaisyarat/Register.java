package com.example.e_learningbahasaisyarat;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.e_learningbahasaisyarat.ui.MyCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class Register extends AppCompatActivity {

    private EditText name, email, pass, phone;
    private Button signUpB;
    private TextView loginB;

    private FirebaseAuth auth;
    private String nameStr, emailStr, passStr, phoneStr;
    private Dialog progressDialog;
    private TextView dialogText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.fullname);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        pass = findViewById(R.id.password);
        signUpB = findViewById(R.id.registerBtn);
        loginB = findViewById(R.id.logintext);

        progressDialog = new Dialog(Register.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogText = progressDialog.findViewById(R.id.dialog_text);
        dialogText.setText("Registering user...");

        auth = FirebaseAuth.getInstance();

        signUpB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validate()) {
                    signupNewUser();
                }
            }
        });

        loginB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);

            }
        });
    }

    private boolean validate() {
        nameStr = name.getText().toString().trim();
        emailStr = email.getText().toString().trim();
        passStr = pass.getText().toString().trim();
        phoneStr = phone.getText().toString().trim();

        if (nameStr.isEmpty()) {
            name.setError("Enter Your Name");
            return false;
        }

        if (emailStr.isEmpty()) {
            email.setError("Enter Your E-Mail");
            return false;
        }

        if (passStr.isEmpty()) {
            pass.setError("Enter Your Password");
            return false;
        }

        if (phoneStr.isEmpty()) {
            phone.setError("Enter Your Phone Number");
            return false;
        }

        return true;

    }

    private void signupNewUser()
    {
        progressDialog.show();
        auth.createUserWithEmailAndPassword(emailStr, passStr)
                .addOnCompleteListener(this, task -> {

                    if (task.isSuccessful()) {
                        // If sign in success, display a message to the user.
                        Toast.makeText(Register.this, "Sign Up Successful", Toast.LENGTH_SHORT).show();

                        DbQuery.createUserData(emailStr, nameStr, new MyCompleteListener(){

                            @Override
                            public void onSuccess() {

                                DbQuery.loadData(new MyCompleteListener() {
                                    @Override
                                    public void onSuccess() {
                                        progressDialog.dismiss();

                                        Intent intent = new Intent(Register.this, Login.class);
                                        startActivity(intent);
                                        Register.this.finish();

                                    }

                                    @Override
                                    public void onFailure() {
                                        Toast.makeText(Register.this, "Something went wrong ! Please Try Again Later!", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();

                                    }
                                });
                            }

                            @Override
                            public void onFailure() {
                                Toast.makeText(Register.this, "Something went wrong ! Please Try Again Later!", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();

                            }
                        });

                    } else {
                        // If sign in fails, display a message to the user.
                        progressDialog.dismiss();
                        Toast.makeText(Register.this, "Unsuccessful", Toast.LENGTH_SHORT).show();
                    }

                });

    }


}