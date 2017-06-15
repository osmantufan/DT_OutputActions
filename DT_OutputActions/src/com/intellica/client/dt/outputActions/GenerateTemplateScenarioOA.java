package com.intellica.client.dt.outputActions;

import com.intellica.client.dt.modules.TemplateScenarioModules;
import com.intellica.evam.sdk.outputaction.AbstractOutputAction;
import com.intellica.evam.sdk.outputaction.IOMParameter;
import com.intellica.evam.sdk.outputaction.OutputActionContext;
import com.intellica.evam.sdk.outputaction.model.ReturnParameter;
import com.intellica.evam.sdk.outputaction.model.ReturnType;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by intellica on 6/15/2017.
 */
public class GenerateTemplateScenarioOA extends AbstractOutputAction
{



    private static final Logger LOGGER = LoggerFactory.getLogger(GenerateTemplateScenarioOA.class);
    public String id="";

    @Override
    public int execute(OutputActionContext outputActionContext) throws Exception
    {
        try {
            String FFjson = (String) outputActionContext.getParameter("jsonFFvalue");
            String scenarioName = (String) outputActionContext.getParameter("scenarioName");
            JSONObject json = new JSONObject(FFjson);
            TemplateScenarioModules tsm=new TemplateScenarioModules();
            tsm.updateProperties(scenarioName);
            tsm.createCsv(scenarioName, json);
            tsm.addLineToCsv(scenarioName, json);
            tsm.generateScenario();
        } catch (Exception e)
        {
            return -1;

        }
        return 0;
    }



    @Override
    public ReturnParameter[] getRetParams()
    {
        ReturnParameter[] response = new ReturnParameter[2];
        response[0] = new ReturnParameter("valueOut", ReturnType.String);
        response[1] = new ReturnParameter("id", ReturnType.String);
        return response;
    }

    @Override
    protected ArrayList<IOMParameter> getParameters()
    {
        ArrayList<IOMParameter> actionParameters = new ArrayList();
        actionParameters.add(new IOMParameter("jsonFFvalue", "Json FF value"));
        actionParameters.add(new IOMParameter("scenarioName", "Template Scenario Name"));

        return actionParameters;
    }
    public static void main(String[] args)
    {
        OutputActionContext arg0 = new OutputActionContext();
        arg0.setReturnMap(new HashMap<String,Object>());
        arg0.setParameter("jsonFFvalue", "{\"ad\":\"cihad\",\"cinsiyet\":\"M\",\"id\":\"123\",\"soyad\":\"yildiz\"}");
        arg0.setParameter("scenarioName", "dummtTemplate");



        try
        {
            new GenerateTemplateScenarioOA().execute(arg0);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }




    public String getDescription()
    {
        return "returns whether the location of the truck is accaptable or not";
    }

    public boolean isReturnable()
    {
        return true;
    }

    public String getVersion()
    {
        return "v1.0";
    }
}
