package com.airtennis.dhisat.fostracking.presenter;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

/**
 * Created by naveen on 28/5/16.
 */
public class NetworkCommon {
    public static String HttpPostRequest(String url, List<NameValuePair> nameValuePar) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);
        String content = "";

        try {

            DebugHandler.Log("post url is " + url);
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePar, "UTF-8"));
            HttpParams httpParameters = httppost.getParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 20000);
            HttpConnectionParams.setSoTimeout(httpParameters, 20000);
            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);

            HttpEntity entity = response.getEntity();

            if (entity != null) {
                InputStream inputStream = null;
                if (entity.getContentEncoding() != null && "gzip".equalsIgnoreCase(entity.getContentEncoding().toString())) {
                    inputStream = new GZIPInputStream(entity.getContent());
                } else if (entity.getContentEncoding() != null && "deflate".equalsIgnoreCase(entity.getContentEncoding().toString())) {
                    inputStream = new InflaterInputStream(entity.getContent());
                } else {
                    inputStream = entity.getContent();
                }

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(inputStream));
                //  System.out.println("Encoding is "+urlConnection.getContentEncoding());
                String inputLine = "";
                if (in != null) {
                    while ((inputLine = in.readLine()) != null) {
                        content += inputLine;
                    }
                }
            }

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (UnsupportedEncodingException e) {
            DebugHandler.Log("UnsupportedEncodingException");
        } catch (SocketTimeoutException e) {
            DebugHandler.Log("SocketTimeoutException");
        } catch (ConnectTimeoutException e) {
            DebugHandler.Log("ConnectTimeoutException");
        } catch (IOException e) {
            DebugHandler.Log("IOException");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            DebugHandler.LogException(e);
        }

        return content;
    }
    public static String HttpGetRequest(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
        urlConnection.setConnectTimeout(30000);
        urlConnection.connect();

        if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP || urlConnection.getResponseCode() == 307) {
            url = new URL(urlConnection.getHeaderField("Location"));
            DebugHandler.Log("new url " + url);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(20000);
            urlConnection.setReadTimeout(20000);
            urlConnection.setDoOutput(true);
            urlConnection.connect();
            DebugHandler.Log("response second:" + urlConnection.getHeaderFields().toString());
            DebugHandler.Log("Response fields message:" + urlConnection.getResponseMessage());
        }

        String content = "";
        try {
            InputStream inputStream = null;
            if ("gzip".equals(urlConnection.getContentEncoding())) {
                inputStream = new GZIPInputStream(urlConnection.getInputStream());
            } else if ("deflate".equals(urlConnection.getContentEncoding())) {
                inputStream = new InflaterInputStream(urlConnection.getInputStream());
            } else {
                inputStream = urlConnection.getInputStream();
            }

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(inputStream));
            //  System.out.println("Encoding is "+urlConnection.getContentEncoding());
            String inputLine = "";
            if (in != null) {
                while ((inputLine = in.readLine()) != null) {
                    content += inputLine;
                }
            }
        } finally {
            urlConnection.disconnect();
        }
        return content;
    }


    //API Project over
    public static boolean IsConnected(Context context) {

        try {
            final ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.getState() == NetworkInfo.State.CONNECTED;
        } catch (Exception e) {
            DebugHandler.LogException(e);
        }
        return true;
    }

}
