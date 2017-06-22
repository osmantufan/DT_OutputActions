package com.intellica.client.dt.modules;


import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class ScenarioStopperModule {

    private String url;
    private String authorizationString;
    private final static String userAgentName = "Intellica Scenario Testing and Management Tools" ;

    public boolean sendResumeMessage( String scenarioName ){

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpDelete httpDelete = new HttpDelete(this.url+"/v1/scenario/"+scenarioName);
        httpDelete.addHeader("Accept", "application/json, text/plain");
        httpDelete.addHeader("Accept-Encoding", "gzip, deflate, sdch");
        httpDelete.addHeader("Accept-Language", "en-US,en;q=0.8,fr;q=0.6,tr;q=0.4");
        httpDelete.addHeader("User-Agent", ScenarioStopperModule.userAgentName );
        httpDelete.addHeader("Content-Type", "application/json;charset=UTF-8");
        httpDelete.addHeader("Authorization", authorizationString);
        httpDelete.addHeader("Host", this.url.replace("http://","").replace("https://",""));
        httpDelete.addHeader("Referer", this.url+"/index.html");
        httpDelete.addHeader("Origin", this.url );
        httpDelete.addHeader("Connection", "keep-alive");

        CloseableHttpResponse response;
        try {
            response = httpClient.execute(httpDelete);

            System.out.println("Response: " + response.getStatusLine() );
            httpClient.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }


        return true;
    }
    public void run(String scenarioName,String[] args) throws Exception {

        Properties properties = new Properties();
        properties.load( new FileReader(new File("conf/"+scenarioName+".properties")) );
        String baseScenarioName = scenarioName;
        int startIndex = Integer.parseInt(properties.getProperty("StartIndex"));
        String mdmUrl = properties.getProperty("MdmUrl");
        String mdmAuthorization = properties.getProperty("MdmAuthoriaztionText");
        ScenarioStopperModule scenarioStopperModule = new ScenarioStopperModule();
        scenarioStopperModule.setAuthorizationString(mdmAuthorization);
        scenarioStopperModule.setUrl(mdmUrl);

        if( args.length == 0 ){
            return;
        }


        for (String scenarios:args)
        {
            System.err.println( "Scenario will deleted that named "+scenarios );
            scenarioStopperModule.sendResumeMessage(scenarios);
            System.err.println( "Scenario deleted that named "+scenarios );
        }




    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAuthorizationString() {
        return authorizationString;
    }

    public void setAuthorizationString(String authorizationString) {
        this.authorizationString = authorizationString;
    }

}
