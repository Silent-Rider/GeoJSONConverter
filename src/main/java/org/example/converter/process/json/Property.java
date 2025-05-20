package org.example.converter.process.json;

public class Property {

    private String Layer;
    private Object PaperSpace; // может быть null
    private String SubClasses;
    private String Linetype;
    private String EntityHandle;
    private Object Text;

    public Property () {}

    public Object getPaperSpace() {
        return PaperSpace;
    }

    public void setPaperSpace(Object paperSpace) {
        PaperSpace = paperSpace;
    }

    public String getLayer() {
        return Layer;
    }

    public void setLayer(String layer) {
        Layer = layer;
    }

    public String getSubClasses() {
        return SubClasses;
    }

    public void setSubClasses(String subClasses) {
        SubClasses = subClasses;
    }

    public String getLinetype() {
        return Linetype;
    }

    public void setLinetype(String linetype) {
        Linetype = linetype;
    }

    public String getEntityHandle() {
        return EntityHandle;
    }

    public void setEntityHandle(String entityHandle) {
        EntityHandle = entityHandle;
    }

    public Object getText() {
        return Text;
    }

    public void setText(Object text) {
        Text = text;
    }
}
