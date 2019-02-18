package zj.health.health_v1.Utils;

import com.github.mikephil.charting.data.BaseEntry;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import zj.health.health_v1.Model.Chart;
import zj.health.health_v1.Model.FriendGraphicDayModel;
import zj.health.health_v1.Model.FriendGraphicModel;

/**
 * Created by Administrator on 2018/11/12.
 */

public class SetFriend_Graph_Util {


    public Chart setBloodChatData(int graphicType, FriendGraphicModel.TypeData typeData, FriendGraphicDayModel friendGraphicDayModel,Calendar startCalendar,float min){
//        charts.clear();
        Chart chart = new Chart();
        List<String> xstringList = new ArrayList<>();
        List<List<? extends BaseEntry>> entriesList = new ArrayList<>();
        List<Entry> entriesys_sysAverage = new ArrayList<>();
        List<Entry> entriesys_diaAverage = new ArrayList<>();

        switch(graphicType){
            case 1:
                for (int day = 0;day<25;day++){
                    xstringList.add(day+"");
                }
                Date date = new Date();
                Calendar calendar = Calendar.getInstance();
                if(friendGraphicDayModel.getBlood_pressure()!=null && friendGraphicDayModel.getBlood_pressure().size()>0 && friendGraphicDayModel.getBlood_pressure().get(0).getSystolicPressure()!=null ) {

                    float sysTotal = 0;//日收缩压平均值
                    float diaTotal = 0;//日舒张压平均值
                    for (int i = 0;i<friendGraphicDayModel.getBlood_pressure().size();i++){
                        String systolic = (friendGraphicDayModel.getBlood_pressure().get(i).getSystolicPressure() != null)?friendGraphicDayModel.getBlood_pressure().get(i).getSystolicPressure():"0";
                        String Diastolic = (friendGraphicDayModel.getBlood_pressure().get(i).getDiastolicPressure() != null)?friendGraphicDayModel.getBlood_pressure().get(i).getDiastolicPressure():"0";
                        sysTotal += Float.parseFloat(systolic);
                        diaTotal += Float.parseFloat(Diastolic);
                    }
                    float sysAverage = sysTotal/friendGraphicDayModel.getBlood_pressure().size();
                    float diaAverage = diaTotal/friendGraphicDayModel.getBlood_pressure().size();



                    date.setTime(Long.parseLong(friendGraphicDayModel.getBlood_pressure().get(0).getTimestamp()));
                    calendar.setTime(date);
                    int Firstx = calendar.get(Calendar.HOUR_OF_DAY);
                    if(Firstx!=1){
                        entriesys_sysAverage.add(new Entry(1, sysAverage));
                        entriesys_diaAverage.add(new Entry(1, diaAverage));
                    }


                    for (int i = 0; i < friendGraphicDayModel.getBlood_pressure().size(); i++) {
                        date.setTime(Long.parseLong(friendGraphicDayModel.getBlood_pressure().get(i).getTimestamp()));
                        calendar.setTime(date);
                        int x = calendar.get(Calendar.HOUR_OF_DAY);
                        entriesys_sysAverage.add(new Entry(Float.parseFloat(x+"."+calendar.get(Calendar.MINUTE)), Float.valueOf(friendGraphicDayModel.getBlood_pressure().get(i).getSystolicPressure())));
                        entriesys_diaAverage.add(new Entry(Float.parseFloat(x+"."+calendar.get(Calendar.MINUTE)), Float.valueOf(friendGraphicDayModel.getBlood_pressure().get(i).getDiastolicPressure())));


                    }

                    date.setTime(Long.parseLong(friendGraphicDayModel.getBlood_pressure().
                            get(friendGraphicDayModel.getBlood_pressure().size()-1).getTimestamp()));
                    calendar.setTime(date);
                    if(calendar.get(Calendar.HOUR)<24){
                        entriesys_sysAverage.add(new Entry(24, sysAverage));
                        entriesys_diaAverage.add(new Entry(24, diaAverage));
                    }

                }else {
                    for (int i = 1;i<25;i++){
                        entriesys_sysAverage.add(new Entry(i, Float.valueOf(min)));
                        entriesys_diaAverage.add(new Entry(i, Float.valueOf(min)));
                    }

                }

                entriesList.add(entriesys_sysAverage);
                entriesList.add(entriesys_diaAverage);
                break;
            case 2:
                int DayForMonth = startCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                for (int day = 0;day<DayForMonth+1;day++){
                    xstringList.add(day+"");
                }
                if(typeData.getDaily()!=null && typeData.getDaily().size()>0) {

                    if(Integer.parseInt(typeData.getDaily().get(0).getDay())!=1){
                        entriesys_sysAverage.add(new Entry(1, Float.parseFloat(typeData.getMonthly().get(0).getSysAverage())));
                        entriesys_diaAverage.add(new Entry(1, Float.parseFloat(typeData.getMonthly().get(0).getDiaAverage())));
                    }

                    for (int i = 0; i < typeData.getDaily().size(); i++) {

                        entriesys_sysAverage.add(new Entry(Float.parseFloat(typeData.getDaily().get(i).getDay()),
                                Float.valueOf(typeData.getDaily().get(i).getSysAverage())));
                        entriesys_diaAverage.add(new Entry(Float.parseFloat(typeData.getDaily().get(i).getDay()),
                                Float.valueOf(typeData.getDaily().get(i).getDiaAverage())));

                    }

                    if(Integer.parseInt(typeData.getDaily().get(typeData.getDaily().size()-1).getDay())<DayForMonth){
                        entriesys_sysAverage.add(new Entry(DayForMonth+1, Float.parseFloat(typeData.getMonthly().get(0).getSysAverage())));
                        entriesys_diaAverage.add(new Entry(DayForMonth+1, Float.parseFloat(typeData.getMonthly().get(0).getDiaAverage())));
                    }

                }else {
                    for (int i = 1;i<DayForMonth+1;i++){
                        entriesys_sysAverage.add(new Entry(i, Float.valueOf(min)));
                        entriesys_diaAverage.add(new Entry(i, Float.valueOf(min)));
                    }
                }
                entriesList.add(entriesys_sysAverage);
                entriesList.add(entriesys_diaAverage);
                break;
            case 3:

                for (int i = 0;i<13;i++){
                    xstringList.add(i+"");
                }
                if(typeData.getMonthly()!=null && typeData.getMonthly().size()>0) {

                    if(Integer.parseInt(typeData.getMonthly().get(0).getMonth())!=1){
                        entriesys_sysAverage.add(new Entry(1, Float.parseFloat(typeData.getYear().get(0).getSysAverage())));
                        entriesys_diaAverage.add(new Entry(1, Float.parseFloat(typeData.getYear().get(0).getDiaAverage())));
                    }

                    for (int i = 0; i < typeData.getMonthly().size(); i++) {

                        entriesys_sysAverage.add(new Entry(Float.parseFloat(typeData.getMonthly().get(i).getMonth()),
                                Float.valueOf(typeData.getMonthly().get(i).getSysAverage())));
                        entriesys_diaAverage.add(new Entry(Float.parseFloat(typeData.getMonthly().get(i).getMonth()),
                                Float.valueOf(typeData.getMonthly().get(i).getDiaAverage())));

                    }

                    if(Integer.parseInt(typeData.getMonthly().
                            get(typeData.getMonthly().size()-1).getMonth())< 12){
                        entriesys_sysAverage.add(new Entry(12, Float.parseFloat(typeData.getYear().get(0).getSysAverage())));
                        entriesys_diaAverage.add(new Entry(12, Float.parseFloat(typeData.getYear().get(0).getDiaAverage())));
                    }

                }else {

                    if(typeData.getYear()!=null && typeData.getYear().size()>0){
                        for (int i = 1;i<13;i++){
                            entriesys_sysAverage.add(new Entry(i, Float.parseFloat(typeData.getYear().get(0).getSysAverage())));
                            entriesys_diaAverage.add(new Entry(i, Float.parseFloat(typeData.getYear().get(0).getDiaAverage())));
                        }
                    }else{
                        for (int i = 1;i<13;i++){
                            entriesys_sysAverage.add(new Entry(i, Float.valueOf(19)));
                            entriesys_diaAverage.add(new Entry(i, Float.valueOf(19)));
                        }
                    }

                }
                entriesList.add(entriesys_sysAverage);
                entriesList.add(entriesys_diaAverage);
                break;
            default:
                break;
        }
        chart.setEntriesList(entriesList);
        chart.setxStringType(xstringList);
        chart.setType(0);

        return chart;

    }










