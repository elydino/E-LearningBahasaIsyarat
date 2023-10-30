package com.example.e_learningbahasaisyarat;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_learningbahasaisyarat.ui.MyCompleteListener;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class Login extends AppCompatActivity {

    private EditText email, pass;
    private Button loginB;
    private TextView forgotPassword, signupB;
    private FirebaseAuth auth;
    private Dialog progressDialog;
    private TextView dialogText;
    private RelativeLayout gSignB;
    private GoogleSignInClient GoogleSignInClient;
    private int RC_SIGN_IN = 104;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        loginB = findViewById(R.id.loginbtn);
        signupB = findViewById(R.id.signuptext);
        gSignB = findViewById(R.id.g_signB);
        forgotPassword = findViewById(R.id.forgotPasswordText);

        progressDialog = new Dialog(Login.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogText = progressDialog.findViewById(R.id.dialog_text);
        dialogText.setText("Signing in...");

        auth = FirebaseAuth.getInstance();

        //Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleSignInClient = GoogleSignIn.getClient(this, gso);

        loginB.setOnClickListener(view -> {
            if (validateData()) {
                login();
            }
        });
        signupB.setOnClickListener(view -> {

            Intent intent = new Intent(Login.this, Register.class);
            startActivity(intent);

        });

        gSignB.setOnClickListener(view -> googleSignIn());

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showForgotPasswordDialog();
            }
        });
    }

    private boolean validateData() {

        if (email.getText().toString().isEmpty()) {
            email.setError("Enter E-Mail");
            return false;
        }

        if (pass.getText().toString().isEmpty()) {
            pass.setError("Enter Password");
            return false;
        }
        return true;
    }

    private void login() {

        progressDialog.show();

        auth.signInWithEmailAndPassword(email.getText().toString().trim(), pass.getText().toString().trim())
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(Login.this, "Login Success", Toast.LENGTH_SHORT).show();

                        DbQuery.loadData(new MyCompleteListener() {
                            @Override
                            public void onSuccess() {
                                progressDialog.dismiss();
                                Intent intent = new Intent(Login.this, MainActivity.class);
                                startActivity(intent);
                            }

                            @Override
                            public void onFailure() {
                                progressDialog.dismiss();
                                // If sign in fails, display a message to the user.
                                Toast.makeText(Login.this, "Something went wrong ! Please Try Again Later !", Toast.LENGTH_SHORT).show();

                            }
                        });

                    } else {
                        progressDialog.dismiss();
                        // If sign in fails, display a message to the user.
                        Toast.makeText(Login.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void googleSignIn() {
        Intent signInIntent = GoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //Google Sign In was successful, authenticate with firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                // Log.d(Tag, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {

                Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {

        progressDialog.show();

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information

                        Toast.makeText(Login.this, "Google Sign In Success", Toast.LENGTH_SHORT).show();

                        FirebaseUser user = auth.getCurrentUser();

                        if (task.getResult().getAdditionalUserInfo().isNewUser()) {
                            DbQuery.createUserData(user.getEmail(), user.getDisplayName(), new MyCompleteListener() {
                                @Override
                                public void onSuccess() {

                                    DbQuery.loadData(new MyCompleteListener() {
                                        @Override
                                        public void onSuccess() {
                                            progressDialog.dismiss();
                                            Intent intent = new Intent(Login.this, MainActivity.class);
                                            startActivity(intent);

                                        }

                                        @Override
                                        public void onFailure() {
                                            progressDialog.dismiss();
                                            // If sign in fails, display a message to the user.
                                            Toast.makeText(Login.this, "Something went wrong ! Please Try Again Later !", Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                }

                                @Override
                                public void onFailure() {
                                    progressDialog.dismiss();
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(Login.this, "Something went wrong ! Please Try Again Later !", Toast.LENGTH_SHORT).show();

                                }
                            });
                        } else {
                            DbQuery.loadData(new MyCompleteListener() {
                                @Override
                                public void onSuccess() {
                                    progressDialog.dismiss();
                                    Intent intent = new Intent(Login.this, MainActivity.class);
                                    startActivity(intent);

                                }

                                @Override
                                public void onFailure() {
                                    progressDialog.dismiss();
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(Login.this, "Something went wrong ! Please Try Again Later !", Toast.LENGTH_SHORT).show();

                                }
                            });
                        }

                    } else {
                        progressDialog.dismiss();
                        // If sign in fails, display a message to the user.
                        Toast.makeText(Login.this, "Something went wrong ! Please Try Again Later !", Toast.LENGTH_SHORT).show();

                    }
                });
    }


    private void showForgotPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.activity_forgot_password, null);
        EditText emailEditText = view.findViewById(R.id.editBox);

        builder.setView(view);
        final AlertDialog dialog = builder.create();

        view.findViewById(R.id.btnReset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (compareEmail(emailEditText, dialog)) {
                    //dialog stay showing
                }
            }
        });

        view.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        dialog.show();
    }

    private boolean compareEmail(EditText emailEditText, AlertDialog dialog) {
        if (emailEditText.getText().toString().isEmpty()) {
            Toast.makeText(this, "Email required", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText().toString()).matches()) {
            Toast.makeText(this, "Email does not exist", Toast.LENGTH_SHORT).show();
            return false;
        }

        auth.sendPasswordResetEmail(emailEditText.getText().toString()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Check your Email", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        });

        return true;
    }
}