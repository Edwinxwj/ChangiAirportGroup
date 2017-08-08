package sg.edu.rp.c346.changiairportgroup;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
    Spinner spnDate, spnGate, spnTerm1;
    ArrayAdapter aa,myAdapter2,myAdapter3;
    ArrayList<Plane> planes;
    DatabaseReference databaseRef;
    String selected;
    String term;
    final ArrayList<String> gate = new ArrayList<>();


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
        registerForContextMenu(lv);


        Intent i = getIntent();
        final String gates = i.getStringExtra("gate");
        term = i.getStringExtra("terminal");
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
//
//    @Override
//    public void onCreateContextMenu (ContextMenu menu, View
//            v, ContextMenu.ContextMenuInfo menuInfo){
//        super.onCreateContextMenu(menu, v, menuInfo);
//        //Context menu
//        menu.add(Menu.NONE, 1, Menu.NONE, "Edit");
//        menu.add(Menu.NONE, 2, Menu.NONE, "Delete");
//    }
//
//    @Override
//    public boolean onContextItemSelected (MenuItem item){
//        // TODO Auto-generated method stub
//        AdapterView.AdapterContextMenuInfo menuinfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
//        long selectid = menuinfo.id; //_id from database in this case
//        final int selectpos = menuinfo.position; //position in the adapter
//        switch (item.getItemId()) {
//            case 1: {
//                LayoutInflater inflater2 =
//                        (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                View viewDialog2 = inflater2.inflate(R.layout.add_flight, null);
//
//                //obtain the UI component in the input.xml layout
//                spnTerm1 = (Spinner) viewDialog2.findViewById(R.id.spinnerTerm1);
//                spnGate = (Spinner) viewDialog2.findViewById(R.id.spinnerGate);
//                final EditText etAirline = (EditText)viewDialog2.findViewById(R.id.editTextAirline);
//                final EditText etDestination = (EditText)viewDialog2.findViewById(R.id.editTextDestination);
//                final EditText etDirection = (EditText)viewDialog2.findViewById(R.id.editTextDirection);
//                final EditText etFlightNo = (EditText)viewDialog2.findViewById(R.id.editTextFlightNo);
//                final EditText etLicensePlate = (EditText)viewDialog2.findViewById(R.id.editTextLicensePlate);
//                final EditText etDate= (EditText)viewDialog2.findViewById(R.id.editTextDate);
//                final EditText etTime = (EditText)viewDialog2.findViewById(R.id.editTextTime);
//
//
//                myAdapter3 = new ArrayAdapter<String>(SecondActivityAdmin.this,
//                        android.R.layout.simple_spinner_item, gate);
//                myAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                spnGate.setAdapter(myAdapter3);
//
//                final Plane currentPlane = planes.get(selectpos);
////                spnGate.setSelection(gate);
//
//                databaseRef.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//
//
//                        final ArrayList<String> term1 = new ArrayList<>();
//
//                        for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
//                            String obj = areaSnapshot.child("terminal").getValue(String.class);
//                            term1.add(obj);
//                        }
//
//                        myAdapter2 = new ArrayAdapter<String>(SecondActivityAdmin.this,
//                                android.R.layout.simple_spinner_item, term1);
//                        myAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        spnTerm1.setAdapter(myAdapter2);
//
//                        spnTerm1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                            @Override
//                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                                String term1 = (String)parent.getItemAtPosition(position);
//                                gate.clear();
//                                Query query = databaseRef.child(term1).orderByKey();
//
//                                query.addChildEventListener(new ChildEventListener() {
//                                    @Override
//                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//
//                                        String gateNum = dataSnapshot.child("gate").getValue(String.class);
//                                        if(gateNum != null){
//                                            gate.add(gateNum);
//                                            myAdapter3.notifyDataSetChanged();
//                                        }
//                                    }
//
//
//                                    @Override
//                                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//                                    }
//
//                                    @Override
//                                    public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//                                    }
//
//                                    @Override
//                                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//                                    }
//
//                                    @Override
//                                    public void onCancelled(DatabaseError databaseError) {
//
//                                    }
//                                });
//
//
//                            }
//
//                            @Override
//                            public void onNothingSelected(AdapterView<?> parent) {
//
//                            }
//                        });
//
//
//
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//
//
//
//
//
//                android.support.v7.app.AlertDialog.Builder myBuilder2 = new android.support.v7.app.AlertDialog.Builder(MainActivityAdmin.this);
//
//                //Set the view of the dialog
//                myBuilder2.setView(viewDialog2);
//                myBuilder2.setTitle("Add Flight Details");
//
//                myBuilder2.setPositiveButton("Add", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        //Extract the Text entered by the user
//                        String Destination = etDestination.getText().toString();
//                        String Airline = etAirline.getText().toString();
//                        String Direction = etDirection.getText().toString();
//                        String LicensePlate = etLicensePlate.getText().toString();
//                        String FlightNo = etFlightNo.getText().toString();
//                        String Date = etDate.getText().toString();
//                        String Time = etTime.getText().toString();
//
//
//                        String terminal = spnTerm1.getSelectedItem().toString();
//                        String Gate = spnGate.getSelectedItem().toString();
//
//                        Plane plane1 = new Plane(LicensePlate,Long.parseLong(Time),Destination,FlightNo,Direction,Airline);
//                        databaseRef.child(terminal).child(Gate).child(Date).child("date").setValue(Date);
//                        databaseRef.child(terminal).child(Gate).child(Date).child(Time).setValue(plane1);
//
////                        gates.add(gate);
////                        myAdapter.notifyDataSetChanged();
//                    }
//
//                });
//
//                myBuilder2.setNegativeButton("Cancel",null);
//                android.support.v7.app.AlertDialog myDialog2 = myBuilder2.create();
//                myDialog2.show();
//
//
//                final Query q1 = databaseRef.child(term).orderByKey().equalTo(lv.getItemAtPosition(selectpos).toString());
//                q1.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        if(dataSnapshot.exists()) {
//                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                                //get the key of the child node that has to be updated
//                                String postkey = ds.getRef().getKey();
//                                Object terminal = ds.child("terminal").getValue();
////                                Toast.makeText(getBaseContext(), terminal.toString(),Toast.LENGTH_SHORT).show();
//                                Intent i = new Intent(SecondActivityAdmin.this,EditActivity.class);
//                                i.putExtra("gate", lv.getItemAtPosition(selectpos).toString());
//                                i.putExtra("terminal",term);
//                                i.putExtra("key",postkey);
//                                startActivityForResult(i,1);
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
////                Intent i = new Intent(MainActivityAdmin.this,EditActivity.class);
////                i.putExtra("gate", lv.getItemAtPosition(selectpos).toString());
////                startActivityForResult(i,1);
//            }
//            break;
//            case 2: {
//
//                Toast.makeText(getBaseContext(),lv.getItemAtPosition(selectpos).toString(),Toast.LENGTH_LONG).show();
//                final Query applesQuery = databaseRef.child().orderByChild().equalTo(lv.getItemAtPosition(selectpos).toString());
//                applesQuery.addChildEventListener(new ChildEventListener() {
//                    @Override
//                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                        dataSnapshot.getRef().setValue(null);
//                        planes.remove(selectpos);
//                        aa.notifyDataSetChanged();
//
//
//                    }
//
//                    @Override
//                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//                    }
//
//                    @Override
//                    public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//                    }
//
//                    @Override
//                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//
//            }
//            break;
//        }
//        return super.onContextItemSelected(item);
//    }





}
