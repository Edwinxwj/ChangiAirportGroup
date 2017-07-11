package sg.edu.rp.c346.changiairportgroup;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

public class MainActivityAdmin extends AppCompatActivity {

    ListView lv;
    ArrayAdapter aa;
    ArrayList<String> gates;
    private String TAG = "a";
    DatabaseReference databaseRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);
        lv = (ListView) findViewById(R.id.lv);

        gates = new ArrayList<String>();


        aa = new ArrayAdapter(this, android.R.layout.simple_list_item_1, gates);
        lv.setAdapter(aa);
        registerForContextMenu(lv);

        databaseRef = FirebaseDatabase.getInstance().getReference().child("gate");

        //Ordering the gateNumber in ascending order
        Query query = databaseRef.orderByChild("gateNumber");

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Object obj = dataSnapshot.getKey();
                Gate gate = dataSnapshot.getValue(Gate.class);
                String gateNumber = gate.toString();
//                        String flightNo = gate.getFlightNo();
//                        String date = gate.getDate();
//                        String timing = gate.getTiming();
//                        String direction = gate.getDirection();

//                        String str = gateNumber + flightNo + date + timing + direction;
//                    String flight = gate.getFlightNo();
                Toast.makeText(getBaseContext(),obj.toString(),Toast.LENGTH_LONG).show();
                gates.add(gateNumber);

                aa.notifyDataSetChanged();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
//                    Object obj = ds.getKey();
//                    Gate gate = ds.getValue(Gate.class);
//                        String gateNumber = gate.toString();
////                        String flightNo = gate.getFlightNo();
////                        String date = gate.getDate();
////                        String timing = gate.getTiming();
////                        String direction = gate.getDirection();
//
////                        String str = gateNumber + flightNo + date + timing + direction;
////                    String flight = gate.getFlightNo();
//                    Toast.makeText(getBaseContext(),obj.toString(),Toast.LENGTH_LONG).show();
//                    gates.add(gateNumber);
//
//                    aa.notifyDataSetChanged();
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

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder mbuilder = new AlertDialog.Builder(
                        MainActivityAdmin.this);
                View mView = getLayoutInflater().inflate(R.layout.activity_alert_airtraffic, null);
                mbuilder.setTitle("Plane Information");
                final Spinner mSpinner = (Spinner) mView.findViewById(R.id.spinner);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivityAdmin.this,
                        android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.DirectionList));
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mSpinner.setAdapter(adapter);

                mbuilder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(!mSpinner.getSelectedItem().toString().equalsIgnoreCase("Choose a Direction")){
                            Toast.makeText(MainActivityAdmin.this,mSpinner.getSelectedItem().toString(),Toast.LENGTH_LONG).show();
                            dialogInterface.dismiss();
                        }
                    }
                });

                mbuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });

                mbuilder.setView(mView);
                AlertDialog dialog = mbuilder.create();
                dialog.show();
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
                                Toast.makeText(getBaseContext(), terminal.toString(),Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(MainActivityAdmin.this,EditActivity.class);
                                i.putExtra("gate", lv.getItemAtPosition(selectpos).toString());
                                i.putExtra("terminal",terminal.toString());
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
            case R.id.add:
                LayoutInflater inflater =
                        (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View viewDialog = inflater.inflate(R.layout.add_gates, null);

                //obtain the UI component in the input.xml layout
                final EditText etGate = (EditText)viewDialog.findViewById(R.id.editTextGate);
                final EditText etTerminal = (EditText)viewDialog.findViewById(R.id.editTextTerminal);

                AlertDialog.Builder myBuilder = new AlertDialog.Builder(MainActivityAdmin.this);

                //Set the view of the dialog
                myBuilder.setView(viewDialog);
                myBuilder.setTitle("Add Gate");

                myBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //Extract the Text entered by the user
                        String gate = etGate.getText().toString();
                        String terminal = etTerminal.getText().toString();
                        Gate newGate = new Gate(gate,terminal);
                        databaseRef.push().setValue(newGate);
//                        gates.add(gate);
                        aa.notifyDataSetChanged();
                    }

                });

                myBuilder.setNegativeButton("Cancel",null);
                AlertDialog myDialog = myBuilder.create();
                myDialog.show();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

}
