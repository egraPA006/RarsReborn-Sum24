package rars.javafx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Orientation;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import rars.Globals;
import rars.api.TellBackend;
import java.util.HashMap;
import java.util.List;
import javafx.scene.input.KeyCode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import rars.javafx.RegisterData;
import rars.simulator.ProgramStatement;
import rars.riscv.Register;

import java.io.*;
import java.util.Map;
import javafx.scene.Cursor;

public class MainInterface extends Application {

    private VBox lineNumberArea;
    private Image runIcon;
    private Button runButton;
    private TextArea mainTextArea;
    private File currentFile;
    private boolean isModified = false;
    private BorderPane borderPane;
    private TableView<RegisterData> tableView1;
    private TableView<RegisterData> tableView2;
    private TableView<InstructionData> instructionTableView;
    private static MainInterface instance;
    private TextArea ioConsole;
    private TextArea logConsole;
    private TextArea lowerTextField;
    private int ioConsoleLength = 0;
    private TabPane fileTabPane;
    private Map<Tab, File> tabFileMap = new HashMap<>();
    private int untitledCounter = 0;
    private VBox fileManagerPane;
    private VBox tableBox;
    private Button openFolderButton;
    private Label folderNameLabel;
    private double initX;
    private double initY;
    private double initWidth;
    private double initHeight;

    private ObservableList<RegisterData> registerData;
    private ObservableList<InstructionData> instructionData;

    public MainInterface() {
        instance = this;
        registerData = FXCollections.observableArrayList();
        instructionData = FXCollections.observableArrayList();
    }

    public static MainInterface getInstance() {
        return instance;
    }

    public TextArea getMainTextArea() {
        return mainTextArea;
    }

    public void updateInstructionTable(List<ProgramStatement> instructions) {
        instructionData.clear();
        for (ProgramStatement statement : instructions) {
            InstructionData data = new InstructionData();
            data.setAddress(Integer.toString(statement.memoryAddress));
            data.setBasic(statement.basicLine);
            data.setMachineCode(statement.machineLine);
            data.setReal(statement.sourceLine.value);
            data.setSource(statement.sourceLine.sourceFile + ": " + Integer.toString(statement.sourceLine.sourceFileLine));
            instructionData.add(data);
        }
        instructionTableView.refresh();
    }

    @Override
    public void start(Stage primaryStage) {

        fileTabPane = new TabPane();
        fileTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);

        // Text Area for Code
        mainTextArea = new TextArea();
        mainTextArea.setStyle("-fx-padding: 5;");
        mainTextArea.setPrefWidth(600);
        mainTextArea.textProperty().addListener((obs, oldText, newText) -> {
            isModified = true;
            updateLineNumbers();
        });

        // Initial Tab with an empty TextArea
        addNewTab("Untitled", "");

        // Line Number Area
        lineNumberArea = new VBox();
        lineNumberArea.setStyle("-fx-padding: 5; -fx-background-color: #f0f0f0;");

        // ScrollPane for Line Number Area
        ScrollPane lineNumberScrollPane = new ScrollPane(lineNumberArea);
        lineNumberScrollPane.setFitToWidth(true);
        lineNumberScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        // Synchronize line numbers with text area
        mainTextArea.addEventFilter(KeyEvent.KEY_RELEASED, event -> updateLineNumbers());
        mainTextArea.caretPositionProperty().addListener((obs, oldPos, newPos) -> updateLineNumbers());

        // Divider Line
        Separator separator = new Separator();
        separator.setOrientation(Orientation.VERTICAL);

        // Layout for Line Numbers and Text Area
        HBox editorBox = new HBox(fileTabPane);
        HBox.setHgrow(fileTabPane, Priority.ALWAYS); // Make the text area grow to fill available space

        // Instruction Table View
        instructionTableView = createInstructionTableView();

        // Lower Text Field
        lowerTextField = new TextArea();
        lowerTextField.setPromptText("");
        lowerTextField.setPrefHeight(200);

