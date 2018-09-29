/**
 * This package contains an example SPDY HTTP client. It will behave like a SPDY-enabled browser and you can see the
 * SPDY frames flowing in and out using the {@link me.jameszhan.thirdparty.netty.http.spdy.SpdyFrameLogger}.
 *
 * <p>
 * This package relies on the Jetty project's implementation of the Transport Layer Security (TLS) extension for Next
 * Protocol Negotiation (NPN) for OpenJDK 7 is required. NPN allows the application layer to negotiate which
 * protocol, SPDY or HTTP, to use.
 * <p>
 * To start, run {@link me.jameszhan.thirdparty.netty.http.spdy.SpdyServer} with the JVM parameter:
 * {@code java -Xbootclasspath/p:<path_to_npn_boot_jar> ...}.
 * The "path_to_npn_boot_jar" is the path on the file system for the NPN Boot Jar file which can be downloaded from
 * Maven at coordinates org.mortbay.jetty.npn:npn-boot. Different versions applies to different OpenJDK versions.
 * See <a href="http://www.eclipse.org/jetty/documentation/current/npn-chapter.html">Jetty docs</a> for more
 * information.
 * <p>
 * After that, you can run {@link me.jameszhan.thirdparty.netty.http.spdy.SpdyClient}, also settings the JVM parameter
 * mentioned above.
 * <p>
 * You may also use the {@code run-example.sh} script to start the server and the client from the command line:
 * <pre>
 *     ./run-example spdy-server
 * </pre>
 * Then start the client in a different terminal window:
 * <pre>
 *     ./run-example spdy-client
 * </pre>
 *
 *
 *
 * This package contains an example SPDY HTTP web server.
 * <p>
 * This package relies on the Jetty project's implementation of the Transport Layer Security (TLS) extension for Next
 * Protocol Negotiation (NPN) for OpenJDK 7 is required. NPN allows the application layer to negotiate which
 * protocol, SPDY or HTTP, to use.
 * <p>
 * To start, run {@link me.jameszhan.thirdparty.netty.http.spdy.SpdyServer} with the JVM parameter:
 * {@code java -Xbootclasspath/p:<path_to_npn_boot_jar> ...}.
 * The "path_to_npn_boot_jar" is the path on the file system for the NPN Boot Jar file which can be downloaded from
 * Maven at coordinates org.mortbay.jetty.npn:npn-boot. Different versions applies to different OpenJDK versions.
 * See <a href="http://www.eclipse.org/jetty/documentation/current/npn-chapter.html">Jetty docs</a> for more
 * information.
 * <p>
 * You may also use the {@code run-example.sh} script to start the server from the command line:
 * <pre>
 *     ./run-example spdy-server
 * </pre>
 * <p>
 * Once started, you can test the server with your
 * <a href="http://en.wikipedia.org/wiki/SPDY#Browser_support_and_usage">SPDY enabled web browser</a> by navigating
 * to <a href="https://localhost:8443/">https://localhost:8443/</a>
 */
package me.jameszhan.thirdparty.netty.http.spdy;