package zj.health.health_v1.Model;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/7/19.
 */

public class SecurityModel implements Serializable{

    private String id;
    private int sid;
    private String question;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

}
