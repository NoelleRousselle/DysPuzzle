package de.uni.oldenburg.dyspuzzle.dataStructures;

// Singleton class to safe the information of the user
public class Person {

    // data attributes of the user
    int age;
    String gender;
    String preferredHand;
    boolean dislexia;
    String uniqueId;

    private static Person person = null;

    // a private constructor so no instances can be made outside this class
    private Person() {}

    // Everytime an instance is needed, call this function
    // synchronized to make the call thread-safe
    public static synchronized Person getInstance() {

        if(person == null)
            person = new Person();

        return person;
    }

    // Getter and setter
    public int getAge() {
        return person.age;
    }

    public void setAge(int age) {
        person.age = age;
    }

    public String getGender() {
        return person.gender;
    }

    public void setGender(String gender) {
        person.gender = gender;
    }

    public String getPreferredHand() {
        return person.preferredHand;
    }

    public void setPreferredHand(String preferredHand) {
        person.preferredHand = preferredHand;
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
