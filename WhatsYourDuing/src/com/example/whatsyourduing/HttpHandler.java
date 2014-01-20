package com.example.whatsyourduing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class HttpHandler {

	public String receiveOnlyPost(String postUrl){
		 
		String responseText = "";
		
		try{
			
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(postUrl);	
			HttpResponse httpResponse = httpClient.execute(httpPost);
			
			//De esta forma se transforma la respuesta a lenguaje entendible para Android
			HttpEntity httpEntity = httpResponse.getEntity();
			responseText = EntityUtils.toString(httpEntity);
		
			return responseText;
			
		}catch(Exception e){
			return e.toString();
		}
	}
	
	/*
	 * Código de Apoyo: http://fahmirahman.wordpress.com/2011/04/26/the-simplest-way-to-post-parameters
	 * 					-between-android-and-php/
	 */
	 public String setAndReceivePost(String postUrl, String wantedService, HashMap<String, String> 
	 																	   paramsForHttpPOST){
		
		String responseText = "";
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(postUrl);
        JSONObject json = new JSONObject();
       
 
        try {
            
        	//datos que se incluyen en el objeto JSON
        	json.put("service", wantedService);
        	if(wantedService.equals("Login")){
        		json.put("username", paramsForHttpPOST.get("username"));
            	json.put("code", paramsForHttpPOST.get("code"));
        	}else if(wantedService.equals("Register")){
        		json.put("username", paramsForHttpPOST.get("username"));
        		json.put("email", paramsForHttpPOST.get("email"));
            	json.put("code", paramsForHttpPOST.get("code"));
        	}
        	        	
            //json.put("name", "Fahmi Rahman");
            //json.put("position", "sysdev");
 
            JSONArray jsonPost = new JSONArray();
            jsonPost.put(json);  //en el JSONArray se incluye el objeto JSON
 
            //se prepara la petición POST con los parámetros ya seteados
            httpPost.setHeader("json", json.toString());
            httpPost.getParams().setParameter("jsonpost", jsonPost);
 
            //se ejecuta la petición POST
            HttpResponse httpResponse = httpClient.execute(httpPost);
 
            //se traduce el objeto JSON que fue retornado a un lenguaje que entiende Android
            if(httpResponse != null){
                InputStream inputStream = httpResponse.getEntity().getContent();
 
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
 
                String line = null;
                try{
                	
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line + "\n");
                    }
                    
                }catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    try {
                    	
                        inputStream.close();
                        
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                responseText = stringBuilder.toString();
            }
 
            return responseText;
 
        }catch (ClientProtocolException e1) {
        	return e1.toString();
        } catch (IOException e) {
        	return e.toString();
        } catch (JSONException e2) {
			return e2.toString();
		}
        
	}
	
}

