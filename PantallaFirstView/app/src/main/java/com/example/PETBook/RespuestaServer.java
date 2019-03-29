package com.example.PETBook;

import org.json.JSONObject;

public class RespuestaServer {
    private JSONObject json;
    private int codigo;


    public RespuestaServer(JSONObject json, int codigo){
        this.json = json;
        this.codigo = codigo;

    }
    public int getCodigo() {
        return codigo;
    }

    public JSONObject getJson() {
        return json;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public void setJson(JSONObject json) {
        this.json = json;
    }
}
