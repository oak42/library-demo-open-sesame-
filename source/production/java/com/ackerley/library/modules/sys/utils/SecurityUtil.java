package com.ackerley.library.modules.sys.utils;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.Validate;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/*
* 安全框架 辅助工具类
* 从jeesite相关部分(SystemService、Digests) 集中 + 阉割 ...
*/
public class SecurityUtil {
    private static SecureRandom random = new SecureRandom();

    public static final String HASH_ALGORITHM = "sha-1";//故意小写试试...
    public static final int HASH_ITERATIONS = 512;
    public static final int SALT_SIZE = 8;

    //生成salt，不懂，边抄、边搬、边阉割...
    private static byte[] generateSalt(int bytes) {
        Validate.isTrue(bytes > 0, "SALT_SIZE 必须是个正整数！");

        byte[] salt = new byte[bytes];  //依后面看来，应是个出参【扣】...
        random.nextBytes(salt);
        return salt;
    }

    //生成digest(摘要)，不懂，边抄、边搬、边阉割...
    private static byte[] digest(byte[] input, String algorithm, byte[] salt, int iterations) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);

            if(salt != null) {  //【扣】jdk api doc没说传null参会怎样...【猜】null是对象？primitive型不能接受null？
                md.update(salt);
            }

            byte[] result = input;

            for (int i = 0; i < iterations; i++) {
                result = md.digest(result);
                md.reset();
            }

            return result;
        } catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException("无此消息摘要算法(" + algorithm + ")！");
        }

    }

    //对Hex.decodeHex与Hex.encodeHex的简单封装，【扣】String、char[]、byte[]之间有空多看看试试，弄清楚关系本质...
    public static byte[] decodeHex(String input) {
        try {
            return Hex.decodeHex(input.toCharArray());
        } catch(DecoderException e) {
            e.printStackTrace();
            throw new RuntimeException("SecurityUtil.decodeHex方法 解码错误！");
        }
    }
    public static String encodeHex(byte[] input) {
        return new String(Hex.encodeHex(input));
    }

    //加密函数(为了密文存储密码，生成密码摘要，最终存储的是密码摘要)
    public static String encryptPwd(String plainText) {
        //未对plainText先应用commons text的unescapeHtml方法...
        byte[] salt = generateSalt(SALT_SIZE);
        byte[] hashedPwd = digest(plainText.getBytes(), HASH_ALGORITHM, salt, HASH_ITERATIONS);
        System.out.println("Hex encoded salt: " + encodeHex(salt));
        return encodeHex(salt) + encodeHex(hashedPwd);
    }

    //密码比对【逻辑上 与前面 加密函数encryptPwd 保持一致】
    // 比对成功 则返回true
    public static boolean validatePwd(String plainPwd, String persistedPwd) {
        //
        byte[] salt = decodeHex(persistedPwd.substring(0, 16)); //【扣】Hex编码，一个byte变俩
        byte[] hashedPwd = digest(plainPwd.getBytes(), HASH_ALGORITHM, salt, HASH_ITERATIONS);
        return persistedPwd.equals(encodeHex(salt) + encodeHex(hashedPwd));
    }



    public static void main(String[] args) {
        String plain = "7777777";  //密码AAA777aaa1111111111111111111111111111111111111111

        String encryptedPwd = encryptPwd(plain);

        System.out.println("encryptedPwd: " + encryptedPwd);

        boolean result = validatePwd(plain, encryptedPwd);        //测试...

        System.out.println("validate result: " + result);
        System.out.println("---------------------persisted pwd length(minus salt): " + (encryptedPwd.length() - 16));   //【扣】可能是iteration的缘故？长度总是40！
    }
}
