package sg.edu.rp.c346.changiairportgroup;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import java.util.ArrayList;

/**
 * Created by 15056215 on 20/6/2017.
 */

public class SecondActivityAdmin extends AppCompatActivity {

    ListView lv;
    TextView tvGateName;
    Spinner spnDate;
    ArrayAdapter aa;
    ArrayList<Plane> planes;
    DatabaseReference databaseRef;
    String selected;

    private Toolbar aToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_admin);

        aToolbar = (Toolbar) findViewById(R.id.admin_second_page_toolbar);
        setSupportActionBar(aToolbar);
        getSupportActionBar().setTitle("Admin Flight Page");


        tvGateName = (TextView) findViewById(R.id.tvGate);

//        spnDate = (Spinner) findViewById(R.id.spinner2);

        lv = (ListView) this.findViewById(R.id.lvPlane);
        planes = new ArrayList<Plane>();

        aa = new CustomAdapterAdmin(this, R.layout.rowadmin, planes);
        lv.setAdapter(aa);


        Intent i = getIntent();
        final String gates = i.getStringExtra("gate");
        final String term = i.getStringExtra("terminal");
        tvGateName.setText(gates);

        databaseRef = FirebaseDatabase.getInstance().getReference("terminals");
        final Query querydate = databaseRef.child(term).child(gates).orderByKey();

        querydate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final ArrayList<String> date = new ArrayList<>();
                for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
                    String obj = areaSnapshot.child("date").getValue(String.class);
                    if(obj != null) {
                        date.add(obj);
                    }
                }

                spnDate = (Spinner) findViewById(R.id.spinner2);
                ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(SecondActivityAdmin.this,
                        android.R.layout.simple_spinner_item, date);
                myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnDate.setAdapter(myAdapter);

                spnDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        planes.clear();
                        selected = (String)parent.getItemAtPosition(position);
//                        Toast.makeText(getBaseContext(),term,Toast.LENGTH_SHORT).show();
                        Query query = databaseRef.child(term).child(gates).child(selected).orderByKey();
                        query.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                                String time = dataSnapshot.getKey();
                                if(dataSnapshot.hasChildren()){
                                    Plane newPlane = dataSnapshot.getValue(Plane.class);
                                    if(newPlane != null) {
                                        Toast.makeText(getBaseContext(), "Newplane:" + newPlane.getDirection(), Toast.LENGTH_SHORT).show();
                                        planes.add(newPlane);
                                        aa.notifyDataSetChanged();
                                    }
                                }


//                                for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
////                                    Plane newPlane = areaSnapshot.getValue(Plane.class);
//                                    Plane newPlane = areaSnapshot.getValue(Plane.class);
//                                    Plane a1 = new Plane(newPlane.getTiming(),newPlane.getFlightNum(),newPlane.getDirection());
//                                    Toast.makeText(getBaseContext(),"Newplane:"+newPlane.toString(),Toast.LENGTH_SHORT).show();
//                                    planes.add(newPlane);
//                                    aa.notifyDataSetChanged();
//                                }



//                                planes.add(newPlane);
//                                aa.notifyDataSetChanged();
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



//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                AlertDialog.Builder mbuilder = new AlertDialog.Builder(SecondActivityAdmin.this);
//                View mView = getLayoutInflater().inflate(R.layout.activity_alert_airtraffic, null);
//                mbuilder.setTitle("Plane Information");
//                final Spinner mSpinner = (Spinner) mView.findViewById(R.id.spinner);
//
//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(SecondActivityAdmin.this,
//                        android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.DirectionList));
//                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                mSpinner.setAdapter(adapter);
//
//                mbuilder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        if(!mSpinner.getSelectedItem().toString().equalsIgnoreCase("Choose a Direction")){
//                            Toast.makeText(SecondActivityAdmin.this,mSpinner.getSelectedItem().toString(),Toast.LENGTH_LONG).show();
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
//
//            }
//        });

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
