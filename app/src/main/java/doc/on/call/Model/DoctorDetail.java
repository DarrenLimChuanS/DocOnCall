package doc.on.call.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DoctorDetail {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setId(String str) {
        this.id = str;
    }

    public void setName(String str) {
        this.name = str;
    }

    @Override
    public String toString() {
        return "DoctorDetail{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}