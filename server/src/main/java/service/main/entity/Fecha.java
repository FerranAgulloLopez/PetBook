package service.main.entity;

import java.util.Calendar;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "domain")
public class Fecha {

    @Id
    private String id;

    private Integer any;
    private Integer mes;
    private Integer dia;
    private Integer hora;

    public Fecha() {
        hora = 0;
        mes = 0;
        dia = 0;
        hora = 0;
        makeId();
    }

    public Fecha(int any, int mes, int dia, Integer hora) {

        this.hora = 0;
        this.dia = 0;
        this.mes = 0;
        this.any = 0;

        this.setHora(hora);
        this.setDia(dia);
        this.setMes(mes);
        this.setAny(any);

        makeId();
    }


    private void makeId() {
        id =" " + dia.toString() + "/" + mes.toString() + "/" + any.toString() + " " +  hora;
    }

    public String getId() { return id; }
    public Integer getDia() { return dia; }
    public Integer getHora() { return hora; }
    public Integer getAny() { return any; }
    public Integer getMes() { return mes; }



    public void setDia(Integer dia) { this.dia = dia; makeId();}
    public void setHora(Integer hora) { this.hora = hora; makeId();}
    public void setMes(Integer mes) { this.mes = mes; makeId(); }
    public void setAny(Integer any) { this.any = any; makeId(); }




}
