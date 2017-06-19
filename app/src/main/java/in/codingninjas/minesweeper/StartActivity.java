package in.codingninjas.minesweeper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class StartActivity extends AppCompatActivity {

    EditText editText;
    TextView nameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        editText = (EditText) findViewById(R.id.editText);
        nameTextView = (TextView) findViewById(R.id.textView);

        final SharedPreferences sharedPreferences = getSharedPreferences("Minesweeper", MODE_PRIVATE);
        String name = sharedPreferences.getString("user_name",null);
        if (name == null) {
            nameTextView.setText("Welcome, User");
        } else {
            nameTextView.setText("Welcome, " + name);
        }

        final Button startButton = (Button) findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editText.getText().toString();
                if (name.isEmpty()) {
                    Toast.makeText(StartActivity.this, "Enter Name !!", Toast.LENGTH_SHORT);
                    return;
                }

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("user_name", name);
                editor.commit();
                Intent intent = new Intent(StartActivity.this,MainActivity.class);
                intent.putExtra("user_name", name);
                startActivity(intent);
            }
        });
    }
}
