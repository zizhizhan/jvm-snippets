package me.jameszhan.io.net.framework;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 16/3/9
 *         Time: PM11:52
 */
public interface IOProtocol {

    void handle(InputStream in, OutputStream out);

}
