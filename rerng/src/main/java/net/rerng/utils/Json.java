/**
 * 
 */
package net.rerng.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * @author TRI
 *
 */
public class Json {

	/**
	 * Post data to web service
	 * @param url
	 * @param nameValuePairs
	 * @return InputStream
	 */
	public String postData(String url, List<NameValuePair> nameValuePairs)
    {
    	// Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);
        String data = "";
        try {
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));
            //Log.i("CALL SERVICE", convertStreamToString(httppost.getEntity().getContent()));
            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            data = convertStreamToString(response.getEntity().getContent());
            //Log.i("RESP JSON", data);
        } catch (ClientProtocolException e) {
        	Log.d("ClientProtocolException", e.getMessage());
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        	Log.d("IOException", e.getMessage()); 
        }
        
        return data;
    }
	
    /**
     * 
     * @param url
     * @return InputStream
     */
    public String getJSONData(String url){
        DefaultHttpClient httpClient = new DefaultHttpClient();
        URI uri;
        String data = "";
        try {
            uri = new URI(url);
            HttpGet method = new HttpGet(uri);
            HttpResponse response = httpClient.execute(method);
            data = convertStreamToString(response.getEntity().getContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
    
    /**
    *
    * @param is
    * @return String
    */
   public String convertStreamToString(InputStream is) {
       BufferedReader reader = new BufferedReader(new InputStreamReader(is));
       StringBuilder sb = new StringBuilder();

       String line = null;
       try {
           while ((line = reader.readLine()) != null) {
               sb.append(line + "\n");
           }
       } catch (IOException e) {
           e.printStackTrace();
       } finally {
           try {
               is.close();
           } catch (IOException e) {
               e.printStackTrace();
           }
       }
       return sb.toString();
   }
   
  
   /**
    * Get json object by json string
    * @param json string value
    * @return jobject
    * 
    */
   public static JsonObject GetJsonObject(String jsonStr){
   	   JsonElement jelement = new JsonParser().parse(jsonStr);
       JsonObject  jobject = jelement.getAsJsonObject();
       jobject = jobject.getAsJsonObject("response");
       jobject = jobject.getAsJsonObject("entity");
      
       return jobject;
   }
   
   /**
    * Get element value from json string and file value
    * @param json string
    * @param file name
    * 
    */
   public static String GetFieldValueFromJson(String jsonStr, String fieldName, boolean isEntity) {
	   JSONObject json;
	   String result = "";
	   
	  try {
			json = new JSONObject(jsonStr);
			json = json.getJSONObject("response");
			if(isEntity) {
				json = json.getJSONObject("entity");
			}
			result = json.getString(fieldName);
			return result;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
