import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import com.sun.tools.javac.Main;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class HW2_AvivFox extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        AddressBookPane pane = AddressBookPane.getInstance();
        Scene scene = new Scene(pane);
        scene.getStylesheets().add("styles.css");
        primaryStage.setTitle("AddressBook");
        primaryStage.setScene(scene);
        primaryStage.show();
        //primaryStage.setAlwaysOnTop(true);
        for (int i = 0; i < AddressBookPane.MAX_PANES; i++) {
            Stage stage = new Stage();
            AddressBookPane addressBookPane = AddressBookPane.getInstance();
            if (addressBookPane == null) break;
            Scene sc = new Scene(addressBookPane);
            sc.getStylesheets().add("styles.css");
            stage.setTitle("AddressBook");
            stage.setScene(sc);
            stage.show();
        }
    }
}

class AddressBookPane extends GridPane {
    protected RandomAccessFile raf;
    // Text fields
    private TextField jtfName = new TextField();
    private TextField jtfStreet = new TextField();
    private TextField jtfCity = new TextField();
    private TextField jtfState = new TextField();
    private TextField jtfZip = new TextField();
    // Buttons
    private FlowPane jpButton;
    private AddButton jbtAdd;
    private FirstButton jbtFirst;
    private NextButton jbtNext;
    private PreviousButton jbtPrevious;
    private LastButton jbtLast;
    public EventHandler<ActionEvent> ae =
            new EventHandler<ActionEvent>() {
                public void handle(ActionEvent arg0) {
                    ((Command) arg0.getSource()).Execute();
                }
            };

    private static int created = 0;
    public static final int MAX_PANES = 3;
    private static RandomAccessFile randomAccessFile = null;

    public static AddressBookPane getInstance() {
        if (randomAccessFile == null)
            try {
                randomAccessFile = new RandomAccessFile("address.dat", "rw");
            } catch (IOException ex) {
                System.out.print("Error: " + ex);
                System.exit(0);
            }
        if (created >= MAX_PANES) {
            System.out.println(String.format("Singelton violation. Only %d panes were created ", MAX_PANES));
            return null;
        }
        created++;
        if (created == 1)
            return new MainPaneDecorator(new AddressBookPane(randomAccessFile)).getPane();
        else
            return new PaneDecorator(new AddressBookPane(randomAccessFile)).getPane();
    }

    private AddressBookPane(RandomAccessFile raf) { // Open or create a random access file
        this.raf = raf;
        jtfState.setAlignment(Pos.CENTER_LEFT);
        jtfState.setPrefWidth(80);
        jtfZip.setPrefWidth(60);
        jbtFirst = new FirstButton(this, raf);
        jbtNext = new NextButton(this, raf);
        jbtPrevious = new PreviousButton(this, raf);
        jbtLast = new LastButton(this, raf);
        Label state = new Label("State");
        Label zp = new Label("Zip");
        Label name = new Label("Name");
        Label street = new Label("Street");
        Label city = new Label("City");
        // Panel p1 for holding labels Name, Street, and City
        GridPane p1 = new GridPane();
        p1.add(name, 0, 0);
        p1.add(street, 0, 1);
        p1.add(city, 0, 2);
        p1.setAlignment(Pos.CENTER_LEFT);
        p1.setVgap(8);
        p1.setPadding(new Insets(0, 2, 0, 2));
        GridPane.setVgrow(name, Priority.ALWAYS);
        GridPane.setVgrow(street, Priority.ALWAYS);
        GridPane.setVgrow(city, Priority.ALWAYS);
        // City Row
        GridPane adP = new GridPane();
        adP.add(jtfCity, 0, 0);
        adP.add(state, 1, 0);
        adP.add(jtfState, 2, 0);
        adP.add(zp, 3, 0);
        adP.add(jtfZip, 4, 0);
        adP.setAlignment(Pos.CENTER_LEFT);
        adP.setHgap(8);
        GridPane.setHgrow(jtfCity, Priority.ALWAYS);
        GridPane.setVgrow(jtfCity, Priority.ALWAYS);
        GridPane.setVgrow(jtfState, Priority.ALWAYS);
        GridPane.setVgrow(jtfZip, Priority.ALWAYS);
        GridPane.setVgrow(state, Priority.ALWAYS);
        GridPane.setVgrow(zp, Priority.ALWAYS);
        // Panel p4 for holding jtfName, jtfStreet, and p3
        GridPane p4 = new GridPane();
        p4.add(jtfName, 0, 0);
        p4.add(jtfStreet, 0, 1);
        p4.add(adP, 0, 2);
        p4.setVgap(1);
        GridPane.setHgrow(jtfName, Priority.ALWAYS);
        GridPane.setHgrow(jtfStreet, Priority.ALWAYS);
        GridPane.setHgrow(adP, Priority.ALWAYS);
        GridPane.setVgrow(jtfName, Priority.ALWAYS);
        GridPane.setVgrow(jtfStreet, Priority.ALWAYS);
        GridPane.setVgrow(adP, Priority.ALWAYS);
        // Place p1 and p4 into jpAddress
        GridPane jpAddress = new GridPane();
        jpAddress.add(p1, 0, 0);
        jpAddress.add(p4, 1, 0);
        GridPane.setHgrow(p1, Priority.NEVER);
        GridPane.setHgrow(p4, Priority.ALWAYS);
        GridPane.setVgrow(p1, Priority.ALWAYS);
        GridPane.setVgrow(p4, Priority.ALWAYS);
        // Set the panel with line border
        jpAddress.setStyle("-fx-border-color: grey;"
                + " -fx-border-width: 1;"
                + " -fx-border-style: solid outside ;");
        // Add buttons to a panel
        jpButton = new FlowPane();
        jpButton.setHgap(5);
        jpButton.getChildren().addAll(jbtFirst,
                jbtNext, jbtPrevious, jbtLast);
        jpButton.setAlignment(Pos.CENTER);
        GridPane.setVgrow(jpButton, Priority.NEVER);
        GridPane.setVgrow(jpAddress, Priority.ALWAYS);
        GridPane.setHgrow(jpButton, Priority.ALWAYS);
        GridPane.setHgrow(jpAddress, Priority.ALWAYS);
        // Add jpAddress and jpButton to the stage
        this.setVgap(5);
        this.add(jpAddress, 0, 0);
        this.add(jpButton, 0, 1);
        jbtFirst.setOnAction(ae);
        jbtNext.setOnAction(ae);
        jbtPrevious.setOnAction(ae);
        jbtLast.setOnAction(ae);
        jbtFirst.Execute();
    }

