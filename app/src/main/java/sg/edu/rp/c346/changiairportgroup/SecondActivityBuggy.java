package sg.edu.rp.c346.changiairportgroup;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import sg.edu.rp.c346.changiairportgroup.Chat.*;
import sg.edu.rp.c346.changiairportgroup.Chat.MainActivity;

public class SecondActivityBuggy extends AppCompatActivity {

    TextView tvGate, tvAirline, tvDestination, tvDirection, tvFlightNumber, tvLicensePlate, tvTime;
    DatabaseReference databaseRef;
    ImageView iv1, iv2;
    Button btnUpdate;
    String currentKey;

    private Toolbar aToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buggy_direction);

        aToolbar = (Toolbar) findViewById(R.id.buggy_direction_page_toolbar);
        setSupportActionBar(aToolbar);
        getSupportActionBar().setTitle("Flight Information");


        Intent i = getIntent();
        final String termKey = i.getStringExtra("termKey");
        final String gateKey = i.getStringExtra("gateKey");
        final String dateKey = i.getStringExtra("dateKey");
        final String timeKey = i.getStringExtra("timeKey");
        final String gate = i.getStringExtra("gate");
        final String licensePlate = i.getStringExtra("licensePlate");


        tvGate = (TextView) findViewById(R.id.textViewGates);
        tvAirline = (TextView) findViewById(R.id.tvAirLine);
        tvDestination = (TextView) findViewById(R.id.tvDestination);
        tvDirection = (TextView) findViewById(R.id.tvDirection);
        tvFlightNumber = (TextView) findViewById(R.id.tvFlightNumber);
        tvTime = (TextView) findViewById(R.id.tvTime);
        tvLicensePlate = (TextView)findViewById(R.id.tvLicensePlate);

        btnUpdate = (Button) findViewById(R.id.buttonUpdate);
        iv1 = (ImageView) findViewById(R.id.imageViewFirst);
        iv2 = (ImageView) findViewById(R.id.imageViewSecond);

        databaseRef = FirebaseDatabase.getInstance().getReference("terminals");


        final Query q1 = databaseRef.child(termKey).child(gateKey).child(dateKey).orderByChild("licensePlate").equalTo(licensePlate);
        q1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Plane a = ds.getValue(Plane.class);
                    tvAirline.setText("Airline: "+a.getAirline());
                    tvDestination.setText("Destination: "+a.getDestination());
                    tvDirection.setText("Direction: "+a.getDirection());
                    tvFlightNumber.setText("Flight Number: "+a.getFlightNo());
                    tvGate.setText(gate);
                    tvLicensePlate.setText("License Plate: "+a.getLicensePlate());
                    tvTime.setText("Time: "+a.getTime());

                    if(a.getDirection().equals("North")){
                        iv1.setImageResource(R.drawable.ic_left);
                        iv2.setImageResource(R.drawable.plane_left);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Query q1 = databaseRef.child(termKey).child(gateKey).child(dateKey).orderByChild("licensePlate").equalTo(licensePlate);
                q1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                            databaseRef.child(termKey).child(gateKey).child(dateKey).child(timeKey).child("pbStatus").setValue("Updated");
                            finish();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
//                Intent i = new Intent(SecondActivityBuggy.this, Main2ActivityBuggy.class);
//                startActivity(i);
            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chat, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.chat:
                Intent intent = new Intent(SecondActivityBuggy.this, MainActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }


}
