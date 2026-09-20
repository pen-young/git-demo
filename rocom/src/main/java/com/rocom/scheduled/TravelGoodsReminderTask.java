package com.rocom.scheduled;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TravelGoodsReminderTask {

    public static void main(String[] args) {
        String listContent = HttpUtil.get("https://www.guoping123.com/hykb_tools/comm/lkwgmerchant/preview.php?id=1&immgj=0");
        List<String> times = ReUtil.findAll("data-time=\"(.*?)\"", listContent, 1);
        List<String> shopInfos = ReUtil.findAll("showShopinfo\\('([^)]*)'\\)", listContent, 1);

        int i = 0;
        int index = 1;
        List<String> effectiveShops = new ArrayList<>();
        effectiveShops.add("【远行商品更新】");
        for (String shopInfo : shopInfos) {
            if (DateUtil.date(NumberUtil.parseLong(times.get(i++)) * 1000).isAfter(DateUtil.date())) {
                effectiveShops.add((index++) + "." + shopInfo.split("','")[1]);
            }
        }
        System.out.println(effectiveShops);
        JSONObject params = new JSONObject();
        params.set("msg", String.join("\n", effectiveShops));
        params.set("group", "961180447");
        HttpUtil.post("https://qmsg.zendee.cn/v3/jsend/00843e0e519e878fc7d38a7be834638c63643efb", params.toString());
    }
    
}
