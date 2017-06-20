package com.intellica.client.dt.modules;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.params.CoreProtocolPNames;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.evam.utils.model.view.mdm.CloneRequest;
import com.evam.utils.model.view.mdm.FlexiField;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Iterator;
/**
 * Created by intellica on 6/15/2017.
 */
public class TemplateScenarioModules
{
    private static BufferedReader bufferedReader;
    private String url;
    private String authorizationString;
    private final static String userAgentName = "Intellica Scenario Testing and Management Tools";
    private int index = 1;
    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateScenarioModules.class);

    public boolean sendPutToMdm(String fileName, String scenarioName, String mainScenarioName)
    {

        // URL url = new
        // URL("http://10.115.209.39:8080/v1/scenario/PZT_NOCOND/clone/PZT_NOCOND9");


        CloseableHttpClient httpClient = HttpClients.createDefault();
        StringBuilder uri=new StringBuilder(url);
        uri.append("/v1/scenario/");
        uri.append(mainScenarioName );
        uri.append("/clone/");
        uri.append(scenarioName);
        System.out.println(uri);

        HttpPut httpPut = new HttpPut(String.valueOf(uri));
        System.out.println(url + "/v1/scenario/" + mainScenarioName + "/clone/" + scenarioName);
        httpPut.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, "UTF-8");
        // httpPut.addHeader("Authorization",
        // "evam|6de621355ca07f5478f549e3c1a89563dccf9a041a837daa1dc5248b7210dfc1");
        // httpPut.addHeader("Content-Type", "application/json;charset=UTF-8");
        httpPut.addHeader("Accept", "application/json, text/plain");
        httpPut.addHeader("Accept-Encoding", "gzip, deflate, sdch");
        httpPut.addHeader("Accept-Language", "en-US,en;q=0.8,fr;q=0.6,tr;q=0.4");
        httpPut.addHeader("User-Agent", userAgentName);
        httpPut.addHeader("Content-Type", "application/json;charset=UTF-8");
        httpPut.addHeader("Authorization", this.authorizationString);
        httpPut.addHeader("Host", this.url.replace("http://", "").replace("https://", ""));
        httpPut.addHeader("Referer", this.url + "/index.html");
        httpPut.addHeader("Origin", this.url);
        httpPut.addHeader("Connection", "keep-alive");

        // HttpEntity reqEntity = (HttpEntity)
        // EntityBuilder.create().chunked().setFile(new
        // File("test/payload.json"));
        FileEntity reqEntity = new FileEntity(new File(fileName));

        httpPut.setEntity(reqEntity);

        System.out.println(httpPut);

        CloseableHttpResponse response;
        try {
            LOGGER.info("WEB SERVICE is calling");
            System.out.println("resume sending");
            response = httpClient.execute(httpPut);
            LOGGER.info("Response: "+response.getStatusLine());
            System.out.println("Response: " + response.getStatusLine());
            httpClient.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            LOGGER.info(e.getMessage());
            e.printStackTrace();
            return false;
        }
		/* Print the response */

        return true;
    }

    public boolean sendResumeMessage(String scenarioName) {

        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost(this.url + "/v1/scenario/" + scenarioName + "/resume");
        httpPost.addHeader("Accept", "application/json, text/plain");
        httpPost.addHeader("Accept-Encoding", "gzip, deflate, sdch");
        httpPost.addHeader("Accept-Language", "en-US,en;q=0.8,fr;q=0.6,tr;q=0.4");
        httpPost.addHeader("User-Agent", "leblebi_scenario_manager");
        httpPost.addHeader("Content-Type", "application/json;charset=UTF-8");
        httpPost.addHeader("Authorization", this.authorizationString);
        httpPost.addHeader("Host", this.url.replace("http://", "").replace("https://", ""));
        httpPost.addHeader("Referer", this.url + "/index.html");
        httpPost.addHeader("Origin", this.url);
        httpPost.addHeader("Connection", "keep-alive");

        CloseableHttpResponse response;
        try {
            response = httpClient.execute(httpPost);

            System.out.println("Response: " + response.getStatusLine());
            httpClient.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            LOGGER.info(e.getMessage());
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public void updateProperties(String scenarioName) throws FileNotFoundException, IOException {
        Properties properties = new Properties();
        properties.load(new FileReader(new File("conf/"+scenarioName+".properties")));
        properties.setProperty("BaseTemplateScenarioName", scenarioName);
        properties.setProperty("ScenarioInputFile", scenarioName + ".csv");


        FileOutputStream out = new FileOutputStream("conf/"+scenarioName+".properties");
        properties.store(out, null);
        out.close();

    }

    public void generateScenario(String scenarioName,String templateScenarioName) throws Exception {

        Properties properties = new Properties();
        properties.load(new FileReader(new File("conf/"+scenarioName+".properties")));
        String baseFileName = properties.getProperty("BaseTemplateJsonFile");
        String baseScenarioName = properties.getProperty("BaseTemplateScenarioName");
        String inputFile = properties.getProperty("ScenarioInputFile");
        String mdmUrl = properties.getProperty("MdmUrl");
        String mdmAuthorization = properties.getProperty("MdmAuthoriaztionText");
        int startIndex = Integer.parseInt(properties.getProperty("StartIndex"));

        ObjectMapper objectMapper = new ObjectMapper();

        TemplateScenarioModules templateScenarioGenerator = new TemplateScenarioModules();
        templateScenarioGenerator.setAuthorizationString(mdmAuthorization);
        templateScenarioGenerator.setUrl(mdmUrl);

        CloneRequest cloneRequest = objectMapper.readValue(new File("inputFiles/" + baseFileName + ".json"),
                CloneRequest.class);

        File instanceList = new File("inputFiles/" + inputFile);

        bufferedReader = new BufferedReader(new FileReader(instanceList));
        String line;
        String[] headers = null;
        String[] flex = null;
        int instanceNum = startIndex;
        int count=0;

        while ((line = bufferedReader.readLine()) != null) {

            if (headers == null) {
                headers = line.split(";");
            } else {

                count++;
                if(count<startIndex)
                {
                    continue;
                }
                System.out
                        .println("line " + line.substring(0, 8) + " ... processing with instance number" + instanceNum);
                flex = line.split(";");
                CloneRequest newInstance = new CloneRequest();
                newInstance.setStDate(cloneRequest.getStDate());
                newInstance.setEndDate(cloneRequest.getEndDate());

                List<FlexiField> newFlexiList = new ArrayList<>();

                for (FlexiField flexiField : cloneRequest.getFlexiFields()) {
                    for (int i = 0; i < headers.length; i++) {
                        if (headers[i].equals(flexiField.getFieldName())) {
                            FlexiField ff = new FlexiField();
                            ff.setFieldDesc(flexiField.getFieldDesc());
                            ff.setFieldName(flexiField.getFieldName());
                            ff.setFieldType(flexiField.getFieldType());
                            ff.setFieldTypeData(flexiField.getFieldTypeData());
                            ff.setFieldTypeDataStr(flexiField.getFieldTypeDataStr());
                            ff.setSensitive(flexiField.isSensitive());
                            ff.setValue(flexiField.getValue());
                            ff.setValueType(flexiField.getValueType());
                            if (flex[i] != null && !flex[i].equals("null") && !flex[i].isEmpty()) {
                                ff.setValue(flex[i]);
                            }
                            newFlexiList.add(ff);
                        }
                    }
                }

                newInstance.setFlexiFields(newFlexiList);

                objectMapper.writeValue(new File("inputFiles/" + templateScenarioName + ".json"), newInstance);
                System.out.println("File created with name " + templateScenarioName + ".json");

                if (!templateScenarioGenerator.sendPutToMdm("inputFiles/" + templateScenarioName + ".json",
                        templateScenarioName, baseScenarioName)) {
                    System.err.println("Scenario can't created with name " + templateScenarioName);
                } else {
                    System.out.println("Scenario created with name " + templateScenarioName);
                }

                instanceNum++;
            }

        }

        bufferedReader.close();
        index = startIndex;
        for (int i = 0; i < instanceNum - startIndex; i++) {

            //String templateScenarioName = baseScenarioName + (startIndex + i);
            if (!templateScenarioGenerator.sendResumeMessage(templateScenarioName)) {
                System.err.println("Scenario can't resumed with name " + templateScenarioName);
            } else {
                System.out.println("Scenario resumed with name " + templateScenarioName);
            }
        }
        properties.setProperty("StartIndex", "" + instanceNum);

        FileOutputStream out = new FileOutputStream("conf/"+scenarioName+".properties");
        properties.store(out, null);
        out.close();

    }

    public String readLineFromCsv(String file) throws FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        line = br.readLine();
        return line;
    }

    public void createCsv(String scenarioName, JSONObject json)
            throws FileNotFoundException, IOException, JSONException {

        String FlexyFields = "";
        Iterator<?> keys = json.keys();

        while (keys.hasNext()) {
            String key = (String) keys.next();
            System.out.println(key);
            FlexyFields = FlexyFields + ";" + key;
        }
        FlexyFields = FlexyFields.substring(1);

        try {
            PrintWriter writer = new PrintWriter("inputFiles/" + scenarioName + ".csv", "UTF-8");
            writer.println(FlexyFields);

            writer.close();
        } catch (IOException e) {
            LOGGER.info(e.getMessage());

            // do something
        }

    }

    public void addLineToCsv(String scenarioName, JSONObject json)
            throws JSONException, FileNotFoundException, IOException {
        String csvFile = "inputFiles/" + scenarioName + ".csv";

        Path p = Paths.get(csvFile);
        String FFValuesToCsv = "";
        String[] FFListInCsv = readLineFromCsv(csvFile).split(";");

        for (String string : FFListInCsv) {
            if (!json.isNull(string))
                FFValuesToCsv = FFValuesToCsv + ";" + json.getString(string);
        }

        FFValuesToCsv = (FFValuesToCsv.substring(1)) ;

//        try (OutputStream out = new BufferedOutputStream(Files.newOutputStream(p, StandardOpenOption.APPEND))) {
//            out.write(FFValuesToCsv.getBytes());
//        } catch (IOException e) {
//            System.err.println(e);
//        }

        try(PrintWriter output = new PrintWriter(new FileWriter(csvFile,true)))
        {
            output.printf("%s\r\n", FFValuesToCsv);
        }
        catch (Exception e) {System.err.println(e);}

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
