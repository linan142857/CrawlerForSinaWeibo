/*    */package mvs.weibo.page.login;

/*    */
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
/*    */
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Hex;

import mvs.weibo.page.service.BaseService;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

/*    */class EncodePassword
/*    */{

	String encodePW2(String passwordString, String serverTime, String nonce) {
		String SINA_PK = "EB2A38568661887FA180BDDB5CABD5F21C7BFD"
				+ "59C090CB2D245A87AC253062882729293E5506350508E7F9A"
				+ "A3BB77F4333231490F915F6D63C55FE2F08A49B353F444AD399"
				+ "3CACC02DB784ABBB8E42A9B1BBFFFB38BE18D78E87A0E41B9"
				+ "B8F73A928EE0CCEE1F6739884B9777E4FE9E88A1BBE495927AC"
				+ "4A799B3181D6442443".trim();
		String sp = "";

		String pwdString = serverTime + "\t" + nonce + "\n" + passwordString;
		System.out.println(pwdString);
		
			sp = rsaCrypt(SINA_PK, "10001", pwdString);
			System.out.println(sp);
	
		return sp;

	}

	/*    */public String encodePW(String passwordString, String serverTime,
			String nonce)
	/*    */{
		/* 24 */String rtn = "";
		// String firstDigest = generateDigest(passwordString, "sha1");
		// String secondDigest = generateDigest(firstDigest, "sha1");
		// String encodedPassword = generateDigest(secondDigest + serverTime +
		// nonce, "sha1");
		// return encodedPassword;

		/*    */try {
			/* 26 */MessageDigest alga = MessageDigest.getInstance("SHA-1");
			/* 27 */String myinfo = passwordString;
			System.out.println(myinfo);
			/* 28 */alga.update(myinfo.getBytes());
			/* 29 */byte[] digesta = alga.digest();
			/* 30 */myinfo = bytes2Hex(digesta);
			/*    */
			/* 32 */alga.update(myinfo.getBytes());
			/* 33 */byte[] digesta1 = alga.digest();
			/* 34 */myinfo = bytes2Hex(digesta1);
			/* 35 */myinfo = myinfo + serverTime + nonce;
			/*    */
			/* 37 */alga.update(myinfo.getBytes());
			/* 38 */byte[] digesta2 = alga.digest();
			/* 39 */rtn = bytes2Hex(digesta2);
			/*    */} catch (NoSuchAlgorithmException e) {
			/* 41 */e.printStackTrace();
			/*    */}
		/*    */
		/* 44 */return rtn;
		/*    */}

	/*    */
	public String rsaCrypt(String modeHex, String exponentHex, String messageg) {
		KeyFactory factory;
		try {
			factory = KeyFactory.getInstance("RSA");
			BigInteger m = new BigInteger(modeHex, 16); /* public exponent */
			BigInteger e = new BigInteger(exponentHex, 16); /* modulus */
			RSAPublicKeySpec spec = new RSAPublicKeySpec(m, e);

			RSAPublicKey pub;

			pub = (RSAPublicKey) factory.generatePublic(spec);

			Cipher enc = Cipher.getInstance("RSA");
			enc.init(Cipher.ENCRYPT_MODE, pub);

			byte[] encryptedContentKey = enc.doFinal(messageg
					.getBytes());
			return new String(Hex.encodeHex(encryptedContentKey));
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoSuchPaddingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InvalidKeyException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InvalidKeySpecException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalBlockSizeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (BadPaddingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return null;

	}

	public String generateDigest(String content, String algorithm) {
		MessageDigest digest = null;
		try {
			digest = java.security.MessageDigest.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace(System.out);
		}
		digest.update(content.getBytes());

		byte[] hash = digest.digest();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < hash.length; i++) {
			sb.append(getByteAsHexString(hash[i]));
		}
		return sb.toString();
	}

	private String getByteAsHexString(byte b) {
		char[] buf = new char[2];
		int radix = 1 << 4;
		int mask = radix - 1;
		buf[1] = digits[(int) (b & mask)];
		b >>>= 4;
		buf[0] = digits[(int) (b & mask)];
		return new String(buf);
	}

	private final char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	/*    */private String bytes2Hex(byte[] bts)
	/*    */{
		/* 54 */String des = "";
		/* 55 */String tmp = null;
		/* 56 */for (int i = 0; i < bts.length; i++) {
			/* 57 */tmp = Integer.toHexString(bts[i] & 0xFF);
			/* 58 */if (tmp.length() == 1) {
				/* 59 */des = des + "0";
				/*    */}
			/* 61 */des = des + tmp;
			/*    */}
		/* 63 */return des;
		/*    */}
	/*    */
}

/*
 * Location: C:\Users\Yaoxu\SkyDrive\backup+hit\bin\ Qualified Name:
 * org.cbapple.weibo.page.login.EncodePassword JD-Core Version: 0.6.1
 */