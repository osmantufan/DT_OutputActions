package com.intellica.client.dt.outputActions;


import com.intellica.evam.sdk.outputaction.AbstractOutputAction;
import com.intellica.evam.sdk.outputaction.IOMParameter;
import com.intellica.evam.sdk.outputaction.OutputActionContext;
import com.intellica.evam.sdk.outputaction.model.ReturnParameter;
import com.intellica.evam.sdk.outputaction.model.ReturnType;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CalculateLocationOA extends AbstractOutputAction {

	private static final Logger LOGGER = LoggerFactory.getLogger(CalculateLocationOA.class);
	
	@Override
	public int execute(OutputActionContext outputActionContext) throws Exception {
		String lat1 = (String) outputActionContext.getParameter("lat1");
		double dlat1 = Double.parseDouble(lat1);

		String lat2 = getLat();
		double dlat2 = Double.parseDouble(lat2);
		String lon1 = (String) outputActionContext.getParameter("lon1");
		double dlon1 = Double.parseDouble(lon1);
		String lon2 =  getLon();
		double dlon2 = Double.parseDouble(lon2);
		
		try {
			
		} catch (Exception e) {
			
		}
		 outputActionContext.getReturnMap().put("valueOut", distance(dlat1, dlon1, dlat2, dlon2, "K"));
		 
		return 0;
	}
	String getLat()
    {
        return "";
    }

    String getLon()
    {
        return "";
    }
	@Override
	public ReturnParameter[] getRetParams()
	  {
	    ReturnParameter[] response = new ReturnParameter[1];
	    response[0] = new ReturnParameter("Distance", ReturnType.Numeric);
	    return response;
	  }
	
	@Override
	protected ArrayList<IOMParameter> getParameters()
	  {
	    ArrayList<IOMParameter> actionParameters = new ArrayList();
	    actionParameters.add(new IOMParameter("lat1", "lat1"));
	    actionParameters.add(new IOMParameter("lon1", "lon1"));
	    return actionParameters;
	  }

	private static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
				+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		if (unit == "K") {
			dist = dist * 1.609344;
		} else if (unit == "N") {
			dist = dist * 0.8684;
		}

		return (dist);
	}

	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	/* :: This function converts decimal degrees to radians : */
	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	/* :: This function converts radians to decimal degrees : */
	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	private static double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}
	
	
	  public String getDescription()
	  {
	    return "Returns age from the given birthdate";
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
