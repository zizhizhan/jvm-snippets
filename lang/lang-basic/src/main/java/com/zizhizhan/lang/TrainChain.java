package com.zizhizhan.lang;

import com.alibaba.fastjson.JSONObject;

import java.util.Optional;

public class TrainChain {

    public static void main(String[] args) {
        JSONObject leftBranch111 = new JSONObject();
        leftBranch111.put("leaf", "leftLeaf");

        JSONObject leftBranch11 = new JSONObject();
        leftBranch11.put("leftBranch111", leftBranch111);

        JSONObject leftBranch1 = new JSONObject();
        leftBranch1.put("leftBranch11", leftBranch11);

        JSONObject root = new JSONObject();
        root.put("leftBranch1", leftBranch1);

        String leftestLeaf = Optional.ofNullable(root.getJSONObject("leftBranch1"))
                .map(o -> o.getJSONObject("leftBranch11"))
                .map(o -> o.getJSONObject("leftBranch111"))
                .map(o -> o.getString("leaf"))
                .orElse(null);

        System.out.println(leftestLeaf);
    }

}
