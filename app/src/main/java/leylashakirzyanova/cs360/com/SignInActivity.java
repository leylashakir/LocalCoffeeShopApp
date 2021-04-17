package leylashakirzyanova.cs360.com;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class SignInActivity extends Activity {

    private static final String TAG = "SignIn";
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]
    EditText getEmail;
    EditText getPassword;
    Button signUp;
    Button signIn;
    Button signOut;
    Button startOrder;
    private String email;
    private String password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
        setContentView(R.layout.activity_signin);

        signIn = findViewById(R.id.signInButton);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEmail = findViewById(R.id.editTextEmailAddress);
                getPassword = findViewById(R.id.editTextTextPassword);
                email = getEmail.getText().toString();
                password = getPassword.getText().toString();
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(SignInActivity.this, "Email and password field cannot be empty.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    signInToUserAccount(email, password);
                }
            }
        });

        signUp = findViewById(R.id.signUpButton);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEmail = findViewById(R.id.editTextEmailAddress);
                getPassword = findViewById(R.id.editTextTextPassword);
                email = getEmail.getText().toString();
                password = getPassword.getText().toString();
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(SignInActivity.this, "Email and password field cannot be empty.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    createAccount(email, password);
                }
            }
        });

        startOrder = findViewById(R.id.userOrderButton);
        startOrder.setVisibility(View.GONE);
        startOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, MenuActivityUpdate.class));
            }
        });

        signOut = findViewById(R.id.signOutButton);
        signOut.setVisibility(View.GONE);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUser.reload();
        }
    }
    // [END on_start_check_user]

    private void signInToUserAccount(String email, String password) {
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                Toast.makeText(SignInActivity.this, "Welcome back!",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(user);
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(SignInActivity.this, "Email or password is incorrect. Please try again.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
        // [END sign_in_with_email]
    }

    private void createAccount(String email, String password) {
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Account creation success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                Toast.makeText(SignInActivity.this, "Account created.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(user);
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthUserCollisionException e) {
                                Log.d(TAG, "createUserWithEmail: emailCollision");
                                Toast.makeText(SignInActivity.this, "Email already exists. Try logging in.",
                                        Toast.LENGTH_SHORT).show();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                Log.d(TAG, "createUserWithEmail: passwordTooShort");
                                Toast.makeText(SignInActivity.this, e.getReason(),
                                        Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Log.d(TAG, "createUserWithEmail: caughtexception");
                                Toast.makeText(SignInActivity.this, "Unable to create account. Please try again.",
                                        Toast.LENGTH_SHORT).show();
                            }
                            updateUI(null);
                        }
                    }
                });
        // [END create_user_with_email]
    }

    private void signOut () {
        FirebaseAuth.getInstance().signOut();
        updateUI(null);
    }

    private void updateUI(@Nullable FirebaseUser user) {
        if (user != null) {
            findViewById(R.id.signUpButton).setVisibility(View.GONE);
            findViewById(R.id.signInButton).setVisibility(View.GONE);
            findViewById(R.id.signOutButton).setVisibility(View.VISIBLE);
            findViewById(R.id.userOrderButton).setVisibility(View.VISIBLE);

        } else if (user == null) {
            getEmail.getText().clear();
            getPassword.getText().clear();
            findViewById(R.id.signUpButton).setVisibility(View.VISIBLE);
            findViewById(R.id.signInButton).setVisibility(View.VISIBLE);
            findViewById(R.id.signOutButton).setVisibility(View.GONE);
            findViewById(R.id.userOrderButton).setVisibility(View.GONE);
        }
    }

}
