/**
 * 
 */
package com.cmri.volte.monitor.dpi;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * @author Amish
 * @date 2014Äê12ÔÂ16ÈÕ
 *
 */
public class LinkAuth {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(digest("lvpin","123456"));
	}
	
	/**
	 * Digest = SHA256(LoginID+MD5(shared secret)+Timestamp+¡±rand=¡±+RAND)
	 * @param loginID
	 * @param password
	 * @param timeStamp
	 * @param rand
	 * @return
	 */
	public static String digest(String argLoginID,String argPassword){
		
		//LoginID
		String loginID = argLoginID;
		int length = argLoginID.length();
		for( int i=0 ; i < 12-length ; i++ ){
			loginID += " ";
		}
		//PassWord
		String password = argPassword;
		String md5PassWord = encryptMD5byString(password);
		//TimeStamp
		Integer timeStampInt = (int) (System.currentTimeMillis()/1000);
		String timeStampStr = Integer.toString(timeStampInt);
		byte[] timeStampBytes = intToBytes(timeStampInt);
        Integer timeStampInt2 = Integer.valueOf(bytesToStringByBit(timeStampBytes),2);
        
		//Rand
		Random random = new Random();
		String randomStr = "rand="+Integer.toString(random.nextInt(65535));
		
		
		System.out.println("argLoginID: "+argLoginID);
		System.out.println("loginID: "+loginID);
		System.out.println("argPassword: "+argPassword);
		System.out.println("md5PassWord: "+md5PassWord);
		System.out.println("timeStampInt: "+timeStampInt);
		System.out.println("timeStampStr: "+timeStampStr);
		System.out.println("timeStampInt2: "+timeStampInt2);
		System.out.println("randomStr: "+randomStr);
		
		
		String sha256Str=loginID+md5PassWord+timeStampStr+randomStr;
		System.out.println("sha256Str: "+sha256Str);
		return encryptSHA256byString(sha256Str);
	}
	
    public static String encryptSHA256byString(String text){
		
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
			messageDigest.update(text.getBytes());
			byte[] textDigest = messageDigest.digest();
			return bytesToHexString(textDigest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
    
    public static String encryptMD5byString(String text){
    	
    	try{
    		
    		MessageDigest messageDigest = MessageDigest.getInstance("MD5");
    		messageDigest.update(text.getBytes());
    		byte[] textDigest = messageDigest.digest();
    		return bytesToHexString(textDigest);
    	} catch (NoSuchAlgorithmException e){
    		e.printStackTrace();
    	}
    	return null;
    }
    
    public static String bytesToHexString(byte[] textBytes){
		if(textBytes==null||textBytes.length<=0){
			return null;
		}
		StringBuilder stringBuilder = new StringBuilder("");
		for (int i = 0; i < textBytes.length; i++) {
			int v = textBytes[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}   
			stringBuilder.append(hv);
		}   
		return stringBuilder.toString();   
	}
    
    private static byte[] intToBytes(int integer){
		int byteNum = (40 -Integer.numberOfLeadingZeros (integer < 0 ? ~ integer : integer))/ 8;
		byte[] byteArray = new byte[4];

		for (int n = 0; n < byteNum; n++)
		    byteArray[3 - n] = (byte) (integer>>> (n * 8));

		return byteArray;
	}
    
    public static String byteToStringByBit(byte b){
		StringBuffer sb = new StringBuffer();
		for (int i = 7; i >= 0; i--) {
			String bitStr = Byte.toString((byte) (b >> i & 1));
            sb.append(bitStr);
        }
		return sb.toString();
	}
    public static String bytesToStringByBit(byte[] bytes){
		StringBuffer bytesStr = new StringBuffer();
		for( byte b : bytes ){
			bytesStr.append(byteToStringByBit(b));
		}
		return bytesStr.toString();
	}


}
