package com.intellica.client.dt.outputActions;


import com.intellica.evam.sdk.outputaction.AbstractOutputAction;
import com.intellica.evam.sdk.outputaction.IOMParameter;
import com.intellica.evam.sdk.outputaction.OutputActionContext;
import com.intellica.evam.sdk.outputaction.model.ReturnParameter;
import com.intellica.evam.sdk.outputaction.model.ReturnType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValidateLocationOA extends AbstractOutputAction {

	private static final Logger LOGGER = LoggerFactory.getLogger(ValidateLocationOA.class);
	public String id="";

	@Override
	public int execute(OutputActionContext outputActionContext) throws Exception {
		int delimeter = Integer.parseInt((String) outputActionContext.getParameter("delimeter"));

		String lat1 = (String) outputActionContext.getParameter("lat1");
		double dlat1 = Double.parseDouble(lat1);

		String lon1 = (String) outputActionContext.getParameter("lon1");
		double dlon1 = Double.parseDouble(lon1);


		outputActionContext.getReturnMap().put("valueOut", distance(dlat1, dlon1, "K",delimeter));
		outputActionContext.getReturnMap().put("id", id);

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
		actionParameters.add(new IOMParameter("lat1", "lat1"));
		actionParameters.add(new IOMParameter("lon1", "lon1"));
		actionParameters.add(new IOMParameter("delimeter", "delimeter"));

		return actionParameters;
	}

	private boolean distance(double lat1, double lon1, String unit, int delimeter ) throws IOException {

		String FILENAME = "conf/locations.txt";
		Double lon2,lat2;
		BufferedReader br = null;
		FileReader fr = null;




			fr = new FileReader(FILENAME);
			br = new BufferedReader(fr);

			String sCurrentLine;

			br = new BufferedReader(new FileReader(FILENAME));
			int index = 0;

			while ((sCurrentLine = br.readLine()) != null)
			{
				index++;
				sCurrentLine=sCurrentLine+","+index;

				String[] kofteciLokasyonu = sCurrentLine.split(",");
				lon2 = Double.parseDouble(kofteciLokasyonu[1]);
				lat2 = Double.parseDouble(kofteciLokasyonu[0]);
				id=kofteciLokasyonu[2];

				double distCalc= distanceCalculate(lat2,lon2,lat1,lon1);
				if(distCalc<=delimeter)
				{
					System.out.println(id);
					return true;
				}

			}
		return false;
	}


	double distanceCalculate(double fromLat, double fromLon, double toLat, double toLon) {
		double radius = 6378137;   // approximate Earth radius, *in meters*
		double deltaLat = toLat - fromLat;
		double deltaLon = toLon - fromLon;
		double angle = 2 * Math.asin( Math.sqrt(
				Math.pow(Math.sin(deltaLat/2), 2) +
						Math.cos(fromLat) * Math.cos(toLat) *
								Math.pow(Math.sin(deltaLon/2), 2) ) );
		return radius * angle;
	}

	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	/* :: This function converts decimal degrees to radians : */
	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	private double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	/* :: This function converts radians to decimal degrees : */
	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	private double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
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

//   public static void main(String[] args)
//	{
//		OutputActionContext arg0 = new OutputActionContext();
//		arg0.setReturnMap(new HashMap<String,Object>());
//		arg0.setParameter("delimeter", "10000");
//		arg0.setParameter("lat1", "-25.274");
//		arg0.setParameter("lon1", "133.775");
//
//
//		try
//		{
//			new ValidateLocationOA().execute(arg0);
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

}
