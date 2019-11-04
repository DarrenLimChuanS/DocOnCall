package doc.on.call;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import doc.on.call.Repository.PatientRepository;

public class FirstActivity extends AppCompatActivity {
    private PatientRepository mPatient;
    EditText editText;
    TextView screen1,screen2,screen3,screen4,screen5,resetPassword,resetPasswordValidate,changePassword,deleteAccount,validateDeleteAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        /*
         * ROOT DETECTION START
         */

//        Checker check_root = new Checker(this);
//        if(check_root.isDeviceRooted()) {
//            CheckerDialog warning_dialog = new CheckerDialog(this);
//            warning_dialog.DisplayDialog();
//        }

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

        mPatient = new PatientRepository(this);

        screen5=(TextView)findViewById(R.id.screen5);

        screen5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editText.getText().toString();
                mPatient.resendRegistrationToken(email);
            }
        });

        resetPassword=(TextView)findViewById(R.id.resetPassword);

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPatient.resetPasswordSendOTP("darrenlim");
            }
        });

        editText = findViewById(R.id.editText);
        resetPasswordValidate=(TextView)findViewById(R.id.resetPasswordValidate);

        resetPasswordValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            String otp = editText.getText().toString();
            mPatient.validateResetPassword(otp);
            }
        });

        final String username = "darrenlim";
        final String oldPassword = "password2!";
        final String newPassword = "password1!";

        changePassword=(TextView)findViewById(R.id.changePassword);

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPatient.changePassword(username,oldPassword,newPassword);
            }
        });

        deleteAccount=(TextView)findViewById(R.id.deleteAccount);

        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Delete account");
                mPatient.deletePatient();
            }
        });

        validateDeleteAccount=(TextView)findViewById(R.id.validateDeleteAccount);

        validateDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String appointmentId = editText.getText().toString();
//                mPatient.respondToDetailsPermission(appointmentId,true);
            }
        });
    }
}
