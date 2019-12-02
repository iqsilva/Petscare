package br.com.android.queiros.igor.petscare.Bean;

/**
 * Created by igorf on 20/09/2017.
 */

public class Procedimento {
    String id;
    String descricao;
    String observacao;

    public Procedimento() {
    }

    public Procedimento(String id, String descricao, String observacao) {
        this.id = id;
        this.descricao = descricao;
        this.observacao = observacao;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
