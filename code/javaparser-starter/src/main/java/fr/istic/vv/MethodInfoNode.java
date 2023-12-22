package fr.istic.vv;

import java.util.ArrayList;
/**
 * Store in a tree the informations for a package, class, method, complexity...
 */
public class MethodInfoNode{
    //private EnumTypeInfo type = EnumTypeInfo.ROOT; //type of information (exemple package)
    private String value = "none"; // default value
    private ArrayList<MethodInfoNode> childs;
    private int nbChilds = 0;

    /**
     * create node with on value
     */
    public MethodInfoNode() {
        childs = new ArrayList<>();
    }

    /**
     * create node with on value
     * @param value of the node
     */
    public MethodInfoNode(String value) {//EnumTypeInfo typeInfo, 
        this.value = value;
        //this.type = typeInfo;
        childs = new ArrayList<>();
    }
    
    public String getValue() {
        return value;
    }

    public void addChild(MethodInfoNode m) {
        childs.add(m);
        nbChilds++;
    }

    public MethodInfoNode getChild(int i) {
        if (nbChilds <= 0)
            return null;
        return childs.get(i);
    }

    public ArrayList<MethodInfoNode> getAllChilds() {
        if (nbChilds <= 0)
            return null;
        return childs;
    }

    public int getNbChilds() {
        return nbChilds;
    }

}