    public Chart setWeightChatData(int graphicType,FriendGraphicModel.TypeData typeData,FriendGraphicDayModel friendGraphicDayModel,Calendar startCalendar,float min){

        Chart chart = new Chart();
        List<String> xstringList = new ArrayList<>();
        List<List<? extends BaseEntry>> entriesList = new ArrayList<>();
        List<Entry> entriesys_Average = new ArrayList<>();

        switch(graphicType){
            case 1:
                for (int day = 0;day<25;day++){
                    xstringList.add(day+"");
                }
                Date date = new Date();
                Calendar calendar = Calendar.getInstance();
                if(friendGraphicDayModel.getWeight()!=null && friendGraphicDayModel.getWeight().size()>0 && friendGraphicDayModel.getWeight().get(0).getVal()!=null) {

                    float Total = 0;
                    for (int i = 0;i<friendGraphicDayModel.getWeight().size();i++){
                        Total += Float.parseFloat(friendGraphicDayModel.getWeight().get(i).getVal());
                    }
                    float average1 = Total/friendGraphicDayModel.getWeight().size();
                    friendGraphicDayModel.getWeight().get(0).setAverage(average1+"");


                    date.setTime(Long.parseLong(friendGraphicDayModel.getWeight().get(0).getTimestamp()));
                    calendar.setTime(date);
                    int Firstx = calendar.get(Calendar.HOUR_OF_DAY);
                    if(Firstx!=1){
                        entriesys_Average.add(new Entry(1, average1));
                    }


                    for (int i = 0; i < friendGraphicDayModel.getWeight().size(); i++) {
                        date.setTime(Long.parseLong(friendGraphicDayModel.getWeight().get(i).getTimestamp()));
                        calendar.setTime(date);
                        int x = calendar.get(Calendar.HOUR_OF_DAY);
                        entriesys_Average.add(new Entry(Float.parseFloat(x+"."+calendar.get(Calendar.MINUTE)), Float.valueOf(friendGraphicDayModel.getWeight().get(i).getVal())));
                    }

                    date.setTime(Long.parseLong(friendGraphicDayModel.getWeight().
                            get(friendGraphicDayModel.getWeight().size()-1).getTimestamp()));
                    calendar.setTime(date);
                    if(calendar.get(Calendar.HOUR)<24){
                        entriesys_Average.add(new Entry(24, average1));
                    }

                }else {
                    for (int i = 1;i<25;i++){
                        entriesys_Average.add(new Entry(i, Float.valueOf(min)));
                    }

                }

                entriesList.add(entriesys_Average);
                break;
            case 2:
                int DayForMonth = startCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                for (int day = 0;day<DayForMonth+1;day++){
                    xstringList.add(day+"");
                }
                if(typeData.getDaily()!=null && typeData.getDaily().size()>0) {

                    if(Integer.parseInt(typeData.getDaily().get(0).getDay())!=1){
                        entriesys_Average.add(new Entry(1, Float.parseFloat(typeData.getMonthly().get(0).getAverage())));
                    }

                    for (int i = 0; i < typeData.getDaily().size(); i++) {

                        entriesys_Average.add(new Entry(Float.parseFloat(typeData.getDaily().get(i).getDay()),
                                Float.valueOf(typeData.getDaily().get(i).getAverage())));


                    }

                    if(Integer.parseInt(typeData.getDaily().get(typeData.getDaily().size()-1).getDay())<DayForMonth){
                        entriesys_Average.add(new Entry(DayForMonth+1, Float.parseFloat(typeData.getMonthly().get(0).getAverage())));
                    }

                }else {
                    for (int i = 1;i<DayForMonth+1;i++){
                        entriesys_Average.add(new Entry(i, Float.valueOf(min)));
                    }
                }
                entriesList.add(entriesys_Average);
                break;
            case 3:

                for (int i = 0;i<13;i++){
                    xstringList.add(i+"");
                }
                if(typeData.getMonthly()!=null && typeData.getMonthly().size()>0) {

                    if(Integer.parseInt(typeData.getMonthly().get(0).getMonth())!=1){
                        entriesys_Average.add(new Entry(1, Float.parseFloat(typeData.getYear().get(0).getAverage())));
                    }

                    for (int i = 0; i < typeData.getMonthly().size(); i++) {

                        entriesys_Average.add(new Entry(Float.parseFloat(typeData.getMonthly().get(i).getMonth()),
                                Float.valueOf(typeData.getMonthly().get(i).getAverage())));


                    }

                    if(Integer.parseInt(typeData.getMonthly().
                            get(typeData.getMonthly().size()-1).getMonth())< 12){
                        entriesys_Average.add(new Entry(12, Float.parseFloat(typeData.getYear().get(0).getAverage())));
                    }

                }else {
                    for (int i = 1;i<13;i++){
                        entriesys_Average.add(new Entry(i, Float.valueOf(min)));
                    }
                }
                entriesList.add(entriesys_Average);

                break;
            default:
                break;
        }
        chart.setEntriesList(entriesList);
        chart.setxStringType(xstringList);
        chart.setType(0);
        return chart;

    }



