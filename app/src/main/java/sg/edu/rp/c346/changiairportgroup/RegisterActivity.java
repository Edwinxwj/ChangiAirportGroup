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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText etName, etPassword, etEmail;
    private Button btnRegister;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabase.setValue("hello");
        mProgress = new ProgressDialog(this);


        etName = (EditText)findViewById(R.id.etName);
        etEmail = (EditText)findViewById(R.id.etEmail);
        etPassword = (EditText)findViewById(R.id.etPassword);

        btnRegister = (Button)findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRegister();
            }
        });

    }

    private void startRegister(){
        final String name = etName.getText().toString().trim();
        final String email = etEmail.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();

        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            mProgress.setMessage("Signing up");
            mProgress.show();

            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){

                        String user_id = mAuth.getCurrentUser().getUid();
                        DatabaseReference current_user_db = mDatabase.child(user_id);

                        current_user_db.child("Name").setValue(name);
                        current_user_db.child("Image").setValue("default");

                        mProgress.dismiss();

                        Intent mainIntent = new Intent(RegisterActivity.this,LoginActivity.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainIntent);
                    }else{
                        Toast.makeText(getBaseContext(), "Failed",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
