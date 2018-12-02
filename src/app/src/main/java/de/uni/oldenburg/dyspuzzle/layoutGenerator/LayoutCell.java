package de.uni.oldenburg.dyspuzzle.layoutGenerator;

public class LayoutCell {

    private CellType type;
    private float weight;

    public LayoutCell(CellType type, float weight){
        this.type = type;
        this.weight = weight;
    }

    public CellType getType() {
        return type;
    }

    public void setType(CellType type) {
        this.type = type;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }
}
