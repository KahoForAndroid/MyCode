package zj.health.health_v1.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/10/10.
 */

public class PrescModel implements Serializable{

    private String id;
    //    private String doctorId;
    private String applyIllness;
    private String remark;
    private String lastModifiedDate;
    private List<Medicines> medicines;
    private String type;
    private String doctorName;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApplyIllness() {
        return applyIllness;
    }

    public void setApplyIllness(String applyIllness) {
        this.applyIllness = applyIllness;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public List<Medicines> getMedicines() {
        return medicines;
    }

    public void setMedicines(List<Medicines> medicines) {
        this.medicines = medicines;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }



    public static class Medicines{

        String medicineName;
        String treatment;

        public String getMedicineName() {
            return medicineName;
        }

        public void setMedicineName(String medicineName) {
            this.medicineName = medicineName;
        }

        public String getTreatment() {
            return treatment;
        }

        public void setTreatment(String treatment) {
            this.treatment = treatment;
        }

    }

}
