package com.intellica.client.dt.outputActions;

import com.intellica.client.dt.modules.ScenarioStopper;
import com.intellica.evam.sdk.outputaction.AbstractOutputAction;
import com.intellica.evam.sdk.outputaction.IOMParameter;
import com.intellica.evam.sdk.outputaction.OutputActionContext;
import com.intellica.evam.sdk.outputaction.model.ReturnParameter;
import com.intellica.evam.sdk.outputaction.model.ReturnType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by intellica on 6/19/2017.
 */
public class StopScenarioOA extends AbstractOutputAction
{


    private static final Logger LOGGER = LoggerFactory.getLogger(ValidateLocationOA.class);
    public String id="";

    @Override
    public int execute(OutputActionContext outputActionContext) {
        try {
            String scenarioName=(String)outputActionContext.getParameter("scenarioName");
            String[] scenarioList=((String)outputActionContext.getParameter("templateScenarioList")).split(";");

            ScenarioStopper stopTheScenario=new ScenarioStopper();
            stopTheScenario.run(scenarioName,scenarioList);


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
        ReturnParameter[] response = new ReturnParameter[2];
        response[0] = new ReturnParameter("scenarioName", ReturnType.String);
        response[1] = new ReturnParameter("templateScenarioList", ReturnType.String);
        return response;
    }

    @Override
    protected ArrayList<IOMParameter> getParameters()
    {
        ArrayList<IOMParameter> actionParameters = new ArrayList();
        return actionParameters;
    }

    public String getDescription()
    {
        return "returns whether the location of the truck is accaptable or not";
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