        VBox instructionBox = new VBox(instructionTableView, lowerTextField);
        VBox.setVgrow(instructionTableView, Priority.ALWAYS);
        VBox.setVgrow(lowerTextField, Priority.NEVER);

        // Empty Box for Switching
        HBox emptyBox = new HBox();
        emptyBox.setStyle("-fx-background-color: white;"); // Just for visual distinction

        runIcon = new Image(getClass().getResourceAsStream("/icons/run.png"));
        Image switchIcon = new Image(getClass().getResourceAsStream("/icons/switch.png"));
        Image filesIcon = new Image(getClass().getResourceAsStream("/icons/files.png"));
        Image debugIcon = new Image(getClass().getResourceAsStream("/icons/debug.png"));
        Image lineIcon = new Image(getClass().getResourceAsStream("/icons/byline.png"));
        Image backIcon = new Image(getClass().getResourceAsStream("/icons/stepback.png"));
        Image pluginIcon = new Image(getClass().getResourceAsStream("/icons/plugins.png"));
        Image settingsIcon = new Image(getClass().getResourceAsStream("/icons/settings.png"));

        Image runIconClicked = new Image(getClass().getResourceAsStream("/icons/run_clicked.png"));
        Image switchIconClicked = new Image(getClass().getResourceAsStream("/icons/switch_clicked.png"));

        // Button Column
        VBox buttonColumn = new VBox(10);
        buttonColumn.setStyle("-fx-padding: 10;");

        Button switchButton = new Button();
        switchButton.setGraphic(new ImageView(switchIcon));
        Button filesButton = new Button();
        filesButton.setGraphic(new ImageView(filesIcon));
        Separator buttonSeparator = new Separator();
        buttonSeparator.setOrientation(Orientation.HORIZONTAL);
        runButton = new Button();
        runButton.setGraphic(new ImageView(runIcon));
        Button debugButton = new Button();
        debugButton.setGraphic(new ImageView(debugIcon));
        Button lineButton = new Button();
        lineButton.setGraphic(new ImageView(lineIcon));
        Button backButton = new Button();
        backButton.setGraphic(new ImageView(backIcon));

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button pluginButton = new Button();
        pluginButton.setGraphic(new ImageView(pluginIcon));
        Button settingsButton = new Button();
        settingsButton.setGraphic(new ImageView(settingsIcon));

        setImageViewSize(debugButton, 24, 24);

        buttonColumn.getChildren().addAll(switchButton, filesButton, buttonSeparator, runButton, debugButton, lineButton, backButton, spacer, pluginButton, settingsButton);

        pluginButton.setOnAction(event -> {
            new ExtensionsWindow().display();
        });


        settingsButton.setOnAction(event -> {
            new SettingsWindow().display();
        });

        fileManagerPane = new VBox();
        fileManagerPane.setPrefWidth(200);
        fileManagerPane.setPadding(new Insets(10));
        Label fileManagerLabel = new Label("File Manager");
        openFolderButton = new Button("Open Folder");
        folderNameLabel = new Label();
        TreeView<String> fileTreeView = new TreeView<>();
        fileTreeView.setShowRoot(false);

        openFolderButton.setOnAction(event -> openFolder(fileTreeView));

        fileManagerPane.getChildren().addAll(fileManagerLabel, openFolderButton, folderNameLabel, fileTreeView);

        // Event handler for filesButton to toggle the file manager pane
        filesButton.setOnAction(event -> {
            if (borderPane.getLeft() == buttonColumn) {
                HBox fileManagerBox = new HBox(buttonColumn, fileManagerPane);
                borderPane.setLeft(fileManagerBox);
            } else {
                borderPane.setLeft(buttonColumn);
            }
        });

        // Event handlers to change icons on button click
        switchButton.setOnAction(event -> {
            ImageView currentIcon = (ImageView) switchButton.getGraphic();
            if (currentIcon.getImage().equals(switchIcon)) {
                switchButton.setGraphic(new ImageView(switchIconClicked));
            } else {
                switchButton.setGraphic(new ImageView(switchIcon));
            }
            if (borderPane.getCenter().equals(fileTabPane)) {
                borderPane.setCenter(instructionBox);
            } else {
                borderPane.setCenter(fileTabPane);
            }
        });

