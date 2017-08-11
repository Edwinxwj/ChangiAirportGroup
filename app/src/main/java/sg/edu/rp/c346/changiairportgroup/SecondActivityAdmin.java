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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static sg.edu.rp.c346.changiairportgroup.R.id.parent;

/**
 * Created by 15056215 on 20/6/2017.
 */

public class SecondActivityAdmin extends AppCompatActivity {

    ListView lv;
    TextView tvGateName;
    Spinner spnDate, spnGate, spnTerm1;
    ArrayAdapter aa,myAdapter2,myAdapter3;
    ArrayList<Plane> planes;
    String selected,gates;
    String term,termKey, gateKey, dateKey, timeKey;
    final ArrayList<String> gate = new ArrayList<>();


    private Toolbar aToolbar;
    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("terminals");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_admin);

        aToolbar = (Toolbar) findViewById(R.id.admin_second_page_toolbar);
        setSupportActionBar(aToolbar);
        getSupportActionBar().setTitle("Admin Flight Page");

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        final String formattedDate = df.format(c.getTime());
        // formattedDate have current date/time
//        Toast.makeText(this, formattedDate, Toast.LENGTH_SHORT).show();

//        Calendar yesterday = Calendar.getInstance();
//        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
//        yesterday.add(Calendar.DATE, -1);
//        Toast.makeText(this, yesterday, Toast.LENGTH_SHORT).show();


        tvGateName = (TextView) findViewById(R.id.tvGate);

//        spnDate = (Spinner) findViewById(R.id.spinner2);

        lv = (ListView) this.findViewById(R.id.lvPlane);
        planes = new ArrayList<Plane>();

        aa = new CustomAdapterAdmin(this, R.layout.rowadmin, planes);
        lv.setAdapter(aa);
        registerForContextMenu(lv);


        Intent i = getIntent();
        gates = i.getStringExtra("gate");
        term = i.getStringExtra("terminal");
        termKey = i.getStringExtra("termKey");
        tvGateName.setText(gates);




        final Query querydate = databaseRef.child(termKey).orderByChild("gate").equalTo(gates);

        querydate.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                gateKey = dataSnapshot.getKey().toString(); //gate1/gate2 etc
//                Toast.makeText(getBaseContext(), "gateKey:" + gateKey, Toast.LENGTH_SHORT).show();
                final ArrayList<String> date = new ArrayList<>();

                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    String obj = areaSnapshot.child("date").getValue(String.class);
