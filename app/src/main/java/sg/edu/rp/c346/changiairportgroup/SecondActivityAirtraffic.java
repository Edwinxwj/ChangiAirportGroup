package sg.edu.rp.c346.changiairportgroup;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by 15056215 on 20/6/2017.
 */

public class SecondActivityAirtraffic extends AppCompatActivity {

    ListView lv;
    TextView tvGateName;
    Spinner spnDate;
    ArrayAdapter aa;
    ArrayList<Plane> planes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_airtraffic);

        tvGateName = (TextView) findViewById(R.id.tvGate);

        spnDate = (Spinner) findViewById(R.id.spinner2);

        lv = (ListView) this.findViewById(R.id.lvPlane);
        planes = new ArrayList<Plane>();

        aa = new CustomAdapterAirtraffic(this, R.layout.row, planes);
        lv.setAdapter(aa);

//        Intent i = getIntent();
//        String year = i.getStringExtra("gates");
        Plane plane1 = new Plane("tvTiming", "tvLicensePlate","tvAirline","tvFlightNum","tvLicensePlate");

        planes.add(plane1);
        Intent i = getIntent();
        String gates = i.getStringExtra("gates");
        tvGateName.setText(gates);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder mbuilder = new AlertDialog.Builder(SecondActivityAirtraffic.this);
                View mView = getLayoutInflater().inflate(R.layout.activity_alert_airtraffic, null);
                mbuilder.setTitle("Plane Information");
                final Spinner mSpinner = (Spinner) mView.findViewById(R.id.spinner);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(SecondActivityAirtraffic.this,
                        android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.DirectionList));
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mSpinner.setAdapter(adapter);

                mbuilder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(!mSpinner.getSelectedItem().toString().equalsIgnoreCase("Choose a Direction")){
                            Toast.makeText(SecondActivityAirtraffic.this,mSpinner.getSelectedItem().toString(),Toast.LENGTH_LONG).show();
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

        spnDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                String Text = spnDate.getSelectedItem().toString();
//                tv.setText(Text);
                switch (i) {
                    case 0:

                        break;
                    case 1:
                        //Your code for Item 2 select
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }





}