    public Chart setTempChatData(int graphicType,FriendGraphicModel.TypeData typeData,FriendGraphicDayModel friendGraphicDayModel,Calendar startCalendar,float min){

        Chart chart = new Chart();
        List<String> xstringList = new ArrayList<>();
        List<List<? extends BaseEntry>> entriesList = new ArrayList<>();
        List<Entry> entriesys_Average = new ArrayList<>();

        switch(graphicType){
            case 1:
                for (int day = 0;day<25;day++){
                    xstringList.add(day+"");
                }
                Date date = new Date();
                Calendar calendar = Calendar.getInstance();
                if(friendGraphicDayModel.getTemp()!=null &&friendGraphicDayModel.getTemp().size()>0 && friendGraphicDayModel.getTemp().get(0).getVal()!=null) {

                    float Total = 0;
                    for (int i = 0;i<friendGraphicDayModel.getTemp().size();i++){
                        Total += Float.parseFloat(friendGraphicDayModel.getTemp().get(i).getVal());
                    }
                    float average1 = Total/friendGraphicDayModel.getTemp().size();
                    friendGraphicDayModel.getTemp().get(0).setAverage(average1+"");


                    date.setTime(Long.parseLong(friendGraphicDayModel.getTemp().get(0).getTimestamp()));
                    calendar.setTime(date);
                    int Firstx = calendar.get(Calendar.HOUR_OF_DAY);
                    if(Firstx!=1){
                        entriesys_Average.add(new Entry(1, average1));
                    }


                    for (int i = 0; i < friendGraphicDayModel.getTemp().size(); i++) {
                        date.setTime(Long.parseLong(friendGraphicDayModel.getTemp().get(i).getTimestamp()));
                        calendar.setTime(date);
                        int x = calendar.get(Calendar.HOUR_OF_DAY);
                        entriesys_Average.add(new Entry(Float.parseFloat(x+"."+calendar.get(Calendar.MINUTE)), Float.valueOf(friendGraphicDayModel.getTemp().get(i).getVal())));
                    }

                    date.setTime(Long.parseLong(friendGraphicDayModel.getTemp().
                            get(friendGraphicDayModel.getTemp().size()-1).getTimestamp()));
                    calendar.setTime(date);
                    if(calendar.get(Calendar.HOUR)<24){
                        entriesys_Average.add(new Entry(24, average1));
                    }

                }else {
                    for (int i = 1;i<25;i++){
                        entriesys_Average.add(new Entry(i, Float.valueOf(min)));
                    }

                }

                entriesList.add(entriesys_Average);
                break;
            case 2:
                int DayForMonth = startCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                for (int day = 0;day<DayForMonth+1;day++){
                    xstringList.add(day+"");
                }
                if(typeData.getDaily()!=null && typeData.getDaily().size()>0) {

                    if(Integer.parseInt(typeData.getDaily().get(0).getDay())!=1){
                        entriesys_Average.add(new Entry(1, Float.parseFloat(typeData.getMonthly().get(0).getAverage())));
                    }

                    for (int i = 0; i < typeData.getDaily().size(); i++) {

                        entriesys_Average.add(new Entry(Float.parseFloat(typeData.getDaily().get(i).getDay()),
                                Float.valueOf(typeData.getDaily().get(i).getAverage())));


                    }

                    if(Integer.parseInt(typeData.getDaily().get(typeData.getDaily().size()-1).getDay())<DayForMonth){
                        entriesys_Average.add(new Entry(DayForMonth+1, Float.parseFloat(typeData.getMonthly().get(0).getAverage())));
                    }

                }else {
                    for (int i = 1;i<DayForMonth+1;i++){
                        entriesys_Average.add(new Entry(i, Float.valueOf(min)));
                    }
                }
                entriesList.add(entriesys_Average);
                break;
            case 3:

                for (int i = 0;i<13;i++){
                    xstringList.add(i+"");
                }
                if(typeData.getMonthly()!=null && typeData.getMonthly().size()>0) {

                    if(Integer.parseInt(typeData.getMonthly().get(0).getMonth())!=1){
                        entriesys_Average.add(new Entry(1, Float.parseFloat(typeData.getYear().get(0).getAverage())));
                    }

                    for (int i = 0; i < typeData.getMonthly().size(); i++) {

                        entriesys_Average.add(new Entry(Float.parseFloat(typeData.getMonthly().get(i).getMonth()),
                                Float.valueOf(typeData.getMonthly().get(i).getAverage())));


                    }

                    if(Integer.parseInt(typeData.getMonthly().
                            get(typeData.getMonthly().size()-1).getMonth())< 12){
                        entriesys_Average.add(new Entry(12, Float.parseFloat(typeData.getYear().get(0).getAverage())));
                    }

                }else {
                    for (int i = 1;i<13;i++){
                        entriesys_Average.add(new Entry(i, Float.valueOf(min)));
                    }
                }
                entriesList.add(entriesys_Average);

                break;
            default:
                break;
        }
        chart.setEntriesList(entriesList);
        chart.setxStringType(xstringList);
        chart.setType(0);
        return chart;

    }


