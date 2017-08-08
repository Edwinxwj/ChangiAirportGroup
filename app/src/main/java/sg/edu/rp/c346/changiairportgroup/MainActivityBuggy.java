package sg.edu.rp.c346.changiairportgroup;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivityBuggy extends AppCompatActivity {

    ListView lv;
    ArrayAdapter aa;
    ArrayList<String> gates;
    private String TAG = "a";
    DatabaseReference databaseRef;
    Spinner Spinner;
    String term;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]
    private FirebaseAuth.AuthStateListener mAuthListener;

    private Toolbar aToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_buggy_terminal);

        aToolbar = (Toolbar) findViewById(R.id.buggy_terminal_page_toolbar);
        setSupportActionBar(aToolbar);
        getSupportActionBar().setTitle("Gates Page");

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    Intent LoginIntent = new Intent(MainActivityBuggy.this,LoginActivity.class);
                    LoginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(LoginIntent);
                }
            }
        };

        lv = (ListView) findViewById(R.id.lv);

        Spinner = (Spinner) findViewById(R.id.spinnerTerminal);

        gates = new ArrayList<String>();


        aa = new ArrayAdapter(this, android.R.layout.simple_list_item_1, gates);
        lv.setAdapter(aa);

        databaseRef = FirebaseDatabase.getInstance().getReference("terminals");

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                final ArrayList<String> terminals = new ArrayList<>();

                for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
                    String obj = areaSnapshot.child("terminal").getValue(String.class);
                    terminals.add(obj.toString());
                }

                Spinner = (Spinner) findViewById(R.id.spinnerTerminal);
                ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(MainActivityBuggy.this,
                        android.R.layout.simple_spinner_item, terminals);
                myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Spinner.setAdapter(myAdapter);

                Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        term = (String)parent.getItemAtPosition(position);
//                        Toast.makeText(getBaseContext(),term,Toast.LENGTH_SHORT).show();
                        gates.clear();
                        Query query = databaseRef.child(term).orderByKey();
                        query.addChildEventListener(new ChildEventListener() {
                            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                String gateNum = dataSnapshot.child("gate").getValue(String.class);
                                if(gateNum != null){
                                    gates.add(gateNum);
                                    aa.notifyDataSetChanged();
//                                    Toast.makeText(getBaseContext(),gateNum,Toast.LENGTH_SHORT).show();
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
//
//        //Creating Spinner
//        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(MainActivityAirTraffic.this,
//                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.spinnersTerminal));
//        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        Spinner.setAdapter(myAdapter);



        //Create on click for the list view
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedGates = gates.get(position);
                Intent intent = new Intent(MainActivityBuggy.this, Main2ActivityBuggy.class);
                intent.putExtra("gates", selectedGates);
                intent.putExtra("terminal",term);
                startActivity(intent);
            }
        });

    }
    //Search bar

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        inflater.inflate(R.menu.main_logout, menu);
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


        return super.onCreateOptionsMenu(menu);
    }
    @Override
    protected void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.logout:
                logout();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void logout(){
        mAuth.signOut();
    }


}
