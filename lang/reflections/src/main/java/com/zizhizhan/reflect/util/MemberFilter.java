package com.zizhizhan.reflect.util;

import java.lang.reflect.Member;

public interface MemberFilter {

    boolean accept(Member m);

    void handle(Member m);

}
