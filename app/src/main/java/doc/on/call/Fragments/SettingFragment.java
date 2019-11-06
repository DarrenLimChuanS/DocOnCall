package doc.on.call.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.chaos.view.PinView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import doc.on.call.Model.Patient;
import doc.on.call.R;
import doc.on.call.Repository.PatientRepository;
import doc.on.call.ViewModel.PatientViewModel;

public class SettingFragment extends Fragment {
    private static final String TAG = SettingFragment.class.getSimpleName();

    // Declare variables
    private View view;
    private Context context;
    private CardView cvUpdatePatient;
    private CardView cvChangePassword;
    private CardView cvDeletePatient;
    private CardView cvLogoutPatient;

    private ProgressBar pbLoading;
    private PatientRepository mPatient;
    private Patient loggedInPatient = new Patient();
    private PatientViewModel mViewModel;

    // Variables for Update Dialog
    private Dialog updateDialog;
    private EditText etAddress;
    private EditText etEmail;
    private EditText etPhone;
    private ImageView btnUpdateClose;
    private Button btnUpdate;
    private Button btnUpdateCancel;

    // Variables for Change Password Dialog
    private Dialog changePasswordDialog;
    private EditText etUsername;
    private EditText etOldPassword;
    private ImageView imgOldPassword;
    private EditText etNewPassword;
    private ImageView imgNewPassword;
    private EditText etConfirmPasword;
    private ImageView imgConfirmPassword;
    private ImageView btnChangeClose;
    private Button btnChange;
    private Button btnChangeCancel;

    // Variables for Delete Dialog
    private Dialog deleteDialog;
    private PinView pinViewDelete;
    private ImageView btnDeleteClose;
    private Button btnDelete;
    private Button btnDeleteCancel;

