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
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import sg.edu.rp.c346.changiairportgroup.Chat.MainActivity;

public class Main2ActivityBuggy extends AppCompatActivity {
    ListView lv;
    TextView tvGateName;
    Spinner spnDate;
    ArrayAdapter aa;
    ArrayList<Plane> planes;
    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("terminals");

    String selectedDate;
    String term,termKey, gateKey, timeKey, dateKey;


    private Toolbar aToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_buggy_flight);

        aToolbar = (Toolbar) findViewById(R.id.buggy_flight_page_toolbar);
        setSupportActionBar(aToolbar);
        getSupportActionBar().setTitle("Buggy Driver Flight");

        tvGateName = (TextView) findViewById(R.id.tvGate);

        spnDate = (Spinner) findViewById(R.id.spinner2);

        lv = (ListView) this.findViewById(R.id.lvPlane);
        planes = new ArrayList<Plane>();

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        final String formattedDate = df.format(c.getTime());

        aa = new CustomAdapterAirtraffic(this, R.layout.row, planes);
        lv.setAdapter(aa);

        Intent i = getIntent();
        final String gate = i.getStringExtra("gate");
        term = i.getStringExtra("terminal");
        termKey = i.getStringExtra("termKey");
        tvGateName.setText(gate);

        final Query querydate = databaseRef.child(termKey).orderByChild("gate").equalTo(gate);


        querydate.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                gateKey = dataSnapshot.getKey().toString(); //gate1/gate2 etc
                final ArrayList<String> date = new ArrayList<>();

                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    String obj = areaSnapshot.child("date").getValue(String.class);
                    if (obj != null) {
                        if(obj.equals(formattedDate)) {
                            date.add(obj);
                        }
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
                        Query query = databaseRef.child(termKey).child(gateKey).orderByChild("date").equalTo(selectedDate);
                        query.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                dateKey = dataSnapshot.getKey().toString();
                                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                                    if (areaSnapshot.hasChildren()) {
                                        timeKey = areaSnapshot.getKey().toString();
                                        Plane newPlane = areaSnapshot.getValue(Plane.class);
                                        if (newPlane != null) {
                                            planes.add(newPlane);
                                            aa.notifyDataSetChanged();
                                        }
                                    }
                                    aa.notifyDataSetChanged();

                                }

                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                                planes.clear();
                                dateKey = dataSnapshot.getKey().toString();
                                Toast.makeText(getBaseContext(), "datekey: " + dateKey, Toast.LENGTH_SHORT).show();
                                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                                    if (areaSnapshot.hasChildren()) {
                                        timeKey = areaSnapshot.getKey().toString();
                                        Toast.makeText(getBaseContext(), "TimeKey:" + timeKey, Toast.LENGTH_SHORT).show();
                                        Plane newPlane = areaSnapshot.getValue(Plane.class);
                                        if (newPlane != null) {
                                            planes.add(newPlane);
                                            aa.notifyDataSetChanged();
                                        }
                                    }
                                }
                                aa.notifyDataSetChanged();
                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {
                                planes.clear();
                                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                                    if (areaSnapshot.hasChildren()) {
                                        Plane newPlane = areaSnapshot.getValue(Plane.class);
                                        if (newPlane != null) {
                                            planes.add(newPlane);
                                            aa.notifyDataSetChanged();
                                        }
                                    }
                                    aa.notifyDataSetChanged();
                                }

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


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Plane currentPlane = planes.get(position);

                Intent i = new Intent(Main2ActivityBuggy.this, SecondActivityBuggy.class);
                i.putExtra("termKey",termKey);
                i.putExtra("dateKey",dateKey);
                i.putExtra("gateKey",gateKey);
                i.putExtra("timeKey", timeKey);
                i.putExtra("licensePlate",currentPlane.getLicensePlate());
                i.putExtra("gate", gate);
                startActivity(i);


            }
        });

//        spnDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
////                String Text = spnDate.getSelectedItem().toString();
////                tv.setText(Text);
//                switch (i) {
//                    case 0:
//
//                        break;
//                    case 1:
//                        //Your code for Item 2 select
//                        break;
//                }
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });


    }



}
