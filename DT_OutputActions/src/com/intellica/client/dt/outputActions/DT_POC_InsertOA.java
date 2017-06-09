package com.intellica.client.dt.outputActions;

import com.intellica.evam.sdk.outputaction.AbstractOutputAction;
import com.intellica.evam.sdk.outputaction.IOMParameter;
import com.intellica.evam.sdk.outputaction.OutputActionContext;

import java.sql.*;
import java.util.ArrayList;

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

        con = DriverManager.getConnection(url, user, password);
        String stm = "INSERT INTO fraudscenariosentmail(to, text) VALUES(?, ?)";
        pst = con.prepareStatement(stm);

        pst.setString(1,to);
        pst.setString(2,text);

        pst.executeUpdate();

        return 0;
    }

    @Override
    protected ArrayList<IOMParameter> getParameters() {
        return null;
    }

    @Override
    public String getVersion() {
        return null;
    }
}
