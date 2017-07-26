package sg.edu.rp.c346.changiairportgroup;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MainActivityAdmin extends AppCompatActivity {

    ListView lv;
    ArrayAdapter aa, myAdapter, myAdapter1,myAdapter2,myAdapter3;
    ArrayList<String> gates;
    private String TAG = "a";
    Spinner Spinner, spnTerm, spnTerm1, spnGate;
    String term;
    final ArrayList<String> gate = new ArrayList<>();


    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("terminals");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);
        lv = (ListView) findViewById(R.id.lv);

        gates = new ArrayList<String>();

        aa = new ArrayAdapter(this, android.R.layout.simple_list_item_1, gates);
        lv.setAdapter(aa);
        registerForContextMenu(lv);

        //Ordering the gateNumber in ascending order
//        Query query = databaseRef.orderByChild("gateNumber");


        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {


                final ArrayList<String> terminals = new ArrayList<>();

                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    String obj = areaSnapshot.child("terminal").getValue(String.class);
//                    Toast.makeText(getBaseContext(), obj, Toast.LENGTH_SHORT).show();

                    terminals.add(obj);
                }

                Spinner = (Spinner) findViewById(R.id.spinnerTerminal);
                myAdapter = new ArrayAdapter<String>(MainActivityAdmin.this,
                        android.R.layout.simple_spinner_item, terminals);
                myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Spinner.setAdapter(myAdapter);

                Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        term = (String)parent.getItemAtPosition(position);
