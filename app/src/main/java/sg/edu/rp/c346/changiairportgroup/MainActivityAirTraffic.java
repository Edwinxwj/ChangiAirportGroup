package sg.edu.rp.c346.changiairportgroup;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

import java.util.ArrayList;

public class MainActivityAirTraffic extends AppCompatActivity {

    ListView lv;
    ArrayAdapter aa;
    ArrayList<String> gates;
    Spinner Spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_airtraffic);

        lv = (ListView) findViewById(R.id.lv);

        Spinner = (Spinner) findViewById(R.id.spinnerTerminal);

        gates = new ArrayList<String>();
        gates.add("E63");
        gates.add("E64");
        gates.add("E65");
        gates.add("E66");
        gates.add("E67");
        gates.add("E68");
        gates.add("E69");
        gates.add("E70");
        gates.add("E71");
        gates.add("E72");
        gates.add("E73");
        gates.add("E74");
        gates.add("E75");


        aa = new ArrayAdapter(this, android.R.layout.simple_list_item_1, gates);
        lv.setAdapter(aa);


        //Creating Spinner
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(MainActivityAirTraffic.this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.spinnersTerminal));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner.setAdapter(myAdapter);

        //Create on click for the list view
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedGates = gates.get(position).toString();
                Intent intent = new Intent(getBaseContext(), SecondActivityAirtraffic.class);
                intent.putExtra("gates", selectedGates.toString());

                startActivity(intent);
            }
        });

    }
    //Search bar

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
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
}
