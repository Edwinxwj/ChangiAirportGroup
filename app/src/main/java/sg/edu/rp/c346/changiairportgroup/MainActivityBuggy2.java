package sg.edu.rp.c346.changiairportgroup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivityBuggy2 extends AppCompatActivity {

    TextView tvGate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buggy);

        tvGate = (TextView) findViewById(R.id.textViewGates);

        Intent i = getIntent();
        tvGate.setText(i.getStringExtra("gate"));
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.option_menu,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == R.id.logout) {
//            Intent i = new Intent(getApplicationContext(),MainActivity.class);
//            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
