package zj.health.health_v1.Model;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2018/9/13.
 */
@Entity
public class QuestionModel implements Parcelable{

    @Id(autoincrement = true)
    private long id;
    private String userid;
    private String question;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }



    protected QuestionModel(Parcel in) {
        id = in.readLong();
        userid = in.readString();
        question = in.readString();
    }

    @Generated(hash = 1279011019)
    public QuestionModel(long id, String userid, String question) {
        this.id = id;
        this.userid = userid;
        this.question = question;
    }

    @Generated(hash = 740234845)
    public QuestionModel() {
    }

    public static final Creator<QuestionModel> CREATOR = new Creator<QuestionModel>() {
        @Override
        public QuestionModel createFromParcel(Parcel in) {
            return new QuestionModel(in);
        }

        @Override
        public QuestionModel[] newArray(int size) {
            return new QuestionModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(userid);
        dest.writeString(question);
    }
}
