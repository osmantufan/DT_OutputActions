package com.intellica.client.dt.outputActions;


import com.intellica.client.dt.exceptions.IllegalOutputActionParameterException;
import com.intellica.evam.sdk.outputaction.AbstractOutputAction;
import com.intellica.evam.sdk.outputaction.IOMParameter;
import com.intellica.evam.sdk.outputaction.OutputActionContext;
import com.intellica.evam.sdk.outputaction.model.ReturnParameter;
import com.intellica.evam.sdk.outputaction.model.ReturnType;
import java.util.ArrayList;
import org.joda.time.Days;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateToDayOA
  extends AbstractOutputAction
{
  private static final Logger LOGGER = LoggerFactory.getLogger(DateToDayOA.class);
  private static final String RETURN_VARIABLE_NAME = "day";
  private static final String DATE = "Date";
  private static final DateTimeFormatter FORMATTER = DateTimeFormat.forPattern("dd.MM.yyyy HH:mm:ss");
  
  public int execute(OutputActionContext outputActionContext)
    throws Exception
  {
    String date = (String)outputActionContext.getParameter("Date");
    checkDate(date);
    try
    {
      int resultValue = getDay(date);
      outputActionContext.getReturnMap().put("day", Integer.valueOf(resultValue));
      LOGGER.debug("Result: " + resultValue);
      outputActionContext.setMessage("Result: " + resultValue);
    }
    catch (Exception e)
    {
      LOGGER.error("General exception while processing request ", e);
      return -1;
    }
    return 0;
  }
  
  private static void checkDate(String date)
  {
    if ((date == null) || (date.isEmpty()) || ("null".equalsIgnoreCase(date))) {
      throw new IllegalOutputActionParameterException("Date parameter cannot be empty.");
    }
  }
  
  private static int getDay(String date)
  {
	  LOGGER.info("*******************************************DateToDayOA******************************************"+date);
    LocalDateTime localDate = FORMATTER.parseLocalDateTime(date);
    LocalDateTime now = new LocalDateTime();
    return Days.daysBetween(now,localDate).getDays();
  }
  
  public ReturnParameter[] getRetParams()
  {
    ReturnParameter[] response = new ReturnParameter[1];
    response[0] = new ReturnParameter("day", ReturnType.Numeric);
    return response;
  }
  
  protected ArrayList<IOMParameter> getParameters()
  {
    ArrayList<IOMParameter> actionParameters = new ArrayList();
    actionParameters.add(new IOMParameter("Date", "Date"));
    return actionParameters;
  }
  
  public String getDescription()
  {
    return "Returns days passed since the given date (dd.mm.yyyy HH:mm:ss)";
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
