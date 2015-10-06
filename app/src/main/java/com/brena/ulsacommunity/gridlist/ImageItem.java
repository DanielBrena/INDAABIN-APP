package com.brena.ulsacommunity.gridlist;

/**
 * Created by DanielBrena on 22/09/15.
 */
public class ImageItem {
    private String image;
    private String id;
    private String nombre;
    private String fecha;
    private String descripcion;



    public ImageItem(String id,String image) {
        super();
        this.id = id;
        this.image = image;

    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}