    public FlowPane getButtonsPane() {
        return this.jpButton;
    }

    public void actionHandled(ActionEvent e) {
        ((Command) e.getSource()).Execute();
    }

    public void SetName(String text) {
        jtfName.setText(text);
    }

    public void SetStreet(String text) {
        jtfStreet.setText(text);
    }

    public void SetCity(String text) {
        jtfCity.setText(text);
    }

    public void SetState(String text) {
        jtfState.setText(text);
    }

    public void SetZip(String text) {
        jtfZip.setText(text);
    }

    public String GetName() {
        return jtfName.getText();
    }

    public String GetStreet() {
        return jtfStreet.getText();
    }

    public String GetCity() {
        return jtfCity.getText();
    }

    public String GetState() {
        return jtfState.getText();
    }

    public String GetZip() {
        return jtfZip.getText();
    }
}

class Address {
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

interface Command {
    public void Execute();
}

class CommandButton extends Button implements Command {
    public final static int NAME_SIZE = 32;
    public final static int STREET_SIZE = 32;
    public final static int CITY_SIZE = 20;
    public final static int STATE_SIZE = 10;
    public final static int ZIP_SIZE = 5;
    public final static int RECORD_SIZE = (NAME_SIZE + STREET_SIZE + CITY_SIZE + STATE_SIZE + ZIP_SIZE);
    protected AddressBookPane p;
    protected RandomAccessFile raf;

    public CommandButton(AddressBookPane pane, RandomAccessFile r) {
        super();
        p = pane;
        raf = r;
    }

    public void clearText() {
        p.SetCity("");
        p.SetName("");
        p.SetState("");
        p.SetStreet("");
        p.SetZip("");
    }

    public void Execute() {
    }

