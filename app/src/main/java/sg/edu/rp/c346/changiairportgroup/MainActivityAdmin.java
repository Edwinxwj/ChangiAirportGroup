package sg.edu.rp.c346.changiairportgroup;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import sg.edu.rp.c346.changiairportgroup.Chat.MainActivity;


public class MainActivityAdmin extends AppCompatActivity {

    ListView lv;
    ArrayAdapter aa, myAdapter, myAdapter1,myAdapter2,myAdapter3;
    ArrayList<String> gates;
    private String TAG = "a";
    Spinner Spinner, spnTerm, spnTerm1, spnGate;
    String term;
    String termKey;
    String key;
    final ArrayList<String> gate = new ArrayList<>();

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]
    private FirebaseAuth.AuthStateListener mAuthListener;


    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("terminals");

    private Toolbar aToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);

        aToolbar = (Toolbar) findViewById(R.id.admin_page_toolbar);
        setSupportActionBar(aToolbar);
        getSupportActionBar().setTitle("Admin Page");


        Toast.makeText(getBaseContext(), "Admin", Toast.LENGTH_SHORT).show();


        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent LoginIntent = new Intent(MainActivityAdmin.this, LoginActivity.class);
                    LoginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(LoginIntent);
                }
            }
        };

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
                        term = (String) parent.getItemAtPosition(position);
//                        Toast.makeText(getBaseContext(), term, Toast.LENGTH_SHORT).show();

//                        Log.d(TAG,dataSnapshot.getValue().toString());
                        gates.clear();
                        Query query = databaseRef.orderByChild("terminal").equalTo(term);

                        query.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                termKey = dataSnapshot.getKey().toString();
                                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                                    String gateNum = areaSnapshot.child("gate").getValue(String.class);
                                    if (gateNum != null) {
                                        gates.add(gateNum);
                                        aa.notifyDataSetChanged();
//                                        Toast.makeText(getBaseContext(),gateNum,Toast.LENGTH_SHORT).show();
                                    }
                                    aa.notifyDataSetChanged();
                                }
//                                String gateNum = dataSnapshot.getKey().toString();
////                                String gateNum = dataSnapshot.child("gate").getValue(String.class);
//                                Toast.makeText(getBaseContext(),gateNum,Toast.LENGTH_SHORT).show();
//
//                                if (gateNum != null) {
//                                    gates.add(gateNum);
//                                    aa.notifyDataSetChanged();
////                                    Toast.makeText(getBaseContext(),gateNum,Toast.LENGTH_SHORT).show();
//                                }
                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                                gates.clear();
                                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                                    String gateNum = areaSnapshot.child("gate").getValue(String.class);
                                    if (gateNum != null) {
                                        gates.add(gateNum);
                                        aa.notifyDataSetChanged();
//                                        Toast.makeText(getBaseContext(),gateNum,Toast.LENGTH_SHORT).show();
                                    }
                                    aa.notifyDataSetChanged();
                                }
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
                Intent i = new Intent(MainActivityAdmin.this, SecondActivityAdmin.class);
                i.putExtra("gate", gates.get(position));
                i.putExtra("terminal", term);
                i.putExtra("termKey", termKey);
//                Toast.makeText(getBaseContext(), termKey, Toast.LENGTH_SHORT).show();
//                Toast.makeText(getBaseContext(), gates.get(position), Toast.LENGTH_SHORT).show();
//                Toast.makeText(getBaseContext(), term, Toast.LENGTH_SHORT).show();
                startActivity(i);
            }
        });
    }
