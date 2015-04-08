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
    
    private int id;
    private String name;
    private String command;
    private String value;
    
    public Action(int id, String name, String command, String value){
        this.id = id;
        this.name = name;
        this.command = command;
        this.value = value;
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
    
    
}
