package doc.on.call.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import doc.on.call.Model.Patient;
import doc.on.call.R;
import doc.on.call.Repository.PatientRepository;
import doc.on.call.ViewModel.PatientViewModel;

public class SettingFragment extends Fragment {
    private static final String TAG = SettingFragment.class.getSimpleName();

    // Declare variables
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
    private ImageView btnClose;
    private Button btnUpdate;
    private Button btnCancel;

    public SettingFragment (Context context) {
        this.context = context;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View view = layoutInflater.inflate(R.layout.fragment_setting, viewGroup, false);

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
        updateDialog.setContentView(R.layout.fragment_update_patient_dialog);
        etAddress = (EditText) updateDialog.findViewById(R.id.etAddress);
        etEmail = (EditText) updateDialog.findViewById(R.id.etEmail);
        etPhone = (EditText) updateDialog.findViewById(R.id.etPhone);
        btnClose = (ImageView) updateDialog.findViewById(R.id.btnClose);
        btnUpdate = (Button) updateDialog.findViewById(R.id.btnUpdate);
        btnCancel = (Button) updateDialog.findViewById(R.id.btnCancel);

        etAddress.setText(loggedInPatient.getAddress());
        etEmail.setText(loggedInPatient.getEmail());
        etPhone.setText(loggedInPatient.getPhone());

        // Event Listeners
        btnClose.setOnClickListener(new OnClickListener() {
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
                    btnClose.setEnabled(false);
                    btnUpdate.setEnabled(false);
                    btnCancel.setEnabled(false);
                    pbLoading.setVisibility(View.VISIBLE);
                    mPatient.updatePatient(address, email, phoneNumber, updateDialog);
                }
            }
        });

        btnCancel.setOnClickListener(new OnClickListener() {
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
    }

    public void deletePatient(){
        mPatient.deletePatient();
    }

    public void logoutPatient() {
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
    /**
     * ================================= END OF VALIDATIONS =================================
     */
}