package com.brena.ulsacommunity.gridlist;

/**
 * Created by DanielBrena on 03/10/15.
 */
public class Comentario {
    private String nombre;
    private String comentario;

    public Comentario(String nombre, String comentario) {
        this.nombre = nombre;
        this.comentario = comentario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
