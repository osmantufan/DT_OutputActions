package com.intellica.client.dt.outputActions;

import com.intellica.evam.sdk.outputaction.AbstractOutputAction;
import com.intellica.evam.sdk.outputaction.IOMParameter;
import com.intellica.evam.sdk.outputaction.OutputActionContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by osman on 6/8/2017.
 */
public class DT_POC_InsertOA extends AbstractOutputAction {


    @Override
    public int execute(OutputActionContext outputActionContext) throws Exception {
       String to= (String)outputActionContext.getParameter("To");
       String text= (String)outputActionContext.getParameter("Text");


        Connection con = null;
        PreparedStatement pst = null;
        String url = "jdbc:postgresql://10.115.210.26:5432/EVAMTESTDB219";
        String user = "evamtest";
        String password = "evamtest123";
        //cihad

        Class.forName("org.postgresql.Driver");
        con = DriverManager.getConnection(url, user, password);
        String stm = "INSERT INTO fraudscenariosentmail(mail_to, text) VALUES(?, ?)";
        pst = con.prepareStatement(stm);

        pst.setString(1,to);
        pst.setString(2,text);

        pst.executeUpdate();

        return 0;
    }

    @Override
    protected ArrayList<IOMParameter> getParameters() {
        ArrayList<IOMParameter> params = new ArrayList();
        params.add(new IOMParameter("To", "TO"));
        params.add(new IOMParameter("Text", "Body"));
        return params;
    }

    @Override
    public String getVersion() {
        return null;
    }

//    public static void main(String[] args)
//  {
//    OutputActionContext arg0 = new OutputActionContext();
//    arg0.setReturnMap(new HashMap<String,Object>());
//    arg0.setParameter("To", "osman.erenay@intellica.net");
//    arg0.setParameter("Text", "HELLO");
//
//    try
//    {
//      new DT_POC_InsertOA().execute(arg0);
//    }
//    catch (Exception e)
//    {
//      e.printStackTrace();
//    }
//  }
}
