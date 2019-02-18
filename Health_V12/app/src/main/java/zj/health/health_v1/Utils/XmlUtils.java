package zj.health.health_v1.Utils;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import zj.health.health_v1.Base.Constant;

/**
 * Created by Administrator on 2018/11/6.
 */

public class XmlUtils {


    /**
     * 调用微信接口提现到银行卡
     * @param enc_bank_no 收款方银行卡号
     * @param enc_true_name 收款方用户名
     * @param bank_code 收款方开户行
     * @param amount 付款金额
     */
    public void Wechat_Post_for_xml(String enc_bank_no,String enc_true_name,String bank_code,String amount){

        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        xml.append("<mch_id>"+ Constant.partnerId+"</mch_id>");
        xml.append("<partner_trade_no>"+StringUtil.generateOrderSN()+"</partner_trade_no>");
        xml.append("<nonce_str>"+StringUtil.createRandom(false,32)+"</nonce_str>");
        xml.append("<sign>"+Constant.WechatApiKey+"</sign>");
        xml.append("<enc_bank_no>"+enc_bank_no+"</enc_bank_no>");
        xml.append("<enc_true_name>"+enc_true_name+"</enc_true_name>");
        xml.append("<bank_code>"+bank_code+"</bank_code>");
        xml.append("<amount>"+amount+"</amount>");

        byte[] xmlbyte = new byte[0];
        try {
            xmlbyte = xml.toString().getBytes("UTF-8");
            System.out.println(xml);

            URL url = new URL("https://api.mch.weixin.qq.com/mmpaysptrans/pay_bank");


            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setDoOutput(true);// 允许输出
            conn.setDoInput(true);
            conn.setUseCaches(false);// 不使用缓存
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Content-Length",
                    String.valueOf(xmlbyte.length));
            conn.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
            conn.setRequestProperty("X-ClientType", "2");//发送自定义的头信息

            conn.getOutputStream().write(xmlbyte);
            conn.getOutputStream().flush();
            conn.getOutputStream().close();


            if (conn.getResponseCode() != 200)
                throw new RuntimeException("请求url失败");

            InputStream is = conn.getInputStream();// 获取返回数据

            // 使用输出流来输出字符(可选)
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int len;
            while ((len = is.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
            String string = out.toString("UTF-8");
            System.out.println(string);
            out.close();


            // xml解析
            String version = null;
            String seqID = null;
            XmlPullParser parser = Xml.newPullParser();

                parser.setInput(new ByteArrayInputStream(string.substring(1)
                        .getBytes("UTF-8")), "UTF-8");
                parser.setInput(is, "UTF-8");
                int eventType = parser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        if ("SSOMessage".equals(parser.getName())) {
                            version = parser.getAttributeValue(0);
                        } else if ("SeqID".equals(parser.getName())) {
                            seqID = parser.nextText();
                        } else if ("ResultCode".equals(parser.getName())) {
//                            resultCode = parser.nextText();
                        }
                    }
                    eventType = parser.next();
                }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }


    }




}
