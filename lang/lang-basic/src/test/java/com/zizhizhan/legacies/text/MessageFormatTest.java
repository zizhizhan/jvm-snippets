package com.zizhizhan.legacies.text;

import java.text.MessageFormat;
import java.text.ParsePosition;
import java.util.Date;

public class MessageFormatTest {

    public static void main(String[] args) {
        System.out.println(MessageFormat.format("[T]{0,date,yyyy.MM.dd HH:mm:SS z} [L]{1} [O]{2} [M]{3}",
                new Date(), "LocalMessage", "object", "message"));

        int planet = 7;
        String event = "a disturbance in the Force";
        String result = MessageFormat.format(
                "At {1,time} on {1,date}, there was {2} on planet {0,number,integer}.",
                planet, new Date(), event);

        System.out.println(result);

        System.out.println(MessageFormat.format("ccc{0}. {0}, {0}","abcdefg"));

        MessageFormat mf = new MessageFormat("{0}, {0}, {0}");
        String forParsing = "x, y, z";
        Object[] objs = mf.parse(forParsing, new ParsePosition(0));
        for (int i = 0; i < objs.length; i++) {
            System.out.println(objs[i]);
        }
    }
}
