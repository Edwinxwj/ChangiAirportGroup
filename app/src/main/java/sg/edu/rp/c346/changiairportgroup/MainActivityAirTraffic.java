package sg.edu.rp.c346.changiairportgroup;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivityAirTraffic extends AppCompatActivity {

    ListView lv;
    ArrayAdapter aa;
    ArrayList<String> gates;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_airtraffic);

        lv = (ListView) findViewById(R.id.lv);

        gates = new ArrayList<String>();
        gates.add("E63");
        gates.add("E64");

        aa = new ArrayAdapter(this, android.R.layout.simple_list_item_1, gates);
        lv.setAdapter(aa);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                AlertDialog.Builder mbuilder = new AlertDialog.Builder(
//                        MainActivityAirTraffic.this);
//                View mView = getLayoutInflater().inflate(R.layout.activity_alert_airtraffic, null);
//                mbuilder.setTitle("Plane Information");
//                final Spinner mSpinner = (Spinner) mView.findViewById(R.id.spinner);
//
//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivityAirTraffic.this,
//                        android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.DirectionList));
//                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                mSpinner.setAdapter(adapter);
//
//                mbuilder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        if(!mSpinner.getSelectedItem().toString().equalsIgnoreCase("Choose a Direction")){
//                            Toast.makeText(MainActivityAirTraffic.this,mSpinner.getSelectedItem().toString(),Toast.LENGTH_LONG).show();
//                            dialogInterface.dismiss();
//                        }
//                    }
//                });
//
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
                Intent i = new Intent(MainActivityAirTraffic.this ,SecondActivityAirtraffic.class);
                i.putExtra("gate", gates.get(position));
                startActivity(i);



            }
        });

    }
}
