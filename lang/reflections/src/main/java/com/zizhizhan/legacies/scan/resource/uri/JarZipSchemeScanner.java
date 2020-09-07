package com.zizhizhan.legacies.scan.resource.uri;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.zizhizhan.legacies.scan.resource.JarFileScanner;
import com.zizhizhan.legacies.scan.resource.ScannerListener;
import com.zizhizhan.legacies.scan.util.Closing;


public class JarZipSchemeScanner implements UriSchemeScanner {
    private static final Charset UTF_8_CHARSET = StandardCharsets.UTF_8;

    public Set<String> getSchemes() {
        return new HashSet<String>(Arrays.asList("jar", "zip"));
    }

    public void scan(final URI u, final ScannerListener cfl) {
        final String ssp = u.getRawSchemeSpecificPart();
        final String jarUrlString = ssp.substring(0, ssp.lastIndexOf('!'));
        final String parent = ssp.substring(ssp.lastIndexOf('!') + 2);
        try {
            closing(jarUrlString).f(in -> JarFileScanner.scan(in, parent, cfl));
        } catch (IOException ex) {
            throw new RuntimeException("IO error when scanning jar " + u, ex);
        }
    }

    protected Closing closing(String jarUrlString) throws IOException {
        try {
            return new Closing(new URL(jarUrlString).openStream());
        } catch (MalformedURLException ex) {
            return new Closing(new FileInputStream(
                    decode(jarUrlString)));
        }
    }

    public static String decode(String s) {
        if (s == null) {
            throw new IllegalArgumentException();
        }

        final int n = s.length();
        if (n == 0) {
            return s;
        }

        // If there are no percent-escaped octets
        if (s.indexOf('%') < 0) {
            // If there are no '+' characters for query param
            return s;
        } else {
            // Malformed percent-escaped octet at the end
            if (n < 2) {
                // TODO localize
                throw new IllegalArgumentException("Malformed percent-encoded octet at index 1");
            }

            // Malformed percent-escaped octet at the end
            if (s.charAt(n - 2) == '%') {
                // TODO localize
                throw new IllegalArgumentException("Malformed percent-encoded octet at index " + (n - 2));
            }
        }
        return decode(s, n);
    }

    private static String decode(String s, int n) {
        final StringBuilder sb = new StringBuilder(n);
        ByteBuffer bb = null;

        for (int i = 0; i < n; ) {
            final char c = s.charAt(i++);
            if (c != '%') {
                sb.append(c);
            } else {
                bb = decodePercentEncodedOctets(s, i, bb);
                i = decodeOctets(i, bb, sb);
            }
        }

        return sb.toString();
    }

    private static ByteBuffer decodePercentEncodedOctets(String s, int i, ByteBuffer bb) {
        if (bb == null)
            bb = ByteBuffer.allocate(1);
        else
            bb.clear();

        while (true) {
            // Decode the hex digits
            bb.put((byte) (decodeHex(s, i++) << 4 | decodeHex(s, i++)));

            // Finish if at the end of the string
            if (i == s.length()) {
                break;
            }

            // Finish if no more percent-encoded octets follow
            if (s.charAt(i++) != '%') {
                break;
            }

            // Check if the byte buffer needs to be increased in size
            if (bb.position() == bb.capacity()) {
                bb.flip();
                // Create a new byte buffer with the maximum number of possible
                // octets, hence resize should only occur once
                ByteBuffer bb_new = ByteBuffer.allocate(s.length() / 3);
                bb_new.put(bb);
                bb = bb_new;
            }
        }

        bb.flip();
        return bb;
    }

    private static int decodeOctets(int i, ByteBuffer bb, StringBuilder sb) {
        // If there is only one octet and is an ASCII character
        if (bb.limit() == 1 && (bb.get(0) & 0xFF) < 0x80) {
            // Octet can be appended directly
            sb.append((char) bb.get(0));
            return i + 2;
        } else {
            // 
            CharBuffer cb = UTF_8_CHARSET.decode(bb);
            sb.append(cb.toString());
            return i + bb.limit() * 3 - 1;
        }
    }

    private static int decodeHex(String s, int i) {
        final int v = decodeHex(s.charAt(i));
        if (v == -1) // TODO localize
        {
            throw new IllegalArgumentException("Malformed percent-encoded octet at index " + i +
                    ", invalid hexadecimal digit '" + s.charAt(i) + "'");
        }
        return v;
    }

    private static int decodeHex(char c) {
        return (c < 128) ? HEX_TABLE[c] : -1;
    }

    private static final int[] HEX_TABLE = createHexTable();

    private static int[] createHexTable() {
        int[] table = new int[0x80];
        Arrays.fill(table, -1);

        for (char c = '0'; c <= '9'; c++) {
            table[c] = c - '0';
        }
        for (char c = 'A'; c <= 'F'; c++) {
            table[c] = c - 'A' + 10;
        }
        for (char c = 'a'; c <= 'f'; c++) {
            table[c] = c - 'a' + 10;
        }
        return table;
    }
}
