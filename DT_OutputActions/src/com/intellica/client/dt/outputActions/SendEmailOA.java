package com.intellica.client.dt.outputActions;

import com.intellica.client.dt.exceptions.IllegalOutputActionParameterException;
import com.intellica.evam.sdk.outputaction.AbstractOutputAction;
import com.intellica.evam.sdk.outputaction.IOMParameter;
import com.intellica.evam.sdk.outputaction.OutputActionContext;
import com.intellica.evam.sdk.outputaction.model.ReturnParameter;
import com.intellica.evam.sdk.outputaction.model.ReturnType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Created by intellica on 6/9/2017.
 */
public class SendEmailOA extends AbstractOutputAction
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DateToDayOA.class);


    public int execute(OutputActionContext outputActionContext)
            throws Exception
    {
        String mail = (String)outputActionContext.getParameter("mail");
        String text = (String)outputActionContext.getParameter("text");
        System.out.println(mail+"---"+text);


        return 0;
    }

    private static void checkDate(String date)
    {
        if ((date == null) || (date.isEmpty()) || ("null".equalsIgnoreCase(date))) {
            throw new IllegalOutputActionParameterException("mail parameter cannot be empty.");
        }
    }



    public ReturnParameter[] getRetParams()
    {
        ReturnParameter[] response = new ReturnParameter[1];
        //response[0] = new ReturnParameter("day", ReturnType.Numeric);
        return response;
    }

    protected ArrayList<IOMParameter> getParameters()
    {
        ArrayList<IOMParameter> actionParameters = new ArrayList();
        actionParameters.add(new IOMParameter("mail", "mail"));
        actionParameters.add(new IOMParameter("text", "text"));
        return actionParameters;
    }

    public String getDescription()
    {
        return "SendSms";
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