package br.udesc.ceavi.ppr.haruichiban.servidor.control.channel;

import com.google.gson.Gson;

/**
 *
 * @author Gustavo C
 */
public class CommunicationPackage {

    private ModelGet get;
    private ModelPost post;
    private String parametro;

    public CommunicationPackage() {
    }

    public void addGet(ModelGet get, String parametro) {
        this.get = get;
        this.post = null;
        this.parametro = parametro;
    }

    public void addPost(ModelPost post, String parametro) {
        this.get = null;
        this.post = post;
        this.parametro = parametro;
    }

    public void addGet(ModelGet get, Object parametro) {
        this.get = get;
        this.post = null;
        this.parametro = new Gson().toJson(parametro);
    }

    public void addPost(ModelPost post, Object parametro) {
        this.get = null;
        this.post = post;
        this.parametro = new Gson().toJson(parametro);
    }

    public String getParametro() {
        return parametro;
    }

    public ModelPost getModelPost() {
        return post;
    }

    public ModelGet getModelGet() {
        return get;
    }

    public boolean isGet() {
        return get != null;
    }

    public boolean isPost() {
        return post != null;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

}
