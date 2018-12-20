import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class Address {
    String name, street, city, state, zip;

    public Address(String name, String street, String city, String state, String zip) {
        this.name = name;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }

    public static Address fromFile(RandomAccessFile raf) {
        try {
            return new Address(FixedLengthStringIO.readFixedLengthString(CommandButton.NAME_SIZE, raf),
                    FixedLengthStringIO.readFixedLengthString(CommandButton.STREET_SIZE, raf),
                    FixedLengthStringIO.readFixedLengthString(CommandButton.CITY_SIZE, raf),
                    FixedLengthStringIO.readFixedLengthString(CommandButton.STATE_SIZE, raf),
                    FixedLengthStringIO.readFixedLengthString(CommandButton.ZIP_SIZE, raf));
        } catch (IOException ex) {
            return null;
        }
    }

    public static ArrayList<Address> arrayFromFile(RandomAccessFile raf) {
        ArrayList<Address> list = new ArrayList<>();
        try {
            raf.seek(0);
            while (raf.getFilePointer() < raf.length())
                list.add(Address.fromFile(raf));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public String getName() {
        return this.name;
    }

    public String getStreet() {
        return this.street;
    }

    public String getCity() {
        return this.city;
    }

    public String getState() {
        return this.state;
    }

    public String getZip() {
        return this.zip;
    }

    public Memento saveStateToMemento(){
        return new Memento(this);
    }

    public static Address getStateFromMemento(Memento memento){
        return memento.getState();
    }

    public static void saveToEndOfFile(RandomAccessFile raf, Address address) {
        try {
            raf.seek(raf.length());
            saveToFile(raf, address);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void saveToFile(RandomAccessFile raf, Address address) {
        try {
            raf.seek(raf.length());
            FixedLengthStringIO.writeFixedLengthString(address.getName(),
                    CommandButton.NAME_SIZE, raf);
            FixedLengthStringIO.writeFixedLengthString(address.getStreet(),
                    CommandButton.STREET_SIZE, raf);
            FixedLengthStringIO.writeFixedLengthString(address.getCity(),
                    CommandButton.CITY_SIZE, raf);
            FixedLengthStringIO.writeFixedLengthString(address.getState(),
                    CommandButton.STATE_SIZE, raf);
            FixedLengthStringIO.writeFixedLengthString(address.getZip(),
                    CommandButton.ZIP_SIZE, raf);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

class Memento {
    private Address state;

    Memento(Address state) {
        this.state = state;
    }

    Address getState() {
        return this.state;
    }
}

class CareTaker {
    private ArrayList<Memento> mementos = new ArrayList<>();
    private int index;

    CareTaker() {
        this.index = mementos.size();
    }

    public void add(Memento state) {
        if (state == null) return;
        this.mementos.add(state);
        index = this.mementos.size() - 1;
    }

    public Memento getNext() {
        if (this.mementos.isEmpty() || index > this.mementos.size() - 1) return null;
        return mementos.get(index++);
    }

    public Memento getPrev() {
        if (this.mementos.isEmpty() || index <= 0) return null;
        return mementos.get(--index);
    }
}

