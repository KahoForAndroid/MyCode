package zj.health.health_v1.Utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import GreenDaoDB.MessageEntityDao;
import zj.health.health_v1.Model.ConsultTimeModel;
import zj.health.health_v1.Model.DrugNameModel;
import zj.health.health_v1.Model.LiveMode;
import zj.health.health_v1.Model.MessageEntity;
import zj.health.health_v1.Model.SportModes;
import zj.health.health_v1.Model.Users;

/**
 * Created by Administrator on 2018/6/1.
 */

public class DbUtils {

    /**
     * 将数据实体通过事务添加至数据库
     *
     * @param context
     * @param list
     */
    public void insertLiveListData(Context context, final List<LiveMode> list) {
        if (null == list || list.size() <= 0) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                GreenDaoManager.getInstance().getSession().getLiveModeDao().insertInTx(list);
            }
        }).start();
    }

    public void insertSportListData(Context context, final List<SportModes> list) {
        if (null == list || list.size() <= 0) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                GreenDaoManager.getInstance().getSession().getSportModesDao().insertInTx(list);
            }
        }).start();

    }


    /**
     * 从数据库中获取所有生活模式数据
     * @return 返回数据集合
     */
    public void getLiveModeDataList(final Handler handler){
        new Thread(new Runnable() {
            @Override
            public void run() {
                QueryBuilder<LiveMode> builder = GreenDaoManager.getInstance().getSession().getLiveModeDao().queryBuilder();
                Message message = new Message();
                message.obj = builder.build().list();
                handler.sendMessage(message);
            }
        }).start();

    }

    /**
     * 从数据库中获取所有运动模式数据
     * @return 返回数据集合
     */
    public void getSportModeDataList(final Handler handler){
        new Thread(new Runnable() {
            @Override
            public void run() {
                QueryBuilder<SportModes> builder = GreenDaoManager.getInstance().getSession().getSportModesDao().queryBuilder();
                Message message = new Message();
                message.obj = builder.build().list();
                handler.sendMessage(message);
            }
        }).start();
    }




    /**
     * 从数据库中获取最新的发布时间以及ID
     * @return 返回数据集合
     */
    public List<ConsultTimeModel> getConsultTimeModelList(){
        QueryBuilder<ConsultTimeModel> builder = GreenDaoManager.getInstance().getSession().getConsultTimeModelDao().queryBuilder();
        return builder.build().list();
    }

    //添加新的发布时间以及ID
    public void insertConsultTimeModelData(Context context, final List<ConsultTimeModel> list) {
        if (null == list || list.size() <= 0) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                //因为该表里只保留最新的发布数据,所以先请空表数据
                GreenDaoManager.getInstance().getSession().getConsultTimeModelDao().deleteAll();
                GreenDaoManager.getInstance().getSession().getConsultTimeModelDao().insertInTx(list);
            }
        }).start();

    }



    /**
     * 从数据库中获取所有历史填写药名数据
     * @return 返回数据集合
     */
    public List<DrugNameModel> getDrugNameDataList(){
        QueryBuilder<DrugNameModel> builder = GreenDaoManager.getInstance().getSession().getDrugNameModelDao().queryBuilder();
        return builder.build().list();
    }

    /**
     * 将数据实体通过事务添加至数据库
     *
     * @param context
     * @param drugNameModel 添加单个对象
     */
    public void insertDrugNameModelData(Context context, final DrugNameModel drugNameModel) {
        if (drugNameModel == null) {
            return;
        }
        new Thread(  new Runnable() {
            @Override
            public void run() {
                GreenDaoManager.getInstance().getSession().getDrugNameModelDao().insert(drugNameModel);
            }
        }).start();
    }


    /**
     * 查询该消息对话的剩余聊天次数
     * @param ConversationId 消息对话的唯一ID
     * @return
     */
    public MessageEntity getMessageEntity(String ConversationId){
        QueryBuilder<MessageEntity> builder = GreenDaoManager.getInstance()
                .getSession().getMessageEntityDao().
                        queryBuilder().where(MessageEntityDao.Properties.ConversationId.eq(ConversationId));
        if(builder.build().list() !=null){
            return (MessageEntity) builder.build().list().get(0);
        }else{
            return null;
        }

    }

    /**
     * 保存当前剩余的聊天次数
     * @param messageEntity 消息次数实体类
     */
    public void setMessageEntity(final MessageEntity messageEntity){

            new Thread(new Runnable() {
                @Override
                public void run() {
                    GreenDaoManager.getInstance()
                            .getSession().getMessageEntityDao().insertOrReplace(messageEntity);
                }
            }).start();

    }

    /**
     * 从数据库中删除该消息次数实体类
     * @param messageEntity 消息次数实体类
     */
    public void DelMessageEntity(MessageEntity messageEntity){
        GreenDaoManager.getInstance()
                .getSession().getMessageEntityDao().deleteByKey(messageEntity.getId());
    }


    /**
     * 将数据实体通过事务添加至数据库
     *
     * @param context
     * @param users 添加单个对象
     */
    public void insertUsersData(Context context, final Users users) {
        if (users == null) {
            return;
        }
        new Thread(  new Runnable() {
            @Override
            public void run() {
                GreenDaoManager.getInstance().getSession().getUsersDao().insert(users);
                GreenDaoManager.getInstance().getSession().clear();
            }
        }).start();
    }

    public void updateUsersData(Context context, final Users users) {
        if (users == null) {
            return;
        }
        new Thread(  new Runnable() {
            @Override
            public void run() {
                GreenDaoManager.getInstance().getSession().getUsersDao().update(users);
                GreenDaoManager.getInstance().getSession().clear();
            }
        }).start();
    }


    public void DelUsersData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                GreenDaoManager.getInstance()
                        .getSession().getUsersDao().deleteAll();
                GreenDaoManager.getInstance().getSession().clear();
            }
        }).start();
    }

    public Users SelectUsersDataFirst(){
        Users users = GreenDaoManager.getInstance()
                .getSession().getUsersDao().load(1l);
        GreenDaoManager.getInstance().getSession().clear();
        return  users;
    }
}
