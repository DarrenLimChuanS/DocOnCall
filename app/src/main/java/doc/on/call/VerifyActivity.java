package doc.on.call;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import doc.on.call.Repository.PatientRepository;

public class VerifyActivity extends AppCompatActivity {
    private static final String TAG = VerifyActivity.class.getSimpleName();

    // Declare variables
    EditText etUsername;
    EditText etPassword;
    ImageView imgPassword;
    EditText etToken;
    TextView btnVerify;
    TextView btnBack;
    TextView tvResendToken;

    ProgressBar pbLoading;

    PatientRepository mPatient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        // Fetch variables
        etUsername = (EditText)findViewById(R.id.etUsername);
        etPassword = (EditText)findViewById(R.id.etPassword);
        imgPassword = (ImageView)findViewById(R.id.imgPassword);
        etToken = (EditText)findViewById(R.id.etToken);
        btnVerify = (TextView)findViewById(R.id.btnVerify);
        btnBack = (TextView)findViewById(R.id.btnBack);
        tvResendToken = (TextView)findViewById(R.id.tvResendToken);

        pbLoading = (ProgressBar)findViewById(R.id.pbLoading);

        mPatient = new PatientRepository(this);

        // Event Listeners
        imgPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                return true;
            }
        });

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyPatient();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToLogin();
            }
        });

        tvResendToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resendToken();
            }
        });
    }
    /**
     * ================================= START OF FUNCTIONS =================================
     */
    public void verifyPatient() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String token = etToken.getText().toString().trim();
        if (!isUsernameValid(username)) {
            etUsername.requestFocus();
            etUsername.setError(getString(R.string.error_username));
        } else if (!isPasswordValid(password)) {
            etPassword.requestFocus();
            etPassword.setError(getString(R.string.error_password));
        } else if (!isTokenValid(token)) {
            etToken.requestFocus();
            etToken.setError(getString(R.string.error_token));
        } else {
            etUsername.setEnabled(false);
            etPassword.setEnabled(false);
            imgPassword.setEnabled(false);
            etToken.setEnabled(false);
            btnVerify.setEnabled(false);
            btnBack.setEnabled(false);
            pbLoading.setVisibility(View.VISIBLE);
            mPatient.verifyPatient(username, password, token);
        }
    }

    public void backToLogin() {
        Intent signIn = new Intent(this, SignInActivity.class);
        startActivity(signIn);
    }

    public void resendToken() {
        mPatient.resendRegistrationToken();
    }

    /**
     * ================================= END OF FUNCTIONS =================================
     */

    /**
     * ================================= START OF VALIDATIONS =================================
     */
    boolean isUsernameValid(String username) {
        return !username.isEmpty() ? true : false;
    }

    boolean isPasswordValid(String password) {
        return !password.isEmpty() ? true : false;
    }

    boolean isTokenValid(String token) {
        return !token.isEmpty() ? true : false;
    }
    /**
     * ================================= END OF VALIDATIONS =================================
     */
}
