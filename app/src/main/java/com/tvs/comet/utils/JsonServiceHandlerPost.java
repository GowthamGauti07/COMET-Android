package com.tvs.comet.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

//import com.tvsandsons.mpay.services.SendMail;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;


public class JsonServiceHandlerPost {
    private String Url = null;
    private String parameter = "";
    private Context context;
    private final String CHAR_SET = "UTF-8";

    //Following fields are for CONTENT_TYPE multipart/form-data
    private HashMap<String, String> parameterHashMap = null;
    private String boundary;
    private PrintWriter writer;
    private static final String LINE_FEED = "\r\n";
    private OutputStream os;
    private File file = null;

    private SharedPreferences sharedPreferences;
    private String token;
    private String from="";
    String fileName="";

    //Following fields are for SOAP services
    private String mNamespace, mMethodName, mSoapAction;

    public static ResponseCodeInterface listener = null;

    public JsonServiceHandlerPost(String Url, Context context) {
        this.Url = Url;
        this.context = context;

//        sharedPreferences = context.getSharedPreferences(Util.EMPLOYEE_PREFERENCE, Context.MODE_PRIVATE);
//        token = sharedPreferences.getString("token", null);

    }

    public JsonServiceHandlerPost(String Url, HashMap<String, String> parameterHashMap, Context context, File file) {
        this.Url = Url;
        this.parameterHashMap = parameterHashMap;
        this.context = context;
        this.file = file;

//        sharedPreferences = context.getSharedPreferences(Util.EMPLOYEE_PREFERENCE, Context.MODE_PRIVATE);
//        token = sharedPreferences.getString("token", null);

    }

    public JsonServiceHandlerPost(String Url, HashMap<String, String> parameterHashMap, Context context) {
        this.Url = Url;
        this.parameterHashMap = parameterHashMap;
        this.context = context;

//        sharedPreferences = context.getSharedPreferences(Util.EMPLOYEE_PREFERENCE, Context.MODE_PRIVATE);
//        token = sharedPreferences.getString("token", null);

    }

    public JsonServiceHandlerPost(String Url, HashMap<String, String> parameterHashMap, Context context, String from) {
        this.Url = Url;
        this.parameterHashMap = parameterHashMap;
        this.context = context;
        this.from = from;

//        sharedPreferences = context.getSharedPreferences(Util.EMPLOYEE_PREFERENCE, Context.MODE_PRIVATE);
//        token = sharedPreferences.getString("token", null);

    }

    public JsonServiceHandlerPost(String Url, HashMap<String, String> parameterHashMap, Context context,
                                  String mNamespace, String mMethodName, String mSoapAction) {
        this.Url = Url;
        this.parameterHashMap = parameterHashMap;
        this.context = context;
        this.mNamespace = mNamespace;
        this.mMethodName = mMethodName;
        this.mSoapAction = mSoapAction;

//        sharedPreferences = context.getSharedPreferences(Util.EMPLOYEE_PREFERENCE, Context.MODE_PRIVATE);
//        token = sharedPreferences.getString("token", null);

    }

