package service.main.entity;

import java.util.Calendar;
import java.util.GregorianCalendar;


public class Fecha {

    private Calendar dia;
    private int hora;

    public Fecha() {
        hora = 0;
        dia = new GregorianCalendar(2019, 1, 1);
    }

    public Fecha(int any, int mes, int dia, int hora) {
        setHora(hora);
        setDia(new GregorianCalendar(any, mes, dia) );
    }

    public Calendar getDia() { return dia; }
    public int getHora() { return hora; }

    public void setDia(Calendar dia) { this.dia = dia; }
    public void setHora(int hora) { this.hora = hora; }
}
