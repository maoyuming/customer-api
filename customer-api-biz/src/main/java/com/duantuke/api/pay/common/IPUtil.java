package com.duantuke.api.pay.common;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IPUtil {

    private static Logger log = LoggerFactory.getLogger(IPUtil.class);

    private static String LOCALIP = null;

    public static String getLocalIPV4() {

        if (LOCALIP == null) {

            LOCALIP = getIP();
        }

        return LOCALIP;

    }

    /**
     * 取本机IP <br>
     * 用InetAddress类有可能会取到127的地址,所以当取到127的地址后使用NetworkInterface类 <br>
     * 在非windows操作系统中,推荐使用本方法
     * 
     * @return 失败返回NULL,成功返回IPV4地址
     */
    @SuppressWarnings("rawtypes")
    private static String getIP() {

        String strIp = null;

        // 首先偿试用InetAddress
        try {

            InetAddress netAddress = InetAddress.getLocalHost();
            strIp = netAddress.getHostAddress();
            if (!strIp.startsWith("127")) {
                return strIp;
            }
        } catch (UnknownHostException e1) {
            log.error("获取IP异常!", e1);
            
        }
        // 未找到非127的地址
        Enumeration netInterfaces = null;
        try {
            netInterfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) { // 使用NetworkInterface失败,只能返回127.0.0.1的地址了,由调用者判断是否重新获取
            return strIp;
        }
        InetAddress ip = null;
        while (netInterfaces.hasMoreElements()) {
            NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
            Enumeration enumInetAddress = ni.getInetAddresses();
            while (enumInetAddress.hasMoreElements()) {
                ip = (InetAddress) enumInetAddress.nextElement();
                strIp = ip.getHostAddress();
                // 过虑掉127和localhost
                if (strIp.startsWith("127") || strIp.startsWith("l") || strIp.startsWith("L")) {
                    continue;
                }
                if (!isboolIp(strIp)) {
                    continue;
                }
                return strIp;
            }
        }
        // 还没有取到,返回空或127.0.0.1，由调用者判断是否重新获取
        return strIp;

    }

    /**
     * 判断是否为合法IP
     * @return the ip
     */
    private static boolean isboolIp(String ipAddress) {

        String ip = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

        Pattern pattern = Pattern.compile(ip);

        Matcher matcher = pattern.matcher(ipAddress);

        return matcher.matches();

    }
    
    public static String getIpAddr(HttpServletRequest request) {

        String ip = request.getHeader("x-forwarded-for");

        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {

        ip = request.getHeader("Proxy-Client-IP");

        }

        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {

        ip = request.getHeader("WL-Proxy-Client-IP");

        }

        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {

        ip = request.getRemoteAddr();
        }
        return ip;

        }
}
