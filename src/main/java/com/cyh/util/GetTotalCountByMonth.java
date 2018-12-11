package com.cyh.util;

import com.cyh.service.MonthBillService;

/**
 * 
 * @author admin 获取当月录入用户总数
 */
public class GetTotalCountByMonth {
    public static int getTotalCountByMonth(MonthBillService monthBillService, int month) {
        int i = 0;
        switch (month) {
        case 1:
            i = monthBillService.selectmone();
            break;
        case 2:
            i = monthBillService.selectmtwo();
            break;
        case 3:
            i = monthBillService.selectmthree();
            break;
        case 4:
            i = monthBillService.selectmfour();
            break;
        case 5:
            i = monthBillService.selectmfive();
            break;
        case 6:
            i = monthBillService.selectmsix();
            break;
        case 7:
            i = monthBillService.selectmseven();
            break;
        case 8:
            i = monthBillService.selectmeight();
            break;
        case 9:
            i = monthBillService.selectmnine();
            break;
        case 10:
            i = monthBillService.selectmten();
            break;
        case 11:
            i = monthBillService.selectmeleven();
            break;
        case 12:
            i = monthBillService.selectmtwelve();
            break;
        case 13:
            i = monthBillService.selectmtwo();
            break;
        }
        return i;
    }
}
