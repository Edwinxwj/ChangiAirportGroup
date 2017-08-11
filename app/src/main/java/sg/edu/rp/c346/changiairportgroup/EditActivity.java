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
    Spinner Spinner;
    ArrayAdapter myAdapter;
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


        databaseRef.child(terminal).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                final ArrayList<String> terminals = new ArrayList<>();
                String obj = dataSnapshot.child("terminal").getValue(String.class);
                Toast.makeText(getBaseContext(), obj, Toast.LENGTH_SHORT).show();
                terminals.add(obj);
//                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
//                    String obj = areaSnapshot.child("terminal").getValue(String.class);
////                    Toast.makeText(getBaseContext(), obj, Toast.LENGTH_SHORT).show();
//                    terminals.add(obj);
//                }

                Spinner = (Spinner) findViewById(R.id.spinnerOldTem);
                myAdapter = new ArrayAdapter<String>(EditActivity.this,
                        android.R.layout.simple_spinner_item, terminals);
                myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Spinner.setAdapter(myAdapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query q1 = databaseRef.orderByChild("terminal").equalTo(Spinner.getSelectedItem().toString());
                q1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                            String a = ds.getKey().toString();
                            Toast.makeText(getBaseContext(), a, Toast.LENGTH_SHORT).show();
                            databaseRef.child(a).child("terminal").setValue(editTerminal.getText().toString());

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
