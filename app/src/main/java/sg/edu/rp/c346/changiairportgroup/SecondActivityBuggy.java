package sg.edu.rp.c346.changiairportgroup;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import sg.edu.rp.c346.changiairportgroup.Chat.*;
import sg.edu.rp.c346.changiairportgroup.Chat.MainActivity;

public class SecondActivityBuggy extends AppCompatActivity {

    TextView tvGate, tvAirline, tvDestination, tvDirection, tvFlightNumber, tvLicensePlate, tvTime;
    DatabaseReference databaseRef;
    Plane planes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buggy_direction);

        Intent i = getIntent();
        final String term = i.getStringExtra("terminal");
        final String gates = i.getStringExtra("gates");
        final String selectedDate = i.getStringExtra("date");
        final String licensePlate = i.getStringExtra("licensePlate");

        tvGate = (TextView) findViewById(R.id.textViewGates);
        tvAirline = (TextView) findViewById(R.id.tvAirLine);
        tvDestination = (TextView) findViewById(R.id.tvDestination);
        tvDirection = (TextView) findViewById(R.id.tvDirection);
        tvFlightNumber = (TextView) findViewById(R.id.tvFlightNumber);
        tvTime = (TextView) findViewById(R.id.tvTime);
        tvLicensePlate = (TextView)findViewById(R.id.tvLicensePlate);

        databaseRef = FirebaseDatabase.getInstance().getReference("terminals");

        final Query q1 = databaseRef.child(term).child(gates).child(selectedDate).orderByChild("licensePlate").equalTo(licensePlate);
        q1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Plane a = ds.getValue(Plane.class);
                        tvAirline.setText("Airline: "+a.getAirline());
                        tvDestination.setText("Destination: "+a.getDestination());
                        tvDirection.setText("Direction: "+a.getDirection());
                        tvFlightNumber.setText("Flight Number: "+a.getFlightNo());
                        tvGate.setText(gates);
                        tvLicensePlate.setText("License Plate: "+a.getLicensePlate());
                        tvTime.setText("Time: "+a.getTime());

                    }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                Intent intent = new Intent(SecondActivityBuggy.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }


}
