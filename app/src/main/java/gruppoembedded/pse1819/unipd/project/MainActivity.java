package gruppoembedded.pse1819.unipd.project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import gruppoembedded.pse1819.unipd.project.tensorflowlite.ClassifierActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button epicButton = findViewById(R.id.button);

        epicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ClassifierActivity.class);
                startActivity(intent);
            }
        });
    }
}
