package com.zoomexamplern;

import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import us.zoom.sdk.ZoomVideoSDKAudioOption;
import us.zoom.sdk.ZoomVideoSDKInitParams;
import us.zoom.sdk.ZoomVideoSDK;
import us.zoom.sdk.ZoomVideoSDKErrors;
import us.zoom.sdk.ZoomVideoSDKSession;
import us.zoom.sdk.ZoomVideoSDKSessionContext;
import us.zoom.sdk.ZoomVideoSDKVideoOption;
//import us.zoom.sdk.Zoom;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.JWT;

public class ZoomManager extends ReactContextBaseJavaModule  {

    private final ReactApplicationContext reactContext;

    ZoomManager(ReactApplicationContext context) {
        super(context);
        this.reactContext = context;
    }

    @NonNull
    @Override
    public String getName() {
        // this defines name of how we will import this module in JS
        return "AwesomeZoomSDK";
    }

    @ReactMethod
    public void initZoom(String publicKey, String privateKey, String domain, Promise promise) {
        Log.d(this.getName(), "Init zoom: " + publicKey
                + " and privateKey: " + privateKey + " domain: " + domain);

        try {
            this.getReactApplicationContext().getCurrentActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ZoomVideoSDK sdk = ZoomVideoSDK.getInstance();
                    ZoomVideoSDKInitParams params = new ZoomVideoSDKInitParams();
                    params.domain = domain;
                    params.enableLog = false;
                    int initResult = sdk.initialize(reactContext.getCurrentActivity(), params);
                    if (initResult == ZoomVideoSDKErrors.Errors_Success) {
                        promise.resolve("Zoom init");
                    } else {
                        Log.e("ERR_UNEXPECTED_EXCEPTIO", String.valueOf(initResult));
                        promise.reject("ERR_UNEXPECTED_EXCEPTION", String.valueOf(initResult));
                    }
                }
            });
        } catch (Exception e) {
            Log.e("ERR_UNEXPECTED_EXCEPTIO", e.getMessage());
            promise.reject("ERR_UNEXPECTED_EXCEPTIO", e);
        }
    }

    @ReactMethod
    public void startMeeting(String username, final String API_KEY, final String API_SECRET , Promise promise) {
        try {

            // Setup audio options
            ZoomVideoSDKAudioOption audioOptions = new ZoomVideoSDKAudioOption();
            audioOptions.connect = true; // Auto connect to audio upon joining
            audioOptions.mute = false; // Auto mute audio upon joining
            // Setup video options
            ZoomVideoSDKVideoOption videoOptions = new ZoomVideoSDKVideoOption();
            videoOptions.localVideoOn = true; // Turn on local/self video upon joining
            // Pass options into session
            ZoomVideoSDKSessionContext params = new ZoomVideoSDKSessionContext();
            params.sessionName = "session";
            params.userName = username;
            params.token = createJWTAccessToken(API_KEY, API_SECRET);
            params.audioOption = audioOptions;
            params.videoOption = videoOptions;

            ZoomVideoSDKSession session = ZoomVideoSDK.getInstance().joinSession(params);

            promise.resolve("Meeting Started");
        } catch (Exception ex) {
            promise.reject("ERR_UNEXPECTED_EXCEPTION", ex);
        }
    }

    public String createJWTAccessToken(final String API_KEY, final String API_SECRET) {
        long EXPIRED_TIME = 3600 * 2;
        long now = System.currentTimeMillis()/1000;
        long time = now + EXPIRED_TIME;

        Algorithm algorithm = Algorithm.HMAC256(API_SECRET);

        Map<String, Object> payloadClaims = new HashMap<>();
        payloadClaims.put("app_key", API_KEY);
        payloadClaims.put("version", 1);
        payloadClaims.put("user_identity", "User ID");
        payloadClaims.put("iat", now);
        payloadClaims.put("exp", time);
        payloadClaims.put("tcp", "session");
        String token = JWT.create()
                .withPayload(payloadClaims)
                .sign(algorithm);

        return token;
    }

}
