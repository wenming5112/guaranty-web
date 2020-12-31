package com.example.guaranty.common.utils;

import com.example.guaranty.common.utils.security.ConstantForSecurity;
import com.example.guaranty.common.utils.security.ConstantForSecurity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串处理工具
 *
 * @author ming
 * @version 1.0.0
 * @date 2019/8/22 17:42
 **/
@Component
public class StringProcessorUtil {


    public static final String EMPTY = "";

    private static Pattern linePattern = Pattern.compile("_(\\w)");

    private static Pattern numPattern = Pattern.compile("\"^[-\\\\+]?[\\\\d]*$\"");

    /**
     * byte数组转16进制字符串
     *
     * @param bytes byte数组
     * @return String
     */
    public static String byte2Hex(byte[] bytes) {
        StringBuilder stringBuffer = new StringBuilder();
        String temp;
        for (byte myb : bytes) {
            temp = Integer.toHexString(myb & 0xFF);
            if (temp.length() == 1) {
                // 得到一位的进行补0操作
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }

        return stringBuffer.toString();
    }

    /**
     * 格式化RSA公钥(去头去尾)
     *
     * @param publicKey 公钥
     * @return String
     */
    public static String formatRsaPublickKey(String publicKey) {
        return publicKey.replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "");
    }

