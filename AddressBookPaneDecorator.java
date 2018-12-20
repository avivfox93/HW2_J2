public interface AddressBookPaneDecorator {
    AddressBookPane getPane();

    void addButtons(CommandButton... buttons);
}

abstract class PaneDecorator implements AddressBookPaneDecorator {
    private AddressBookPane pane;

    public PaneDecorator(AddressBookPane pane) {
        this.pane = pane;
    }

    @Override
    public AddressBookPane getPane() {
        return this.pane;
    }

    @Override
    public void addButtons(CommandButton... buttons) {
        this.pane.getButtonsPane().getChildren().addAll(buttons);
    }

}

class MainPaneDecorator extends PaneDecorator {
    MainPaneDecorator(AddressBookPane pane) {
        super(pane);
        CareTaker careTaker = new CareTaker();
        AddButton jbtAdd = new AddButton(pane, pane.raf, careTaker);
        RedoButton jbtRedo = new RedoButton(pane, pane.raf, careTaker);
        UndoButton jbtUndo = new UndoButton(pane, pane.raf, careTaker);
        jbtAdd.setOnAction(pane.ae);
        jbtRedo.setOnAction(pane.ae);
        jbtUndo.setOnAction(pane.ae);
        Address.arrayFromFile(pane.raf).forEach(a -> {
            careTaker.add(a.saveStateToMemento());
        });
        this.addButtons(jbtAdd, jbtRedo, jbtUndo);
    }
}

class SecondPaneDecorator extends PaneDecorator {
    SecondPaneDecorator(AddressBookPane pane) {
        super(pane);
    }
}