    public SettingFragment (Context context) {
        this.context = context;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        view = layoutInflater.inflate(R.layout.fragment_setting, viewGroup, false);

        // Fetch variables
        pbLoading = (ProgressBar) view.findViewById(R.id.pbLoading);

        cvUpdatePatient = (CardView) view.findViewById(R.id.cvUpdatePatient);
        cvChangePassword = (CardView) view.findViewById(R.id.cvChangePassword);
        cvDeletePatient = (CardView) view.findViewById(R.id.cvDeletePatient);
        cvLogoutPatient = (CardView) view.findViewById(R.id.cvLogoutPatient);

        toggleSettingState(false);

        mPatient = new PatientRepository(context);

        mViewModel = (PatientViewModel) ViewModelProviders.of(this).get(PatientViewModel.class);
        mViewModel.getPatientLiveData().observe(this, new Observer<Patient>() {
            @Override
            public void onChanged(Patient patient) {
                if (patient != null) {
                    toggleSettingState(true);
                    loggedInPatient = patient;
                }
            }
        });

        updateDialog = new Dialog(context);
        changePasswordDialog = new Dialog(context);
        deleteDialog = new Dialog(context);

        // Event Listeners
        cvUpdatePatient.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                updatePatient();
            }
        });

        cvChangePassword.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                changePassword();
            }
        });

        cvDeletePatient.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                deletePatient();
            }
        });

        cvLogoutPatient.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                logoutPatient();
            }
        });

        return view;
    }

    public void updatePatient(){
        // Dialog for update patient
        updateDialog.setContentView(R.layout.dialog_update_patient);
        etAddress = (EditText) updateDialog.findViewById(R.id.etAddress);
        etEmail = (EditText) updateDialog.findViewById(R.id.etEmail);
        etPhone = (EditText) updateDialog.findViewById(R.id.etPhone);
        btnUpdateClose = (ImageView) updateDialog.findViewById(R.id.btnUpdateClose);
        btnUpdate = (Button) updateDialog.findViewById(R.id.btnUpdate);
        btnUpdateCancel = (Button) updateDialog.findViewById(R.id.btnUpdateCancel);

        etAddress.setText(loggedInPatient.getAddress());
        etEmail.setText(loggedInPatient.getEmail());
        etPhone.setText(loggedInPatient.getPhone());

        // Event Listeners
        btnUpdateClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDialog.dismiss();
            }
        });

        btnUpdate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = etAddress.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String phone = etPhone.getText().toString().trim();
                if (!isAddressValid(address)) {
                    etAddress.requestFocus();
                    etAddress.setError(getString(R.string.error_address));
                } else if(!isEmailValid(email)) {
                    etEmail.requestFocus();
                    etEmail.setError(getString(R.string.error_email));
                } else if (!isPhoneValid(phone)) {
                    etPhone.requestFocus();
                    etPhone.setError(getString(R.string.error_phone));
                } else {
                    int phoneNumber = Integer.parseInt(phone);
                    etAddress.setEnabled(false);
                    etEmail.setEnabled(false);
                    etPhone.setEnabled(false);
                    btnUpdateClose.setEnabled(false);
                    btnUpdate.setEnabled(false);
                    btnUpdateCancel.setEnabled(false);
                    pbLoading.setVisibility(View.VISIBLE);
                    mPatient.updatePatient(address, email, phoneNumber, updateDialog);
                }
            }
        });

        btnUpdateCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDialog.dismiss();
            }
        });

        // Show dialog
        updateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        updateDialog.show();
    }

    public void changePassword(){
        // Dialog for change password
        changePasswordDialog.setContentView(R.layout.dialog_change_password);
        etUsername = (EditText) changePasswordDialog.findViewById(R.id.etUsername);
        etOldPassword = (EditText) changePasswordDialog.findViewById(R.id.etOldPassword);
        imgOldPassword = (ImageView) changePasswordDialog.findViewById(R.id.imgOldPassword);
        etNewPassword = (EditText) changePasswordDialog.findViewById(R.id.etNewPassword);
        imgNewPassword = (ImageView) changePasswordDialog.findViewById(R.id.imgNewPassword);
        etConfirmPasword = (EditText) changePasswordDialog.findViewById(R.id.etConfirmPassword);
        imgConfirmPassword = (ImageView) changePasswordDialog.findViewById(R.id.imgConfirmPassword);
        btnChangeClose = (ImageView) changePasswordDialog.findViewById(R.id.btnChangeClose);
        btnChange = (Button) changePasswordDialog.findViewById(R.id.btnChange);
        btnChangeCancel = (Button) changePasswordDialog.findViewById(R.id.btnChangeCancel);

        // Event Listeners
        imgOldPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    etOldPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    etOldPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                return true;
            }
        });

        imgNewPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    etNewPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    etNewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                return true;
            }
        });

        imgConfirmPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    etConfirmPasword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    etConfirmPasword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                return true;
            }
        });

        btnChangeClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changePasswordDialog.dismiss();
            }
        });

        btnChange.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString().trim();
                String oldPassword = etOldPassword.getText().toString().trim();
                String newPassword = etNewPassword.getText().toString().trim();
                String confirmPassword = etConfirmPasword.getText().toString().trim();
                if (!isUsernameValid(username)) {
                    etUsername.requestFocus();
                    etUsername.setError(getString(R.string.error_username));
                } else if(!isPasswordValid(oldPassword)) {
                    etOldPassword.requestFocus();
                    etOldPassword.setError(getString(R.string.error_password));
                } else if(!isPasswordValid(newPassword)) {
                    etNewPassword.requestFocus();
                    etNewPassword.setError(getString(R.string.error_password));
                } else if(!isPasswordValid(confirmPassword)) {
                    etConfirmPasword.requestFocus();
                    etConfirmPasword.setError(getString(R.string.error_password));
                } else if(!isPasswordMatchValid(newPassword, confirmPassword)) {
                    etNewPassword.requestFocus();
                    etConfirmPasword.requestFocus();
                    etNewPassword.setError(getString(R.string.error_password_match));
                    etConfirmPasword.setError(getString(R.string.error_password_match));
                } else {
                    etUsername.setEnabled(false);
                    etOldPassword.setEnabled(false);
                    etNewPassword.setEnabled(false);
                    etConfirmPasword.setEnabled(false);
                    btnChangeClose.setEnabled(false);
                    btnChange.setEnabled(false);
                    btnChangeCancel.setEnabled(false);
                    pbLoading.setVisibility(View.VISIBLE);
                    mPatient.changePassword(username, oldPassword, newPassword, changePasswordDialog);
                }
            }
        });

        btnChangeCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changePasswordDialog.dismiss();
            }
        });

        // Show dialog
        changePasswordDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        changePasswordDialog.show();
    }

    public void deletePatient(){
        // Create Delete Dialog
        deleteDialog.setContentView(R.layout.dialog_delete_patient);
        pinViewDelete = (PinView) deleteDialog.findViewById(R.id.pinViewDelete);
        btnDeleteClose = (ImageView) deleteDialog.findViewById(R.id.btnDeleteClose);
        btnDelete = (Button) deleteDialog.findViewById(R.id.btnDelete);
        btnDeleteCancel = (Button) deleteDialog.findViewById(R.id.btnDeleteCancel);

        btnDeleteClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.dismiss();
            }
        });

        btnDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = pinViewDelete.getText().toString().trim();
                if (!isOtpValid(otp)) {
                    pinViewDelete.requestFocus();
                    pinViewDelete.setError(getString(R.string.error_otp));
                } else {
                    pinViewDelete.setEnabled(false);
                    btnDeleteClose.setEnabled(false);
                    btnDelete.setEnabled(false);
                    btnDeleteCancel.setEnabled(false);
                    pbLoading.setVisibility(View.VISIBLE);
                    // Send Delete OTP
                    mPatient.validateDeletePatient(otp, deleteDialog);
                }
            }
        });

        btnDeleteCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.dismiss();
            }
        });

        deleteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Send Delete OTP
        pbLoading.setVisibility(View.VISIBLE);
        mPatient.deletePatient(deleteDialog);
    }

    public void logoutPatient() {
        pbLoading.setVisibility(View.VISIBLE);
        mPatient.logoutPatient();
    }

    public void toggleSettingState(boolean status) {
        if (status) {
            cvUpdatePatient.setEnabled(status);
            cvChangePassword.setEnabled(status);
            cvDeletePatient.setEnabled(status);
            cvLogoutPatient.setEnabled(status);
            pbLoading.setVisibility(View.GONE);
        } else {
            cvUpdatePatient.setEnabled(status);
            cvChangePassword.setEnabled(status);
            cvDeletePatient.setEnabled(status);
            cvLogoutPatient.setEnabled(status);
            pbLoading.setVisibility(View.VISIBLE);
        }
    }

    /**
     * ================================= START OF VALIDATIONS =================================
     */
    boolean isAddressValid(String address) {
        return !address.isEmpty() ? true : false;
    }

    boolean isEmailValid(String email) {
        return !email.isEmpty() ? true : false;
    }

    boolean isPhoneValid(String phone) {
        try {
            Integer.parseInt(phone);
            return true;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
    }

    boolean isUsernameValid(String username) {
        return !username.isEmpty() ? true : false;
    }

    boolean isPasswordValid(String password) {
        return !password.isEmpty() ? true : false;
    }

    boolean isPasswordMatchValid(String firstPassword, String secondPassword) {
        return firstPassword.equals(secondPassword);
    }

    boolean isOtpValid(String otp) {
        return !otp.isEmpty() ? true : false;
    }
    /**
     * ================================= END OF VALIDATIONS =================================
     */
}