    /**
     * Write a record at the end of the file
     */
    public Address writeAddress() {
        try {
            raf.seek(raf.length());
            FixedLengthStringIO.writeFixedLengthString(p.GetName(),
                    NAME_SIZE, raf);
            FixedLengthStringIO.writeFixedLengthString(p.GetStreet(),
                    STREET_SIZE, raf);
            FixedLengthStringIO.writeFixedLengthString(p.GetCity(),
                    CITY_SIZE, raf);
            FixedLengthStringIO.writeFixedLengthString(p.GetState(),
                    STATE_SIZE, raf);
            FixedLengthStringIO.writeFixedLengthString(p.GetZip(),
                    ZIP_SIZE, raf);
            return new Address(p.GetName(), p.GetStreet(), p.GetCity(), p.GetState(), p.GetZip());
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Read a record at the specified position
     */
    public void readAddress(long position) throws IOException {
        String name, street, city, state, zip;
        try {
            raf.seek(position);
            name =
                    FixedLengthStringIO.readFixedLengthString(NAME_SIZE, raf);
            street =
                    FixedLengthStringIO.readFixedLengthString(STREET_SIZE, raf);
            city =
                    FixedLengthStringIO.readFixedLengthString(CITY_SIZE, raf);
            state =
                    FixedLengthStringIO.readFixedLengthString(STATE_SIZE, raf);
            zip =
                    FixedLengthStringIO.readFixedLengthString(ZIP_SIZE, raf);
        } catch (IOException ex) {
            name = "";
            street = "";
            city = "";
            state = "";
            zip = "";
        }
        p.SetName(name);
        p.SetStreet(street);
        p.SetCity(city);
        p.SetState(state);
        p.SetZip(zip);
    }
}

class AddButton extends CommandButton {
    private CareTaker careTaker;
    private Originator originator = new Originator();

    public AddButton(AddressBookPane pane, RandomAccessFile r, CareTaker careTaker) {
        super(pane, r);
        this.setText("Add");
        this.careTaker = careTaker;
    }

    @Override
    public void Execute() {
        Address address = writeAddress();
        if (address == null) return;
        originator.setState(address);
        careTaker.add(originator.saveStateToMemento());
    }
}

class NextButton extends CommandButton {
    public NextButton(AddressBookPane pane, RandomAccessFile r) {
        super(pane, r);
        this.setText("Next");
    }

    @Override
    public void Execute() {
        try {
            long currentPosition = raf.getFilePointer();
            if (currentPosition < raf.length())
                readAddress(currentPosition);
            else if (raf.length() == 0)
                clearText();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

class PreviousButton extends CommandButton {
    public PreviousButton(AddressBookPane pane, RandomAccessFile r) {
        super(pane, r);
        this.setText("Previous");
    }

    @Override
    public void Execute() {
        try {
            long currentPosition = raf.getFilePointer();
            if (currentPosition - 2 * 2 * RECORD_SIZE >= 0)
                readAddress(currentPosition - 2 * 2 * RECORD_SIZE);
            else if (raf.length() == 0)
                clearText();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

class LastButton extends CommandButton {
    public LastButton(AddressBookPane pane, RandomAccessFile r) {
        super(pane, r);
        this.setText("Last");
    }

    @Override
    public void Execute() {
        try {
            long lastPosition = raf.length();
            if (lastPosition > 0)
                readAddress(lastPosition - 2 * RECORD_SIZE);
            else if (raf.length() == 0)
                clearText();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

class FirstButton extends CommandButton {
    public FirstButton(AddressBookPane pane, RandomAccessFile r) {
        super(pane, r);
        this.setText("First");
    }

    @Override
    public void Execute() {
        try {
            if (raf.length() > 0) readAddress(0);
            else if (raf.length() == 0)
                clearText();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

class UndoButton extends CommandButton {

    private Originator originator;
    private CareTaker careTaker;

    public UndoButton(AddressBookPane pane, RandomAccessFile r, CareTaker careTaker) {
        super(pane, r);
        this.setText("Undo");
        this.originator = new Originator();
        this.careTaker = careTaker;
    }

    @Override
    public void Execute() {
        try {
            if (super.raf.length() > 0) {
                super.raf.setLength(super.raf.length() - CommandButton.RECORD_SIZE * 2);
                this.careTaker.getPrev();
                readAddress(0);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

class RedoButton extends CommandButton {

    private Originator originator;
    private CareTaker careTaker;

    public RedoButton(AddressBookPane pane, RandomAccessFile r, CareTaker careTaker) {
        super(pane, r);
        this.setText("Redo");
        this.originator = new Originator();
        this.careTaker = careTaker;
    }

    @Override
    public void Execute() {
        Memento m = careTaker.getNext();
        if (m == null) return;
        originator.getStateFromMemento(m);
        if (originator.getState() == null) return;
        Address.saveToEndOfFile(super.raf, originator.getState());
        try {
            readAddress(super.raf.length() - 2 * CommandButton.RECORD_SIZE);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

interface AddressBookPaneDecorator {
    public AddressBookPane getPane();
}

class PaneDecorator implements AddressBookPaneDecorator {
    private AddressBookPane pane;

    public PaneDecorator(AddressBookPane pane) {
        this.pane = pane;
    }

    @Override
    public AddressBookPane getPane() {
        return this.pane;
    }
}

class MainPaneDecorator extends PaneDecorator {
    MainPaneDecorator(AddressBookPane pane) {
        super(pane);
        Originator originator = new Originator();
        CareTaker careTaker = new CareTaker();
        AddButton jbtAdd = new AddButton(pane, pane.raf, careTaker);
        RedoButton jbtRedo = new RedoButton(pane, pane.raf, careTaker);
        UndoButton jbtUndo = new UndoButton(pane, pane.raf, careTaker);
        jbtAdd.setOnAction(pane.ae);
        jbtRedo.setOnAction(pane.ae);
        jbtUndo.setOnAction(pane.ae);
        Address.arrayFromFile(pane.raf).forEach(a -> {
            originator.setState(a);
            careTaker.add(originator.saveStateToMemento());
        });
        this.getPane().getButtonsPane().getChildren().addAll(jbtAdd, jbtRedo, jbtUndo);
    }
}

class Memento {
    private Address address;

    Memento(Address address) {
        this.address = address;
    }

    Address getAddress() {
        return this.address;
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

class Originator {
    Address state;

    public void setState(Address state) {
        this.state = state;
    }

    public Address getState() {
        return this.state;
    }

    public Memento saveStateToMemento() {
        return new Memento(this.state);
    }

    public void getStateFromMemento(Memento memento) {
        this.state = memento.getAddress();
    }
}

