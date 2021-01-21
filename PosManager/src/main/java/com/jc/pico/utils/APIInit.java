package com.jc.pico.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class APIInit {
    private static Retrofit retrofit;
    private static SolapiMsgV4 messageService;
   // private static SolapiImgApi imageService;

    public static String getHeaders() throws IOException {
        try {
        	
        /*
         * header 인증 문자열 예시 (퍼플북)
         * Authorization: HMAC-SHA256 apiKey=NCSYZBM5EEWHTTY,
         *                date=2021-01-20 18:16:09,
         *                salt=f4105e4eec24f86e28a2b5b4b35528c37e9e7941dfaab96cbbd916068296dabf,
         *                signature=cfc86a376a09e38b1318093d5756b0d05dbc0fdf1fe90194d3ec4bd9cd9cb781
         */
        	
        	String apiKey = "NCSYZBM5EEWHTTY8"; // 퍼플북 제공 코드
        	String apiSecret = "8KLWTWFLCAUVGW8ZVVHXIXNZYINAPFVL"; // 퍼플북 제공 코드
        	
        	String date = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toString().split("\\[")[0];
            String salt = UUID.randomUUID().toString().replaceAll("-", "");
            

            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(apiSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            
            String signature = new String(Hex.encodeHex(sha256_HMAC.doFinal((date + salt).getBytes(StandardCharsets.UTF_8))));
            
            String authorization = "HMAC-SHA256 ApiKey=" + apiKey + ", Date=" + date + ", salt=" + salt + ", signature=" + signature;
			return authorization;
        } catch (InvalidKeyException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static SolapiMsgV4 getAPI() {
        if (messageService == null) {
            setRetrofit();
            messageService = retrofit.create(SolapiMsgV4.class);
        }
        return messageService;
    }

/*    public static SolapiImgApi getImageAPI() {
        if (imageService == null) {
            setRetrofit();
            imageService = retrofit.create(SolapiImgApi.class);
        }
        return imageService;
    }*/

    public static void setRetrofit() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        Request 시 로그가 필요하면 추가하세요.
//        interceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        String domain = "api.solapi.com";
        String protocol = "https";
        String prefix = "/";

    /*    try {
        	//Ini ini = new Ini(new File("config.ini"));
            //Ini ini = new Ini(new File("/the9Kiosk/MEMmxYg0KJhrQH-20111132522036-solapi-java/MEMmxYg0KJhrQH-20111132522036-solapi-java/app/config.ini"));
            //if (ini.get("SERVER", "domain") != null) domain = ini.get("SERVER", "domain");
            //if (ini.get("SERVER", "protocol") != null) protocol = ini.get("SERVER", "protocol");
            //if (ini.get("SERVER", "prefix") != null) prefix = ini.get("SERVER", "prefix");
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(protocol + "://" + domain + prefix)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }
}
