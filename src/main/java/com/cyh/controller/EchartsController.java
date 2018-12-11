package com.cyh.controller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.cyh.pojo.ConfigureInfo;
import com.cyh.service.UtilService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cyh.pojo.MonthBill;
import com.cyh.pojo.WaterConsumption;
import com.cyh.service.WaterConsumptionService;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class EchartsController {
    @Autowired
    private WaterConsumptionService waterConsumptionService;

    @Autowired
    private UtilService utilService;

    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
    public static SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
    public static SimpleDateFormat sdfMonth = new SimpleDateFormat("M");

    /**
     * 查看全镇用水量与水费
     * 
     * @param year
     *            要查询的年份
     * @param type
     *            统计各月还是各季度
     * @return map
     */
    @SuppressWarnings("null")
    @RequestMapping("createEchartsByAll")
    @ResponseBody
    public Map<String, Object> createEchartsByAll(int year, int type) {
        Calendar date = Calendar.getInstance();
        MonthBill cost;
        WaterConsumption amount;
        Map<String, Object> map = new HashMap<>();
        if (date.get(Calendar.YEAR) == year) {
            cost = waterConsumptionService.waterConsumptionAllCost("");
            amount = waterConsumptionService.waterConsumptionAllAmount("");
        } else {
            cost = waterConsumptionService.waterConsumptionAllCost(String.valueOf(year));
            amount = waterConsumptionService.waterConsumptionAllAmount(String.valueOf(year));
        }
        if (type == 1) { // 如果是统计各月份
            List<Double> listcost = new ArrayList<>();
            listcost.add(cost.getMone());
            listcost.add(cost.getMtwo());
            listcost.add(cost.getMthree());
            listcost.add(cost.getMfour());
            listcost.add(cost.getMfive());
            listcost.add(cost.getMsix());
            listcost.add(cost.getMseven());
            listcost.add(cost.getMeight());
            listcost.add(cost.getMnine());
            listcost.add(cost.getMten());
            listcost.add(cost.getMeleven());
            listcost.add(cost.getMtwelve());

            List<Double> listamount = new ArrayList<>();
            listamount.add(amount.getWone());
            listamount.add(amount.getWtwo());
            listamount.add(amount.getWthree());
            listamount.add(amount.getWfour());
            listamount.add(amount.getWfive());
            listamount.add(amount.getWsix());
            listamount.add(amount.getWseven());
            listamount.add(amount.getWeight());
            listamount.add(amount.getWnine());
            listamount.add(amount.getWten());
            listamount.add(amount.getWeleven());
            listamount.add(amount.getWtwelve());
            map.put("listcost", listcost);
            map.put("listamount", listamount);
            return map;
        }

        // 如果是按季度统计
        List<Double> listQuartercost = new ArrayList<>();
        List<Double> listQuarteramount = new ArrayList<>();

        listQuartercost.add(new BigDecimal(cost.getMone() + cost.getMtwo() + cost.getMthree())
                .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        listQuartercost.add(new BigDecimal(cost.getMfour() + cost.getMfive() + cost.getMsix())
                .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        listQuartercost.add(new BigDecimal(cost.getMseven() + cost.getMeight() + cost.getMnine())
                .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        listQuartercost.add(new BigDecimal(cost.getMten() + cost.getMeleven() + cost.getMtwelve())
                .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());

        listQuarteramount.add(new BigDecimal(amount.getWone() + amount.getWtwo() + amount.getWthree())
                .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        listQuarteramount.add(new BigDecimal(amount.getWfour() + amount.getWfive() + amount.getWsix())
                .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        listQuarteramount.add(new BigDecimal(amount.getWseven() + amount.getWeight() + amount.getWnine())
                .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        listQuarteramount.add(new BigDecimal(amount.getWten() + amount.getWeleven() + amount.getWtwelve())
                .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());

        map.put("listcost", listQuartercost);
        map.put("listamount", listQuarteramount);
        return map;
    }

    /**
     * 查看个人用水量与水费
     * 
     * @param year
     *            要查询的年份
     * @param type
     *            统计各月还是各季度
     * @param cid
     *            用户编号
     * @return map
     */
    @SuppressWarnings("null")
    @RequestMapping("createEchartsByUname")
    @ResponseBody
    public Map<String, Object> createEchartsByUname(int year, int type, int cid){
        Calendar date = Calendar.getInstance();
        MonthBill cost;
        WaterConsumption amount;
        Map<String, Object> map = new HashMap<>();
        if (date.get(Calendar.YEAR) == year) {
            cost = waterConsumptionService.waterConsumptionUserCost("", cid);
            amount = waterConsumptionService.waterConsumptionUserAmount("", cid);
        } else {
            cost = waterConsumptionService.waterConsumptionUserCost(String.valueOf(year), cid);
            amount = waterConsumptionService.waterConsumptionUserAmount(String.valueOf(year), cid);
        }
        if (type == 1) { // 如果是统计各月份
            List<Double> listcost = new ArrayList<Double>();
            listcost.add(cost.getMone());
            listcost.add(cost.getMtwo());
            listcost.add(cost.getMthree());
            listcost.add(cost.getMfour());
            listcost.add(cost.getMfive());
            listcost.add(cost.getMsix());
            listcost.add(cost.getMseven());
            listcost.add(cost.getMeight());
            listcost.add(cost.getMnine());
            listcost.add(cost.getMten());
            listcost.add(cost.getMeleven());
            listcost.add(cost.getMtwelve());

            List<Double> listamount = new ArrayList<Double>();
            listamount.add(amount.getWone());
            listamount.add(amount.getWtwo());
            listamount.add(amount.getWthree());
            listamount.add(amount.getWfour());
            listamount.add(amount.getWfive());
            listamount.add(amount.getWsix());
            listamount.add(amount.getWseven());
            listamount.add(amount.getWeight());
            listamount.add(amount.getWnine());
            listamount.add(amount.getWten());
            listamount.add(amount.getWeleven());
            listamount.add(amount.getWtwelve());
            map.put("listcost", listcost);
            map.put("listamount", listamount);
            return map;
        }

        // 如果是按季度统计
        List<Double> listQuartercost = new ArrayList<Double>();
        List<Double> listQuarteramount = new ArrayList<Double>();

        listQuartercost.add(new BigDecimal(cost.getMone() + cost.getMtwo() + cost.getMthree())
                .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        listQuartercost.add(new BigDecimal(cost.getMfour() + cost.getMfive() + cost.getMsix())
                .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        listQuartercost.add(new BigDecimal(cost.getMseven() + cost.getMeight() + cost.getMnine())
                .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        listQuartercost.add(new BigDecimal(cost.getMten() + cost.getMeleven() + cost.getMtwelve())
                .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());

        listQuarteramount.add(new BigDecimal(amount.getWone() + amount.getWtwo() + amount.getWthree())
                .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        listQuarteramount.add(new BigDecimal(amount.getWfour() + amount.getWfive() + amount.getWsix())
                .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        listQuarteramount.add(new BigDecimal(amount.getWseven() + amount.getWeight() + amount.getWnine())
                .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        listQuarteramount.add(new BigDecimal(amount.getWten() + amount.getWeleven() + amount.getWtwelve())
                .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());

        map.put("listcost", listQuartercost);
        map.put("listamount", listQuarteramount);
        return map;
    }

    /**
     *
     * @param request
     * @param type   1: 全镇统计(beforeDataTableForAll)  2：个人统计(beforeDataTableForPerson)  3：大数据分析(beforeDataTableForData)
     * @return
     * @throws ParseException
     */
    @RequestMapping(value={"beforeDataTableForAll.action","beforeDataTableForPerson.action","beforeDataTableForData.action"})
    public ModelAndView beforeBigDataAnalysis(HttpServletRequest request,int type) throws ParseException {
        ModelAndView mv = new ModelAndView();
        Map<String,List<String>> map;
        if(request.getSession().getAttribute("selectTime")==null){
            ConfigureInfo settings = utilService.getConfigureInfoByName("bigDataAnalysis");
            Date startDate = sdf.parse(settings.getPath());
            Date endDate = new Date();
            map = createDaysDatas(startDate,endDate);
            request.getSession().setAttribute("selectTime",map);
        }else{
            map = (Map<String, List<String>>) request.getSession().getAttribute("selectTime");
        }
        Gson gson = new Gson();
        mv.addObject("selectTime",gson.toJson(map));
        if(type == 1){
            mv.setViewName("jsp/waterConsumptionAll.jsp");
        }else if(type ==2){
            mv.setViewName("jsp/waterConsumptionPersonal.jsp");
        }else if(type ==3){
            mv.setViewName("jsp/bigDataAnalysis.jsp");
        }
        return mv;
    }

    @ResponseBody
    @RequestMapping("bigDataAnalysis.action")
    public Map<String,Object> bigDataAnalysis(HttpServletRequest request,String year,String month,String top){
        Map<String,Object> map = new HashMap<>();
        try{
            Map<String,List<String>> selectTimeMap = (Map<String, List<String>>) request.getSession().getAttribute("selectTime");
            if(selectTimeMap.get(year) == null){
                map.put("error","服务器内部错误!");
                return map;
            }else{
                if(selectTimeMap.get(year).get(Integer.valueOf(month)-1) ==null){
                    map.put("error","服务器内部错误!");
                    return map;
                }
            }
            Integer.valueOf(top);
        }catch (Exception e){
            map.put("error","服务器内部错误!");
            return map;
        }
        //用水量查询
        String wMonthStr = getWaterconsumptionColumn(Integer.valueOf(month));
        String wLastMonthStr;
        String table = getTableNameByYearAndTabName("waterconsumption",year);
        List<Map<String, Object>> waterConsumpTopData;
        if("1".equals(month)){
            wLastMonthStr = "wtwelve";
            String lastTable = getLastTableNameByYearAndTabName("waterconsumption",year);
            waterConsumpTopData = waterConsumptionService.getTopDataByLastTable(table,lastTable,wMonthStr,wLastMonthStr,top);
        }else{
            wLastMonthStr = getWaterconsumptionColumn(Integer.valueOf(month)-1);
            waterConsumpTopData = waterConsumptionService.getTopDataByMonths(table,wMonthStr,wLastMonthStr,top);
        }
        List<Integer>  waterConsumpPieData = waterConsumptionService.getPieDataByMonths(table,wMonthStr,top);
        map.put("wTopData",waterConsumpTopData); //前top位用户用水量
        map.put("wPieData",waterConsumpPieData);//前top位用户用水量占当月总用水量的比重

        //月水费查询
        String mMonthStr = getMonthbillColumn(Integer.valueOf(month));
        String mLastMonthStr;
        String mTable = getTableNameByYearAndTabName("monthbill",year);
        List<Map<String, Object>> monthbillTopData;
        if("1".equals(month)){
            mLastMonthStr = "mtwelve";
            String lastTable = getLastTableNameByYearAndTabName("monthbill",year);
            monthbillTopData = waterConsumptionService.getTopDataByLastTable(mTable,lastTable,mMonthStr,mLastMonthStr,top);
        }else{
            mLastMonthStr = getMonthbillColumn(Integer.valueOf(month)-1);
            monthbillTopData = waterConsumptionService.getTopDataByMonths(mTable,mMonthStr,mLastMonthStr,top);
        }
        List<Integer>  monthbillPieData = waterConsumptionService.getPieDataByMonths(mTable,mMonthStr,top);

        map.put("mTopData",monthbillTopData); //前top位用户水费
        map.put("mPieData",monthbillPieData);//前top位用户水费占当月总水费的比重
        return map;
    }

    /**
     * @param wMonth 数字类型的月份
     * @return  返回Waterconsumption表的月份列
     */
    public String getWaterconsumptionColumn(int wMonth) {
        String month = null;
        switch (wMonth) {
            case 1:
                month = "wone";
                break;
            case 2:
                month = "wtwo";
                break;
            case 3:
                month = "wthree";
                break;
            case 4:
                month = "wfour";
                break;
            case 5:
                month = "wfive";
                break;
            case 6:
                month = "wsix";
                break;
            case 7:
                month = "wseven";
                break;
            case 8:
                month = "weight";
                break;
            case 9:
                month = "wnine";
                break;
            case 10:
                month = "wten";
                break;
            case 11:
                month = "weleven";
                break;
            case 12:
                month = "wtwelve";
                break;
        }
        return month;
    }

    /**
     * @param mMonth 数字类型的月份
     * @return  返回monthbill表的月份列
     */
    public static String getMonthbillColumn(int mMonth) {
        String month = null;
        switch (mMonth) {
            case 1:
                month = "mone";
                break;
            case 2:
                month = "mtwo";
                break;
            case 3:
                month = "mthree";
                break;
            case 4:
                month = "mfour";
                break;
            case 5:
                month = "mfive";
                break;
            case 6:
                month = "msix";
                break;
            case 7:
                month = "mseven";
                break;
            case 8:
                month = "meight";
                break;
            case 9:
                month = "mnine";
                break;
            case 10:
                month = "mten";
                break;
            case 11:
                month = "meleven";
                break;
            case 12:
                month = "mtwelve";
                break;
        }
        return month;
    }

    public String getTableNameByYearAndTabName(String tableName,String year){
        ConfigureInfo settings = utilService.getConfigureInfoByName("updateTables"); //获取创建表初始化表的配置
        if(settings.getPath().equals(year)){
            return tableName;
        }else{
            return tableName+year;
        }
    }

    public String getLastTableNameByYearAndTabName(String tableName,String year){
        int y = Integer.valueOf(year);
        y = y-1;
        return tableName+y;
    }

    /**
     *  根据开始时间和结束时间，年——>list 月份
     * @param startDate
     * @param endDate
     * @return
     */
    private Map<String,List<String>> createDaysDatas(Date startDate,Date endDate) {
        Map<String,List<String>> map = new HashMap<>();
        List<String> month = new ArrayList<>();
        String year = sdfYear.format(startDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        boolean bContinue = true;
        while (bContinue) {
            // 测试此日期是否在指定日期之后
            if (endDate.after(cal.getTime())) {
                if(year.equals(sdfYear.format(cal.getTime()))){
                    month.add(sdfMonth.format(cal.getTime()));
                }else{
                    List<String> temp = new ArrayList<>();
                    for (String num : month) {
                        temp.add(num);
                    }
                    map.put(year,temp);
                    month.clear();
                    year = sdfYear.format(cal.getTime());
                    month.add(sdfMonth.format(cal.getTime()));
                }
                // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
                cal.add(Calendar.MONTH, 1);
            } else {
                map.put(year,month); // 把最后一年截止到现在的月份集合加到map
                break;
            }
        }
        return map;
    }
}
