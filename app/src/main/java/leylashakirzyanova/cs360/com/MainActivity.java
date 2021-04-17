package leylashakirzyanova.cs360.com;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    Button mapActivity;
    Button menuView;
    Button logInActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    protected void onResume() {
        super.onResume();

        // Opens Google Maps with the location of the Local Coffee Shop when pressing the "Map" button on the main screen
        mapActivity = findViewById(R.id.map);
        mapActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MapsActivity.class));
            }
        });

        // Initiate Firebase Authentication
        logInActivity = findViewById(R.id.signInActivityButton);
        logInActivity.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SignInActivity.class));
            }
        }));
    }
}
