package com.zizhizhan.legacies.thirdparty.httpclient.sample;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;

public class HttpClient1 {

    public static void main(String[] args) throws IOException {
        HttpClient client = new HttpClient();

        HttpMethod method = new GetMethod("http://maps.google.com/maps?f=q&source=s_q&hl=zh-CN&q=hotels&sll=36.130000,-115.200000&sspn=0.320000,0.720000&ie=UTF8&radius=24.3&split=1&rq=1&ev=zo&jsv=259e&vps=8&sa=N&start=60");

        int status = client.executeMethod(method);

        if (status == HttpStatus.SC_OK) {
            System.out.println(method.getResponseBodyAsString());
        } else {
            System.out.println(status);
        }
    }


}
