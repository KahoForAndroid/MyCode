package zj.health.health_v1.Base;

import android.os.Environment;

import java.util.UUID;

/**
 * 常量
 */
public class Constant {

    public static final int ACCOUNT_TYPE = 792;
    //sdk appid 由腾讯分配
    public static final int SDK_APPID = 1400001533;

    //更换头像时图片默认保存的路径
    public static final String User_Icon_Path = Environment.getExternalStorageDirectory().getPath()+"/";
    //更换头像时图片默认名
    public static final String UserPhoto_Name = "user_icon"+".jpg";
    //设置医生头像时图片默认名
    public static final String doctorIcon_Name = "doctor_icon"+".jpg";

    public static final String ip = "https://health.zhzjzdh.com/";
    //接口ip
    public static final String postIP = "http://112.90.146.226:9090/";
    //socket地址
    public static final String socketIp = "ws://http://112.90.146.226:9090/ws?";
    //图片ip
    public static final String photo_IP = "http://112.90.146.226:9090";
    //本地数据库名
    public static final String DBNAME = "health.db";
    //本地数据库访问密码
    public static final String DBPASSWORD = "zhzj888888";
    //要求重新登录返回码
    public static final int UserErrorCode = 404;
    //微信分享APIID
    public static final String WeChat_ShareID = "wx03d704215ea4560d";
    //微信支付商户号
    public static final String partnerId = "1517350481";
    //微信商户号里的API Key
    public static final String WechatApiKey = "2DB9AECEE01598DB6DB7DE7B791E835D";


    /** --------------------- 进入会话状态----------------------- **/
    public static final int SELECTDOCTORSTATE = 1;


    /** -----------------------  接口名 -------------------------- **/

