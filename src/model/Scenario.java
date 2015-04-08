/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for DB table Scenario
 * @author Philippe Lüthi, Elia Kocher
 */
public class Scenario {
    
    private int id;
    private boolean enter;
    private List<Action> actions = new ArrayList<>();
    private List<Region> regions = new ArrayList<>();
    private List<Role> roles = new ArrayList<>();
    
    
    public Scenario(int id, boolean ent){
        this.id = id;
        this.enter = ent;
    }

    /**
     * add an action to this scenario
     * @param a action to add
     */
    public void addAction(Action a){
        actions.add(a);
    }
    
    /**
     * add a region to this scenario
     * @param r region to add
     */
    public void addRegion(Region r){
        regions.add(r);
    }
    
    /**
     * add a role to this scenario
     * @param r role to add
     */
    public void addRole(Role r){
        roles.add(r);
    }
    
    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the enter
     */
    public boolean isEnter() {
        return enter;
    }
    
    
    
}
