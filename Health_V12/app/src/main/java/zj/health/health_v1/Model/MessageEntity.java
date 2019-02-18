package zj.health.health_v1.Model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2018/8/16.
 */

@Entity
public class MessageEntity {

    @Id(autoincrement = true)
    private long id;
    private String ConsultId;
    private String ConversationId;//对方id 作为聊天唯一标识
    private int frequency;//聊天次数(40为最大次数,即20组)

    @Generated(hash = 157054262)
    public MessageEntity(long id, String ConsultId, String ConversationId,
            int frequency) {
        this.id = id;
        this.ConsultId = ConsultId;
        this.ConversationId = ConversationId;
        this.frequency = frequency;
    }

    @Generated(hash = 1797882234)
    public MessageEntity() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getConsultId() {
        return ConsultId;
    }

    public void setConsultId(String consultId) {
        ConsultId = consultId;
    }

    public String getConversationId() {
        return ConversationId;
    }

    public void setConversationId(String conversationId) {
        ConversationId = conversationId;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }
}
