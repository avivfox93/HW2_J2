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
        primaryStage.setAlwaysOnTop(true);
        for (int i = 0; i < AddressBookPane.MAX_PANES; i++) {
            Stage stage = new Stage();
            AddressBookPane addressBookPane = AddressBookPane.getInstance();
            if (addressBookPane == null) break;
            Scene sc = new Scene(addressBookPane);
            sc.getStylesheets().add("styles.css");
            stage.setTitle("AddressBook");
            stage.setScene(sc);
            stage.setAlwaysOnTop(true);
            stage.show();
            stage.setOnCloseRequest(event -> AddressBookPane.decreaseCreated());
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
            return new SecondPaneDecorator(new AddressBookPane(randomAccessFile)).getPane();
    }

    public static void decreaseCreated() {
        created--;
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

    public AddButton(AddressBookPane pane, RandomAccessFile r, CareTaker careTaker) {
        super(pane, r);
        this.setText("Add");
        this.careTaker = careTaker;
    }

    @Override
    public void Execute() {
        Address address = writeAddress();
        if (address == null) return;
        careTaker.add(address.saveStateToMemento());
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

    private CareTaker careTaker;

    public UndoButton(AddressBookPane pane, RandomAccessFile r, CareTaker careTaker) {
        super(pane, r);
        this.setText("Undo");
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

    private CareTaker careTaker;

    public RedoButton(AddressBookPane pane, RandomAccessFile r, CareTaker careTaker) {
        super(pane, r);
        this.setText("Redo");
        this.careTaker = careTaker;
    }

    @Override
    public void Execute() {
        Memento m = careTaker.getNext();
        if (m == null) return;
        Address a = Address.getStateFromMemento(m);
        if (a == null) return;
        Address.saveToEndOfFile(super.raf, a);
        try {
            readAddress(super.raf.length() - 2 * CommandButton.RECORD_SIZE);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
