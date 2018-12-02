package de.uni.oldenburg.dyspuzzle.dataStructures;

// A copy of the 'Person class'
// This class is needed to make an personData object out of a json object
// After generating a personData object it is possible to safe the values of the object
// in the singelton person instance
public class PersonData {

    // data attributes of the user
    int age;
    String gender;
    String preferredHand;
    boolean dislexia;
    String uniqueId;


    public PersonData() {}

    // Getter and Setter
    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPreferredHand() {
        return this.preferredHand;
    }

    public void setPreferredHand(String preferredHand) {
        this.preferredHand = preferredHand;
    }

    public boolean getDislexia() {
        return dislexia;
    }

    public void setDislexia(boolean dislexia) {
        this.dislexia = dislexia;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }
}