    public Chart setHeart_RateChatData(int graphicType,FriendGraphicModel.TypeData typeData,FriendGraphicDayModel friendGraphicDayModel,Calendar startCalendar,float min){

        Chart chart = new Chart();
        List<String> xstringList = new ArrayList<>();
        List<List<? extends BaseEntry>> entriesList = new ArrayList<>();
        List<Entry> entriesys_Average = new ArrayList<>();

        switch(graphicType){
            case 1:
                for (int day = 0;day<25;day++){
                    xstringList.add(day+"");
                }
                Date date = new Date();
                Calendar calendar = Calendar.getInstance();
                if(friendGraphicDayModel.getHeart_rate()!=null && friendGraphicDayModel.getHeart_rate().size()>0 && friendGraphicDayModel.getHeart_rate().get(0).getVal()!=null) {

                    float Total = 0;
                    for (int i = 0;i<friendGraphicDayModel.getHeart_rate().size();i++){
                        Total += Float.parseFloat(friendGraphicDayModel.getHeart_rate().get(i).getVal());
                    }
                    float average1 = Total/friendGraphicDayModel.getHeart_rate().size();
                    friendGraphicDayModel.getHeart_rate().get(0).setAverage(average1+"");


                    date.setTime(Long.parseLong(friendGraphicDayModel.getHeart_rate().get(0).getTimestamp()));
                    calendar.setTime(date);
                    int Firstx = calendar.get(Calendar.HOUR_OF_DAY);
                    if(Firstx!=1){
                        entriesys_Average.add(new Entry(1, average1));
                    }


                    for (int i = 0; i < friendGraphicDayModel.getHeart_rate().size(); i++) {
                        date.setTime(Long.parseLong(friendGraphicDayModel.getHeart_rate().get(i).getTimestamp()));
                        calendar.setTime(date);
                        int x = calendar.get(Calendar.HOUR_OF_DAY);
//                        String minute ;
//                        if (calendar.get(Calendar.MINUTE)<=9){
//                            minute = "0"+calendar.get(Calendar.MINUTE);
//                        }else{
//                            minute = String.valueOf(calendar.get(Calendar.MINUTE));
//                        }
                        double minute_double = Double.parseDouble(calendar.get(Calendar.MINUTE)+"")/60.0;
                        String minute = String.valueOf(minute_double).substring(2,String.valueOf(minute_double).length());
                        entriesys_Average.add(new Entry(Float.parseFloat(x+"."+minute), Float.valueOf(friendGraphicDayModel.getHeart_rate().get(i).getVal())));
                    }

                    date.setTime(Long.parseLong(friendGraphicDayModel.getHeart_rate().
                            get(friendGraphicDayModel.getHeart_rate().size()-1).getTimestamp()));
                    calendar.setTime(date);
                    if(calendar.get(Calendar.HOUR)<24){
                        entriesys_Average.add(new Entry(24, average1));
                    }

                }else {
                    for (int i = 1;i<25;i++){
                        entriesys_Average.add(new Entry(i, Float.valueOf(min)));
                    }

                }

                entriesList.add(entriesys_Average);
                break;
            case 2:
                int DayForMonth = startCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                for (int day = 0;day<DayForMonth+1;day++){
                    xstringList.add(day+"");
                }
                if(typeData.getDaily()!=null && typeData.getDaily().size()>0) {

                    if(Integer.parseInt(typeData.getDaily().get(0).getDay())!=1){
                        entriesys_Average.add(new Entry(1, Float.parseFloat(typeData.getMonthly().get(0).getAverage())));
                    }

                    for (int i = 0; i < typeData.getDaily().size(); i++) {

                        entriesys_Average.add(new Entry(Float.parseFloat(typeData.getDaily().get(i).getDay()),
                                Float.valueOf(typeData.getDaily().get(i).getAverage())));


                    }

                    if(Integer.parseInt(typeData.getDaily().get(typeData.getDaily().size()-1).getDay())<DayForMonth){
                        entriesys_Average.add(new Entry(DayForMonth+1, Float.parseFloat(typeData.getMonthly().get(0).getAverage())));
                    }

                }else {
                    for (int i = 1;i<DayForMonth+1;i++){
                        entriesys_Average.add(new Entry(i, Float.valueOf(min)));
                    }
                }
                entriesList.add(entriesys_Average);
                break;
            case 3:

                for (int i = 0;i<13;i++){
                    xstringList.add(i+"");
                }
                if(typeData.getMonthly()!=null && typeData.getMonthly().size()>0) {

                    if(Integer.parseInt(typeData.getMonthly().get(0).getMonth())!=1){
                        entriesys_Average.add(new Entry(1, Float.parseFloat(typeData.getYear().get(0).getAverage())));
                    }

                    for (int i = 0; i < typeData.getMonthly().size(); i++) {

                        entriesys_Average.add(new Entry(Float.parseFloat(typeData.getMonthly().get(i).getMonth()),
                                Float.valueOf(typeData.getMonthly().get(i).getAverage())));


                    }

                    if(Integer.parseInt(typeData.getMonthly().
                            get(typeData.getMonthly().size()-1).getMonth())< 12){
                        entriesys_Average.add(new Entry(12, Float.parseFloat(typeData.getYear().get(0).getAverage())));
                    }

                }else {
                    for (int i = 1;i<13;i++){
                        entriesys_Average.add(new Entry(i, Float.valueOf(min)));
                    }
                }
                entriesList.add(entriesys_Average);

                break;
            default:
                break;
        }
        chart.setEntriesList(entriesList);
        chart.setxStringType(xstringList);
        chart.setType(0);
        return chart;

    }



