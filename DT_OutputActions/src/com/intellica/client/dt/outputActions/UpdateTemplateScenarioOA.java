package com.intellica.client.dt.outputActions;

import com.intellica.client.dt.modules.ScenarioStopperModule;
import com.intellica.client.dt.modules.TemplateScenarioModule;
import com.intellica.evam.sdk.outputaction.AbstractOutputAction;
import com.intellica.evam.sdk.outputaction.IOMParameter;
import com.intellica.evam.sdk.outputaction.OutputActionContext;
import com.intellica.evam.sdk.outputaction.model.ReturnParameter;
import com.intellica.evam.sdk.outputaction.model.ReturnType;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Created by intellica on 6/19/2017.
 */
public class UpdateTemplateScenarioOA  extends AbstractOutputAction
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ValidateLocationOA.class);
    public String id="";

    @Override
    public int execute(OutputActionContext outputActionContext) {
        try {
            String scenarioName=(String)outputActionContext.getParameter("scenarioName");
            String templateScenarioName=(String)outputActionContext.getParameter("templateScenarioName");
            String FFjson=(String)outputActionContext.getParameter("jsonFFvalue");
            String[] templateScenarioList=new String[1];
            templateScenarioList[0]=templateScenarioName;
            JSONObject json = new JSONObject(FFjson);
            ScenarioStopperModule stopTheScenario=new ScenarioStopperModule();
            stopTheScenario.run(scenarioName,templateScenarioList);
            TemplateScenarioModule tsm=new TemplateScenarioModule();
            tsm.run(scenarioName,templateScenarioName,json);


            outputActionContext.getReturnMap().put("id", id);
        } catch (Exception e)
        {
            e.printStackTrace();
            return -1;
        }

        return 0;
    }



    @Override
    public ReturnParameter[] getRetParams()
    {
        ReturnParameter[] response = new ReturnParameter[0];

        return response;
    }

    @Override
    protected ArrayList<IOMParameter> getParameters()
    {
        ArrayList<IOMParameter> actionParameters = new ArrayList();
        actionParameters.add(new IOMParameter("jsonFFvalue", "Json FF value"));
        actionParameters.add(new IOMParameter("scenarioName", "Base Scenario Name"));
        actionParameters.add(new IOMParameter("templateScenarioName", "Template Scenario Name"));
        return actionParameters;
    }

    public String getDescription()
    {
        return "update template scenario";
    }

    public boolean isReturnable()
    {
        return false;
    }

    public String getVersion()
    {
        return "v1.0";
    }

}