    //获取验证码
    public static final String getVerification_Code = "sms-code/send?";
    //校验验证码是否有效或者与手机号码是否匹配
    public static final String verificationSuccess = "sms-code/verification?";
    //注册接口
    public static final String register = "user/register?";
    //上传、获取用户头像
    public static final String usericon = "user/image/upload?";
    //上传、获取医生用户头像
    public static final String doctoricon = "user/image/upload?";
    //上传、获取医生身份证
    public static final String doctorId_img = "user/image/upload?";
//    //上传、获取医生执业证明
//    public static final String doctor_certificate1 = "doctor/certificate1?";
    //医院列表
    public static final String doctor_hospital_config = "doctor/hospital-config?";
    //医生注册
    public static final String doctor_register = "doctor/register?";
    //医生审核状态
    public static final String doctor_audit_status = "doctor/audit-status?";
    //上传医生执业照片
    public static final String doctor_certificate = "user/image/upload?";
    //上传、获取报告图片
    public static final String report_img_Upload = "user/image/upload?";
    //上传报告
    public static final String report_Upload = "user/exam-report?";
    //获取报告列表
    public static final String report_list = "user/exam-report/list?";
    //上传或更新体检报告
    public static final String report_update = "user/exam-report?";
    //获取专项配置
    public static final String get_report_config = "user/exam-report/config?";
    //手机重新绑定-旧手机发送验证码
    public static final String old_phone = "sms-code/rebind-old/send?";
    //手机重新绑定-新手机发送验证码
    public static final String new_phone = "sms-code/rebind-new/send?";
    //手机重新绑定-通过新旧手机验证码重绑
    public static final String old_phone_toNewPhone = "sms-code/rebind/verification?";
    //手机重新绑定-通过密保重绑
    public static final String security_question = "rebind/security-question/verification?";
    //手机重新绑定-亲友手机发送验证码
    public static final String friend_to_send = "sms-code/rebind-friend/send?";
    //手机重新绑定-通过亲友手机验证码重绑
    public static final String friend_to_send_newPhone = "sms-code/rebind-friend/verification?";
    //查找亲友
    public static final String Search_friend = "friend/search?";
    //添加亲友
    public static final String add_friend = "friend/request?";
    //新的亲友列表
    public static final String new_friend_list = "friend/requests?";
    //接受亲友请求
    public static final String accept_friend = "friend/accept?";
    //修改亲友权限
    public static final String permission_friend = "friend/permission?";
    //亲友列表
    public static final String list_friend = "friend/list?";
    //查看亲友权限
    public static final String checkPermission ="friend/permission?";
    //发布健康咨询
    public static final String consult = "user/consult?";
    //健康咨询历史发布
    public static final String consult_history = "user/consult/history?";
    //获取腾讯云IM_sig
    public static final String getSig = "user/tx-im/signature?";
    //获取已邀请健康咨询的医生列表
    public static final String getCounseling_list = "user/consult/doctor-list?";
    //医生获取患者列表
    public static final String getConsultList = "doctor/consult/list?";
    //医生邀请健康咨询
    public static final String getConsultInvite= "doctor/consult/invite?";
    //用户选择咨询医生
    public static final String user_choose = "user/consult/choose?";
    //获取运动情况列表
    public static final String sport_mode = "user/sport-mode-config?";
    //获取生活情况列表
    public static final String live_mode = "user/live-mode-config?";
    //更新用户头像
    public static final String update_icon = "user/icon?";
    //更新用户信息
    public static final String updateData = "user/base-info?";
    //获取用户信息
    public static final String getUserData = "user/base-info?";
    //聊天数自增
    public static final String inc_chat = "doctor/consult/inc-chat?";
    //获取疾病列表
    public static final String illness_List = "user/illness-item-config?";
    //更新或获取用户疾病信息
    public static final String updateOrPost_illness = "user/illness?";
    //录入血压
    public static final String post_blood_pressure = "user/data/blood-pressure?";
    //录入体重
    public static final String post_weight = "user/data/weight?";
    //录入体温
    public static final String post_temp = "user/data/body-temp?";
    //录入心率
    public static final String post_heart_rate = "user/data/heart-rate?";
    //录入血糖
    public static final String post_blood_glucose = "user/data/blood-glucose?";
    //更新健康咨询聊天会话ID
    public static final String updateConversationId = "doctor/consult/conversation?";
    //获取健康聊天数
    public static final String getChatCount = "doctor/consult/chat-count?";
    //聊天会话ID查询健康咨询ID
    public static final String ChatSelectConsultID = "user/consult/conversation?";
    //通过医生审核
    public static final String audit_pass = "admin/doctor/audit-pass?";
    //获取血压每月每天平均数据
    public static final String blood_pressure = "user/data/blood-pressure?";
    //获取血压全天数据
    public static final String blood_pressure_day = "user/data/blood-pressure/day?";
    //获取血糖每月每天平均数据
    public static final String blood_Sugar = "user/data/blood-glucose?";
    //获取血糖全天数据
    public static final String blood_Sugar_day = "user/data/blood-glucose/day?";
    //获取心率每月每天平均数据
    public static final String heart_rate = "user/data/heart-rate?";
    //获取心率全天数据
    public static final String heart_rate_day = "user/data/heart-rate/day?";
    //获取体重每月每天平均数据
    public static final String weight = "user/data/weight?";
    //获取体重全天数据
    public static final String weight_day = "user/data/weight/day?";
    //获取心率每月每天平均数据
    public static final String temp = "user/data/body-temp?";
    //获取体重全天数据
    public static final String temp_day = "user/data/body-temp/day?";
    //获取指定月日历项
    public static final String calendar_for_month = "user/medicine/reminder/calendar?";
    //获取服药提醒列表
    public static final String reminderList = "user/medicine/reminder?";
    //添加,更新服药提醒列表
    public static final String commitRemind = "user/medicine/reminder?";
    //医生获取处方列表
    public static final String doctor_presc_list = "doctor/doc/presc-list?";
    //医生为处方添加或更新药物
    public static final String doctor_post_presc = "doctor/doc/presc/medicine?";
    //医生获取指定或创建处方建议
    public static final String getPrescription = "doctor/doc/prescription?";
    //医生发送处方给患者
    public static final String send_presc = "doctor/doc/send-presc?";
    //获取在线医生
    public static final String getDoctorOnLine = "user/online/doctor?";
    //患者获取处方概要列表
    public static final String getUserSuggestion = "user/medicine/suggestion-brief?";
    //患者获取指定处方详情
    public static final String getUserSuggestionDetail = "user/medicine/suggestion?";
    //获取推荐好友数量
    public static final String getReferral_count = "user/referral-count?";
    //查看亲友健康数据
    public static final String CheckFriendDate = "user/data/friend?";
    //删除某一提醒模板
    public static final String DeleteMedicine = "user/medicine/reminder?";
    //获取所有血压数据
    public static final String blood_pressure_list = "user/data/blood-pressure-all?";
    //录入或更新、删除血压数据
    public static final String update_or_delete_blood_pressure_list = "user/data/blood-pressure?";
    //获取所有体重数据
    public static final String weight_list = "user/data/weight-all?";
    //录入或更新、删除体重数据
    public static final String update_or_delete_weight_list = "user/data/weight?";
    //获取所有体重数据
    public static final String temp_list = "user/data/body-temp-all?";
    //录入或更新、删除体温数据
    public static final String update_or_delete_temp_list = "user/data/body-temp?";
    //获取所有血糖数据
    public static final String Sugar_list = "user/data/blood-glucose-all?";
    //录入或更新、删除血糖数据
    public static final String update_or_delete_Sugar_list = "user/data/blood-glucose?";
    //获取所有心率数据
    public static final String heart_rate_list = "user/data/heart-rate-all?";
    //录入或更新、删除心率数据
    public static final String update_or_delete_heart_rate_list = "user/data/heart-rate?";
    //查看亲友图表数据
    public static final String check_friend_graph_list = "user/data/friend?";
    //查看亲友图表数据
    public static final String check_friend_graph_Day = "user/data/friend/day?";
    //微信支付获取预支付订单号
    public static final String Wechat_prepay = "user/order/wx-prepay?";
    //获取支付结果
    public static final String Wechat_pay_check = "user/order/check?";
    //结束健康咨询
    public static final String end_consult = "user/consult/status/end?";
    //获取用户余额
    public static final String get_balance = "user/balance?";
    //获取正在沟通的健康咨询列表
    public static final String get_communicating = "user/consult/communicating?";
    //用户余额变动列表
    public static final String get_transaction = "user/transaction?";
    //获取医生咨询设置
    public static final String consult_setting = "doctor/consult-setting?";
    //获取单个健康咨询
    public static final String getconsult = "user/consult?";
    //获取医生信息
    public static final String getDoctorData = "user/doctor-info?";
    //健康咨询标签配置
    public static final String health_item_config = "user/consult-item-config?";
    //获取亲友体检报告列表
    public static final String friend_report_list = "friend/exam-report/list?";
    //获取亲友指定月日历项
    public static final String friend_reminder = "friend/medicine/reminder/calendar?";
    //修改是否服药
    public static final String remind_take = "user/medicine/reminder/take?";
    //查看亲友对我设置的权限
    public static final String friend_permission4me = "friend/permission4me?";
    //获取最近一条心率数据
    public static final String data_heart_rate_latest = "user/data/heart-rate/latest?";





}
