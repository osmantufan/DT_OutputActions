package com.intellica.client.dt.outputActions;

import com.intellica.evam.sdk.outputaction.AbstractOutputAction;
import com.intellica.evam.sdk.outputaction.IOMParameter;
import com.intellica.evam.sdk.outputaction.OutputActionContext;
import com.intellica.evam.sdk.outputaction.model.ReturnParameter;
import com.intellica.evam.sdk.outputaction.model.ReturnType;
import java.util.ArrayList;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FloorOA
  extends AbstractOutputAction
{
  private static final Logger LOGGER = LoggerFactory.getLogger(FloorOA.class);
  private static final String RETURN_VARIABLE_NAME = "valueOut";
  public static final String PARAM_NAME = "value";
  
  public int execute(OutputActionContext outputActionContext)
    throws Exception
  {
	  
	    LOGGER.info("*******************************************FloorOA******************************************EXECUTE");
    double variable = 0.0D;
    try
    {
      variable = Double.parseDouble((String)outputActionContext.getParameter("value"));
    }
    catch (Exception e)
    {
      LOGGER.error("Can't get parameter to floor. parameter: [" + outputActionContext
        .getParameter("value") + "]." + e);
      
      throw e;
    }
    outputActionContext.getReturnMap().put("valueOut", Double.valueOf(Math.floor(variable)));
    
    return 0;
  }
  
  public ArrayList<IOMParameter> getParameters()
  {
    ArrayList<IOMParameter> actionParams = new ArrayList();
    actionParams.add(new IOMParameter("value", "Value to floor"));
    return actionParams;
  }
  
  public ReturnParameter[] getRetParams()
  {
    ReturnParameter[] response = new ReturnParameter[1];

    response[0] = new ReturnParameter("valueOut", ReturnType.Numeric);
    return response;
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
