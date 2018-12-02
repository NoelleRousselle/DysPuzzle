package de.uni.oldenburg.dyspuzzle.layoutGenerator;

public class LayoutDetail {

    private float weight;
    private LayoutCell[] values;

    public LayoutDetail(float weight, LayoutCell[] values) {
        this.weight = weight;
        this.values = values;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public LayoutCell[] getValues() {
        return values;
    }

    public void setValues(LayoutCell[] values) {
        this.values = values;
    }
}
