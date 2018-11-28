package me.jameszhan.io.net.http;

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
 * Date: 16/3/9
 * Time: PM11:35
 */
public class SimpleHttpServer {

    private static Logger LOGGER = LoggerFactory.getLogger(SimpleHttpServer.class);

    private String webroot;
    private int port;
    private int threads;
    private AtomicBoolean started = new AtomicBoolean();

    private Executor executor;

    public SimpleHttpServer() {
        ResourceBundle bundle = ResourceBundle.getBundle("server", Locale.CHINESE);

        port = Integer.parseInt(bundle.getString("server.port"));
        threads = Integer.parseInt(bundle.getString("server.maxThreads"));
        webroot = bundle.getString("server.webroot");

        executor = Executors.newFixedThreadPool(threads);
    }

    public void startup() throws IOException {
        if (started.compareAndSet(false, true)) {
            LOGGER.info("Server Information: ");
            LOGGER.info("port: {}", port);
            LOGGER.info("max threads: {}", threads);
            LOGGER.info("webroot: {}", webroot);

            try (ServerSocket serverSocket = new ServerSocket(port)) {
                while (started.get()) {
                    executor.execute(new Processor(serverSocket.accept(), webroot));
                }
            }
        }
    }

//    public void setExecutor(Executor executor) {
//        this.executor = executor;
//    }

    private static class Processor implements Runnable {
        private Socket socket;
        private String webRoot;

        Processor(Socket socket, String webRoot) {
            this.socket = socket;
            this.webRoot = webRoot;
        }

        @Override
        public void run() {
            try {
                LOGGER.info("\nSocketAddress: {} => {} \nInetAddress: {} => {} \nPort: {} => {} \nBufferSize Send: {} \n"
                                + "Received: {} \nLinger: {} \nTimeout: {} \ntrafficClass: {}",
                    socket.getLocalSocketAddress(), socket.getRemoteSocketAddress(), socket.getLocalAddress(),
                    socket.getInetAddress(), socket.getLocalPort(), socket.getPort(), socket.getSendBufferSize(),
                    socket.getReceiveBufferSize(), socket.getSoLinger(), socket.getSoTimeout(), socket.getTrafficClass());

                try (InputStream in = socket.getInputStream()) {
                    String fileName = parseHttp(in);
                    try (PrintStream out = new PrintStream(socket.getOutputStream())) {
                        if (fileName != null) {
                            sendFile(fileName, out);
                        } else {
                            sendErrorMessage(400, "Client query Error", out);
                        }
                    }
                }

            } catch (IOException e) {
                LOGGER.info("UnexpectedError: {}.", e.getMessage(), e);
            }
        }

        String parseHttp(InputStream in) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String fileName = null;
            try {
                String line = br.readLine();
                LOGGER.info("Request: {}", line);
                String[] content = line.split(" ");
                if (content.length != 3) {
                    return null;
                }
                fileName = content[1];
                LOGGER.info("Method: {}; fileName: {}; http version: {}", content[0], content[1], content[2]);
                while ((line = br.readLine()) != null && !"".equalsIgnoreCase(line)) {
                    LOGGER.info("-> {}", line);
                }
            } catch (IOException ex) {
                LOGGER.info("Unexpected Error.", ex);
            }
            return fileName;
        }

        void sendFile(String fileName, PrintStream out) {
            File file = new File(webRoot + fileName);
            if (file.isDirectory()) {
                file = new File(file, "index.html");
            }
            try (InputStream in = new FileInputStream(file)) {
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
            } catch (FileNotFoundException ex) {
                LOGGER.info(ex.getMessage());
                sendErrorMessage(404, String.format("%s Not Found", fileName), out);
            } catch (IOException ex) {
                LOGGER.info("Unexpected Error", ex);
                sendErrorMessage(500, ex.getMessage(), out);
            }
        }

        private void sendErrorMessage(int errCode, String errMsg, PrintStream out) {
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
