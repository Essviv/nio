package com.cmcc.syw.file;

import java.io.*;

/**
 * Created by sunyiwei on 2016/1/28.
 */
public class ObjectIO implements Serializable {
    private FirstLayerA fla;
    private FirstLayerB flb;

    public ObjectIO(String name) {
        fla = new FirstLayerA(name);
        flb = new FirstLayerB(name);
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        final String filename = "C:\\Users\\Lenovo\\Desktop\\test";
        ObjectOutputStream os = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(filename)));

        ObjectIO oio = new ObjectIO("sunyiwei");
        os.writeObject(oio);
        os.close();

        ObjectInputStream is = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)));
        ObjectIO newOio = (ObjectIO) is.readObject();
        System.out.println(newOio);
        is.close();
    }

    @Override
    public String toString() {
        return "ObjectIO{" +
                "fla=" + fla +
                ", flb=" + flb +
                '}';
    }
}

class FirstLayerA implements Serializable {
    private SecondLayerA sla;
    private String name;

    public FirstLayerA(String name) {
        sla = new SecondLayerA(name);
        this.name = name;
    }

    public SecondLayerA getSla() {
        return sla;
    }

    public void setSla(SecondLayerA sla) {
        this.sla = sla;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "FirstLayerA{" +
                "sla=" + sla +
                ", name='" + name + '\'' +
                '}';
    }
}

class FirstLayerB implements Serializable {
    private SecondLayerB slb;
    private String name;

    public FirstLayerB(String name) {
        slb = new SecondLayerB(name);
        this.name = name;
    }

    public SecondLayerB getSlb() {
        return slb;
    }

    public void setSlb(SecondLayerB slb) {
        this.slb = slb;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "FirstLayerB{" +
                "slb=" + slb +
                ", name='" + name + '\'' +
                '}';
    }
}

class SecondLayerA implements Serializable {
    private String name;

    public SecondLayerA(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "SecondLayerA{" +
                "name='" + name + '\'' +
                '}';
    }
}

class SecondLayerB implements Serializable {
    private String value;

    public SecondLayerB(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "SecondLayerB{" +
                "value='" + value + '\'' +
                '}';
    }
}


