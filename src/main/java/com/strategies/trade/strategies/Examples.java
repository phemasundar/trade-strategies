package com.strategies.trade.strategies;

import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.SessionExpiryHook;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;

/**
 * @author hemasundarpenugonda
 */
public class Examples {

    @NotNull
    public KiteConnect loginKiteConnect() throws KiteException, IOException {
        // First you should get request_token, public_token using kitconnect login and then use request_token, public_token, api_secret to make any kiteConnect api call.
        // Initialize KiteSdk with your apiKey.
        KiteConnect kiteConnect = new KiteConnect("xxxxyyyyzzzz");

        //If you wish to enable debug logs send true in the constructor, this will log request and response.
        //KiteConnect kiteConnect = new KiteConnect("xxxxyyyyzzzz", true);

        // If you wish to set proxy then pass proxy as a second parameter in the constructor with api_key. syntax:- new KiteConnect("xxxxxxyyyyyzzz", proxy).
        //KiteConnect kiteConnect = new KiteConnect("xxxxyyyyzzzz", userProxy, false);

        // Set userId
        kiteConnect.setUserId("xxxxx");

        // Get login url
        String url = kiteConnect.getLoginURL();

        // Set session expiry callback.
        kiteConnect.setSessionExpiryHook(new SessionExpiryHook() {
            @Override
            public void sessionExpired() {
                System.out.println("session expired");
            }
        });

            /* The request token can to be obtained after completion of login process. Check out https://kite.trade/docs/connect/v3/user/#login-flow for more information.
               A request token is valid for only a couple of minutes and can be used only once. An access token is valid for one whole day. Don't call this method for every app run.
               Once an access token is received it should be stored in preferences or database for further usage.
            */
        User user =  kiteConnect.generateSession("xxxxxtttyyy", "xxxxxxxyyyyy");
        kiteConnect.setAccessToken(user.accessToken);
        kiteConnect.setPublicToken(user.publicToken);
        return kiteConnect;
    }

    public Profile getProfile(KiteConnect kiteConnect) throws IOException, KiteException {
        Profile profile = kiteConnect.getProfile();
        System.out.println(profile.userName);
        return profile;
    }

    /** Logout user.*/
    public void logout(KiteConnect kiteConnect) throws KiteException, IOException {
        /** Logout user and kill session. */
        JSONObject jsonObject10 = kiteConnect.logout();
        System.out.println(jsonObject10);
    }

}
