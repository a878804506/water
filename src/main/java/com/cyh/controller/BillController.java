package com.cyh.controller;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSON;
import com.cyh.pojo.*;
import com.cyh.util.*;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cyh.exception.MyException;
import com.cyh.service.BillService;
import com.cyh.service.CustomerService;
import com.cyh.service.MonthBillService;
import com.cyh.service.Operator_logService;
import com.cyh.service.UtilService;
import com.cyh.service.WaterConsumptionService;

import jxl.SheetSettings;
import jxl.Workbook;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

@Controller
public class BillController {
    @Autowired
    private BillService billService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private MonthBillService monthBillService;
    @Autowired
    private Operator_logService operator_logService;
    @Autowired
    private UtilService utilService;
    @Autowired
    private WaterConsumptionService waterConsumptionService;

    // 根据编号和月份来修改用户的止码，并读取上个月的止码作为该月的起码
    @RequestMapping("addBill") // condition 条件类
    public ModelAndView addBill(Condition condition, HttpSession session, HttpServletRequest request) throws MyException {
        // 先改bill 止码
        Bill bill = null;
        //查询用户信息是因为此表中有用户的单价
        Customer customer = customerService.getCustomerById(condition.getId());
        // 创建过度对象 jsp所需的数据都放在过度对象中
        Middle mi = new Middle();
        mi.setUid(customer.getCid());
        mi.setUname(customer.getCname());
        mi.setAddress(customer.getCaddress());
        mi.setPrice(customer.getCprice());
        mi.setMonth(condition.getMonth());
        //返回到jsp的ModelAndView
        ModelAndView mv = new ModelAndView();
        mv.addObject("countSize", customerService.getAllCustomerCount()); // 用户总数 传到生成账单的页面上
        // 标识符号 用于判断点击‘导出Excel’按钮时，是否生成了收据 0：未点击’生成收据‘按钮 1：已点击
        mv.addObject("symbol", 1);
        Calendar cal = Calendar.getInstance(); // 获取当前时间
        mi.setYear(cal.get(Calendar.YEAR)); // 获取当前时间的年份
        int water_consumption = 0 ;  // 客户当月用水量
        switch (condition.getMonth()) { // 判断用户输入的是几月
        case 1:
            billService.updateone(condition);
            bill = billService.selectBill(condition); // 查询修改之后的用户账单
            // 创建过度对象 jsp所需的数据都放在过度对象中
            mi.setFirstNumber(bill.getThirteen());
            mi.setLastNumber(bill.getOne());
            BigDecimal y1 = new BigDecimal(customer.getCprice() * (bill.getOne() - bill.getThirteen()));
            mi.setTotal(y1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            mi.setFirstTotal(mi.getTotal() + 5.0);
            monthBillService.updatemone(mi);
            water_consumption = bill.getOne() - bill.getThirteen();
            // 更新用户用水量
            updateWaterConsumptionByMonth(condition.getMonth(), water_consumption, customer.getCid());
            break;
        case 2:
            billService.updatetwo(condition);
            bill = billService.selectBill(condition); // 查询修改之后的用户账单
            // 创建过度对象 jsp所需的数据都放在过度对象中
            mi.setFirstNumber(bill.getOne());
            mi.setLastNumber(bill.getTwo());
            BigDecimal y2 = new BigDecimal((customer.getCprice() * (bill.getTwo() - bill.getOne())));
            mi.setTotal(y2.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            mi.setFirstTotal(mi.getTotal() + 5.0);
            monthBillService.updatemtwo(mi);
            water_consumption = bill.getTwo() - bill.getOne();
            // 更新用户用水量
            updateWaterConsumptionByMonth(condition.getMonth(), water_consumption, customer.getCid());
            break;
        case 3:
            billService.updatethree(condition);
            bill = billService.selectBill(condition); // 查询修改之后的用户账单
            // 创建过度对象 jsp所需的数据都放在过度对象中
            mi.setFirstNumber(bill.getTwo());
            mi.setLastNumber(bill.getThree());
            BigDecimal y3 = new BigDecimal(customer.getCprice() * (bill.getThree() - bill.getTwo()));
            mi.setTotal(y3.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            mi.setFirstTotal(mi.getTotal() + 5.0);
            monthBillService.updatemthree(mi);
            water_consumption = bill.getThree() - bill.getTwo();
            // 更新用户用水量
            updateWaterConsumptionByMonth(condition.getMonth(), water_consumption, customer.getCid());
            break;
        case 4:
            billService.updatefour(condition);
            bill = billService.selectBill(condition); // 查询修改之后的用户账单
            // 创建过度对象 jsp所需的数据都放在过度对象中
            mi.setFirstNumber(bill.getThree());
            mi.setLastNumber(bill.getFour());
            BigDecimal y4 = new BigDecimal(customer.getCprice() * (bill.getFour() - bill.getThree()));
            mi.setTotal(y4.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            mi.setFirstTotal(mi.getTotal() + 5.0);
            monthBillService.updatemfour(mi);
            water_consumption = bill.getFour() - bill.getThree();
            // 更新用户用水量
            updateWaterConsumptionByMonth(condition.getMonth(), water_consumption, customer.getCid());
            break;
        case 5:
            billService.updatefive(condition);
            bill = billService.selectBill(condition); // 查询修改之后的用户账单
            // 创建过度对象 jsp所需的数据都放在过度对象中
            mi.setFirstNumber(bill.getFour());
            mi.setLastNumber(bill.getFive());
            BigDecimal y5 = new BigDecimal(customer.getCprice() * (bill.getFive() - bill.getFour()));
            mi.setTotal(y5.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            mi.setFirstTotal(mi.getTotal() + 5.0);
            monthBillService.updatemfive(mi);
            water_consumption = bill.getFive() - bill.getFour();
            // 更新用户用水量
            updateWaterConsumptionByMonth(condition.getMonth(), water_consumption, customer.getCid());
            break;
        case 6:
            billService.updatesix(condition);
            bill = billService.selectBill(condition); // 查询修改之后的用户账单
            // 创建过度对象 jsp所需的数据都放在过度对象中
            mi.setFirstNumber(bill.getFive());
            mi.setLastNumber(bill.getSix());
            BigDecimal y6 = new BigDecimal(customer.getCprice() * (bill.getSix() - bill.getFive()));
            mi.setTotal(y6.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            mi.setFirstTotal(mi.getTotal() + 5.0);
            monthBillService.updatemsix(mi);
            water_consumption = bill.getSix() - bill.getFive();
            // 更新用户用水量
            updateWaterConsumptionByMonth(condition.getMonth(), water_consumption, customer.getCid());
            break;
        case 7:
            billService.updateseven(condition);
            bill = billService.selectBill(condition); // 查询修改之后的用户账单
            // 创建过度对象 jsp所需的数据都放在过度对象中
            mi.setFirstNumber(bill.getSix());
            mi.setLastNumber(bill.getSeven());
            BigDecimal y7 = new BigDecimal(customer.getCprice() * (bill.getSeven() - bill.getSix()));
            mi.setTotal(y7.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            mi.setFirstTotal(mi.getTotal() + 5.0);
            monthBillService.updatemseven(mi);
            water_consumption = bill.getSeven() - bill.getSix();
            // 更新用户用水量
            updateWaterConsumptionByMonth(condition.getMonth(), water_consumption, customer.getCid());
            break;
        case 8:
            billService.updateeight(condition);
            bill = billService.selectBill(condition); // 查询修改之后的用户账单
            // 创建过度对象 jsp所需的数据都放在过度对象中
            mi.setFirstNumber(bill.getSeven());
            mi.setLastNumber(bill.getEight());
            BigDecimal y8 = new BigDecimal(customer.getCprice() * (bill.getEight() - bill.getSeven()));
            mi.setTotal(y8.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            mi.setFirstTotal(mi.getTotal() + 5.0);
            monthBillService.updatemeight(mi);
            water_consumption = bill.getEight() - bill.getSeven();
            // 更新用户用水量
            updateWaterConsumptionByMonth(condition.getMonth(), water_consumption, customer.getCid());
            break;
        case 9:
            billService.updatenine(condition);
            bill = billService.selectBill(condition); // 查询修改之后的用户账单
            // 创建过度对象 jsp所需的数据都放在过度对象中
            mi.setFirstNumber(bill.getEight());
            mi.setLastNumber(bill.getNine());
            BigDecimal y9 = new BigDecimal(customer.getCprice() * (bill.getNine() - bill.getEight()));
            mi.setTotal(y9.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            mi.setFirstTotal(mi.getTotal() + 5.0);
            monthBillService.updatemnine(mi);
            water_consumption = bill.getNine() - bill.getEight();
            // 更新用户用水量
            updateWaterConsumptionByMonth(condition.getMonth(), water_consumption, customer.getCid());
            break;
        case 10:
            billService.updateten(condition);
            bill = billService.selectBill(condition); // 查询修改之后的用户账单
            // 创建过度对象 jsp所需的数据都放在过度对象中
            mi.setFirstNumber(bill.getNine());
            mi.setLastNumber(bill.getTen());
            BigDecimal y10 = new BigDecimal(customer.getCprice() * (bill.getTen() - bill.getNine()));
            mi.setTotal(y10.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            mi.setFirstTotal(mi.getTotal() + 5.0);
            monthBillService.updatemten(mi);
            water_consumption = bill.getTen() - bill.getNine();
            // 更新用户用水量
            updateWaterConsumptionByMonth(condition.getMonth(), water_consumption, customer.getCid());
            break;
        case 11:
            billService.updateeleven(condition);
            bill = billService.selectBill(condition); // 查询修改之后的用户账单
            // 创建过度对象 jsp所需的数据都放在过度对象中
            mi.setFirstNumber(bill.getTen());
            mi.setLastNumber(bill.getEleven());
            BigDecimal y11 = new BigDecimal(customer.getCprice() * (bill.getEleven() - bill.getTen()));
            mi.setTotal(y11.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            mi.setFirstTotal(mi.getTotal() + 5.0);
            monthBillService.updatemeleven(mi);
            water_consumption = bill.getEleven() - bill.getTen();
            // 更新用户用水量
            updateWaterConsumptionByMonth(condition.getMonth(), water_consumption, customer.getCid());
            break;
        case 12:
            billService.updatetwelve(condition);
            bill = billService.selectBill(condition); // 查询修改之后的用户账单
            // 创建过度对象 jsp所需的数据都放在过度对象中
            mi.setFirstNumber(bill.getEleven());
            mi.setLastNumber(bill.getTwelve());
            BigDecimal y12 = new BigDecimal(customer.getCprice() * (bill.getTwelve() - bill.getEleven()));
            mi.setTotal(y12.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            mi.setFirstTotal(mi.getTotal() + 5.0);
            monthBillService.updatemtwelve(mi);
            water_consumption = bill.getTwelve() - bill.getEleven();
            // 更新用户用水量
            updateWaterConsumptionByMonth(condition.getMonth(), water_consumption, customer.getCid());
            break;
        case 13:
            // 1-2 月份连月开票走这里
            billService.updatetwo(condition);
            bill = billService.selectBill(condition); // 查询修改之后的用户账单
            // 创建过度对象 jsp所需的数据都放在过度对象中
            mi.setMonth(2);
            mi.setFirstNumber(bill.getThirteen());
            mi.setLastNumber(bill.getTwo());
            BigDecimal y13 = new BigDecimal((customer.getCprice() * (bill.getTwo() - bill.getThirteen())));
            mi.setTotal(y13.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            mi.setFirstTotal(mi.getTotal() + 10.0);
            monthBillService.updatemtwo(mi);
            water_consumption = bill.getTwo() - bill.getThirteen();
            // 更新用户用水量
            updateWaterConsumptionByMonth(2, water_consumption, customer.getCid());
            break;
        case 14:
            //  3-4 月份连月开票走这里
            billService.updatefour(condition);
            bill = billService.selectBill(condition); // 查询修改之后的用户账单
            // 创建过度对象 jsp所需的数据都放在过度对象中
            mi.setMonth(4);
            mi.setFirstNumber(bill.getTwo());
            mi.setLastNumber(bill.getFour());
            BigDecimal y14 = new BigDecimal((customer.getCprice() * (bill.getFour() - bill.getTwo())));
            mi.setTotal(y14.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            mi.setFirstTotal(mi.getTotal() + 10.0);
            monthBillService.updatemfour(mi);
            water_consumption = bill.getFour() - bill.getTwo();
            // 更新用户用水量
            updateWaterConsumptionByMonth(4, water_consumption, customer.getCid());
            break;
        }
        //设置默认地址
        if(org.apache.commons.lang3.StringUtils.isBlank(mi.getAddress())){
            mi.setAddress("紫金");
        }
        // 添加日志
        Operator_logUtil.insertOperator_log(customer, 5, session, null, mi, null, operator_logService);
        //添加到综合表记录
        TransitionModel tm = new TransitionModel(); // 记录客户各月用水量和水费 综合表
        tm.setCustomer_id(customer.getCid());
        tm.setCustomer_name(customer.getCname());
        tm.setYear(CommonUtil.DateToString(new Date(),"yyyy"));
        tm.setMonth(condition.getMonth().toString());
        tm.setCreate_time(CommonUtil.DateToString(new Date(),"yyyy-MM-dd HH:mm:ss"));
        tm.setWater_consumption(Double.valueOf(water_consumption));
        tm.setMonth_bill_cost(mi.getFirstTotal());
        utilService.replaceTransitionModel(tm);

        // 获取总价 准备分解数字 在jsp页面用
        String[] s;
        String[] ss;
        String[] y = new String[5];
        List<String> list = new ArrayList<>();
        String[] yy = new String[5];
        Double total1 = mi.getTotal(); // 获取没有加容量水费之前的水费
        Double total2 = 0.0;
        // 判断用户是要单月开票还是连月（1-2月）开票
        if (request.getParameter("password").equals("1")) {
            total2 = mi.getTotal() + 5;
        }
        if (request.getParameter("password").equals("2")|| request.getParameter("password").equals("4")) {
            total2 = mi.getTotal() + 10;
        }
        s = total1.toString().split("\\.");
        ss = total2.toString().split("\\.");
        String jiao = s[1]; // 把字符串‘角’写进过度对象
        for (int v = 0; v < jiao.length(); v++) {
            list.add(String.valueOf(jiao.charAt(v)));
        }

        mi.setJiao(list.get(0));
        String m = s[0]; // 获取‘.’以前的数字字符串 （不要分角）
        String n = ss[0]; // 获取‘.’以后的数字字符串 （不要分角）
        for (int i = 0; i < m.length(); i++) {
            y[i] = String.valueOf(m.charAt(i));
        }
        for (int j = 0; j < n.length(); j++) {
            yy[j] = String.valueOf(n.charAt(j));
        }
        // 判断y ，yy 数组的长度 以便写到Middle过度对象中
        if (y[4] != null) { // 五位数
            mi.setWan(y[0]);
            mi.setQian(y[1]);
            mi.setBai(y[2]);
            mi.setShi(y[3]);
            mi.setYuan(y[4]);
        } else if (y[3] != null) {
            mi.setQian(y[0]);
            mi.setBai(y[1]);
            mi.setShi(y[2]);
            mi.setYuan(y[3]);
        } else if (y[2] != null) {
            mi.setBai(y[0]);
            mi.setShi(y[1]);
            mi.setYuan(y[2]);
        } else if (y[1] != null) {
            mi.setShi(y[0]);
            mi.setYuan(y[1]);
        } else if (y[0] != null) {
            mi.setYuan(y[0]);
        }
        // 判断 yy
        if (yy[4] != null) {
            mi.setWan1(yy[0]);
            mi.setQian1(yy[1]);
            mi.setBai1(yy[2]);
            mi.setShi1(yy[3]);
            mi.setYuan1(yy[4]);
        } else if (yy[3] != null) {
            mi.setQian1(yy[0]);
            mi.setBai1(yy[1]);
            mi.setShi1(yy[2]);
            mi.setYuan1(yy[3]);
        } else if (yy[2] != null) {
            mi.setBai1(yy[0]);
            mi.setShi1(yy[1]);
            mi.setYuan1(yy[2]);
        } else if (yy[1] != null) {
            mi.setShi1(yy[0]);
            mi.setYuan1(yy[1]);
        } else if (yy[0] != null) {
            mi.setYuan1(yy[0]);
        }
        // 判断用户是从哪里点击进入的该方法
        if (request.getParameter("password").equals("1")) {
            mi.setWu("5");
        }
        if (request.getParameter("password").equals("2")) {
            mi.setTwoandtwo(1);
            mi.setLianyue("1-2");
        }
        if (request.getParameter("password").equals("4")) {
            mi.setTwoandtwo(1);
            mi.setLianyue("3-4");
        }
        mi.setLing("0");
        mv.addObject("mi", mi);

        // 统计的当天开票数量并展示
        tm.setCreate_time(CommonUtil.DateToString(new Date(),"yyyy-MM-dd"));
        int operate_cutomer_count_date = utilService.getOperateCutomerCountByToDate(tm);
        mv.addObject("operate_cutomer_count_date", operate_cutomer_count_date);

        // 获取当月录入用户总数
        mv.addObject("totalMonthCount", GetTotalCountByMonth.getTotalCountByMonth(monthBillService, mi.getMonth()));

        mi.setDaxie(NumToCNMoneyUtil.number2CNMontrayUnit(total2));
        mv.addObject("pwd", request.getParameter("password"));
        mv.setViewName("jsp/bill.jsp");
        session.setAttribute("middle", mi);
        session.setAttribute("mm", mi.getMonth());

        // 取出excel的存储路径
        ConfigureInfo excelSettings = utilService.getConfigureInfoByName("excel");
        if (StringUtils.isEmpty(excelSettings.getPath())) {
            throw new MyException("请先设置文件在服务器的生成路径！");
        }else{
            String fullMonth = String.format("%02d", mi.getMonth()); // 01,02,03,04,10,11,12 这样的月份
            session.setAttribute("fullFilePath", excelSettings.getPath() + System.getProperty("file.separator") + mi.getYear()+fullMonth);
        }
        return mv;
    }

    /**
     * 写入excel操作
     *
     * @param mi
     *            待生成数据
     * @param chineseNum
     *            大写价格
     * @param request
     * @throws Exception
     */
    public String excel(Middle mi, String chineseNum, HttpServletRequest request) throws Exception {
        // 第一种字体样式
        WritableFont font1 = new WritableFont(WritableFont.createFont("黑体"), 12, WritableFont.BOLD);
        WritableCellFormat format1 = new WritableCellFormat(font1);
        format1.setBorder(Border.BOTTOM, BorderLineStyle.DOUBLE);
        format1.setAlignment(jxl.format.Alignment.CENTRE); // 把水平对齐方式指定为居中
        format1.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE); // 把垂直对齐方式指定为居中
        // 第二种字体样式
        WritableFont font2 = new WritableFont(WritableFont.TIMES, 12);
        WritableCellFormat format2 = new WritableCellFormat(font2);
        format2.setAlignment(jxl.format.Alignment.CENTRE); // 把水平对齐方式指定为居中
        // 第三种字体样式
        WritableFont font3 = new WritableFont(WritableFont.createFont("黑体"), 10);
        WritableCellFormat format3 = new WritableCellFormat(font3);
        format3.setWrap(true);// 自动换行
        format3.setBorder(Border.ALL, BorderLineStyle.THIN);
        format3.setAlignment(jxl.format.Alignment.CENTRE); // 把水平对齐方式指定为居中
        format3.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE); // 把垂直对齐方式指定为居中
        // 第四种字体样式
        WritableFont font4 = new WritableFont(WritableFont.TIMES, 10);
        WritableCellFormat format4 = new WritableCellFormat(font4);
        format4.setWrap(true);// 自动换行
        format4.setBorder(Border.ALL, BorderLineStyle.THIN);
        format4.setAlignment(jxl.format.Alignment.CENTRE); // 把水平对齐方式指定为居中
        format4.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE); // 把垂直对齐方式指定为居中
        // 第五种字体样式
        WritableFont font5 = new WritableFont(WritableFont.TIMES, 9);
        WritableCellFormat format5 = new WritableCellFormat(font5);
        format5.setWrap(true);// 自动换行
        format5.setBorder(Border.ALL, BorderLineStyle.THIN);
        format5.setAlignment(jxl.format.Alignment.CENTRE); // 把水平对齐方式指定为居中
        format5.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE); // 把垂直对齐方式指定为居中
        // 第六种字体样式
        WritableFont font6 = new WritableFont(WritableFont.createFont("黑体"), 8);
        WritableCellFormat format6 = new WritableCellFormat(font6);
        format6.setBorder(Border.ALL, BorderLineStyle.THIN);
        format6.setAlignment(jxl.format.Alignment.CENTRE); // 把水平对齐方式指定为居中
        format6.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE); // 把垂直对齐方式指定为居中
        // 第七种字体样式
        WritableFont font7 = new WritableFont(WritableFont.createFont("黑体"), 10);
        WritableCellFormat format7 = new WritableCellFormat(font7);
        format7.setBorder(Border.NONE, BorderLineStyle.THIN);
        format7.setAlignment(jxl.format.Alignment.CENTRE); // 把水平对齐方式指定为居中
        format7.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE); // 把垂直对齐方式指定为居中
        // 第八种字体样式
        WritableFont font8 = new WritableFont(WritableFont.createFont("黑体"), 9);
        WritableCellFormat format8 = new WritableCellFormat(font8);
        format8.setBorder(Border.NONE, BorderLineStyle.THIN);
        format8.setAlignment(jxl.format.Alignment.CENTRE); // 把水平对齐方式指定为居中
        format8.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE); // 把垂直对齐方式指定为居中
        // 第九种字体样式
        WritableFont font9 = new WritableFont(WritableFont.createFont("黑体"), 10,WritableFont.BOLD);
        WritableCellFormat format9 = new WritableCellFormat(font9);
        format9.setBorder(Border.ALL, BorderLineStyle.THIN);
        format9.setAlignment(jxl.format.Alignment.CENTRE); // 把水平对齐方式指定为居中
        format9.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE); // 把垂直对齐方式指定为居中
        // 第十种字体样式
        WritableFont font10 = new WritableFont(WritableFont.createFont("黑体"), 8);
        WritableCellFormat format10 = new WritableCellFormat(font10);
        format10.setWrap(true);// 自动换行
        format10.setBorder(Border.ALL, BorderLineStyle.THIN);
        format10.setAlignment(jxl.format.Alignment.CENTRE); // 把水平对齐方式指定为居中
        format10.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE); // 把垂直对齐方式指定为居中
        // 用户编号前面+“0”
        String bianhao = String.format("%04d", mi.getUid());
        WritableWorkbook workbook = null;

        // 判断文件夹是否存在
        String fullFilePath = request.getSession().getAttribute("fullFilePath").toString();
        File filePath = new File(fullFilePath);
        if (!filePath .exists()  && !filePath .isDirectory()) {
            System.out.println("文件夹不存在");
            filePath .mkdir();
        }
        String fileName = "";
        File file = null;
        // 判断用户是要单月开票还是连月（1-2月）开票
        if (request.getParameter("password").equals("1")) {
            fileName = bianhao + "-" + mi.getUname() + "的" + mi.getYear() + "年" + mi.getMonth() + "月收据.xls";
        }
        if (request.getParameter("password").equals("2")) {
            fileName = bianhao + "-" + mi.getUname() + "的" + mi.getYear() + "年1-2月份收据.xls";
        }
        if (request.getParameter("password").equals("4")) {
            fileName = bianhao + "-" + mi.getUname() + "的" + mi.getYear() + "年3-4月份收据.xls";
        }
        file = new File(fullFilePath + System.getProperty("file.separator") + fileName);
        // 若文件不存在
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                // 创建文件
                System.out.println("文件不存在");
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        workbook = Workbook.createWorkbook(file);
        // 把用户的名字作为工作表的第一页，参数0表示第一页
        WritableSheet sheet = workbook.createSheet("这里是第一页", 0);
        // 合并第一行
        sheet.mergeCells(0, 0, 12, 0);
        Label a = new Label(0, 0, "谷城县水利局紫金农水站水费收据", format1);
        sheet.addCell(a);
        // 合并第二行
        Calendar cal = Calendar.getInstance(); // 获取当前时间
        int year = cal.get(Calendar.YEAR); // 获取当前时间的年份
        sheet.mergeCells(0, 1, 12, 1);
        Label b = null;
        // 判断用户是要单月开票还是连月（1-2月）开票
        if (request.getParameter("password").equals("1")) {
            b = new Label(0, 1, year + "年" + mi.getMonth() + "月", format2);
        }
        if (request.getParameter("password").equals("2")) {
            b = new Label(0, 1, year + "年1-2月", format2);
        }
        if (request.getParameter("password").equals("4")) {
            b = new Label(0, 1, year + "年3-4月", format2);
        }

        sheet.addCell(b);
        Label c = new Label(0, 2, "用户", format3);
        sheet.addCell(c);
        // 合并BC列
        sheet.mergeCells(1, 2, 2, 2);
        Label d = new Label(1, 2, mi.getUname(), format9);
        sheet.addCell(d);
        Label e = new Label(3, 2, "住址", format3);
        sheet.addCell(e);
        // 合并E-L列
        sheet.mergeCells(4, 2, 11, 2);
        Label f = new Label(4, 2, mi.getAddress(), format3);
        sheet.addCell(f);
        Label g = new Label(12, 2, "备注", format4);
        sheet.addCell(g);
        // 合并A4 A5
        sheet.mergeCells(0, 3, 0, 4);
        Label h = new Label(0, 3, "项目及类别", format3);
        sheet.addCell(h);
        // 合并B4 C4
        sheet.mergeCells(1, 3, 2, 3);
        Label i = new Label(1, 3, "水表动态", format3);
        sheet.addCell(i);
        Label j = new Label(1, 4, "起码", format3);
        sheet.addCell(j);
        Label k = new Label(2, 4, "止码", format3);
        sheet.addCell(k);
        // 合并D4 D5
        sheet.mergeCells(3, 3, 3, 4);
        Label l = new Label(3, 3, "用水量(立方米)", format5);
        sheet.addCell(l);
        // 合并E4 E5
        sheet.mergeCells(4, 3, 4, 4);
        Label m = new Label(4, 3, "单价", format6);
        sheet.addCell(m);
        // 合并F4-L4
        sheet.mergeCells(5, 3, 11, 3);
        Label n = new Label(5, 3, "金额", format3);
        sheet.addCell(n);
        Label n1 = new Label(5, 4, "万", format3);
        sheet.addCell(n1);
        Label n2 = new Label(6, 4, "千", format3);
        sheet.addCell(n2);
        Label n3 = new Label(7, 4, "百", format3);
        sheet.addCell(n3);
        Label n4 = new Label(8, 4, "十", format3);
        sheet.addCell(n4);
        Label n5 = new Label(9, 4, "元", format3);
        sheet.addCell(n5);
        Label n6 = new Label(10, 4, "角", format3);
        sheet.addCell(n6);
        Label n7 = new Label(11, 4, "分", format3);
        sheet.addCell(n7);
        Label o = new Label(0, 5, "计量水费", format6);
        sheet.addCell(o);
        jxl.write.Number p = new jxl.write.Number(1, 5, mi.getFirstNumber(), format9); // 起码
        sheet.addCell(p);
        jxl.write.Number q = new jxl.write.Number(2, 5, mi.getLastNumber(), format9); // 止码
        sheet.addCell(q);
        jxl.write.Number r = new jxl.write.Number(3, 5, mi.getLastNumber() - mi.getFirstNumber(), format9); // 用水量
        sheet.addCell(r);
        jxl.write.Number s = new jxl.write.Number(4, 5, mi.getPrice(), format6); // 单价
        sheet.addCell(s);
        Label t = new Label(5, 5, mi.getWan(), format9); // 万
        sheet.addCell(t);
        Label t1 = new Label(6, 5, mi.getQian(), format9); // 千
        sheet.addCell(t1);
        Label t2 = new Label(7, 5, mi.getBai(), format9); // 百
        sheet.addCell(t2);
        Label t3 = new Label(8, 5, mi.getShi(), format9); // 十
        sheet.addCell(t3);
        Label t4 = new Label(9, 5, mi.getYuan(), format9); // 元
        sheet.addCell(t4);
        Label t5 = new Label(10, 5, mi.getJiao(), format9); // 角
        sheet.addCell(t5);
        Label t6 = new Label(11, 5, "0", format9); // 分
        sheet.addCell(t6);
        Label t7 = new Label(0, 6, "容量水费", format6);
        sheet.addCell(t7);
        Label t8 = new Label(1, 6, "", format9); // 空格
        sheet.addCell(t8);
        Label t9 = new Label(2, 6, "", format9);
        sheet.addCell(t9);
        Label t0 = new Label(3, 6, "", format9);
        sheet.addCell(t0);
        Label t11 = new Label(4, 6, "", format9);
        sheet.addCell(t11);
        Label t12 = new Label(5, 6, "", format9);
        sheet.addCell(t12);
        Label t13 = new Label(6, 6, "", format9);
        sheet.addCell(t13);
        Label t14 = new Label(7, 6, "", format9);
        sheet.addCell(t14);
        if (request.getParameter("password").equals("1")) { // 如果是单月开票
            Label t15 = new Label(8, 6, "", format9);
            sheet.addCell(t15);
            jxl.write.Number u1 = new jxl.write.Number(9, 6, 5, format9); // 元
            sheet.addCell(u1);
        }
        if (request.getParameter("password").equals("2") || request.getParameter("password").equals("4")) {
            // 如果连月开票 容量水费应该是十元
            Label t15 = new Label(8, 6, "1", format9); // 拾
            sheet.addCell(t15);
            jxl.write.Number u1 = new jxl.write.Number(9, 6, 0, format9); // 元
            sheet.addCell(u1);
        }
        jxl.write.Number u2 = new jxl.write.Number(10, 6, 0, format9); // 角
        sheet.addCell(u2);
        jxl.write.Number u3 = new jxl.write.Number(11, 6, 0, format9); // 分
        sheet.addCell(u3);
        Label v = new Label(0, 7, "合计大写", format6);
        sheet.addCell(v);
        // 合并B8-E8
        sheet.mergeCells(1, 7, 4, 7);
        Label v1 = new Label(1, 7, chineseNum, format9);
        sheet.addCell(v1);
        Label v2 = new Label(5, 7, mi.getWan1(), format9); // 万
        sheet.addCell(v2);
        Label v3 = new Label(6, 7, mi.getQian1(), format9); // 千
        sheet.addCell(v3);
        Label v4 = new Label(7, 7, mi.getBai1(), format9); // 百
        sheet.addCell(v4);
        Label v5 = new Label(8, 7, mi.getShi1(), format9); // 十
        sheet.addCell(v5);
        Label v6 = new Label(9, 7, mi.getYuan1(), format9); // 元
        sheet.addCell(v6);
        Label v7 = new Label(10, 7, mi.getJiao(), format9); // 角
        sheet.addCell(v7);
        Label v8 = new Label(11, 7, "0", format9); // 分
        sheet.addCell(v8);
        // 合并备注单元格
        sheet.mergeCells(12, 3, 12, 7);
        Label w = new Label(12, 3, "", format6);
        sheet.addCell(w);

        Label x = new Label(0, 8, "合计：", format7);
        sheet.addCell(x);
        Label y = new Label(2, 8, "复核：", format7);
        sheet.addCell(y);
        Label z = new Label(4, 8, "收款：", format7);
        sheet.addCell(z);
        // 合并K8L8
        sheet.mergeCells(10, 8, 11, 8);
        Label z1 = new Label(10, 8, "开票：", format7);
        sheet.addCell(z1);

        Label num = new Label(12, 8, bianhao, format8);
        sheet.addCell(num);

        // 将第一行的高度设为200
        sheet.setRowView(0, 435);
        sheet.setRowView(1, 435); // 第二行高度
        sheet.setRowView(2, 435); // 第三行高度
        sheet.setRowView(3, 435);
        sheet.setRowView(4, 435);
        sheet.setRowView(5, 435);
        sheet.setRowView(6, 435);
        sheet.setRowView(7, 435);
        sheet.setRowView(8, 435);
        // 将第一列的宽度设为30
        sheet.setColumnView(0, 7); // A列
        sheet.setColumnView(1, 5); // B列
        sheet.setColumnView(2, 5); // C列
        sheet.setColumnView(3, 5); // D列
        sheet.setColumnView(4, 4); // E列
        sheet.setColumnView(5, 3);
        sheet.setColumnView(6, 3);
        sheet.setColumnView(7, 3);
        sheet.setColumnView(8, 3);
        sheet.setColumnView(9, 3);
        sheet.setColumnView(10, 3);
        sheet.setColumnView(11, 3); // L列
        sheet.setColumnView(12, 4); // M列

        SheetSettings sheetSettings = sheet.getSettings(); // 获取原sheet的属性
        sheetSettings.setLeftMargin(0.4); // 设置左边距
        sheetSettings.setTopMargin(0.4); // 设置上边距

        workbook.write();
        workbook.close();

        return fileName;

    }

    // 导出Excel
    @RequestMapping("downloadExcel")
    public void getExcel(String password, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Middle mi = (Middle) request.getSession().getAttribute("middle");
        String fileName = excel(mi, mi.getDaxie(), request);
        if (StringUtils.isNotEmpty(fileName)) {
            try {
                DownloadExcelUtil.fileDownload(response, fileName, request.getSession().getAttribute("fullFilePath").toString());
            } catch (MyException e) {
                e.printStackTrace();
            }
        }
    }

    // 获取当月录入用户总数
    @RequestMapping("getTotalMonthCount")
    @ResponseBody
    public String getTotalMonthCount(int month) {
        return "{\"totalMonthCount\":\"" + GetTotalCountByMonth.getTotalCountByMonth(monthBillService, month) + "\"}";
    }

    // 查询所有的用户月起止码
    @RequestMapping("showAllBills")
    public ModelAndView showAllBills(CustomerPage cp) throws Exception {
        if (cp.getPageNow() == null) {
            cp.setPageNow(1);
        }
        cp.setPageNow((cp.getPageNow() - 1) * cp.getPageSize());
        List<Bill> bills;
        boolean isNum;
        if (cp.getName() != null) {
            isNum = cp.getName().matches("[0-9]+"); // 如果输入的是数字就按照id查询，如果不是数字就like姓名
            if (isNum == true) {
                bills = billService.getBB(cp);
                cp.setCount(customerService.getCustomerCountById(cp));
            } else {
                bills = billService.getAllBills(cp);
                cp.setCount(customerService.getCustomerCount(cp)); // 查询总的信息条数(如果有条件，按时按照条件查询后的总的条数)
            }
        } else {
            bills = billService.getAllBills(cp);
            cp.setCount(customerService.getCustomerCount(cp)); // 查询总的信息条数(如果有条件，按时按照条件查询后的总的条数)
        }

        ModelAndView mv = new ModelAndView();
        Gson gson = new Gson();
        mv.addObject("list", gson.toJson(bills));
        // 应该再次逆向计算当前的页码
        cp.setPageNow(cp.getPageNow() / cp.getPageSize() + 1);
        mv.addObject("page", cp);
        mv.setViewName("jsp/showAllBills.jsp");
        return mv;
    }

    // 修改用户起止码
    @ResponseBody
    @RequestMapping(value = "toupdateBill", method = RequestMethod.POST, produces = "text/html;charset=utf-8")
    public String toupdateBill(Bill bill,HttpSession session){
        try {
            bill.setThirteen(bill.getTwelve());
            billService.updateBills(bill);
            Operator_logUtil.insertOperator_log(bill, 6, session, null, null, billService, operator_logService); // 添加日志
            return "修改成功！";
        }catch (Exception e){
            e.printStackTrace();
            return "修改失败！";
        }
    }

    /**
     *
     * @param monthNum
     *            月份
     * @param waterConsumption
     *            用水量
     * @param uid
     *            用户id
     * @throws Exception
     */
    private void updateWaterConsumptionByMonth(int monthNum, int waterConsumption, int uid) {
        String month = null;
        switch (monthNum) {
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
        waterConsumptionService.updateWaterConsumptionByMonth(month, waterConsumption, uid);
    }

    // 进入今日录入水费详情
    @RequestMapping("beforeGetToDayDatas")
    public ModelAndView beforeGetToDayDatas(/*HttpServletRequest request*/) {
        String startDate = UtilController.getDateFormat(new Date(),"yyyy-MM-dd")+" 00:00:00";
        String endDate = UtilController.getDateFormat(new Date(),"yyyy-MM-dd")+" 23:59:59";
        ModelAndView mv = new ModelAndView();
        int toDayCount = billService.getSectionNum(startDate,endDate);
        List<Integer> toDayCountList = new ArrayList<>();
        if(toDayCount != 0){
            int temp = 0;
            while(temp < toDayCount){
                toDayCountList.add(temp);
                temp +=50;
            }
            mv.addObject("toDayCountList", toDayCountList);
            mv.addObject("selected", temp);
        }else{
            toDayCountList.add(-1);
            mv.addObject("toDayCountList", toDayCountList);
            mv.addObject("selected", 0);
        }
        mv.setViewName("jsp/showToDayDatas.jsp");
        return mv;
    }

    // 按 limit 获取今日录入用户的详情
    @ResponseBody
    @RequestMapping(value = "getToDayDatas", produces = "text/html;charset=utf-8")
    public String getToDayDatas(/*HttpServletRequest request,*/String limit) {
        Map<String,Object> results = new HashMap<>();
        String startDate = UtilController.getDateFormat(new Date(),"yyyy-MM-dd")+" 00:00:00";
        String endDate = UtilController.getDateFormat(new Date(),"yyyy-MM-dd")+" 23:59:59";
        int toDayCount = billService.getSectionNum(startDate,endDate); //今日总录入数
        int count = 50; // 本次查询用户数量
        int water = 0 ; // 总用水量
        Double cost = 0d; // 总水费
        List<TransitionModel> toDayDatasList = billService.getToDayDatasList(startDate,endDate,limit);
        for (TransitionModel temp: toDayDatasList) {
            water += temp.getWater_consumption();
            cost += temp.getMonth_bill_cost();
        }
        results.put("toDayCount",toDayCount);
        results.put("count",count);
        results.put("water",water);
        results.put("cost",cost);
        results.put("toDayDatasList",toDayDatasList);
        return JSON.toJSONString(results);
    }
}
