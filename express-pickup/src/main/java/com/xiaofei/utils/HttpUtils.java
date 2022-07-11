package com.xiaofei.utils;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.xiaofei.common.IdCardResponseStatus;
import com.xiaofei.vo.IdCardVo;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * 发送ajax请求
 *
 * @date 2022/5/6 22:13
 */
public class HttpUtils {
    private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);
    private static final String APPCODE = "82b144c8ab854143a23452b72b8703bf";
    private static final String URL = "https://eid.shumaidata.com/eid/check";

    public static IdCardVo postForm(Map<String, String> params) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder().build();
        FormBody.Builder formbuilder = new FormBody.Builder();
        Iterator<String> it = params.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            formbuilder.add(key, params.get(key));
        }
        FormBody body = formbuilder.build();
        Request request = new Request.Builder().url(URL).addHeader("Authorization", "APPCODE " + APPCODE).post(body).build();
        Response response = client.newCall(request).execute();
        logger.info("返回状态码" + response.code() + ",message:" + response.message());
        String dataObj = response.body().string();
        logger.info(dataObj);
        IdCardVo idCardVo = new IdCardVo();
        if (IdCardResponseStatus.isSuccess(response.code())) {
            idCardVo = JSONUtil.toBean(dataObj, IdCardVo.class);
//            idCardVo = JSONObject.parseObject(dataObj, IdCardVo.class);
        }
        idCardVo.setCode(response.code());
        idCardVo.setMessage(response.message());
        return idCardVo;
    }
}
