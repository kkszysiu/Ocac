package org.yaaic.model;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;


public class OnetAuth
{
    private static String username = "";
    private static String password = "";

    final static int[] append = { 29, 43, 7, 5, 52, 58, 30, 59, 26, 35, 35, 49, 45, 4, 22, 4, 0, 7, 4, 30, 51, 39, 16, 6, 32, 13, 40, 44, 14, 58, 27, 41, 52, 33, 9, 30, 30, 52, 16, 45, 43, 18, 27, 52, 40, 52, 10, 8, 10, 14, 10, 38, 27, 54, 48, 58, 17, 34, 6, 29, 53, 39, 31, 35, 60, 44, 26, 34, 33, 31, 10, 36, 51, 44, 39, 53, 5, 56 };
    final static int[] arraycopy = { 7, 32, 25, 39, 22, 26, 32, 27, 17, 50, 22, 19, 36, 22, 40, 11, 41, 10, 10, 2, 10, 8, 44, 40, 51, 7, 8, 39, 34, 52, 52, 4, 56, 61, 59, 26, 22, 15, 17, 9, 47, 38, 45, 10, 0, 12, 9, 20, 51, 59, 32, 58, 19, 28, 11, 40, 8, 28, 6, 0, 13, 47, 34, 60, 4, 56, 21, 60, 59, 16, 38, 52, 61, 44, 8, 35, 4, 11 };
    final static int[] charAt = { 60, 30, 12, 34, 33, 7, 15, 29, 16, 20, 46, 25, 8, 31, 4, 48, 6, 44, 57, 16, 12, 58, 48, 59, 21, 32, 2, 18, 51, 8, 50, 29, 58, 6, 24, 34, 11, 23, 57, 43, 59, 50, 10, 56, 27, 32, 12, 59, 16, 4, 40, 39, 26, 10, 49, 56, 51, 60, 21, 37, 12, 56, 39, 15, 53, 11, 33, 43, 52, 37, 30, 25, 19, 55, 7, 34, 48, 36 };
    final static int[] length = { 11, 9, 12, 0, 1, 4, 10, 13, 3, 6, 7, 8, 15, 5, 2, 14 };
    final static int[] toString = { 1, 13, 5, 8, 7, 10, 0, 15, 12, 3, 14, 11, 2, 9, 6, 4 };

    private static final String URL1 = "http://kropka.onet.pl/_s/kropka/1?DV=czat/applet/FULL";
    private static final String URL2 = "http://czat.onet.pl/myimg.gif";
    private static final String URL3 = "http://secure.onet.pl/mlogin.html";
    private static final String URL4 = "http://czat.onet.pl/include/ajaxapi.xml.php3";

    private static AsyncHttpClient client = new AsyncHttpClient();
    private static PersistentCookieStore cookieStore;

    public static void setOCUsername(String username) {
        OnetAuth.username = username;
    }

    public static void setOCPassword(String password) {
        OnetAuth.password = password;
    }

    public static void initialiseCookieStore(Context context) {
        cookieStore = new PersistentCookieStore(context);
    }

    public static void authoriseSession(final AsyncHttpResponseHandler lastResponseHandler) {
        authoriseFirstPart(new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                //Log.d("OnetAuth", "Auth authoriseFirstPart onSuccess: "+response.toString());
                authoriseSecondPart(new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(String response) {
                        //Log.d("OnetAuth", "Auth authoriseSecondPart onSuccess: "+response.toString());
                        authoriseThirdPart(new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(String response) {
                                //Log.d("OnetAuth", "Auth authoriseThirdPart onSuccess: "+response.toString());
                                authoriseFourthPart(lastResponseHandler);
                            }
                        });
                    }
                    @Override
                    public void onFailure(Throwable e) {
                        Log.d("OnetAuth", "Auth third step failure: "+e.getMessage());
                    }
                });
            }
        });
    }

    public static void authoriseFirstPart(AsyncHttpResponseHandler responseHandler) {
        Log.i("OnetAuth", "Executing "+URL1);
        client.setCookieStore(cookieStore);
        client.get(URL1, new RequestParams(), responseHandler);
    }

    public static void authoriseSecondPart(AsyncHttpResponseHandler responseHandler) {
        Log.i("OnetAuth", "Executing "+URL2);
        client.setCookieStore(cookieStore);
        client.get(URL2, new RequestParams(), responseHandler);
    }

    public static void authoriseThirdPart(AsyncHttpResponseHandler responseHandler) {
        Log.i("OnetAuth", "Executing "+URL3);
        client.setCookieStore(cookieStore);

        //r=&url=&login=%1&haslo=%2&app_id=20&ssl=1&ok=1
        RequestParams params = new RequestParams();
        params.put("r",     "");
        params.put("url",   "");
        params.put("login", username);
        params.put("haslo", password);
        params.put("app_id","20");
        params.put("ssl",   "0");
        params.put("ok",    "1");

        client.post(URL3, params, responseHandler);
    }

    public static void authoriseFourthPart(AsyncHttpResponseHandler responseHandler) {
        Log.i("OnetAuth", "Executing "+URL4);
        client.setCookieStore(cookieStore);

        RequestParams params2 = new RequestParams();
        params2.put("api_function", "getUoKey");
        params2.put("params", "a:3:{s:4:\"nick\";s:"+username.length()+":\""+username+"\";s:8:\"tempNick\";i:0;s:7:\"version\";s:22:\"1.1(20110818-1158 - R)\";}");

        client.post(URL4, params2, responseHandler);
    }

    public final static String generateAuthKey(String paramString)
    {
        int i;
        int[] arrayOfInt1 = new int[16];
        int[] arrayOfInt2 = new int[16];
        if (paramString.length() < 16) {
            return "(key to short)";
        }
        for (i = 0; i < 16; i++)
        {
            char c = paramString.charAt(i);
            arrayOfInt1[i] = (c > 57 ? c > 90 ? (c - 97) + 36 : (c - 65) + 10 : c - 48);
        }
        for (i = 0; i < 16; i++) {
            arrayOfInt1[i] = OnetAuth.append[(arrayOfInt1[i] + i)];
        }
        System.arraycopy(arrayOfInt1, 0, arrayOfInt2, 0, 16);
        for (i = 0; i < 16; i++) {
            arrayOfInt1[i] = ((arrayOfInt1[i] + arrayOfInt2[OnetAuth.length[i]]) % 62);
        }
        for (i = 0; i < 16; i++) {
            arrayOfInt1[i] = OnetAuth.arraycopy[(arrayOfInt1[i] + i)];
        }
        System.arraycopy(arrayOfInt1, 0, arrayOfInt2, 0, 16);
        for (i = 0; i < 16; i++) {
            arrayOfInt1[i] = ((arrayOfInt1[i] + arrayOfInt2[OnetAuth.toString[i]]) % 62);
        }
        for (i = 0; i < 16; i++) {
            arrayOfInt1[i] = OnetAuth.charAt[(arrayOfInt1[i] + i)];
        }
        for (i = 0; i < 16; i++)
        {
            int j = arrayOfInt1[i];
            arrayOfInt1[i] = (j >= 10 ? j >= 36 ? (97 + j) - 36 : (65 + j) - 10 : 48 + j);
        }
        StringBuffer localStringBuffer = new StringBuffer();
        for (int j = 0; j < 16; j++) {
            localStringBuffer.append((char)arrayOfInt1[j]);
        }
        return localStringBuffer.toString();
    }
}
