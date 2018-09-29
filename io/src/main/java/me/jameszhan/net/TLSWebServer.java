package me.jameszhan.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ServerSocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.util.concurrent.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 16/3/10
 *         Time: AM12:47
 */
public class TLSWebServer {

    private static Logger LOGGER = LoggerFactory.getLogger(TLSWebServer.class);

    private ExecutorService m_threadPool;
    private Semaphore m_semphore;
    private volatile boolean isRunning;
    private int port;
    private int backlog;
    private char[] passphrase;
    private boolean useTLS;

    public TLSWebServer(int port, int backlog, char[] passphrase, boolean useTLS) {
        super();
        this.isRunning = true;
        this.port = port;
        this.backlog = backlog;
        this.passphrase = passphrase;
        this.useTLS = useTLS;
        this.m_semphore = new Semaphore(backlog);
        this.m_threadPool = new ThreadPoolExecutor(1, Integer.MAX_VALUE, 60, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>());
    }

    public void start(){
        try {
            ServerSocketFactory ssf = getServerSocketFactory();
            ServerSocket server = ssf.createServerSocket(port, backlog);
            LOGGER.info("Server begin listening: " + port);
            while(isRunning){
                Socket socket = server.accept();
                process(socket);
            }
        } catch (GeneralSecurityException e) {
            LOGGER.error("Can't load SSL config.", e);
        } catch (IOException e) {
            LOGGER.error("Unexpected IOError.", e);
        }
    }

    public void process(Socket socket){
        m_semphore.acquireUninterruptibly();
        try {
            m_threadPool.execute(new Acceptor(socket));
        } finally {
            m_semphore.release();
        }
    }

    public void handle(Socket socket) {
        try {
            socket.setSoTimeout(60 * 1000);
            LOGGER.info("Accepting-" + socket);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            String get = in.readUTF();
            LOGGER.info(get);
            out.writeUTF("Echo: " + get);
        } catch (IOException e) {
            LOGGER.error("Exception while socket handling! socket=" + socket, e);
        } finally {
            try {
                socket.close();
            } catch (Exception e) {
                LOGGER.error("Exception while socket close! socket=" + socket, e);
            }
        }
    }

    private ServerSocketFactory getServerSocketFactory() throws GeneralSecurityException, IOException {
        ServerSocketFactory factory;
        if (useTLS) {
            SSLContext context = SSLContext.getInstance("TLS");
            KeyManagerFactory keyManagerFactory = createKeyManagerFactory("server.keystore", passphrase);

            context.init(keyManagerFactory.getKeyManagers(), null, null);
            factory = context.getServerSocketFactory();
        } else {
            factory = ServerSocketFactory.getDefault();
        }
        return factory;
    }

    private class Acceptor implements Runnable {
        private Socket socket;

        public Acceptor(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            handle(socket);
        }
    }


/**
 static{
     try {
         SSLContext context = SSLContext.getDefault();
         System.out.println("Protocol: " + context.getProtocol());
         SSLParameters params = context.getDefaultSSLParameters();
         System.out.println("Protocol: ");
         for(String p : params.getProtocols()){
            System.out.println("\t" + p);
         }
         System.out.println("CipherSuites: ");
         for(String c : params.getCipherSuites()){
            System.out.println("\t" + c);
         }

         Provider p = context.getProvider();
         String providerInfo = String.format("{name: %s,  \tvesion: %s, \tinfo: %s, \t%s}", p.getName(), p
         .getVersion(), p.getInfo(), p.getClass());
         System.out.println(providerInfo);

         for(Service s : p.getServices()){
         System.out.println(String.format("\t{type: %s, \talgorithm: %s, \tclass: %s}", s.getType(),
         s.getAlgorithm(), s.getClassName()));
         }


     } catch (NoSuchAlgorithmException e) {
         e.printStackTrace();
     }

 }
 */



    public static KeyManagerFactory createKeyManagerFactory(String keyStoreFile, char[] passphrase)
            throws GeneralSecurityException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(classLoader.getResourceAsStream(keyStoreFile), passphrase);

        keyManagerFactory.init(keyStore, passphrase);
        return keyManagerFactory;
    }

    public static TrustManagerFactory createTrustManagerFactory(String keyStoreFile, char[] passphrase)
            throws GeneralSecurityException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(classLoader.getResourceAsStream(keyStoreFile), passphrase);
        trustManagerFactory.init(keyStore);
        return trustManagerFactory;
    }

    public static void main(String[] args) {
        new TLSWebServer(8086, 50, "password".toCharArray(), true).start();
    }

}
