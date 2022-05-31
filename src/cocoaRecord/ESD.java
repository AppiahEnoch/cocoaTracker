package cocoaRecord;

public class ESD {


    public String getCounter() {
        return counter;
    }

    public void setCounter(String counter) {
        this.counter = counter;
    }

    public String getBag() {
        return bag;
    }

    public ESD setBag(Object bag) {
        this.bag = (String) bag;
        return null;
    }

    public String getKilo() {
        return kilo;
    }

    public ESD setKilo(Object kilo) {
        this.kilo = (String) kilo;
        return null;
    }

    public String getUserTime() {
        return userTime;
    }

    public void setUserTime(String userTime) {
        this.userTime = userTime;
    }

    public ESD(String counter, String userTime, String bag, String kilo) {
        this.counter = counter;
        this.bag = bag;
        this.kilo = kilo;
        this.userTime = userTime;
    }
    String counter="";
    String bag="";
    String kilo="";
    String userTime="";
}
