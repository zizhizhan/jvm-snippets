package com.zizhizhan.lang;

public class AssertTest {
    
    public static void main(String[] args) {
        int i = 100;
       
        //assert i-- == 99 : "i != 100";
        assert i-- == 98;
    }

}
