package io.flippedclassroom.server.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.flippedclassroom.server.config.Const;

import java.util.Date;

/**
 * Token 生成与校验
 */
public class TokenUtils {
	/**
	 * 校验 token
	 *
	 * @param token    待校验的 token
	 * @param username 用户名
	 * @param password 用户密码
	 * @return 成功与否
	 */
	public static boolean verify(String token, String username, String password) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(password.getBytes());
			JWTVerifier verifier = JWT.require(algorithm)
					.withClaim("username", username)
					.build();
			verifier.verify(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 获取 Token 中的信息，该信息无需 secret 解密即可获得
	 *
	 * @param token token
	 * @return username or null
	 */
	public static String getUsername(String token) {
		try {
			DecodedJWT decodedJWT = JWT.decode(token);
			return decodedJWT.getClaim("username").asString();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 生成签名
	 * 签名默认有效期为一天
	 *
	 * @param username 用户名
	 * @param password 用户密码
	 * @return 加密的 token
	 */
	public static String sign(String username, String password) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(password.getBytes());
			return JWT.create()
					.withClaim("username", username)
					.withClaim("date", new Date())
					.withExpiresAt(new Date(Const.expire()))
					.sign(algorithm);
		} catch (Exception e) {
			return null;
		}
	}
}