//
@Override
public void onCreateContextMenu (ContextMenu menu, View
        v, ContextMenu.ContextMenuInfo menuInfo){
    super.onCreateContextMenu(menu, v, menuInfo);
    //Context menu
    menu.add(Menu.NONE, 1, Menu.NONE, "Edit Gate");
    menu.add(Menu.NONE, 2, Menu.NONE, "Delete Gate");
}

    @Override
    public boolean onContextItemSelected (MenuItem item) {
        // TODO Auto-generated method stub
        AdapterView.AdapterContextMenuInfo menuinfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        long selectid = menuinfo.id; //_id from database in this case
        final int selectpos = menuinfo.position; //position in the adapter
        switch (item.getItemId()) {
            case 1: {
                android.app.AlertDialog.Builder mbuilder = new android.app.AlertDialog.Builder(MainActivityAdmin.this);
                LayoutInflater inflater2 =
                        (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View viewDialog2 = inflater2.inflate(R.layout.edit_gate, null);
                mbuilder.setTitle("Edit Gate");
                final EditText editGate = (EditText) viewDialog2.findViewById(R.id.editTextEditGate);

                editGate.setText(lv.getItemAtPosition(selectpos).toString());

                mbuilder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        final Query q1 = databaseRef.child(termKey).orderByChild("gate").equalTo(lv.getItemAtPosition(selectpos).toString());
                        q1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    String obj = ds.getKey().toString(); //same as timeKey
//                                    Toast.makeText(MainActivityAdmin.this,"obj: "+obj,Toast.LENGTH_LONG).show();
                                    databaseRef.child(termKey).child(obj).child("gate").setValue(editGate.getText().toString());
                                    aa.notifyDataSetChanged();
                                }
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
                android.app.AlertDialog dialog = mbuilder.create();
                dialog.show();
            }
            break;

            case 2: {
//                Toast.makeText(getBaseContext(),gates.get(selectpos).toString(),Toast.LENGTH_LONG).show();

                final Query q1 = databaseRef.child(termKey).orderByChild("gate").equalTo(lv.getItemAtPosition(selectpos).toString());
                q1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String a = ds.getKey().toString();
//                            Toast.makeText(getBaseContext(),"query"+a,Toast.LENGTH_LONG).show();
                            databaseRef.child(termKey).child(a).removeValue();
                            aa.notifyDataSetChanged();

                        }
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
    protected void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add, menu);
        MenuItem item = menu.findItem(R.id.SearchId);
        SearchView searchView = (SearchView)item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                aa.getFilter().filter(newText);
                return true;
            }
        });

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.chat:
                Intent intent = new Intent(MainActivityAdmin.this, MainActivity.class);
                startActivity(intent);
                return true;

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
                        String terminal = spnTerm.getSelectedItem().toString();
                        Query queryAdd = databaseRef.orderByChild("terminal").equalTo(terminal);

                        queryAdd.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                String newTermKey = dataSnapshot.getKey().toString();
                                String gate = etGate.getText().toString();
                                databaseRef.child(newTermKey).push().child("gate").setValue(gate);
                                gates.add(gate);
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
                        databaseRef.push().child("terminal").setValue(newterminal);
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
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                String formattedDate = df.format(c.getTime());
                // formattedDate have current date/time
//                Toast.makeText(this, formattedDate, Toast.LENGTH_SHORT).show();

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

                etDate.setText(formattedDate);

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
                                Query query = databaseRef.orderByChild("terminal").equalTo(term1);

                                query.addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                        key = dataSnapshot.getKey().toString(); //TERMKEY
//                                        Toast.makeText(getBaseContext(),"Key: "+key,Toast.LENGTH_SHORT).show();

                                        for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                                            String gateNum = areaSnapshot.child("gate").getValue(String.class);
                                            if (gateNum != null) {
                                                gate.add(gateNum);
                                                myAdapter3.notifyDataSetChanged();
//                                        Toast.makeText(getBaseContext(),gateNum,Toast.LENGTH_SHORT).show();
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





                AlertDialog.Builder myBuilder2 = new AlertDialog.Builder(MainActivityAdmin.this);

                //Set the view of the dialog
                myBuilder2.setView(viewDialog2);
                myBuilder2.setTitle("Add Flight Details");

                myBuilder2.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Query queryAdd = databaseRef.child(key).orderByChild("gate").equalTo(spnGate.getSelectedItem().toString());
                        queryAdd.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                                String newgateK = dataSnapshot.getKey().toString();
//                               Toast.makeText(getBaseContext(),"new gateK:" +newgateK,Toast.LENGTH_SHORT).show();
                                String Destination = etDestination.getText().toString();
                                String Airline = etAirline.getText().toString();
                                String Direction = etDirection.getText().toString();
                                String LicensePlate = etLicensePlate.getText().toString();
                                String FlightNo = etFlightNo.getText().toString();
                                String Date = etDate.getText().toString();
                                Long Time = Long.parseLong(etTime.getText().toString());
                                Plane plane1 = new Plane(LicensePlate,Time,Destination,FlightNo,Direction,Airline);

                                String uID = databaseRef.child(key).child(newgateK).push().getKey().toString();
                                databaseRef.child(key).child(newgateK).child(uID).child("date").setValue(Date);
                                databaseRef.child(key).child(newgateK).child(uID).child(Time.toString()).setValue(plane1);

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

                });

                myBuilder2.setNegativeButton("Cancel",null);
                AlertDialog myDialog2 = myBuilder2.create();
                myDialog2.show();

                return true;

            case R.id.logout:
                logout();

                return true;

//            case R.id.SearchId:
//                SearchView searchView = (SearchView)item.getActionView();
//
//                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                    @Override
//                    public boolean onQueryTextSubmit(String query) {
//                        return true;
//                    }
//
//                    @Override
//                    public boolean onQueryTextChange(String newText) {
//                        aa.getFilter().filter(newText);
//                        return true;
//                    }
//                });
//                return true;

            case R.id.addUser:
                Intent i = new Intent(MainActivityAdmin.this, RegisterActivity.class);
                startActivity(i);
                return true;

            case R.id.editTerm:
                Intent intent1 = new Intent(MainActivityAdmin.this,EditActivity.class);
                intent1.putExtra("termKey",term);
                startActivity(intent1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void logout(){
        mAuth.signOut();
    }

}
