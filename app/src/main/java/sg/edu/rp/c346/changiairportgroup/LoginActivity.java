package sg.edu.rp.c346.changiairportgroup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPassWord;
    private Button btnLogin;
    private Button btnCreate;

    private com.google.firebase.auth.FirebaseAuth mAuth;

    private ProgressDialog progressDialog;
    private DatabaseReference mDatabaseUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = com.google.firebase.auth.FirebaseAuth.getInstance();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");

        progressDialog = new ProgressDialog(this);

        etEmail = (EditText)findViewById(R.id.editTextEmail);
        etPassWord = (EditText)findViewById(R.id.editTextPassword);

        btnLogin = (Button)findViewById(R.id.buttonLogin);

        btnCreate = (Button)findViewById(R.id.buttonNewAccount);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signupIntent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(signupIntent);
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
        String email = etEmail.getText().toString().trim();
        String password = etPassWord.getText().toString().trim();

        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){

            progressDialog.setMessage("Checking login");
            progressDialog.show();
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        checkUserExist();

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
                    Intent mainIntent = new Intent(LoginActivity.this,MainActivityAdmin.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);

                }else{
                    Toast.makeText(LoginActivity.this,"You need setup your account",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