//                    Toast.makeText(getBaseContext(), "date:" + obj, Toast.LENGTH_SHORT).show();
                    if (obj != null) {
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
                        selected = (String) parent.getItemAtPosition(position);
//                        Toast.makeText(getBaseContext(), selected, Toast.LENGTH_SHORT).show();
                        Query query = databaseRef.child(termKey).child(gateKey).orderByChild("date").equalTo(selected);
                        query.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                dateKey = dataSnapshot.getKey().toString();
//                                Toast.makeText(getBaseContext(), "datekey: " + dateKey, Toast.LENGTH_SHORT).show();
                                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                                    if (areaSnapshot.hasChildren()) {
                                        timeKey = areaSnapshot.getKey().toString();
//                                        Toast.makeText(getBaseContext(), "TimeKey:" + timeKey, Toast.LENGTH_SHORT).show();
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

//
    }
    @Override
    public void onCreateContextMenu (ContextMenu menu, View
            v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        //Context menu
        menu.add(Menu.NONE, 1, Menu.NONE, "Edit Flight");
        menu.add(Menu.NONE, 2, Menu.NONE, "Delete Flight");
    }

    @Override
    public boolean onContextItemSelected (MenuItem item){
        // TODO Auto-generated method stub
        AdapterView.AdapterContextMenuInfo menuinfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        long selectid = menuinfo.id; //_id from database in this case
        final int selectpos = menuinfo.position; //position in the adapter
        switch (item.getItemId()) {
            case 1: {
                AlertDialog.Builder mbuilder = new AlertDialog.Builder(SecondActivityAdmin.this);
                LayoutInflater inflater2 =
                        (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View viewDialog2 = inflater2.inflate(R.layout.edit_flight, null);
                mbuilder.setTitle("Edit Flight Information");
                final TextView tvTerm = (TextView)viewDialog2.findViewById(R.id.spinnerTerm1);
                final TextView tvGate = (TextView)viewDialog2.findViewById(R.id.spinnerGate);
                final TextView tvDirection = (TextView)viewDialog2.findViewById(R.id.editTextDirection);
                final EditText etAirline = (EditText)viewDialog2.findViewById(R.id.editTextAirline);
                final EditText etDestination = (EditText)viewDialog2.findViewById(R.id.editTextDestination);
                final EditText etFlightNo = (EditText)viewDialog2.findViewById(R.id.editTextFlightNo);
                final EditText etLicensePlate = (EditText)viewDialog2.findViewById(R.id.editTextLicensePlate);
                final EditText etDate= (EditText)viewDialog2.findViewById(R.id.editTextDate);
                final EditText etTime = (EditText)viewDialog2.findViewById(R.id.editTextTime);

                final Plane currentPlane = planes.get(selectpos);
//                Toast.makeText(SecondActivityAirtraffic.this,currentPlane.getAirline(),Toast.LENGTH_LONG).show();
                tvTerm.setText(term);
                tvGate.setText(gates);
                etAirline.setText(currentPlane.getAirline());
                etDestination.setText(currentPlane.getDestination());
                etFlightNo.setText(currentPlane.getFlightNo());
                etTime.setText(currentPlane.getTime().toString());
                etLicensePlate.setText(currentPlane.getLicensePlate());
                tvDirection.setText(currentPlane.getDirection());
                etDate.setText(selected);

                mbuilder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        final Query q1 = databaseRef.child(termKey).child(gateKey).child(dateKey).orderByChild("licensePlate").equalTo(currentPlane.getLicensePlate());
                        q1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String obj = dataSnapshot.getKey().toString(); //same as timeKey
//                                Toast.makeText(SecondActivityAdmin.this,"obj: "+obj,Toast.LENGTH_LONG).show();
                                String Destination = etDestination.getText().toString();
                                String Airline = etAirline.getText().toString();
                                String LicensePlate = etLicensePlate.getText().toString();
                                String FlightNo = etFlightNo.getText().toString();
                                String Date = etDate.getText().toString();
                                String Direction = tvDirection.getText().toString();
                                Long Time = Long.parseLong(etTime.getText().toString());

                                Plane plane1 = new Plane(LicensePlate,Time,Destination,FlightNo, Direction,Airline);

                                databaseRef.child(termKey).child(gateKey).child(dateKey).child(obj).setValue(plane1);
                                databaseRef.child(termKey).child(gateKey).child(dateKey).child("date").setValue(Date);
                                aa.notifyDataSetChanged();

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
//                            Toast.makeText(SecondActivityAirtraffic.this,mSpinner.getSelectedItem().toString(),Toast.LENGTH_LONG).show();
                        dialogInterface.dismiss();

                    }
                });

                mbuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });

                mbuilder.setView(viewDialog2);
                AlertDialog dialog = mbuilder.create();
                dialog.show();
            }
            break;

            case 2: {
//                Toast.makeText(getBaseContext(),planes.get(selectpos).getLicensePlate().toString(),Toast.LENGTH_LONG).show();

                final Query q1 = databaseRef.child(termKey).child(gateKey).child(dateKey).orderByChild("licensePlate").equalTo(planes.get(selectpos).getLicensePlate().toString());
                q1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()){
                            String a = ds.getKey().toString();
//                            Toast.makeText(getBaseContext(),"query"+a,Toast.LENGTH_LONG).show();
                            databaseRef.child(termKey).child(gateKey).child(dateKey).child(a).removeValue();
                            aa.notifyDataSetChanged();

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
//                q1.addChildEventListener(new ChildEventListener() {
//                    @Override
//                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                        Toast.makeText(getBaseContext(),"query",Toast.LENGTH_LONG).show();
//                        if(dataSnapshot.hasChildren()) {
//                            dataSnapshot.getRef().setValue(null);
//                            planes.remove(selectpos);
//                            aa.notifyDataSetChanged();
//                        }
//
//
//                    }
//

            }
            break;
        }
        return super.onContextItemSelected(item);
    }



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





}
