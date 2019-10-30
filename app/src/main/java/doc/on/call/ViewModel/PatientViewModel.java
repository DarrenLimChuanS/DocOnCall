package doc.on.call.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import doc.on.call.Model.Patient;
import doc.on.call.Repository.PatientRepository;

public class PatientViewModel extends AndroidViewModel {
    private PatientRepository patientRepository = new PatientRepository(getApplication());
    private LiveData<Patient> patientResponseLiveData;

    public PatientViewModel(Application application) {
        super(application);
    }

    public LiveData<Patient> getArticleResponseLiveData() {
        return this.patientResponseLiveData;
    }
}