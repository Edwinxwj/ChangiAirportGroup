package sg.edu.rp.c346.changiairportgroup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Main2ActivityBuggy extends AppCompatActivity {
    ListView lv;
    TextView tvGateName;
    Spinner spnDate;
    ArrayAdapter aa;
    ArrayList<Plane> planes;
    DatabaseReference databaseRef;
    String selectedDate;

    private Toolbar aToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_buggy_flight);

        aToolbar = (Toolbar) findViewById(R.id.buggy_flight_page_toolbar);
        setSupportActionBar(aToolbar);
        getSupportActionBar().setTitle("Flight Information Page");

        tvGateName = (TextView) findViewById(R.id.tvGate);

        spnDate = (Spinner) findViewById(R.id.spinner2);

        lv = (ListView) this.findViewById(R.id.lvPlane);
        planes = new ArrayList<Plane>();

        aa = new CustomAdapterAirtraffic(this, R.layout.row, planes);
        lv.setAdapter(aa);

//        Intent i = getIntent();
//        String year = i.getStringExtra("gates");
//        Plane plane1 = new Plane("tvTiming", "tvLicensePlate","tvAirline","tvFlightNum","tvLicensePlate");
//
//        planes.add(plane1);
        Intent i = getIntent();
        final String gates = i.getStringExtra("gates");
        final String term = i.getStringExtra("terminal");
        tvGateName.setText(gates);

        databaseRef = FirebaseDatabase.getInstance().getReference("terminals");
        final Query querydate = databaseRef.child(term).child(gates).orderByKey();

        querydate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final ArrayList<String> date = new ArrayList<>();
                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    String obj = areaSnapshot.child("date").getValue(String.class);
                    if (obj != null) {
                        date.add(obj);
                    }
                }

                spnDate = (Spinner) findViewById(R.id.spinner2);
                ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(Main2ActivityBuggy.this,
                        android.R.layout.simple_spinner_item, date);
                myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnDate.setAdapter(myAdapter);

                spnDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        planes.clear();
                        selectedDate = (String) parent.getItemAtPosition(position);
                        Query query = databaseRef.child(term).child(gates).child(selectedDate).orderByKey();
                        query.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                if (dataSnapshot.hasChildren()) {
                                    Plane newPlane = dataSnapshot.getValue(Plane.class);
                                    if (newPlane != null) {
                                        planes.add(newPlane);
                                        aa.notifyDataSetChanged();
                                    }
                                }

                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Plane currentPlane = planes.get(position);

                Intent i = new Intent(Main2ActivityBuggy.this, SecondActivityBuggy.class);
                i.putExtra("terminal",term);
                i.putExtra("date",selectedDate);
                i.putExtra("gates",gates);
                i.putExtra("licensePlate",currentPlane.getLicensePlate());
                startActivity(i);



//                AlertDialog.Builder mbuilder = new AlertDialog.Builder(SecondActivityAirtraffic.this);
//                View mView = getLayoutInflater().inflate(R.layout.activity_alert_airtraffic, null);
//                mbuilder.setTitle("Plane Information");
//                final Spinner mSpinner = (Spinner) mView.findViewById(R.id.spinner);
//                TextView tvAirline = (TextView) mView.findViewById(R.id.tvAirline);
//                TextView tvDestination = (TextView) mView.findViewById(R.id.tvDestination);
//                TextView tvFlightNo = (TextView) mView.findViewById(R.id.tvFlightNo);
//                TextView tvLicenseNo = (TextView) mView.findViewById(R.id.tvLicensePlate);
//                TextView tvTime = (TextView) mView.findViewById(R.id.tvTime);
//
//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(SecondActivityAirtraffic.this,
//                        android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.DirectionList));
//                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                mSpinner.setAdapter(adapter);
//
//                final Plane currentPlane = planes.get(position);
////                Toast.makeText(SecondActivityAirtraffic.this,currentPlane.getAirline(),Toast.LENGTH_LONG).show();
//                tvAirline.setText(currentPlane.getAirline());
//                tvDestination.setText(currentPlane.getDestination());
//                tvFlightNo.setText(currentPlane.getFlightNo());
//                tvTime.setText(currentPlane.getTime().toString());
//                tvLicenseNo.setText(currentPlane.getLicensePlate());
//                if (currentPlane.getDirection().equalsIgnoreCase("North")) {
//                    mSpinner.setSelection(2);
//                } else if (currentPlane.getDirection().equalsIgnoreCase("South")) {
//                    mSpinner.setSelection(1);
//                } else {
//                    mSpinner.setSelection(0);
//                }
//
//
//                mbuilder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        if (!mSpinner.getSelectedItem().toString().equalsIgnoreCase("Choose a Direction")) {
//                            final Query q1 = databaseRef.child(term).child(gates).child(selected).orderByChild("licensePlate").equalTo(currentPlane.getLicensePlate());
//                            q1.addListenerForSingleValueEvent(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(DataSnapshot dataSnapshot) {
//
//                                    if (dataSnapshot.exists()) {
//                                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                                            String postkey = ds.getRef().getKey();
////                                            Toast.makeText(SecondActivityAirtraffic.this,postkey,Toast.LENGTH_LONG).show();
//
//                                            //get the key of the child node that has to be updated
//                                            databaseRef.child(term).child(gates).child(selected).child(postkey).child("direction").setValue(mSpinner.getSelectedItem().toString());
//                                        }
//                                    }
//                                }
//
//                                @Override
//                                public void onCancelled(DatabaseError databaseError) {
//
//                                }
//                            });
////                            Toast.makeText(SecondActivityAirtraffic.this,mSpinner.getSelectedItem().toString(),Toast.LENGTH_LONG).show();
//                            dialogInterface.dismiss();
//                        }
//                    }
//                });
//
//                mbuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                    }
//                });
//
//                mbuilder.setView(mView);
//                AlertDialog dialog = mbuilder.create();
//                dialog.show();

            }
        });

    }


}
