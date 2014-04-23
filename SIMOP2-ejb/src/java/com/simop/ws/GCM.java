/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.simop.ws;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 53B45
 */
public class GCM {
    
    public static boolean send(String key, String regId, String msg){
        
        try {
            URL url = new URL("https://android.googleapis.com/gcm/send");
            
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Authorization", "key=AIzaSyAOyF6k6jSvPAR7HYZsHxVrC_ku_RHJTbo");
            conn.setDoOutput(true);
            
            String body = "collapse_key=" + key + "&registration_id=" + regId + "&data.msg=" + msg;
            
            conn.setRequestProperty("Content-Length", Integer.toString(body.length()));
            conn.getOutputStream().write(body.getBytes("UTF8"));
            
            int responseCode = conn.getResponseCode();
            System.out.println("response code: " + responseCode);
            
            if(responseCode == HttpURLConnection.HTTP_OK){
                
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while((inputLine = in.readLine()) != null){
                    response.append(inputLine);
                }

                in.close();

                System.out.println("Response: " + response.toString());
            
                return true;
            }
            
        } catch (IOException ex) {
            Logger.getLogger(GCM.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
}
