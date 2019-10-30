package doc.on.call.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import doc.on.call.Repository.PatientRepository;
import doc.on.call.RetroFit.Response.PatientResponse;

public class PatientViewModel extends AndroidViewModel {
    private PatientRepository patientRepository = new PatientRepository(getApplication());
    private LiveData<PatientResponse> patientResponseLiveData;

    public PatientViewModel(Application application) {
        super(application);
    }

    public LiveData<PatientResponse> getArticleResponseLiveData() {
        return this.patientResponseLiveData;
    }
}