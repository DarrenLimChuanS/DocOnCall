package doc.on.call;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import doc.on.call.Utilities.Checker;
import doc.on.call.Utilities.CheckerDialog;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class FirstActivity extends AppCompatActivity {
    TextView screen1,screen2,screen3,screen4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        /*
         * ROOT DETECTION START
         */

        Checker check_root = new Checker(this);
        if(check_root.isDeviceRooted()) {
            CheckerDialog warning_dialog = new CheckerDialog(this);
            warning_dialog.DisplayDialog();
        }

        /*
         * ROOT DETECTION END
         */

        screen1=(TextView)findViewById(R.id.screen1);

        screen1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(FirstActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        screen2=(TextView)findViewById(R.id.screen2);

        screen2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(FirstActivity.this,SignInActivity.class);
                startActivity(intent);
            }
        });

        screen3=(TextView)findViewById(R.id.screen3);

        screen3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(FirstActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });

        screen4=(TextView)findViewById(R.id.screen4);

        screen4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(FirstActivity.this,PropertyDetailsActivity.class);
                startActivity(intent);
            }
        });
    }
}
