package sg.edu.rp.c346.changiairportgroup;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EditActivity extends AppCompatActivity {

    EditText editTerminal;
    Button btnUpdate, btnDelete;
    String term;

    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("terminals");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        editTerminal = (EditText)findViewById(R.id.editTextEditTerminal);

        btnUpdate = (Button)findViewById(R.id.buttonUpdate);
        btnDelete = (Button)findViewById(R.id.buttonDel);

        Intent intent = getIntent();
        final String terminal = intent.getStringExtra("termKey");

        editTerminal.setText(terminal);



        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseRef.orderByChild("terminal").equalTo(terminal).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                            String obj = areaSnapshot.getKey().toString();
                            Toast.makeText(getBaseContext(), obj, Toast.LENGTH_SHORT).show();
                            databaseRef.child(obj).child("terminal").setValue(editTerminal.getText().toString());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                finish();
//
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseRef.orderByChild("terminal").equalTo(terminal).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String a = ds.getKey().toString();
//                            Toast.makeText(getBaseContext(),"query"+a,Toast.LENGTH_LONG).show();
                            databaseRef.child(a).removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
finish();
            }
        });

    }
}
