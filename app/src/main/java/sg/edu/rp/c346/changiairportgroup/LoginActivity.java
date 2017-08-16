package sg.edu.rp.c346.changiairportgroup;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import sg.edu.rp.c346.changiairportgroup.Chat.*;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPassWord;
    private Button btnLogin;
    private Button btnForget;
    private DatabaseReference databaseRef;
    private String role;

    private com.google.firebase.auth.FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;



    private ProgressDialog progressDialog;
    private DatabaseReference mDatabaseUsers;

    // For Action bar
    private Toolbar aToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // For action bar
        aToolbar = (Toolbar) findViewById(R.id.login_page_toolbar);
        setSupportActionBar(aToolbar);
        getSupportActionBar().setTitle("Login Page");

        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null){
                   checkUserExist();
                }
            }
        };

        mAuth = com.google.firebase.auth.FirebaseAuth.getInstance();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");

        progressDialog = new ProgressDialog(this);

        etEmail = (EditText)findViewById(R.id.editTextEmail);
        etPassWord = (EditText)findViewById(R.id.editTextPassword);

        btnLogin = (Button)findViewById(R.id.buttonLogin);

        btnForget = (Button)findViewById(R.id.buttonForgetPW);

        btnForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder myBuilder = new AlertDialog.Builder(LoginActivity.this);

                myBuilder.setTitle("Reset Password");
                myBuilder.setMessage("An email will be sent to you!");
                myBuilder.setCancelable(false);
                myBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        mAuth.sendPasswordResetEmail(user.getEmail());
                    }
                });
                myBuilder.setNegativeButton("Cancel", null);

                AlertDialog myDialog = myBuilder.create();
                myDialog.show();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLogin();
            }
        });


    }

    private void checkLogin(){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final SharedPreferences.Editor editor = settings.edit();

        final String email = etEmail.getText().toString().trim();
        final String password = etPassWord.getText().toString().trim();
        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            progressDialog.setMessage("Checking login");
            progressDialog.show();
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        checkUserExist();
                        editor.putString("email", email);
                        editor.putString("password", password);
                        editor.commit();

                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this,"Error",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private void checkUserExist(){
        final String user_id = mAuth.getCurrentUser().getUid();
        mDatabaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(user_id)){
                    String role = dataSnapshot.child(user_id).child("Role").getValue(String.class);
                    if(role.equals("Admin")){
                        Toast.makeText(getBaseContext(),"Role: "+role ,Toast.LENGTH_SHORT).show();
                        Intent mainIntent = new Intent(LoginActivity.this,MainActivityAdmin.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainIntent);
                    }else if(role.equals("Buggy Driver")){
                        Toast.makeText(getBaseContext(),"Role: "+role ,Toast.LENGTH_SHORT).show();
                        Intent mainIntent = new Intent(LoginActivity.this,MainActivityBuggy.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainIntent);

                    }else if(role.equals("Airtraffic Controller")){
                        Toast.makeText(getBaseContext(),"Role: "+role ,Toast.LENGTH_SHORT).show();
                        Intent mainIntent = new Intent(LoginActivity.this,MainActivityAirTraffic.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainIntent);
                    }else {
                        Toast.makeText(LoginActivity.this, "Invalid role" + role, Toast.LENGTH_LONG).show();
                    }

                }else{
                    Toast.makeText(LoginActivity.this,"Contact admin to setup your account",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();


        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String email = pref.getString("email", "");
        String password = pref.getString("password", "");

        etEmail.setText(email);
        etPassWord.setText(password);

    }
}
