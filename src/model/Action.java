/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 * Class for DB table Action
 * @author Philippe Lüthi, Elia Kocher
 */
public class Action {
    
    //private int id;
    private final String name;
    private final String command;
    private final String value;
    private final int delay;
    private final int type;
    
    public Action(String name, String command, String value, int delay, int type){
        //this.id = id;
        this.name = name;
        this.command = command;
        this.value = value;
        this.delay = delay;
        this.type = type;
    }

    /**
     * @return the id
     */
    /*
    public int getId() {
        return id;
    }
    */

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the command
     */
    public String getCommand() {
        return command;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }
    
    /**
     * @return the delay
     */
    public int getDelay() {
        return delay;
    }
    
    /**
     * @return the type
     */
    public int getType() {
        return type;
    }
    
}
