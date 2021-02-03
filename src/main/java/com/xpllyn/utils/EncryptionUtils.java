package com.xpllyn.utils;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Md5Hash;

import java.util.HashMap;
import java.util.Map;

/**
 * 加密
 */
public class EncryptionUtils {

    public static String salt = "jfaog09823gl_flan=jal";

    /**
     * 密码加密
     * @param pwd
     * @return
     */
    public String encryption(String pwd) {
        String password = new Md5Hash(pwd, salt, 2).toString();
        return password;
    }
}
