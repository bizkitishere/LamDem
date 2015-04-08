/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for DB table Performance
 * @author Philippe Lüthi, Elia Kocher
 */
public class Performance {
    
    private int id;
    private String name;
    private List<Scenario> scenarios = new ArrayList<>();
    
    public Performance(int id, String name){
        this.id = id;
        this.name = name;
    }

    /**
     * adds a scenario to this performance
     * @param s scenario to add
     */
    public void addScenario(Scenario s){
        scenarios.add(s);
    }
    
    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    
    
    
}
