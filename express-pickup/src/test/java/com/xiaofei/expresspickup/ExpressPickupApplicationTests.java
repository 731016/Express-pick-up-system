package com.xiaofei.expresspickup;

import com.xiaofei.constant.IdCardResponseStatus;
import com.xiaofei.utils.HttpUtils;
import com.xiaofei.dto.IdCardDto;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class ExpressPickupApplicationTests {

    @Test
    void contextLoads() {
        Map<String, String> params = new HashMap<>();
        params.put("idcard", "429006200001115733");
        params.put("name", "涂鏊飞");
        try {
            IdCardDto idCardDto = HttpUtils.postForm(params);
            int code = idCardDto.getCode();
            //状态码“0”，请求成功
            Boolean success = IdCardResponseStatus.isSuccess(code);
            if (success) {
                String res = idCardDto.getResult().getRes();
                //核验结果“1”，身份证号一致
                switch (res) {
                    case "1":

                        break;
                    //不一致
                    case "2":
                        //无记录
                        System.out.println(idCardDto.getResult().getDescription());
                    case "3":
                        System.out.println(idCardDto.getResult().getDescription());
                }
            } else {
                System.out.println(idCardDto.getMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