    public Chart setSugarChatData(int graphicType,FriendGraphicModel.TypeData typeData,FriendGraphicDayModel friendGraphicDayModel,Calendar startCalendar,float min){

        Chart chart = new Chart();
        List<String> xstringList = new ArrayList<>();
        List<List<? extends BaseEntry>> entriesList = new ArrayList<>();
        List<Entry> entriesys_Average = new ArrayList<>();

        switch(graphicType){
            case 1:
                for (int day = 0;day<25;day++){
                    xstringList.add(day+"");
                }
                Date date = new Date();
                Calendar calendar = Calendar.getInstance();
                if(friendGraphicDayModel.getBlood_glucose()!=null && friendGraphicDayModel.getBlood_glucose().size()>0 && friendGraphicDayModel.getBlood_glucose().get(0).getVal()!=null) {

                    float Total = 0;
                    for (int i = 0;i<friendGraphicDayModel.getBlood_glucose().size();i++){
                        Total += Float.parseFloat(friendGraphicDayModel.getBlood_glucose().get(i).getVal());
                    }
                    float average1 = Total/friendGraphicDayModel.getBlood_glucose().size();
                    friendGraphicDayModel.getBlood_glucose().get(0).setAverage(average1+"");


                    date.setTime(Long.parseLong(friendGraphicDayModel.getBlood_glucose().get(0).getTimestamp()));
                    calendar.setTime(date);
                    int Firstx = calendar.get(Calendar.HOUR_OF_DAY);
                    if(Firstx!=1){
                        entriesys_Average.add(new Entry(1, average1));
                    }


                    for (int i = 0; i < friendGraphicDayModel.getBlood_glucose().size(); i++) {
                        date.setTime(Long.parseLong(friendGraphicDayModel.getBlood_glucose().get(i).getTimestamp()));
                        calendar.setTime(date);
                        int x = calendar.get(Calendar.HOUR_OF_DAY);
                        entriesys_Average.add(new Entry(Float.parseFloat(x+"."+calendar.get(Calendar.MINUTE)), Float.valueOf(friendGraphicDayModel.getBlood_glucose().get(i).getVal())));
                    }

                    date.setTime(Long.parseLong(friendGraphicDayModel.getBlood_glucose().
                            get(friendGraphicDayModel.getBlood_glucose().size()-1).getTimestamp()));
                    calendar.setTime(date);
                    if(calendar.get(Calendar.HOUR)<24){
                        entriesys_Average.add(new Entry(24, average1));
                    }

                }else {
                    for (int i = 1;i<25;i++){
                        entriesys_Average.add(new Entry(i, Float.valueOf(min)));
                    }

                }

                entriesList.add(entriesys_Average);
                break;
            case 2:
                int DayForMonth = startCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                for (int day = 0;day<DayForMonth+1;day++){
                    xstringList.add(day+"");
                }
                if(typeData.getDaily()!=null && typeData.getDaily().size()>0) {

                    if(Integer.parseInt(typeData.getDaily().get(0).getDay())!=1){
                        entriesys_Average.add(new Entry(1, Float.parseFloat(typeData.getMonthly().get(0).getAverage())));
                    }

                    for (int i = 0; i < typeData.getDaily().size(); i++) {

                        entriesys_Average.add(new Entry(Float.parseFloat(typeData.getDaily().get(i).getDay()),
                                Float.valueOf(typeData.getDaily().get(i).getAverage())));


                    }

                    if(Integer.parseInt(typeData.getDaily().get(typeData.getDaily().size()-1).getDay())<DayForMonth){
                        entriesys_Average.add(new Entry(DayForMonth+1, Float.parseFloat(typeData.getMonthly().get(0).getAverage())));
                    }

                }else {
                    for (int i = 1;i<DayForMonth+1;i++){
                        entriesys_Average.add(new Entry(i, Float.valueOf(min)));
                    }
                }
                entriesList.add(entriesys_Average);
                break;
            case 3:

                for (int i = 0;i<13;i++){
                    xstringList.add(i+"");
                }
                if(typeData.getMonthly()!=null && typeData.getMonthly().size()>0) {

                    if(Integer.parseInt(typeData.getMonthly().get(0).getMonth())!=1){
                        entriesys_Average.add(new Entry(1, Float.parseFloat(typeData.getYear().get(0).getAverage())));
                    }

                    for (int i = 0; i < typeData.getMonthly().size(); i++) {

                        entriesys_Average.add(new Entry(Float.parseFloat(typeData.getMonthly().get(i).getMonth()),
                                Float.valueOf(typeData.getMonthly().get(i).getAverage())));


                    }

                    if(Integer.parseInt(typeData.getMonthly().
                            get(typeData.getMonthly().size()-1).getMonth())< 12){
                        entriesys_Average.add(new Entry(12, Float.parseFloat(typeData.getYear().get(0).getAverage())));
                    }

                }else {
                    for (int i = 1;i<13;i++){
                        entriesys_Average.add(new Entry(i, Float.valueOf(min)));
                    }
                }
                entriesList.add(entriesys_Average);

                break;
            default:
                break;
        }
        chart.setEntriesList(entriesList);
        chart.setxStringType(xstringList);
        chart.setType(0);
        return chart;

    }

}
