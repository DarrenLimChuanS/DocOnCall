package doc.on.call.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PatientDetail {
    @SerializedName("age")
    @Expose
    private int age;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("issue")
    @Expose
    private String issue;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("phone")
    @Expose
    private String phone;

    public int getAge() {
        return this.age;
    }

    public String getId() {
        return this.id;
    }

    public String getIssue() {
        return this.issue;
    }

    public String getName() {
        return this.name;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setAge(int i) {
        this.age = i;
    }

    public void setId(String str) {
        this.id = str;
    }

    public void setIssue(String str) {
        this.issue = str;
    }

    public void setName(String str) {
        this.name = str;
    }

    public void setPhone(String str) {
        this.phone = str;
    }

    @Override
    public String toString() {
        return "PatientDetail{" +
                "age=" + age +
                ", id='" + id + '\'' +
                ", issue='" + issue + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}