    /**
     * 16进制字符串转字节数组
     *
     * @param hex hexString
     * @return 字节数组
     */
    public static byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] hexChars = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (ConstantForSecurity.HEX_NUMS_STR.indexOf(hexChars[pos]) << 4 | ConstantForSecurity.HEX_NUMS_STR
                    .indexOf(hexChars[pos + 1]));
        }
        return result;
    }

    /**
     * 将指定byte数组转换成16进制字符串
     *
     * @param b 字节数组
     * @return HexString
     */
    public static String byteToHexString(byte[] b) {
        StringBuilder hexString = new StringBuilder();
        for (byte myb : b) {
            String hex = Integer.toHexString(myb & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            hexString.append(hex.toUpperCase());
        }
        return hexString.toString();
    }

    /**
     * 字节数组转16进制
     *
     * @param bytes 字节数组
     * @return HexString
     */
    public static String bufferToHex(byte[] bytes) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte[] bytes, int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        // 取字节中高 4 位的数字转换, >>>
        char c0 = ConstantForSecurity.HEX_DIGITS[(bt & 0xf0) >> 4];
        // 为逻辑右移，将符号位一起右移,此处未发现两种符号有何不同
        // 取字节中低 4 位的数字转换
        char c1 = ConstantForSecurity.HEX_DIGITS[bt & 0xf];
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }


    /**
     * 判断字符串是否不为空
     *
     * @param str 源数据
     * @return Boolean
     */
    public static boolean isNotEmpty(String str) {
        return null != str && !"".equals(str.trim());
    }

    /**
     * 判断字符串是否为空
     *
     * @param str 字符串
     * @return boolean
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0 || "".equals(str.trim());
    }

    /**
     * 手机号码验证
     *
     * @param phone 手机号码
     * @return boolean
     */
    public static boolean phoneRegex(String phone) {
        String regex = "^1(3|4|5|7|8)\\d{9}$";
        return Pattern.matches(regex, phone);
    }

    /**
     * 判断字符序列
     *
     * @param cs
     * @return
     */
    public static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断对象或对象数组中每一个对象是否为空
     * 对象为null; 字符序列长度为0; 集合类、Map为empty
     *
     * @param obj 对象
     * @return boolean
     */
    public static boolean objIsNullOrEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length() == 0;
        }
        if (obj instanceof Collection) {
            return ((Collection) obj).isEmpty();
        }
        if (obj instanceof Map) {
            return ((Map) obj).isEmpty();
        }
        if (obj instanceof Object[]) {
            Object[] object = (Object[]) obj;
            if (object.length == 0) {
                return true;
            }
            boolean empty = true;
            for (int i = 0; i < object.length; i++) {
                if (!objIsNullOrEmpty(object[i])) {
                    empty = false;
                    break;
                }
            }
            return empty;
        }
        return false;
    }

    /**
     * 下划线转驼峰
     *
     * @param str 字符串
     * @return String
     */
    public static String lineToHump(String str) {
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * Object 对象转换成字符串
     *
     * @param obj 对象
     * @return String
     */
    public static String toStringByObject(Object obj) {
        return toStringByObject(obj, false, null);
    }

    /**
     * Object 对象转换成字符串，并可以根据参数去掉两端空格
     *
     * @param obj 对象
     * @return String
     */
    private static String toStringByObject(Object obj, boolean isqdkg, String datatype) {
        if (obj == null) {
            return "";
        } else {
            if (isqdkg) {
                return obj.toString().trim();
            } else {
                // 如果有设置时间格式类型，这转换
                if (hasText(datatype)) {
                    if (obj instanceof Timestamp) {
                        return DateProcessorUtil.format((Timestamp) obj, datatype);
                    } else if (obj instanceof Date) {
                        return DateProcessorUtil.format((Timestamp) obj, datatype);
                    }
                }
                return obj.toString();
            }
        }
    }

    /**
     * 判断一个字符序列是否有内容
     *
     * @param str 字符序列
     * @return boolean
     */
    public static boolean hasText(CharSequence str) {
        if (!hasLength(str)) {
            return false;
        } else {
            int strLen = str.length();
            for (int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(str.charAt(i))) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * 判断字符序列是否有长度
     *
     * @param str 字符序列
     * @return boolean
     */
    public static boolean hasLength(CharSequence str) {
        return str != null && str.length() > 0;
    }

    /**
     * 判断一个字符串是否有内容
     *
     * @param str 字符串
     * @return boolean
     */
    public static boolean hasText(String str) {
        return hasText((CharSequence) str);
    }

    /**
     * 转整数
     *
     * @param str 字符串
     * @return int
     */
    private static int parseInt(Object str) {
        if (str == null || "".equals(str)) {
            return 0;
        }
        String s = str.toString().trim();
        if (!s.matches("-?\\d+")) {
            return 0;
        }
        return Integer.parseInt(s);
    }

    public static String join(Collection list, String sep) {
        return join(list, sep, (String) null);
    }

    private static String join(Collection list, String sep, String prefix) {
        Object[] array = list == null ? null : list.toArray();
        return join(array, sep, prefix);
    }

    private static String join(Object[] array, String sep, String prefix) {
        if (array == null) {
            return "";
        } else {
            int arraySize = array.length;
            if (arraySize == 0) {
                return "";
            } else {
                if (sep == null) {
                    sep = "";
                }
                if (prefix == null) {
                    prefix = "";
                }
                StringBuilder buf = new StringBuilder(prefix);
                for (int i = 0; i < arraySize; ++i) {
                    if (i > 0) {
                        buf.append(sep);
                    }
                    buf.append(array[i] == null ? "" : array[i]);
                }
                return buf.toString();
            }
        }
    }

    /**
     * 获得用户远程地址
     *
     * @param request 请求对象
     * @return String
     */
    public static String getRemoteAddr(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        String ip = request.getHeader("x-forwarded-for");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if (ip.contains(".")) {
                ip = ip.split(",")[0];
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 判断字符串是不是数字
     *
     * @param str 字符串
     * @return boolean
     */
    public static boolean isInteger(String str) {
        return numPattern.matcher(str).matches();
    }

    /**
     * 数组转字符串
     *
     * @param strs 数组
     * @return String
     */
    public static String arrayToString(Object[] strs) {
        String result = "";
        for (int i = 0; i < strs.length; i++) {
            if ("".equals(strs[i]) || strs[i] == null) {
                continue;
            }
            if (i == 0) {
                result = strs[i].toString();
            } else {
                result += "," + strs[i];
            }
        }
        if ("".equals(result)) {
            return null;
        } else {
            return result;
        }
    }

    /**
     * 获取随机数
     *
     * @param num 位数
     * @return String
     */
    public static String getRandomNum(int num) {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < num; i++) {
            stringBuilder.append(random.nextInt(10));
        }
        return stringBuilder.toString();
    }


//    public PrivateKeyAndOthersVO getPrivateKeyAndOthers(String cryptographicPassword, String sixWordskey, String publicKeyStr, RedisCacheUtil redisCacheUtil) throws BaseException {
//        PrivateKeyAndOthersVO privateKeyAndOthersVo = new PrivateKeyAndOthersVO();
//        // 这里需要确保keypair不为空
//        String privateKeyStr = redisCacheUtil.getString(publicKeyStr);
//        //解密后的明文
//        String rsaDecryptPwd = RSAUtils.decode(cryptographicPassword, privateKeyStr);
//        //缓存中的六位码
//        String sixKeyWordFromCache = redisCacheUtil.getString(sixWordskey);
//        //明文中的六位码
//        String sixKeyWordFromCleartext = rsaDecryptPwd.substring(rsaDecryptPwd.length() - 6);
//        //去掉后六位
//        String pwd = rsaDecryptPwd.substring(0, rsaDecryptPwd.length() - 6);
//
//        privateKeyAndOthersVo.setPassword(pwd);
//        privateKeyAndOthersVo.setSixKeyWordFromCache(sixKeyWordFromCache);
//        privateKeyAndOthersVo.setSixKeyWordFromCleartext(sixKeyWordFromCleartext);
//        return privateKeyAndOthersVo;
//    }


    /**
     * 响应行数验证
     *
     * @param raw         行
     * @param responseRaw 响应行
     * @param msg         消息
     */
    public static void influenceRawVer(int raw, int responseRaw, String msg) {
        if (responseRaw != raw) {
            throw new RuntimeException(msg);
        }
    }

    /**
     * 响应行数验证
     *
     * @param responseRaw 响应行
     * @param msg         消息
     */
    public static void influenceRawVer(int responseRaw, String msg) {
        if (responseRaw <= 0) {
            throw new RuntimeException(msg);
        }
    }

    /**
     * 获取完整的异常栈信息
     *
     * @param throwable 抛异常 对象
     * @return String
     */
    public static String getTrace(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        throwable.printStackTrace(writer);
        StringBuffer buffer = stringWriter.getBuffer();
        return buffer.toString();
    }

    /**
     * 将String转map
     *
     * @param str 字符串
     * @return Map
     */
    public static Map<String, Object> stringToMap(String str) {
        if (str == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        String[] strParams = str.split("&");
        for (String s : strParams) {
            String[] kv = s.split("=");
            map.put(kv[0], kv[1]);
        }
        return map;
    }

    /**
     * 将需要签名的map根据键自然排序，并转换成字符串，
     * 方便后续进行签名和验签
     *
     * @param map map
     * @return 字符串
     */
    public static String mapToString(Map<String, Object> map) {
        if (map == null) {
            return null;
        }
        List<String> keyList = new ArrayList<>(map.keySet());
        Collections.sort(keyList);
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : keyList) {
            Object value = map.get(s);
            stringBuilder.append(s + "=" + value + "&");
        }
        return stringBuilder.substring(0, stringBuilder.length() - 1);
    }

    /**
     * 字符串首字母大写
     *
     * @param str 字符串
     * @return String
     */
    public static String upperCase(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * 获取组织MSPID
     *
     * @param orgName 组织名
     * @return String
     */
    public static String getOrgMSPID(String orgName) {
        orgName = upperCase(orgName);
        return orgName + "MSP";
    }

}
