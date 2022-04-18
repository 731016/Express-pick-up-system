package com.xiaofei.utils;

import com.xiaofei.entity.order.OrderCommentEntity;
import io.swagger.models.auth.In;

import java.util.Date;
import java.util.Random;

/**
 * TODO 类描述
 *
 * @date 2022/4/9 21:53
 */
public class OrderUtils {
    /**
     * 获取流水号
     */
    public static String genSerialNumber() {
        String str = System.currentTimeMillis() + "";
        String num = new Random().nextInt(731016) + "";
        return str + num;
    }

    /**
     * 通过订单状态，获取进度条的step值
     */
    public static Integer calOrderStep(Integer orderStatus) {
        Integer step = 0;
        switch (orderStatus) {
            case 10:
                step = 1;
                break;
            case 20:
                step = 2;
                break;
            case 30:
                step = 3;
                break;
            case 40:
                step = 4;
                break;
        }
        return step;
    }
    /**
     * 0都未评价
     * 1普通用户已评价
     * 2配送员已评价
     * 3普通用户和配送员都已评价
     */
    public static Integer calEvaluateStatus(OrderCommentEntity comment) {
        Integer sum = 0;
        Double userRate = comment.getUserRating();
        Double deliveryRate = comment.getDeliveryRating();
        if (userRate != null) {
            sum += 1;
        }
        if (deliveryRate != null) {
            sum += 2;
        }
        return sum;
    }

}