    @SuppressLint("LongLogTag")
    public JSONObject ServiceDataApplicationJson(boolean tokenNeeded) {
        JSONObject Jobj = new JSONObject();
        String response = "";
        HttpURLConnection conn = null;

        try {
            Log.e("param service handler", parameter);
            Log.e("Url service handler", Url);

            // creates a unique boundary based on time stamp
            boundary = "===" + System.currentTimeMillis() + "===";
            URL url = new URL(Url);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(120000);
            conn.setConnectTimeout(120000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            if (tokenNeeded) {
                Log.e("token", token);
                conn.setRequestProperty("Authorization", "Bearer " + token);
            }
            conn.setRequestProperty("Content-Type", "application/json");      //  Setting content type as json
            byte[] outputBytes = parameter.getBytes(CHAR_SET);
            os.write(outputBytes);    // Used when content type is json

            int responseCode = conn.getResponseCode();
            Log.e("-=-=-=-", "Response Code " + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) {
                Log.e("Entered", "" + HttpURLConnection.HTTP_OK);
                String line;

                if (tokenNeeded) {
                    String reqProperty = conn.getRequestProperty("Authorization");
                    Log.e("-=-=-=-", "reqProperty: " + reqProperty);
                    String authorization1 = conn.getHeaderField("Authorization");
                    Log.e("ResponsePropertyAuthorization", authorization1);
//                    String authorization = conn.getHeaderField(5);
//                    Log.e("ResponseProperty", authorization);
                    String[] strings = authorization1.split(" ");
                    String bearerToken = strings[1];
                    Log.e("bearerToken", bearerToken);
                    //  Replacing new token into EMPLOYEE_PREFERENCE
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("token", bearerToken);
                    editor.apply();
                }

                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    Log.e("Entered", "" + HttpURLConnection.HTTP_UNAUTHORIZED);

                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        response += line;
                    }
                    JSONObject jsonObject1 = new JSONObject(response);
                    final String errorMsg = jsonObject1.getString("error");
                    ((Activity) context).runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast toast = Toast.makeText(context,
                                    errorMsg,
                                    Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();

                        }

                    });

                } else if (responseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
                    Log.e("Entered", "" + HttpURLConnection.HTTP_BAD_REQUEST);
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        response += line;
                    }
                    JSONObject jsonObject1 = new JSONObject(response);
                    final String errorMsg = jsonObject1.getString("error");
                    ((Activity) context).runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast toast = Toast.makeText(context,
                                    errorMsg,
                                    Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();

                        }

                    });

                } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                    Log.e("Entered", "" + HttpURLConnection.HTTP_NOT_FOUND);
                    ((Activity) context).runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast toast = Toast.makeText(context,
                                    "Requested resource not found",
                                    Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();

                        }

                    });
                } else if (responseCode == HttpURLConnection.HTTP_INTERNAL_ERROR) {
                    Log.e("Entered", "" + HttpURLConnection.HTTP_INTERNAL_ERROR);
                    ((Activity) context).runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast toast = Toast.makeText(context,
                                    "Something went wrong at server end",
                                    Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }

                    });
                } else {
                    Log.e("Entered", "" + responseCode);
                    ((Activity) context).runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            Toast toast = Toast.makeText(context,
                                    "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]",
                                    Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }

                    });
                }
            }
            Log.e("json", response);
            Jobj = new JSONObject(response);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {

            try {

//                Log.e("Entered", "" + HttpURLConnection.HTTP_UNAUTHORIZED);
                String line;
                if (!(conn == null)) {

                    if (tokenNeeded) {
                        String reqProperty = conn.getRequestProperty("Authorization");
                        Log.e("-=-=-=-", "reqProperty: -1- " + reqProperty);
                        String authorization1 = conn.getHeaderField("Authorization");
                        Log.e("ResponsePropertyAuthorization", authorization1);
//                        String authorization = conn.getHeaderField(5);
//                        Log.e("ResponseProperty  -1- ", authorization);
                        String[] strings = authorization1.split("=");
                        String splitString = strings[1];
                        String[] strings1 = splitString.split(";");
                        String bearerToken = strings1[0];
                        Log.e("bearerToken  -1- ", bearerToken);
                        //  Replacing new token into EMPLOYEE_PREFERENCE
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("token", bearerToken);
                        editor.apply();
                    }

                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                    while ((line = br.readLine()) != null) {
                        response += line;
                    }
                    Log.e("error response", response);
                    JSONObject jsonObject1 = new JSONObject(response);
                    final String errorMsg = jsonObject1.getString("error");
                    ((Activity) context).runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast toast = Toast.makeText(context,
                                    errorMsg,
                                    Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();

                        }

                    });

                    return jsonObject1;

                }

            } catch (Exception e1) {
                e1.printStackTrace();
            }

            e.printStackTrace();
        } catch (JSONException e) {
            Log.e("JSON Parser Service Handler", "Error parsing data " + e.toString());
            //Toast.makeText(SyncDetail.context, "Data Not Inserted", Toast.LENGTH_SHORT ).show();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
        return Jobj;

    }

    @SuppressLint("LongLogTag")
    public JSONObject ServiceDataMultipart(boolean tokenNeeded) {
        JSONObject Jobj = new JSONObject();
        String response = "";
        HttpURLConnection conn = null;
        try {
            Log.e("Url service handler", Url);

            // creates a unique boundary based on time stamp
            boundary = "===" + System.currentTimeMillis() + "===";
            URL url = new URL(Url);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(120000);
            conn.setConnectTimeout(120000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
//            conn.setRequestProperty("Authorization", "Bearer " + "pass the lwt token here");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Cache-Control", "no-cache");
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);   //Setting content type as form-data
            Log.e("tokenNeeded", "" + tokenNeeded);
            if (tokenNeeded) {
                Log.e("token", token);
                conn.setRequestProperty("Authorization", "Bearer " + token);
            }
            os = conn.getOutputStream();
            //Used when content type is form-data
            writer = new PrintWriter(new OutputStreamWriter(os, CHAR_SET), true);

            if (parameterHashMap != null) {
                Set<String> keys = parameterHashMap.keySet();
                int count = 1, listSize = parameterHashMap.size();
                boolean lastElement = false;
                for (String key : keys) {
                    if (count == listSize) {
                        lastElement = true;
                    }
                    addFormField(key, parameterHashMap.get(key), lastElement);
                    Log.e("Key", key);
                    Log.e("Value", parameterHashMap.get(key));
                    count++;
                }
            }

            if (file != null) {
                addFilePart("image", file);
            }
            writer.append(LINE_FEED).flush();
            writer.append("--")
                    .append(boundary)
                    .append("--")
                    .append(LINE_FEED);
            writer.close();

//            conn.getRequestProperty("Authorization");
            int responseCode = conn.getResponseCode();
            Log.e("-=-=-=-", "Response Code " + responseCode);
            if(listener != null) listener.getResponseCode(responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) {
                Log.e("Entered", "" + HttpURLConnection.HTTP_OK);
                String line;

                if (tokenNeeded) {
                    String reqProperty = conn.getRequestProperty("Authorization");
                    Log.e("-=-=-=-", "reqProperty: " + reqProperty);
                    String authorization1 = conn.getHeaderField("Authorization");
                    Log.e("ResponsePropertyAuthorization", authorization1);
//                    String authorization = conn.getHeaderField(5);
//                    Log.e("ResponseProperty", authorization);
                    String[] strings = authorization1.split(" ");
                    String bearerToken = strings[1];
                    Log.e("bearerToken", bearerToken);
                    //  Replacing new token into EMPLOYEE_PREFERENCE
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("token", bearerToken);
                    editor.apply();
                }

                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    Log.e("Entered", "" + HttpURLConnection.HTTP_UNAUTHORIZED);
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        response += line;
                    }
                    JSONObject jsonObject1 = new JSONObject(response);
//                    sendMail(parameterHashMap.get("id") + "\n" + fileName, Url, ""+responseCode, response);
                    final String errorMsg = jsonObject1.getString("error");

                    if(!(from.equals("ProfileActivity"))) {
                        ((Activity) context).runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                Toast toast = Toast.makeText(context,
                                        errorMsg,
                                        Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();

                            }

                        });
                    }

                } else if (responseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
                    Log.e("Entered", "" + HttpURLConnection.HTTP_BAD_REQUEST);
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        response += line;
                    }
                    JSONObject jsonObject1 = new JSONObject(response);