        runButton.setOnAction(event -> {
            ImageView currentIcon = (ImageView) runButton.getGraphic();
            if (!Globals.isCompiled) {
                saveFile();
                Globals.assembleFile = currentFile;
            }
            if (currentIcon.getImage().equals(runIcon)) {
                runButton.setGraphic(new ImageView(runIconClicked));
            } else {
                runButton.setGraphic(new ImageView(runIcon));
            }
            TellBackend.runFullProgram();
        });

        lineButton.setOnAction(event -> {
            if (!Globals.isCompiled) {
                saveFile();
                Globals.assembleFile = currentFile;
                runButton.setGraphic(new ImageView(runIconClicked));
            }
            TellBackend.runLine();
        });

        // Create consoles
        ioConsole = new TextArea();
        ioConsole.setPromptText("Input/Output Console");
        ioConsole.setEditable(false);
        ioConsole.setPrefRowCount(5);
        logConsole = new TextArea();
        logConsole.setPromptText("Log Console");
        logConsole.setEditable(false);
        logConsole.setPrefRowCount(5);
        logConsole.setPrefWidth(500);

        ioConsole.setPrefHeight(150);
        logConsole.setPrefHeight(150);

        SplitPane consoleSplitPane = new SplitPane(ioConsole, logConsole);
        consoleSplitPane.setOrientation(Orientation.HORIZONTAL);
        consoleSplitPane.setDividerPositions(0.5);

        VBox consoleBox = new VBox(consoleSplitPane);
        consoleBox.setPrefHeight(150);
        consoleBox.setPadding(new Insets(10));
        VBox.setVgrow(consoleSplitPane, Priority.ALWAYS);

