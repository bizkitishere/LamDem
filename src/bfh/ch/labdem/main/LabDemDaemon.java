/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bfh.ch.labdem.main;

import bfh.ch.labdem.helper.DB;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Action;

/**
 *
 * @author Philippe Lüthi, Elia Kocher
 */
public class LabDemDaemon implements Runnable{

    @Override
    public void run() {
        
        
        
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
    public void getActions(int performanceId, int regionId, int roleId, boolean enter){
        
        List<Action> actions = DB.getActions(performanceId, regionId, roleId, enter);
        
        

    }
    
}