//                    sendMail(parameterHashMap.get("id") + "\n" + fileName, Url, ""+responseCode, response);
                    final String errorMsg = jsonObject1.getString("error");
                    if(!(from.equals("ProfileActivity"))) {
                        ((Activity) context).runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                Toast toast = Toast.makeText(context,
                                        errorMsg,
                                        Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();

                            }

                        });
                    }

                } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                    Log.e("Entered", "" + HttpURLConnection.HTTP_NOT_FOUND);
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = br.readLine()) != null){
                        response += line;
                    }
//                    sendMail(parameterHashMap.get("id") + "\n" + fileName, Url, ""+responseCode, response);
                    ((Activity) context).runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast toast = Toast.makeText(context,
                                    "Requested resource not found",
                                    Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();

                        }

                    });
                } else if (responseCode == HttpURLConnection.HTTP_INTERNAL_ERROR) {
                    Log.e("Entered", "" + HttpURLConnection.HTTP_INTERNAL_ERROR);
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = br.readLine()) != null){
                        response += line;
                    }
//                    sendMail(parameterHashMap.get("id") + "\n" + fileName, Url, ""+responseCode, response);
                    ((Activity) context).runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast toast = Toast.makeText(context,
                                    "Something went wrong at server end",
                                    Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }

                    });
                } else {
                    Log.e("Entered", "" + responseCode);
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = br.readLine()) != null){
                        response += line;
                    }
