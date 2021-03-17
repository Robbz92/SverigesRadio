package com.example.demo.configs;

public class GenericObject {

    private Object id;
    private Object name;
    private Object param3;
    private Object param4;
    private Object param5;
    private Object param6;

    public GenericObject(Object id, Object name) {
        this.id = id;
        this.name = name;
    }
    // Konstruktor för radiokanaler
    public GenericObject(Object id, Object name, Object param3, Object param4, Object param5, Object param6) {
        this.id = id;
        this.name = name;
        this.param3 = param3;
        this.param4 = param4;
        this.param5 = param5;
        this.param6 = param6;
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public Object getName() {
        return name;
    }

    public void setName(Object name) {
        this.name = name;
    }

    public Object getParam3() {
        return param3;
    }

    public void setParam3(Object param3) {
        this.param3 = param3;
    }

    public Object getParam4() {
        return param4;
    }

    public void setParam4(Object param4) {
        this.param4 = param4;
    }

    public Object getParam5() {
        return param5;
    }

    public void setParam5(Object param5) {
        this.param5 = param5;
    }

    public Object getParam6() {
        return param6;
    }

    public void setParam6(Object param6) {
        this.param6 = param6;
    }

}
