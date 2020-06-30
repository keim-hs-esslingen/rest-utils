/*
 * MIT License
 * 
 * Copyright (c) 2020 Hochschule Esslingen
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE. 
 */
package de.hsesslingen.keim.restutils;

import java.util.regex.Pattern;

/**
 * Contains methods and fields for working with URLs.
 *
 * @author k.sivarasah, boesch 27 Sep 2019
 */
public class UrlUtils {

    private static Pattern domainPattern;
    private static final String DOMAIN_NAME_REGEX = "^((?!-)[A-Za-z0-9-]{1,63}(?<!-)\\.)+[A-Za-z]{2,6}$";

    private static Pattern ipAddressPattern;
    private static final String IPADDRESS_REGEX
            = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
            + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
            + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
            + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    static {
        domainPattern = Pattern.compile(DOMAIN_NAME_REGEX);
        ipAddressPattern = Pattern.compile(IPADDRESS_REGEX);
    }

    /**
     * Checks whether the given {@code  domainName} is a valid domain name.
     *
     * @param domainName
     * @return
     */
    public static boolean isValidDomainName(String domainName) {
        return domainPattern.matcher(domainName).matches();
    }

    /**
     * Checks whether the given {@code ip} is a valid IP-address.
     *
     * @param ip
     * @return
     */
    public static boolean isValidIpAddress(String ip) {
        return ipAddressPattern.matcher(ip).matches();
    }

}
