package me.jameszhan.dirty.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 16/3/9
 *         Time: PM11:35
 */
public class WebServer {

    private static Logger LOGGER = LoggerFactory.getLogger(WebServer.class);

    private String webroot;
    private int port;
    private int threads;
    private AtomicBoolean started = new AtomicBoolean();

    private Executor executor;

    public WebServer() {

        ResourceBundle bundle = ResourceBundle.getBundle("server", Locale.CHINESE);

        port = Integer.parseInt(bundle.getString("server.port"));
        threads = Integer.parseInt(bundle.getString("server.maxThreads"));
        webroot = bundle.getString("server.webroot");

        executor = Executors.newFixedThreadPool(threads);
    }

    public void startup() {
        if (started.compareAndSet(false, true)) {
            LOGGER.info("Server Information: ");
            LOGGER.info("port : " + port);
            LOGGER.info("max threads: " + threads);
            LOGGER.info("webroot: " + webroot);

            ServerSocket server;
            try {
                server = new ServerSocket(port);
                while (started.get()) {
                    Socket socket = server.accept();
                    executor.execute(new Processor(socket));
                }
            } catch (IOException ex) {
                LOGGER.error(ex.getMessage());
            }
        }
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

    public static void main(String[] args) {
        WebServer server = new WebServer();
        server.startup();
    }

    public class Processor implements Runnable {
        private Socket socket;
        private InputStream in;
        private PrintStream out;

        public Processor(Socket socket) {
            this.socket = socket;
            try {
                this.in = this.socket.getInputStream();
                this.out = new PrintStream(socket.getOutputStream());

                LOGGER.info("SocketAddress: {} => {}, InetAddress: {} => {}, Port: {} => {}, BufferSize Send: {}, " +
                                "Received: {}, Linger: {}, Timeout: {}, trafficClass: {}",
                        socket.getLocalSocketAddress(), socket.getRemoteSocketAddress(),
                        socket.getLocalAddress(), socket.getInetAddress(),
                        socket.getLocalPort(), socket.getPort(),
                        socket.getSendBufferSize(),  socket.getReceiveBufferSize(),
                        socket.getSoLinger(), socket.getSoTimeout(),
                        socket.getTrafficClass());
            } catch (IOException e) {
                LOGGER.info("UnexpectedError: {}.", e.getMessage(), e);
            }
        }

        @Override
        public void run() {
            String fileName = parseHttp(in);
            sendFile(fileName);
        }

        public String parseHttp(InputStream in) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String fileName = null;
            try {
                String line = br.readLine();
                String[] content = line.split(" ");
                if (content.length != 3) {
                    sendErrorMessage(400, "Client query Error");
                    return null;
                }
                fileName = content[1];
                LOGGER.info("Method: " + content[0] + "; fileName: "
                        + content[1] + "; http version: " + content[2]);
                while (!(line = br.readLine()).equals("") || line.equals(null)) {
                    LOGGER.info(line);
                }
            } catch (IOException ex) {
                LOGGER.info(ex.getMessage());
            }

            return fileName;

        }

        public void sendFile(String fileName) {
            File file = new File(webroot + fileName);

            try {
                InputStream in = new FileInputStream(file);
                byte[] content = new byte[(int) file.length()];
                int size = in.read(content);
                LOGGER.info("Read {} size: {}.", content, size);
                out.println("HTTP/1.1 200 OK");
                out.println("Content-Length: " + content.length);
                out.println("Content-Type: text/html");
                out.println("Date: " + new Date());
                out.println();
                out.write(content);
                out.flush();
                out.close();
                in.close();

            } catch (FileNotFoundException ex) {
                LOGGER.info(ex.getMessage());
                sendErrorMessage(404, "File Not Found");
            } catch (IOException ex) {
                LOGGER.info(ex.getMessage());
            }
        }

        private void sendErrorMessage(int errCode, String errMsg) {
            out.println("HTTP/1.1 " + errCode + " " + errMsg);
            out.println("content-type: text/html");
            out.println();
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Error Message</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Error Code: " + errCode + ", Message: " + errMsg + "</h1>");
            out.println("</body>");
            out.println("</html>");
            out.flush();
            out.close();

        }
    }
}