//                        Log.d(TAG,dataSnapshot.getValue().toString());
                        gates.clear();
                        Query query = databaseRef.child(term).orderByKey();

                        query.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                String gateNum = dataSnapshot.child("gate").getValue(String.class);
                                if(gateNum != null){
                                    gates.add(gateNum);
                                    aa.notifyDataSetChanged();
                                    Toast.makeText(getBaseContext(),gateNum,Toast.LENGTH_SHORT).show();
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





//        databaseRef.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                Object obj = dataSnapshot.getKey();
////                Gate gate = dataSnapshot.getValue(Gate.class);
////                String gateNumber = gate.toString();
////                        String flightNo = gate.getFlightNo();
////                        String date = gate.getDate();
////                        String timing = gate.getTiming();
////                        String direction = gate.getDirection();
//
////                        String str = gateNumber + flightNo + date + timing + direction;
////                    String flight = gate.getFlightNo();
////                Toast.makeText(getBaseContext(),obj.toString(),Toast.LENGTH_LONG).show();
////                gates.add(gateNumber);
//
////                aa.notifyDataSetChanged();
////                for(DataSnapshot ds : dataSnapshot.getChildren()){
//////                    Object obj = ds.getKey();
//////                    Gate gate = ds.getValue(Gate.class);
//////                        String gateNumber = gate.toString();
////////                        String flightNo = gate.getFlightNo();
////////                        String date = gate.getDate();
////////                        String timing = gate.getTiming();
////////                        String direction = gate.getDirection();
//////
////////                        String str = gateNumber + flightNo + date + timing + direction;
////////                    String flight = gate.getFlightNo();
//////                    Toast.makeText(getBaseContext(),obj.toString(),Toast.LENGTH_LONG).show();
//////                    gates.add(gateNumber);
//////
//////                    aa.notifyDataSetChanged();
////                }
//
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainActivityAdmin.this ,SecondActivityAdmin.class);
                i.putExtra("gate", gates.get(position));
                i.putExtra("terminal",term);
                Toast.makeText(getBaseContext(),gates.get(position),Toast.LENGTH_SHORT).show();
                Toast.makeText(getBaseContext(),term,Toast.LENGTH_SHORT).show();
                startActivity(i);
            }
        });



    }
    @Override
    public void onCreateContextMenu (ContextMenu menu, View
            v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        //Context menu
        menu.add(Menu.NONE, 1, Menu.NONE, "Edit");
        menu.add(Menu.NONE, 2, Menu.NONE, "Delete");
    }
    @Override
    public boolean onContextItemSelected (MenuItem item){
        // TODO Auto-generated method stub
        AdapterView.AdapterContextMenuInfo menuinfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        long selectid = menuinfo.id; //_id from database in this case
        final int selectpos = menuinfo.position; //position in the adapter
        switch (item.getItemId()) {
            case 1: {
                final Query q1 = databaseRef.orderByChild("gateNumber").equalTo(lv.getItemAtPosition(selectpos).toString());
                q1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists()) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                //get the key of the child node that has to be updated
                                String postkey = ds.getRef().getKey();
                                Object terminal = ds.child("terminal").getValue();
//                                Toast.makeText(getBaseContext(), terminal.toString(),Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(MainActivityAdmin.this,EditActivity.class);
                                i.putExtra("gate", lv.getItemAtPosition(selectpos).toString());
//                                i.putExtra("terminal",terminal.toString());
                                i.putExtra("key",postkey);
                                startActivityForResult(i,1);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
//                Intent i = new Intent(MainActivityAdmin.this,EditActivity.class);
//                i.putExtra("gate", lv.getItemAtPosition(selectpos).toString());
//                startActivityForResult(i,1);
            }
            break;
            case 2: {

                Toast.makeText(getBaseContext(),lv.getItemAtPosition(selectpos).toString(),Toast.LENGTH_LONG).show();
                final Query applesQuery = databaseRef.orderByChild("gateNumber").equalTo(lv.getItemAtPosition(selectpos).toString());
                applesQuery.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        dataSnapshot.getRef().setValue(null);
                        gates.remove(selectpos);
                        aa.notifyDataSetChanged();


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
            break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                final String result = data.getStringExtra("result");
                final String terminal = data.getStringExtra("terminal");
                final String key = data.getStringExtra("key");
                final Query q1 = databaseRef.orderByKey().equalTo(key);
                q1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {

                                //get the key of the child node that has to be updated
                                databaseRef.child(key).child("gateNumber").setValue(result);
                                databaseRef.child(key).child("terminal").setValue(terminal);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
            if (resultCode == Activity.RESULT_CANCELED) {
            }
        }
    }//onActivityResult

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.addGate:
                LayoutInflater inflater =
                        (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View viewDialog = inflater.inflate(R.layout.add_gates, null);
                spnTerm = (Spinner) viewDialog.findViewById(R.id.spinnerTerm);

                databaseRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        final ArrayList<String> term = new ArrayList<>();

                        for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
                            String obj = areaSnapshot.child("terminal").getValue(String.class);
                            term.add(obj);
                        }

                        myAdapter1 = new ArrayAdapter<String>(MainActivityAdmin.this,
                                android.R.layout.simple_spinner_item, term);
                        myAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spnTerm.setAdapter(myAdapter1);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                //obtain the UI component in the input.xml layout
                final EditText etGate = (EditText)viewDialog.findViewById(R.id.editTextGate);

                AlertDialog.Builder myBuilder = new AlertDialog.Builder(MainActivityAdmin.this);

                //Set the view of the dialog
                myBuilder.setView(viewDialog);
                myBuilder.setTitle("Add Gate");

                myBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //Extract the Text entered by the user
                        String gate = etGate.getText().toString();
                        String terminal = spnTerm.getSelectedItem().toString();
//                        Gate newGate = new Gate(gate,terminal);
                        databaseRef.child(terminal).child(gate).child("gate").setValue(gate);
//                        gates.add(gate);
                        aa.notifyDataSetChanged();
                    }

                });

                myBuilder.setNegativeButton("Cancel",null);
                AlertDialog myDialog = myBuilder.create();
                myDialog.show();

                return true;

            case R.id.addTerminal:
                LayoutInflater inflater1 =
                        (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View viewDialog1 = inflater1.inflate(R.layout.add_terminal, null);

                //obtain the UI component in the input.xml layout
                final EditText etTerminal = (EditText)viewDialog1.findViewById(R.id.editTextTerminal);

                AlertDialog.Builder myBuilder1 = new AlertDialog.Builder(MainActivityAdmin.this);

                //Set the view of the dialog
                myBuilder1.setView(viewDialog1);
                myBuilder1.setTitle("Add Terminal");

                myBuilder1.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //Extract the Text entered by the user
                        String newterminal = etTerminal.getText().toString();
                        databaseRef.child(newterminal).child("terminal").setValue(newterminal);
//                        gates.add(gate);
                        myAdapter.notifyDataSetChanged();
                    }

                });

                myBuilder1.setNegativeButton("Cancel",null);
                AlertDialog myDialog1 = myBuilder1.create();
                myDialog1.show();

                return true;

            case R.id.addFlight:
                LayoutInflater inflater2 =
                        (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View viewDialog2 = inflater2.inflate(R.layout.add_flight, null);

                //obtain the UI component in the input.xml layout
                spnTerm1 = (Spinner) viewDialog2.findViewById(R.id.spinnerTerm1);
                spnGate = (Spinner) viewDialog2.findViewById(R.id.spinnerGate);
                final EditText etAirline = (EditText)viewDialog2.findViewById(R.id.editTextAirline);
                final EditText etDestination = (EditText)viewDialog2.findViewById(R.id.editTextDestination);
                final EditText etDirection = (EditText)viewDialog2.findViewById(R.id.editTextDirection);
                final EditText etFlightNo = (EditText)viewDialog2.findViewById(R.id.editTextFlightNo);
                final EditText etLicensePlate = (EditText)viewDialog2.findViewById(R.id.editTextLicensePlate);
                final EditText etDate= (EditText)viewDialog2.findViewById(R.id.editTextDate);
                final EditText etTime = (EditText)viewDialog2.findViewById(R.id.editTextTime);


                myAdapter3 = new ArrayAdapter<String>(MainActivityAdmin.this,
                        android.R.layout.simple_spinner_item, gate);
                myAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnGate.setAdapter(myAdapter3);

                databaseRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        final ArrayList<String> term1 = new ArrayList<>();

                        for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
                            String obj = areaSnapshot.child("terminal").getValue(String.class);
                            term1.add(obj);
                        }

                        myAdapter2 = new ArrayAdapter<String>(MainActivityAdmin.this,
                                android.R.layout.simple_spinner_item, term1);
                        myAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spnTerm1.setAdapter(myAdapter2);

                        spnTerm1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                               String term1 = (String)parent.getItemAtPosition(position);
                                gate.clear();
                                Query query = databaseRef.child(term1).orderByKey();

                                query.addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                                            String gateNum = dataSnapshot.child("gate").getValue(String.class);
                                            if(gateNum != null){
                                                gate.add(gateNum);
                                                myAdapter3.notifyDataSetChanged();
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





                AlertDialog.Builder myBuilder2 = new AlertDialog.Builder(MainActivityAdmin.this);

                //Set the view of the dialog
                myBuilder2.setView(viewDialog2);
                myBuilder2.setTitle("Add Flight Details");

                myBuilder2.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //Extract the Text entered by the user
                        String Destination = etDestination.getText().toString();
                        String Airline = etAirline.getText().toString();
                        String Direction = etDirection.getText().toString();
                        String LicensePlate = etLicensePlate.getText().toString();
                        String FlightNo = etFlightNo.getText().toString();
                        String Date = etDate.getText().toString();
                        String Time = etTime.getText().toString();


                        String terminal = spnTerm1.getSelectedItem().toString();
                        String Gate = spnGate.getSelectedItem().toString();

                        Plane plane1 = new Plane(LicensePlate,Long.parseLong(Time),Destination,FlightNo,Direction,Airline);
                        databaseRef.child(terminal).child(Gate).child(Date).child("date").setValue(Date);
                        databaseRef.child(terminal).child(Gate).child(Date).child(Time).setValue(plane1);

//                        gates.add(gate);
//                        myAdapter.notifyDataSetChanged();
                    }

                });

                myBuilder2.setNegativeButton("Cancel",null);
                AlertDialog myDialog2 = myBuilder2.create();
                myDialog2.show();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
