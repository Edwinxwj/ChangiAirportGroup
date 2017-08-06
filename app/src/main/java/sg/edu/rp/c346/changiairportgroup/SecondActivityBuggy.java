package sg.edu.rp.c346.changiairportgroup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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
//                String postkey = dataSnapshot.getRef().getKey();
//                Toast.makeText(getBaseContext(),postkey,Toast.LENGTH_LONG).show();

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
    }
}
//
//        Intent i = getIntent();
//        tvGate.setText(i.getStringExtra("gates"));

//        databaseRef = FirebaseDatabase.getInstance().getReference("terminals");
//        final Query querydate = databaseRef.child(term).child(gates).orderByKey();


//        querydate.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                final ArrayList<String> date = new ArrayList<>();
//                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
//                    String obj = areaSnapshot.child("date").getValue(String.class);
//                    if (obj != null) {
//                        date.add(obj);
//                    }
//                }


//                spnDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                        planes.clear();
//                        selected = (String) parent.getItemAtPosition(position);
//                        Query query = databaseRef.child(term).child(gates).child(selected).orderByKey();
//                        query.addChildEventListener(new ChildEventListener() {
//                            @Override
//                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                                if (dataSnapshot.hasChildren()) {
//                                    Plane newPlane = dataSnapshot.getValue(Plane.class);
//                                    if (newPlane != null) {
//                                        planes.add(newPlane);
//                                        aa.notifyDataSetChanged();
//                                    }
//                                }
//
//                            }
//
//                            @Override
//                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//                            }
//
//                            @Override
//                            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//                            }
//
//                            @Override
//                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//
//                            }
//                        });
//
//
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> parent) {
//
//                    }
//                });
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });


//        final Plane currentPlane = planes.get(position);
////                Toast.makeText(SecondActivityAirtraffic.this,currentPlane.getAirline(),Toast.LENGTH_LONG).show();
//        tvAirline.setText(currentPlane.getAirline());
//        tvDestination.setText(currentPlane.getDestination());
//        tvFlightNo.setText(currentPlane.getFlightNo());
//        tvTime.setText(currentPlane.getTime().toString());
//        tvLicenseNo.setText(currentPlane.getLicensePlate());
//        if (currentPlane.getDirection().equalsIgnoreCase("North")) {
//            mSpinner.setSelection(2);
//        } else if (currentPlane.getDirection().equalsIgnoreCase("South")) {
//            mSpinner.setSelection(1);
//        } else {
//            mSpinner.setSelection(0);
//        }
//
//
//        mbuilder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                if (!mSpinner.getSelectedItem().toString().equalsIgnoreCase("Choose a Direction")) {
//                    final Query q1 = databaseRef.child(term).child(gates).child(selected).orderByChild("licensePlate").equalTo(currentPlane.getLicensePlate());
//                    q1.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//
//                            if (dataSnapshot.exists()) {
//                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                                    String postkey = ds.getRef().getKey();
////                                            Toast.makeText(SecondActivityAirtraffic.this,postkey,Toast.LENGTH_LONG).show();
//
//                                    //get the key of the child node that has to be updated
//                                    databaseRef.child(term).child(gates).child(selected).child(postkey).child("direction").setValue(mSpinner.getSelectedItem().toString());
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
////                            Toast.makeText(SecondActivityAirtraffic.this,mSpinner.getSelectedItem().toString(),Toast.LENGTH_LONG).show();
//                    dialogInterface.dismiss();
//                }
//            }
//        });
//


//
//    public boolean onCreateOptionsMenu(Menu menu){
//        getMenuInflater().inflate(R.menu.option_menu,menu);
//        return true;
//    }
//    public boolean onOptionsItemSelected(MenuItem item){
//        int id = item.getItemId();
//        if (id == R.id.logout) {
////            Intent i = new Intent(getApplicationContext(),MainActivity.class);
////            startActivity(i);
//            return true;
//        }
//
////        return super.onOptionsItemSelected(item);
//    }

