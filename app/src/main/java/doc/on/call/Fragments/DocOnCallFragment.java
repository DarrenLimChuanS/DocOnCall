package doc.on.call.Fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import doc.on.call.Model.Patient;
import doc.on.call.R;
import doc.on.call.Repository.PatientRepository;
import doc.on.call.ViewModel.PatientViewModel;

import static doc.on.call.Utilities.Commons.isDateValid;
import static doc.on.call.Utilities.Commons.isIssueValid;
import static doc.on.call.Utilities.Commons.isTimeValid;
import static doc.on.call.Utilities.Commons.showMessage;

public class DocOnCallFragment extends Fragment {
    private static final String TAG = PatientFragment.class.getSimpleName();

    // Declare variables
    private View view;
    private Context context;

    private ProgressBar pbLoading;

    private ConstraintLayout clCreateAppointment;
    private EditText etIssue;
    private TextView tvDate;
    private Button btnDate;
    private TextView tvTime;
    private Button btnTime;
    private Button btnCreate;
    private int mYear, mMonth, mDay, mHour, mMinute;

    private ConstraintLayout clUpcomingAppointment;
    private TextView tvPlaceholderBody;

    private PatientViewModel mViewModel;
    private Patient loggedInPatient = new Patient();

    private PatientRepository mPatient;

    public DocOnCallFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_doc_on_call, container, false);

        // Fetch variables
        pbLoading = (ProgressBar) view.findViewById(R.id.pbLoading);
        pbLoading.setVisibility(View.VISIBLE);

        clCreateAppointment = (ConstraintLayout) view.findViewById(R.id.clCreateAppointment);
        etIssue = (EditText) view.findViewById(R.id.etIssue);
        tvDate = (TextView) view.findViewById(R.id.tvDate);
        btnDate = (Button) view.findViewById(R.id.btnDate);
        tvTime = (TextView) view.findViewById(R.id.tvTime);
        btnTime = (Button) view.findViewById(R.id.btnTime);
        btnCreate = (Button) view.findViewById(R.id.btnCreate);

        clUpcomingAppointment = (ConstraintLayout) view.findViewById(R.id.clUpcomingAppointment);
        tvPlaceholderBody = (TextView) view.findViewById(R.id.tvPlaceholderBody);

        mViewModel = (PatientViewModel) ViewModelProviders.of(this).get(PatientViewModel.class);
        mViewModel.getPatientLiveData().observe(this, new Observer<Patient>() {
            @Override
            public void onChanged(Patient patient) {
                if (patient != null) {
                    pbLoading.setVisibility(View.GONE);
                    loggedInPatient = patient;
                    // Check if patient has appointments
                    if (patient.getAppointments().size() > 0) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:m:s");
                        // Get the patient's latest appointment
                        int latestAppointment = patient.getAppointments().size() - 1;
                        String appointmentString = patient.getAppointments().get(latestAppointment).getAppointmentDateTime();
                        try {
                            Date appointmentDate = sdf.parse(appointmentString);
                            if (new Date().after(appointmentDate)) {
                                // Latest appointment is history, allow creation of appointment
                                clCreateAppointment.setVisibility(View.VISIBLE);
                                clUpcomingAppointment.setVisibility(View.GONE);
                            } else {
                                // Latest appointment has yet to come, show time to appointment
                                clUpcomingAppointment.setVisibility(View.VISIBLE);
                                clCreateAppointment.setVisibility(View.GONE);
                                String placeholderBody = getString(R.string.placeholder_body_have_appointment, patient.getAppointments().get(latestAppointment).getDoctorDetail().getName(), appointmentString);
                                tvPlaceholderBody.setText(placeholderBody);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else {
                        // No appointments, allow creation of appointment
                        clCreateAppointment.setVisibility(View.VISIBLE);
                        clUpcomingAppointment.setVisibility(View.GONE);
                    }
                }
            }
        });

        mPatient = new PatientRepository(context);

        // Event Listener
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAppointment();
            }
        });

        return view;
    }

    /**
     * ================================= START OF FUNCTIONS =================================
     */
    public void showDatePicker() {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // Check for day
                        if (dayOfMonth < 10) {
                            // Check for month
                            if (monthOfYear < 9) {
                                tvDate.setText(year + "-" + (monthOfYear + 1) + "0-0" + dayOfMonth);
                            } else {
                                tvDate.setText(year + "-" + (monthOfYear + 1) + "-0" + dayOfMonth);
                            }
                            // Check for day
                        } else {
                            // Check for month
                            if (monthOfYear < 9) {
                                tvDate.setText(year + "-" + (monthOfYear + 1) + "0-" + dayOfMonth);
                            } else {
                                tvDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.setMessage(getResources().getString(R.string.date_picker_message));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    public void showTimePicker() {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        // Check for hour
                        if (hourOfDay < 10) {
                            // Check for minute
                            if (minute < 10) {
                                tvTime.setText("0" + hourOfDay + ":0" + minute);
                            } else {
                                tvTime.setText("0" + hourOfDay + ":" + minute);
                            }
                            // Check for hour
                        } else {
                            // Check for minute
                            if (minute < 10) {
                                tvTime.setText(hourOfDay + ":0" + minute);
                            } else {
                                tvTime.setText(hourOfDay + ":" + minute);
                            }
                        }
                    }
                }, mHour, mMinute, false);
        timePickerDialog.setMessage(getResources().getString(R.string.time_picker_message));
        timePickerDialog.show();
    }

    public void createAppointment() {
        String issue = etIssue.getText().toString().trim();
        String date = tvDate.getText().toString().trim();
        String time = tvTime.getText().toString().trim();
        if (!isIssueValid(issue)) {
            etIssue.requestFocus();
            etIssue.setError(getString(R.string.error_issue));
        } else if (!isDateValid(date)) {
            showMessage(getString(R.string.error_date), context);
        } else if (!isTimeValid(time)) {
            showMessage(getString(R.string.error_time), context);
        } else {
            try {
                etIssue.setEnabled(false);
                btnDate.setEnabled(false);
                btnTime.setEnabled(false);
                btnCreate.setEnabled(false);
                pbLoading.setVisibility(View.VISIBLE);

                String dateTime = date + " " + time;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:m");

                Date dateFormat = sdf.parse(dateTime);
                Date currentDate = new Date();
                SimpleDateFormat jsonDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                String jsonDate = jsonDateFormat.format(dateFormat);
                String currentJsonDate = jsonDateFormat.format(currentDate);
                mPatient.createAppointment(jsonDate, currentJsonDate, issue, loggedInPatient.getName());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * ================================= END OF FUNCTIONS =================================
     */
}
