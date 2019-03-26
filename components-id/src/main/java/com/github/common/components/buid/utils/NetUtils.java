/*
 * Copyright (c) 2017 Baidu, Inc. All Rights Reserve.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.common.components.buid.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * NetUtils
 * 
 */
public abstract class NetUtils {

    /**
     * Pre-loaded local address
     */
    public static InetAddress localAddress;

    static {
        try {
            localAddress = getLocalInetAddress();
        } catch (SocketException e) {
            throw new RuntimeException("fail to get local ip.");
        }
    }

    /**
     * Retrieve the first validated local ip address(the Public and LAN ip addresses are validated).
     *
     * @return the local address
     * @throws SocketException the socket exception
     */
    public static InetAddress getLocalInetAddress() throws SocketException {
        // enumerates all network interfaces
        Enumeration<NetworkInterface> enu = NetworkInterface.getNetworkInterfaces();

        while (enu.hasMoreElements()) {
            NetworkInterface ni = enu.nextElement();
            if (ni.isLoopback()) {
                continue;
            }

            Enumeration<InetAddress> addressEnumeration = ni.getInetAddresses();
            while (addressEnumeration.hasMoreElements()) {
                InetAddress address = addressEnumeration.nextElement();

                // ignores all invalidated addresses
                if (address.isLinkLocalAddress() || address.isLoopbackAddress() || address.isAnyLocalAddress()) {
                    continue;
                }

                return address;
            }
        }

        throw new RuntimeException("No validated local address!");
    }

    /**
     * Retrieve local address
     * 
     * @return the string local address
     */
    public static String getLocalAddress() {
        return localAddress.getHostAddress();
    }

    /**
     * 获取第2段和第4段ip，并合并成4位,用于特殊场景下的ip特征
     *
     * @return the string local address
     */
    public static String getLocalAddressParts() {
        String address = localAddress.getHostAddress();
        String[] parts = address.split("\\.");
        String second;
        String fourth;
        if (parts == null || parts.length < 3) {
            //under Wifi, ip is like:2001:0:9d38:6ab8:1444:650a:8b18:44a6
            String[] partsWifi = address.split(":");
            second = partsWifi[1].substring(0, 1);
            fourth = partsWifi[3].substring(0, 1);
        } else {
            second = parts[1];
            fourth = parts[3];
        }
        second = second.length() >= 3 ? second.substring(1, 2) : second;
        fourth = fourth.length() >= 3 ? fourth.substring(1, 2) : fourth;
        return second + fourth;
    }

}