//                    sendMail(parameterHashMap.get("id") + "\n" + fileName, Url, ""+responseCode, response);
                    ((Activity) context).runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            Toast toast = Toast.makeText(context,
                                    "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]",
                                    Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }

                    });
                }
            }
            Log.e("json", response);
            Jobj = new JSONObject(response);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {

            try {

                Log.e("Entered", "" + HttpURLConnection.HTTP_UNAUTHORIZED);
                String line;
                if (!(conn == null)) {

                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                    while ((line = br.readLine()) != null) {
                        response += line;
                    }
                    Log.e("error response", response);
                    JSONObject jsonObject1 = new JSONObject(response);
                    final String errorMsg = jsonObject1.getString("error");

                    if(!(from.equals("ProfileActivity"))) {
                        ((Activity) context).runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                Toast toast = Toast.makeText(context,
                                        errorMsg,
                                        Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();

                            }

                        });
                    }

                    return jsonObject1;

                }

            } catch (Exception e1) {
                e1.printStackTrace();
            }

            e.printStackTrace();
        } catch (JSONException e) {
            Log.e("JSON Parser Service Handler", "Error parsing data " + e.toString());
            //Toast.makeText(SyncDetail.context, "Data Not Inserted", Toast.LENGTH_SHORT ).show();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
        return Jobj;

    }

    @SuppressLint("LongLogTag")
    public SoapObject ServiceSoap() {


        SoapObject request = new SoapObject(mNamespace, mMethodName);
        Set<String> keys = parameterHashMap.keySet();
        for (String key : keys) {
            request.addProperty(key, parameterHashMap.get(key));
            Log.e("Key", key);
            Log.e("Value", parameterHashMap.get(key));
        }
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        envelope.dotNet = true;
//        HttpsURLConnection.setDefaultHostnameVerifier(new JsonServiceHandlerPost.HostVerifier());    //Verifying host
        HttpTransportSE androidHttpTransport = new HttpTransportSE(Url, 120000);

        SoapObject response = null;

        try {
            androidHttpTransport.call(mSoapAction, envelope);
//            Object o = envelope.getResponse();
//            Log.e("-=-=-==-=-=-=-=-", ""+o.toString());
            response = (SoapObject) envelope.getResponse();
            Log.e("-=-=-==rerererererre-=-=-=-=-", ""+response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;

    }

    /**
     * Adds a form field to the request
     *
     * @param name  field name
     * @param value field value
     */
    private void addFormField(String name, String value, boolean lastElement) {
        writer.append("--")
                .append(boundary)
                .append(LINE_FEED)
                .append("Content-Disposition: form-data; name=\"")
                .append(name)
                .append("\"")
                .append(LINE_FEED)
                .append("Content-Type: text/plain; charset=")
                .append(CHAR_SET)
                .append(LINE_FEED)
                .append(LINE_FEED)
                .append(value);
        if (!(lastElement)) writer.append(LINE_FEED);
        writer.flush();
    }

    /**
     * Adds a upload file section to the request
     *
     * @param fieldName  name attribute in <input type="file" name="..." />
     * @param uploadFile a File to be uploaded
     * @throws IOException
     */
    private void addFilePart(String fieldName, File uploadFile)
            throws IOException {
        fileName = uploadFile.getName();
        writer.append("--")
                .append(boundary)
                .append(LINE_FEED)
                .append("Content-Disposition: form-data; name=\"")
                .append(fieldName)
                .append("\"; filename=\"")
                .append(fileName)
                .append("\"")
                .append(LINE_FEED)
                .append("Content-Type: ")
                .append(URLConnection.guessContentTypeFromName(fileName))
                .append(LINE_FEED)
                .append("Content-Transfer-Encoding: binary")
                .append(LINE_FEED)
                .append(LINE_FEED);
        writer.flush();

        FileInputStream inputStream = new FileInputStream(uploadFile);
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        os.flush();
        inputStream.close();

        writer.append(LINE_FEED);
        writer.flush();
    }

    /**
     * Adds a header field to the request.
     *
     * @param name  - name of the header field
     * @param value - value of the header field
     */
    private void addHeaderField(String name, String value) {
        writer.append(name)
                .append(": ")
                .append(value)
                .append(LINE_FEED);
        writer.flush();
    }

    private class HostVerifier implements HostnameVerifier {

        @Override
        public boolean verify(String hostname, SSLSession session) {
            Log.e("HostVerifier", "HOST NAME " + hostname);
            if (hostname.contentEquals("124.7.43.36")) {
                Log.e("HostVerifier", "Approving certificate for host " + hostname);
                return true;
            }
            return false;
        }
    }

    public static void onBindListener(ResponseCodeInterface responseCodeInterface){
        listener = responseCodeInterface;
    }

    public interface ResponseCodeInterface{
        void getResponseCode(int responseCode);
    }

//    private void sendMail(String requestString, String url, String responseCode, String responseString){
//        Util.setReqResPreference(context, Util.REQUEST_STRING, requestString);
//        Util.setReqResPreference(context, Util.REQUEST_URL, url);
//        Util.setReqResPreference(context, Util.RESPONSE_CODE, responseCode);
//        Util.setReqResPreference(context, Util.RESPONSE_STRING, responseString);
//
//        Intent myService = new Intent(context, SendMail.class);
//        context.startService(myService);
//    }

}
