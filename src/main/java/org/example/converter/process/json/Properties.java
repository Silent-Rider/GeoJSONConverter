package org.example.converter.process.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Properties {

    @JsonProperty("Layer")
    private String layer;

    @JsonProperty("PaperSpace")
    private Object paperSpace;

    @JsonProperty("SubClasses")
    private String subClasses;

    @JsonProperty("Linetype")
    private String linetype;

    @JsonProperty("EntityHandle")
    private String entityHandle;

    @JsonProperty("Text")
    private Object text;

    public Properties() {}

    public Object getText() {
        return text;
    }

    public void setText(Object text) {
        this.text = text;
    }

    public String getLayer() {
        return layer;
    }

    public void setLayer(String layer) {
        this.layer = layer;
    }

    public Object getPaperSpace() {
        return paperSpace;
    }

    public void setPaperSpace(Object paperSpace) {
        this.paperSpace = paperSpace;
    }

    public String getSubClasses() {
        return subClasses;
    }

    public void setSubClasses(String subClasses) {
        this.subClasses = subClasses;
    }

    public String getLinetype() {
        return linetype;
    }

    public void setLinetype(String linetype) {
        this.linetype = linetype;
    }

    public String getEntityHandle() {
        return entityHandle;
    }

    public void setEntityHandle(String entityHandle) {
        this.entityHandle = entityHandle;
    }


}
