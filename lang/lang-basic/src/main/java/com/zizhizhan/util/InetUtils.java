package com.zizhizhan.util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

@Slf4j
public class InetUtils {

    private static InetAddress localAddr;

    static {
        localAddr = findFirstNonLoopbackAddr();
    }

    public static String getLocalIP() {
        return localAddr.getHostAddress();
    }

    private static InetAddress findFirstNonLoopbackAddr() {
        InetAddress address = null;
        try {
            int lowest = Integer.MAX_VALUE;
            for (Enumeration<NetworkInterface> nics = NetworkInterface.getNetworkInterfaces(); nics.hasMoreElements();) {
                NetworkInterface ifc = nics.nextElement();
                if (ifc.isUp()) {
                    log.info("Check ifc {} @ {}.", ifc.getDisplayName(), ifc.getIndex());
                    if (ifc.getIndex() < lowest || address == null) {
                        lowest = ifc.getIndex();
                    }

                    for (Enumeration<InetAddress> addrs = ifc.getInetAddresses(); addrs.hasMoreElements(); ) {
                        InetAddress ifcAddress = addrs.nextElement();
                        if (ifcAddress instanceof Inet4Address && !ifcAddress.isLoopbackAddress()) {
                            log.info("Found non-loopback interface: {}.", ifc.getDisplayName());
                            address = ifcAddress;
                        }
                    }
                }
            }
        } catch (IOException ex) {
            log.error("Cannot get first non-loopback address.", ex);
        }

        if (address != null) {
            return address;
        }

        try {
            return InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            log.warn("Unable to retrieve localhost");
            throw new RuntimeException(e);
        }
    }

}