        ioConsole.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleInputConsole();
                event.consume();
            }
        });

        // Create two TableViews
        tableView1 = createTableView();
        tableView2 = createTableView();

        // Initialize tableBox
        tableBox = new VBox();
        tableBox.setPadding(new Insets(10));
        tableBox.setPrefWidth(300);
        tableBox.setPrefHeight(800);
        VBox.setVgrow(tableBox, Priority.ALWAYS);

        TabPane tabPane = new TabPane();
        Tab tab1 = new Tab("Registers", new VBox(tableView1));
        Tab tab2 = new Tab("Floating Point", new VBox(tableView2));

        // Make tabs non-closable
        tab1.setClosable(false);
        tab2.setClosable(false);

        tabPane.getTabs().addAll(tab1, tab2);

        tableBox.getChildren().add(tabPane);

        tabPane.setPrefHeight(800);

        // Make tableBox resizable
        makeResizable(tableBox, Double.MAX_VALUE, Double.MAX_VALUE);

        // Make fileManagerPane resizable
        makeResizable(fileManagerPane, Double.MAX_VALUE, Double.MAX_VALUE);

        // Overall Layout with Menu Bar
        borderPane = new BorderPane();
        borderPane.setTop(createMenuBar());
        borderPane.setLeft(buttonColumn);
        borderPane.setCenter(createPlaceholderBox());
        borderPane.setBottom(consoleBox);
        borderPane.setRight(tableBox);
        borderPane.setCenter(fileTabPane);

        // Scene
        Scene scene = new Scene(borderPane, 800, 600);

        primaryStage.setTitle("RARS Reborn");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Initial update to line numbers
        updateLineNumbers();

        // Synchronize scrolling
        ScrollBar mainTextAreaScrollBar = (ScrollBar) mainTextArea.lookup(".scroll-bar:vertical");
        ScrollBar lineNumberScrollBar = (ScrollBar) lineNumberScrollPane.lookup(".scroll-bar:vertical");

        if (mainTextAreaScrollBar != null && lineNumberScrollBar != null) {
            lineNumberScrollBar.valueProperty().bindBidirectional(mainTextAreaScrollBar.valueProperty());
        }

        primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> adjustMaxWidths());
        borderPane.widthProperty().addListener((obs, oldVal, newVal) -> adjustMaxWidths());
    }

    private void makeResizable(Node node, double maxWidth, double maxHeight) {
        node.setOnMouseMoved(event -> {
            Region region = (Region) node;
            double width = region.getWidth();
            double height = region.getHeight();
            double border = 5;

            if (event.getX() > width - border && event.getY() > height - border) {
                node.setCursor(Cursor.SE_RESIZE);
            } else if (event.getX() > width - border) {
                node.setCursor(Cursor.E_RESIZE);
            } else if (event.getY() > height - border) {
                node.setCursor(Cursor.S_RESIZE);
            } else if (event.getX() < border) {
                node.setCursor(Cursor.W_RESIZE);
            } else {
                node.setCursor(Cursor.DEFAULT);
            }
        });

        node.setOnMousePressed(event -> {
            initX = event.getScreenX();
            initY = event.getScreenY();
            initWidth = ((Region) node).getWidth();
            initHeight = ((Region) node).getHeight();
        });

        node.setOnMouseDragged(event -> {
            double deltaX = event.getScreenX() - initX;
            double deltaY = event.getScreenY() - initY;

            if (event.isPrimaryButtonDown()) {
                Region region = (Region) node;
                if (node.getCursor() == Cursor.E_RESIZE || node.getCursor() == Cursor.SE_RESIZE) {
                    double newWidth = initWidth + deltaX;
                    if (newWidth > 50 && newWidth <= maxWidth) {
                        region.setPrefWidth(newWidth);
                    }
                }
                if (node.getCursor() == Cursor.S_RESIZE || node.getCursor() == Cursor.SE_RESIZE) {
                    double newHeight = initHeight + deltaY;
                    if (newHeight > 50 && newHeight <= maxHeight) {
                        region.setPrefHeight(newHeight);
                    }
                }
                if (node.getCursor() == Cursor.W_RESIZE) {
                    double newWidth = initWidth - deltaX;
                    if (newWidth > 50 && newWidth <= maxWidth) {
                        region.setPrefWidth(newWidth);
                        region.setLayoutX(region.getLayoutX() + deltaX);
                    }
                }
            }
        });

        node.setOnMouseReleased(event -> node.setCursor(Cursor.DEFAULT));
    }

    private void adjustMaxWidths() {
        double availableWidth = borderPane.getWidth();
        double minWidthForCenter = 200;

        double maxWidth = (availableWidth - minWidthForCenter) / 2;
        maxWidth = Math.max(maxWidth, 100);

        fileManagerPane.setMaxWidth(maxWidth);
        tableBox.setMaxWidth(maxWidth);
    }

    private VBox createPlaceholderBox() {
        VBox placeholderBox = new VBox();
        placeholderBox.setStyle("-fx-border-color: black; -fx-padding: 20; -fx-background-color: #f9f9f9;");
        Label placeholderLabel = new Label("No file is currently open.");
        placeholderLabel.setStyle("-fx-font-size: 16; -fx-text-fill: #333;");
        placeholderBox.getChildren().add(placeholderLabel);
        placeholderBox.setAlignment(Pos.CENTER);
        return placeholderBox;
    }

    public class FileTreeItem extends TreeItem<String> {
        private String fullPath;

        public FileTreeItem(String name, String fullPath) {
            super(name);
            this.fullPath = fullPath;
        }

        public String getFullPath() {
            return fullPath;
        }
    }

    private void openFolder(TreeView<String> fileTreeView) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Open Folder");
        File selectedDirectory = directoryChooser.showDialog(null);

        if (selectedDirectory != null) {
            folderNameLabel.setText(selectedDirectory.getName());

            FileTreeItem rootItem = new FileTreeItem(selectedDirectory.getName(), selectedDirectory.getAbsolutePath());
            createFileTree(selectedDirectory, rootItem);
            fileTreeView.setRoot(rootItem);
            fileTreeView.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    TreeItem<String> selectedItem = fileTreeView.getSelectionModel().getSelectedItem();
                    if (selectedItem != null && selectedItem.isLeaf() && selectedItem instanceof FileTreeItem) {
                        FileTreeItem fileTreeItem = (FileTreeItem) selectedItem;
                        openFile(new File(fileTreeItem.getFullPath()));
                    }
                }
            });
        }
    }

    private void createFileTree(File directory, FileTreeItem parent) {
        for (File file : directory.listFiles()) {
            FileTreeItem treeItem = new FileTreeItem(file.getName(), file.getAbsolutePath());
            parent.getChildren().add(treeItem);
            if (file.isDirectory()) {
                createFileTree(file, treeItem);
            } else if (file.getName().endsWith(".asm")) {
                treeItem.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, event -> {
                    if (event.getClickCount() == 2) {
                        openFile(new File(treeItem.getFullPath()));
                    }
                });
            }
        }
    }

    public void inputConsole() {
        ioConsole.setEditable(true);
    }
    private void handleInputConsole() {
        String input = ioConsole.getText(ioConsoleLength, ioConsole.getLength());
        ioConsoleLength = ioConsole.getLength();
        Globals.logger.debug("Input detected");
        ioConsole.setEditable(false);
        TellBackend.inputConsole(input.trim());
    }
    public void reset() {
        ioConsoleLength = 0;
        ioConsole.clear();
        registerData.clear();
        for (Register register : Globals.registerTable.values()) {
            registerData.add(new RegisterData(register.getName(), register.getValue(), register.getNum()));
        }
        tableView1.refresh();
    }
    public void updateOuputConsole(String message) {
        ioConsole.appendText(message + "\n");
        ioConsoleLength = ioConsole.getLength();
        Globals.currentSysCall = null;
    }
    public void updateLogConsole(String message) {
        logConsole.appendText(message + "\n");
    }

    public void updateRegisterTable(String name, int value) {
        for (RegisterData data : registerData) {
            if (data.getName().equals(name)) {
                data.setValue(value);
                tableView1.refresh();
                return;
            }
        }
        registerData.add(new RegisterData(name, value, Globals.registerTable.get(name).getNum()));
        tableView1.refresh();
    }

    // Updated method to create a TableView with RegisterData
    private TableView<RegisterData> createTableView() {
        TableView<RegisterData> tableView = new TableView<>(registerData);

        TableColumn<RegisterData, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<RegisterData, Integer> numberColumn = new TableColumn<>("Number");
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));

        TableColumn<RegisterData, Integer> valueColumn = new TableColumn<>("Value");
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));

        tableView.getColumns().addAll(nameColumn, numberColumn, valueColumn);
        return tableView;
    }

    private TableView<InstructionData> createInstructionTableView() {
        TableView<InstructionData> tableView = new TableView<>();

        TableColumn<InstructionData, String> addressColumn = new TableColumn<>("Address");
        addressColumn.setCellValueFactory(cellData -> cellData.getValue().getAddress());

        TableColumn<InstructionData, String> machineCodeColumn = new TableColumn<>("Machine Code");
        machineCodeColumn.setCellValueFactory(cellData -> cellData.getValue().getMachineCode());

        TableColumn<InstructionData, String> basicColumn = new TableColumn<>("Basic Instruction");
        basicColumn.setCellValueFactory(cellData -> cellData.getValue().getBasic());

        TableColumn<InstructionData, String> realColumn = new TableColumn<>("Real Instruction");
        realColumn.setCellValueFactory(cellData -> cellData.getValue().getReal());

        TableColumn<InstructionData, String> sourceColumn = new TableColumn<>("Source");
        sourceColumn.setCellValueFactory(cellData -> cellData.getValue().getSource());

        tableView.getColumns().add(addressColumn);
        tableView.getColumns().add(machineCodeColumn);
        tableView.getColumns().add(basicColumn);
        tableView.getColumns().add(realColumn);
        tableView.getColumns().add(sourceColumn);

        tableView.setItems(instructionData);
        return tableView;
    }

    public void highlightInstructionTableRow(int pcValue) {
        Platform.runLater(() -> {
            instructionTableView.getSelectionModel().clearSelection();
            instructionTableView.getSelectionModel().select(pcValue);
            instructionTableView.scrollTo(pcValue);
        });
    }

    public void highlightRegisterRow(String registerName) {
        Platform.runLater(() -> {
            tableView1.getSelectionModel().clearSelection();
            for (int i = 0; i < registerData.size(); i++) {
                if (registerData.get(i).getName().equals(registerName)) {
                    tableView1.getSelectionModel().select(i);
                    tableView1.scrollTo(i);
                    break;
                }
            }
        });
    }

    public void resetInstructionTable() {
        instructionData.clear();
    }

    private void setImageViewSize(Button button, double width, double height) {
        ImageView imageView = (ImageView) button.getGraphic();
        if (imageView != null) {
            imageView.setFitWidth(width);
            imageView.setFitHeight(height);
        }
    }
    public void stopExecution() {
        runButton.setGraphic(new ImageView(runIcon));
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("File");
        MenuItem newFileItem = new MenuItem("New File");
        MenuItem openFileItem = new MenuItem("Open File");
        MenuItem saveFileItem = new MenuItem("Save File");

        newFileItem.setOnAction(event -> createFile());
        openFileItem.setOnAction(event -> openFile(null));
        saveFileItem.setOnAction(event -> saveFile());

        fileMenu.getItems().addAll(newFileItem, openFileItem, saveFileItem);

        Menu editMenu = new Menu("Edit");
        Menu runMenu = new Menu("Run");
        Menu settingsMenu = new Menu("Settings");

        MenuItem settingsItem = new MenuItem("Open Settings");
        settingsItem.setOnAction(event -> new SettingsWindow().display());
        settingsMenu.getItems().add(settingsItem);

        Menu helpMenu = new Menu("Help");

        MenuItem helpItem = new MenuItem("Open Help");
        helpItem.setOnAction(event -> new HelpWindow().display());
        helpMenu.getItems().add(helpItem);

        menuBar.getMenus().addAll(fileMenu, editMenu, runMenu, settingsMenu, helpMenu);

        return menuBar;
    }

    private void createFile() {
        if (isModified && confirmSave()) {
            saveFile();
        }
        untitledCounter++;
        String title = "Untitled-" + untitledCounter;
        Tab newTab = addNewTab(title, "");
        tabFileMap.put(newTab, null);
        fileTabPane.getSelectionModel().select(newTab);
        borderPane.setCenter(fileTabPane);
        isModified = false;
    }

    private boolean confirmSave() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Unsaved Changes");
        alert.setHeaderText("You have unsaved changes.");
        alert.setContentText("Do you want to save your changes before creating a new file?");
        ButtonType saveButton = new ButtonType("Save");
        ButtonType dontSaveButton = new ButtonType("Don't Save");
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(saveButton, dontSaveButton, cancelButton);

        ButtonType result = alert.showAndWait().orElse(cancelButton);
        return result == saveButton;
    }

    private void openFile(File file) {
        if (isModified && confirmSave()) {
            saveFile();
        }

        if (file == null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Assembly File");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Assembly Files", "*.asm"));
            file = fileChooser.showOpenDialog(null);
        }

        if (file != null) {
            // Check if the file is already open
            for (Map.Entry<Tab, File> entry : tabFileMap.entrySet()) {
                if (entry.getValue() != null && entry.getValue().equals(file)) {
                    // Switch to the existing tab
                    fileTabPane.getSelectionModel().select(entry.getKey());
                    return;
                }
            }

            currentFile = file;
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
                Tab newTab = addNewTab(file.getName(), content.toString());
                tabFileMap.put(newTab, file);
                fileTabPane.getSelectionModel().select(newTab);
                borderPane.setCenter(fileTabPane);
                isModified = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveFile() {
        Tab currentTab = fileTabPane.getSelectionModel().getSelectedItem();
        if (currentTab != null) {
            TextArea currentTextArea = (TextArea) ((HBox) currentTab.getContent()).getChildren().get(1);
            currentFile = tabFileMap.get(currentTab);
            if (currentFile == null) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save Assembly File");
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Assembly Files", "*.asm"));
                currentFile = fileChooser.showSaveDialog(null);
                if (currentFile != null) {
                    tabFileMap.put(currentTab, currentFile);
                }
            }
            if (currentFile != null) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(currentFile))) {
                    writer.write(currentTextArea.getText());
                    currentTab.setText(currentFile.getName());
                    isModified = false;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void updateLineNumbers() {
        String[] lines = mainTextArea.getText().split("\n");
        lineNumberArea.getChildren().clear();
        for (int i = 0; i < lines.length; i++) {
            Label lineNumber = new Label(String.valueOf(i + 1));
            lineNumber.setMinWidth(Region.USE_PREF_SIZE);
            lineNumberArea.getChildren().add(lineNumber);
        }
    }

    private Tab addNewTab(String title, String content) {
        TextArea textArea = new TextArea(content);
        textArea.setStyle("-fx-padding: 5;");
        textArea.textProperty().addListener((obs, oldText, newText) -> {
            isModified = true;
            updateLineNumbersForTab(lineNumberArea, textArea);
        });

        VBox lineNumberAreaForTab = new VBox();
        lineNumberAreaForTab.setStyle("-fx-padding: 5; -fx-background-color: #f0f0f0;");

        ScrollPane lineNumberScrollPaneForTab = new ScrollPane(lineNumberAreaForTab);
        lineNumberScrollPaneForTab.setFitToWidth(true);
        lineNumberScrollPaneForTab.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        HBox editorBoxForTab = new HBox(lineNumberScrollPaneForTab, textArea);
        HBox.setHgrow(textArea, Priority.ALWAYS);

        Tab newTab = new Tab(title, editorBoxForTab);
        fileTabPane.getTabs().add(newTab);
        fileTabPane.getSelectionModel().select(newTab);

        textArea.addEventFilter(KeyEvent.KEY_RELEASED, event -> updateLineNumbersForTab(lineNumberAreaForTab, textArea));
        textArea.caretPositionProperty().addListener((obs, oldPos, newPos) -> updateLineNumbersForTab(lineNumberAreaForTab, textArea));

        newTab.setOnClosed(event -> {
            tabFileMap.remove(newTab);
            if (fileTabPane.getTabs().isEmpty()) {
                currentFile = null;
                isModified = false;
                borderPane.setCenter(createPlaceholderBox());
            }
        });

        updateLineNumbersForTab(lineNumberAreaForTab, textArea);

        return newTab;
    }

    private void updateLineNumbersForTab(VBox lineNumberArea, TextArea textArea) {
        String[] lines = textArea.getText().split("\n");
        lineNumberArea.getChildren().clear();
        for (int i = 0; i < lines.length; i++) {
            Label lineNumber = new Label(String.valueOf(i + 1));
            lineNumber.setMinWidth(Region.USE_PREF_SIZE);
            lineNumberArea.getChildren().add(lineNumber);
        }
    }

    public class HelpWindow {

        public void display() {
            Stage helpStage = new Stage();
            helpStage.setTitle("Help");

            VBox layout = new VBox(10);

            Scene scene = new Scene(layout, 600, 400);
            helpStage.setScene(scene);
            helpStage.show();
        }
    }

    public class SettingsWindow {
        public void display() {
            Stage settingsStage = new Stage();
            settingsStage.setTitle("Settings");

            VBox layout = new VBox(10);
            layout.setPadding(new Insets(10));

            Scene scene = new Scene(layout, 600, 400);
            settingsStage.setScene(scene);
            settingsStage.show();
        }
    }

    public class ExtensionsWindow {
        public void display() {
            Stage extensionsStage = new Stage();
            extensionsStage.setTitle("Extensions Manager");

            VBox layout = new VBox(10);
            layout.setPadding(new Insets(10));

            Scene scene = new Scene(layout, 600, 400);
            extensionsStage.setScene(scene);
            extensionsStage.show();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}