package doc.on.call;

import androidx.appcompat.app.AppCompatActivity;
import doc.on.call.Repository.PatientRepository;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chaos.view.PinView;

import static doc.on.call.Utilities.Commons.isOtpValid;
import static doc.on.call.Utilities.Commons.isUsernameValid;

public class ForgotPasswordActivity extends AppCompatActivity {
    private static final String TAG = ForgotPasswordActivity.class.getSimpleName();

    // Declare variables
    EditText etUsername;
    TextView btnResetPassword;
    TextView btnBack;
    LinearLayout llResetInputs;

    ProgressBar pbLoading;

    PinView pinView;
    TextView btnSubmit;
    TextView btnBack2;
    LinearLayout llOtpInputs;

    PatientRepository mPatient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Fetch variables
        etUsername = (EditText) findViewById(R.id.etUsername);
        btnResetPassword = (TextView) findViewById(R.id.btnResetPassword);
        btnBack = (TextView) findViewById(R.id.btnBack);
        llResetInputs = (LinearLayout) findViewById(R.id.llResetInputs);

        pbLoading = (ProgressBar) findViewById(R.id.pbLoading);

        pinView = (PinView) findViewById(R.id.pinView);
        btnSubmit = (TextView) findViewById(R.id.btnSubmit);
        btnBack2 = (TextView) findViewById(R.id.btnBack2);
        llOtpInputs = (LinearLayout) findViewById(R.id.llOtpInputs);

        mPatient = new PatientRepository(this);

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPatient();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToLogin();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitOTP();
            }
        });

        btnBack2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToReset();
            }
        });
    }

    /**
     * ================================= START OF FUNCTIONS =================================
     */
    public void resetPatient() {
        final String username = this.etUsername.getText().toString().trim();
        if (!isUsernameValid(username)) {
            etUsername.requestFocus();
            etUsername.setError(getString(R.string.error_username));
        } else {
            etUsername.setEnabled(false);
            btnResetPassword.setEnabled(false);
            btnBack.setEnabled(false);
            pbLoading.setVisibility(View.VISIBLE);

            // Send OTP to patient's registered email
            mPatient.resetPasswordSendOTP(username);
        }
    }

    public void backToLogin() {
        super.onBackPressed();
    }

    public void submitOTP() {
        String otp = pinView.getText().toString().trim();
        if (isOtpValid(otp)) {
            pinView.setEnabled(false);
            btnSubmit.setEnabled(false);
            btnBack2.setEnabled(false);
            pbLoading.setVisibility(View.VISIBLE);
            mPatient.validateResetPassword(otp);
        } else {
            pinView.requestFocus();
            pinView.setError(getString(R.string.error_otp));
        }
    }

    public void backToReset() {
        etUsername.setEnabled(true);
        btnResetPassword.setEnabled(true);
        btnBack.setEnabled(true);
        etUsername.getText().clear();
        llOtpInputs.setVisibility(View.GONE);
        llResetInputs.setVisibility(View.VISIBLE);
    }
    /**
     * ================================= END OF FUNCTIONS =================================
     */
}
