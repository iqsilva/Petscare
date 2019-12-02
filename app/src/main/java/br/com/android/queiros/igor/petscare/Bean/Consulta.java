package br.com.android.queiros.igor.petscare.Bean;

/**
 * Created by igorf on 19/09/2017.
 */

public class Consulta {
     String consultaId;
     String consultaName;
     String consultaStatus;
     String consultaState;

    public Consulta(){

    }

    public Consulta(String consultaId, String consultaName, String consultaStatus, String consultaState) {
        this.consultaId = consultaId;
        this.consultaName = consultaName;
        this.consultaStatus = consultaStatus;
        this.consultaState = consultaState;
    }

    public String getConsultaId() {
        return consultaId;
    }

    public String getConsultaName() {
        return consultaName;
    }

    public String getConsultaStatus() {
        return consultaStatus;
    }

    public String getConsultaState() {
        return consultaState;
    }
}

