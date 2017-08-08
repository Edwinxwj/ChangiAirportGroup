package sg.edu.rp.c346.changiairportgroup;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;

public class EditActivity extends AppCompatActivity {

    EditText editGate,editTerminal;
    Button btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        editGate = (EditText)findViewById(R.id.editTextEditGate);
        editTerminal = (EditText)findViewById(R.id.editTextEditTerminal);

        btnUpdate = (Button)findViewById(R.id.buttonUpdate);

        Intent intent = getIntent();
        String gate = intent.getStringExtra("gate");
        String terminal = intent.getStringExtra("terminal");
        final String key = intent.getStringExtra("key");
        editGate.setText(gate);
        editTerminal.setText(terminal);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();

                returnIntent.putExtra("result",editGate.getText().toString());
                returnIntent.putExtra("terminal",editTerminal.getText().toString());
                returnIntent.putExtra("key",key);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();

            }
        });

    }
}
