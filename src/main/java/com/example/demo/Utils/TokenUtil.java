package com.example.demo.Utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.common.Message;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
public class TokenUtil implements Message {
    /** 设置token的有效时间为30天*/
    private static final long TOKEN_EMPIRE_TIME = 7 * 24 * 60 * 60 * 1000;
    /** 设置生成所需要的密钥盐 */
    private static final String TOKEN_SECRET = "Patent-Manage";

    /**
     * jwt生成唯一的token值，包含用户ID，有效时间和密钥
     *
     * @param userId 用户id
     * @return token token值
     * */
    public static String getToken(String userId) {
        String token = "";
        try {
            // 获取当前时间并根据有效时间计算出失效时间
            Date expiresAt = new Date(System.currentTimeMillis() + TOKEN_EMPIRE_TIME);
            // 生成token
            token = JWT.create()
                    .withIssuer("auth0")
                    .withClaim("userId", userId)
                    .withExpiresAt(expiresAt)
                    .sign(Algorithm.HMAC256(TOKEN_SECRET));
            //异常处理
        } catch (JWTCreationException jwtException) {
            log.error(MSG_E_LOG_005);
            jwtException.printStackTrace();
            throw new JWTCreationException(MSG_E_LOG_005, jwtException);
        } catch (IllegalArgumentException illException) {
            log.error(MSG_E_LOG_005);
            illException.printStackTrace();
            throw new IllegalArgumentException(MSG_E_LOG_005);
        }
        return token;
    }

    /**
     * token值验证
     * @param token
     * @return
     */
    public static boolean verify(String token) {
        JWTVerifier verifier = null;
        DecodedJWT jwt = null;
        try {
            verifier = JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).build();
            jwt = verifier.verify(token);
        } catch (Exception e){
            e.printStackTrace();
            log.error("认证失败：" + "\r\n"
                    + "userId: " + jwt.getClaim("userId").asString() + "\r\n"
                    + "过期时间：" + jwt.getExpiresAt());
            return false;
        }
        return true;
    }
}
