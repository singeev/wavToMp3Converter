package com.singeev.controller;

import com.jfoenix.controls.*;
import com.singeev.Constants;
import com.singeev.MainApp;
import com.singeev.service.ConvertService;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);

    @FXML
    private Label ddLabel;

    @FXML
    private Label orLabel;

    @FXML
    private JFXButton findFileBtn;

    @FXML
    private JFXButton startEncodingBtn;

    @FXML
    private FontAwesomeIconView compressionSettingsBtn;

    @FXML
    private FontAwesomeIconView finishEditBtn;

    @FXML
    private FontAwesomeIconView ddIcon;

    @FXML
    private FontAwesomeIconView fileNameEditBtn;

    @FXML
    private FontAwesomeIconView finishEditFilenameBtn;

    @FXML
    private Label sourceFileNameLabel;

    @FXML
    private Label sourceFileLengthLabel;

    @FXML
    private Label sourceFileDate;

    @FXML
    private Label sourceFileSizeLabel;

    @FXML
    private Label destinationPathLabel;

    @FXML
    private JFXTextField newFileNameField;

    @FXML
    private Label estimatedSizeLabel;

    @FXML
    private JFXComboBox<String> bitrateComboBox;

    @FXML
    private JFXComboBox<String> chanelsComboBox;

    @FXML
    private JFXComboBox<String> frequencyComboBox;

    @FXML
    private GridPane settingsPane;

    @FXML
    private Pane ddPane;

    @FXML
    private Label processPercendLabel;

    @FXML
    private JFXCheckBox deleteSourceCheckBox;

    @FXML
    private JFXProgressBar progressBar;

    @FXML
    private Label successLabel;

    @FXML
    private Label encodingFailMessage;

    @FXML
    private Label badFileNameMessage;

    @FXML
    private Label wavOnlyMessage;

    private ConvertService service = new ConvertService();

    public void initialize(URL location, ResourceBundle resources) {
//        startEncodingBtn.
        bitrateComboBox.setItems(Constants.BIT_RATES);
        bitrateComboBox.getSelectionModel().select("64 kbps");
        bitrateComboBox.setDisable(true);

        chanelsComboBox.setItems(Constants.CHANEL_MODE);
        chanelsComboBox.getSelectionModel().select("mono");
        chanelsComboBox.setDisable(true);

        frequencyComboBox.setItems(Constants.SAMPLING_FREQUENCY);
        frequencyComboBox.getSelectionModel().select("44100 herz");
        frequencyComboBox.setDisable(true);

        newFileNameField.setText("-");
        newFileNameField.setDisable(true);

        progressBar.setProgress(0);
        progressBar.setDisable(true);
    }

    @FXML
    private void handleDrugAndDrop(DragEvent event) {
        if (event.getDragboard().hasFiles()) {
            List<File> files = event.getDragboard().getFiles();
            final File source = files.get(0);
            if (source.getName().endsWith("wav")) {
                event.acceptTransferModes(TransferMode.ANY);
                ddLabel.setVisible(false);
                orLabel.setVisible(false);
                findFileBtn.setVisible(false);
                ddIcon.setVisible(true);
            } else {
                hideAnyMessages();
                wavOnlyMessage.setVisible(true);
            }
        }
    }

    @FXML
    private void handleDragExit(DragEvent event) {
        if (event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.ANY);
            ddLabel.setVisible(true);
            orLabel.setVisible(true);
            findFileBtn.setVisible(true);
            ddIcon.setVisible(false);
        }
        if(wavOnlyMessage.isVisible()) {
            wavOnlyMessage.setVisible(false);
        }
    }

    @FXML
    private void handleDrop(DragEvent event) {
        List<File> files = event.getDragboard().getFiles();
        final File source = files.get(0);
        if (source.getName().endsWith("wav")) {
            service.getSourceFileInfoAndShowIt(
                    source,
                    sourceFileNameLabel,
                    sourceFileLengthLabel,
                    sourceFileDate,
                    sourceFileSizeLabel,
                    destinationPathLabel,
                    newFileNameField,
                    estimatedSizeLabel);
        }
        resetProgressUiElements();
    }

    @FXML
    private void handleChooseFile(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "WAV files (*.wav)",
                "*.wav");
        fileChooser.getExtensionFilters().add(extFilter);
        File source = fileChooser.showOpenDialog(MainApp.getMainStage());
        if (source != null && source.getName().endsWith("wav")) {
            service.getSourceFileInfoAndShowIt(
                    source,
                    sourceFileNameLabel,
                    sourceFileLengthLabel,
                    sourceFileDate,
                    sourceFileSizeLabel,
                    destinationPathLabel,
                    newFileNameField,
                    estimatedSizeLabel);
        }
        resetProgressUiElements();
    }

    private void resetProgressUiElements() {
        if (processPercendLabel.getText().equals("100%")) {
            progressBar.setProgress(0);
            progressBar.setDisable(true);
            processPercendLabel.setText("0%");
            processPercendLabel.setVisible(false);
            successLabel.setVisible(false);
        }
    }

    @FXML
    private void handleBitrate(ActionEvent event) {
        String selectedBitrate = (String) ((JFXComboBox) event.getSource()).getSelectionModel().getSelectedItem();
        int bitRate = Integer.valueOf(selectedBitrate.substring(0, selectedBitrate.length() - 5));
        service.setBitRate(bitRate * 1000);
        service.estimateResultFileSizeAndShowIt(estimatedSizeLabel);
        System.out.println(bitRate);
    }

    @FXML
    private void handleChanels(ActionEvent event) {
        String selectedChanels = (String) ((JFXComboBox) event.getSource()).getSelectionModel().getSelectedItem();
        int channels = selectedChanels.equals("mono") ? 1 : 2;
        service.setChannels(channels);
        System.out.println(channels);
    }

    @FXML
    private void handleFrequency(ActionEvent event) {
        String selectedFrequency = (String) ((JFXComboBox) event.getSource()).getSelectionModel().getSelectedItem();
        int frequency = Integer.valueOf(selectedFrequency.substring(0, selectedFrequency.length() - 5));
        service.setSamplingRate(frequency);
        System.out.println(frequency);
    }

    @FXML
    private void handleCompressSettingsBtn() {
        compressionSettingsBtn.setVisible(false);
        bitrateComboBox.setDisable(false);
        chanelsComboBox.setDisable(false);
        frequencyComboBox.setDisable(false);
        finishEditBtn.setVisible(true);
    }

    @FXML
    private void handleFinishSettingsBtn() {
        finishEditBtn.setVisible(false);
        bitrateComboBox.setDisable(true);
        chanelsComboBox.setDisable(true);
        frequencyComboBox.setDisable(true);
        compressionSettingsBtn.setVisible(true);
    }

    @FXML
    private void handleDirectoryChoose() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        if (service.getDestinationPath() != null) {
            directoryChooser.setInitialDirectory(new File(service.getDestinationPath()));
        } else if (service.getSourcePath() != null) {
            directoryChooser.setInitialDirectory(new File(service.getSourcePath()));
        }
        String path = directoryChooser.showDialog(MainApp.getMainStage()).getAbsolutePath();
        service.setDestinationPath(path);
        destinationPathLabel.setText(path);
    }

    @FXML
    private void handleFileNameEdit() {
        newFileNameField.setDisable(false);
        newFileNameField.requestFocus();
        fileNameEditBtn.setVisible(false);
        finishEditFilenameBtn.setVisible(true);
    }

    @FXML
    private void handleFinishFileNameEditBtn() {
        String finalNewFileName;
        String newFileName = newFileNameField.getText().trim();
        if (StringUtils.containsAny(newFileName, Constants.RESTRICTED_FILENAME_SYMBOLS)) {
            hideAnyMessages();
            badFileNameMessage.setVisible(true);
            startEncodingBtn.setDisable(true);
        } else {
            finalNewFileName = newFileName.endsWith(".mp3") ? newFileName : newFileName + ".mp3";
            service.setNewFileName(finalNewFileName);
            newFileNameField.setText(finalNewFileName);
            newFileNameField.setDisable(true);
            finishEditFilenameBtn.setVisible(false);
            fileNameEditBtn.setVisible(true);
            if(badFileNameMessage.isVisible()) {
                badFileNameMessage.setVisible(false);
                startEncodingBtn.setDisable(false);
            }
        }
    }

    @FXML
    private void handleDeleteSourceCheckBox(ActionEvent event) {
        boolean selected = ((JFXCheckBox) event.getSource()).isSelected();
        service.setDeleteSourceFile(selected);
    }

    @FXML
    private void handleStartConvertation() {
        settingsPane.setDisable(true);
        ddPane.setDisable(true);
        deleteSourceCheckBox.setDisable(true);
        startEncodingBtn.setDisable(true);
        progressBar.setProgress(0);
        progressBar.setDisable(false);
        processPercendLabel.setVisible(true);
        EventHandler<WorkerStateEvent> onSuccessHandler = event -> {
            settingsPane.setDisable(false);
            ddPane.setDisable(false);
            deleteSourceCheckBox.setDisable(false);
            startEncodingBtn.setDisable(false);
            service.deleteSourceFileIfNeeded();
            successLabel.setVisible(true);
        };

        EventHandler<WorkerStateEvent> onFailHandler = event -> {
            settingsPane.setDisable(false);
            ddPane.setDisable(false);
            deleteSourceCheckBox.setDisable(false);
            startEncodingBtn.setDisable(false);
            service.deleteSourceFileIfNeeded();
            successLabel.setVisible(true);
            encodingFailMessage.setVisible(true);
        };
        service.encodeFile(progressBar, processPercendLabel, onSuccessHandler, onFailHandler);
    }

    private void hideAnyMessages() {
        if(badFileNameMessage.isVisible()) {
            badFileNameMessage.setVisible(false);
        }
        if(encodingFailMessage.isVisible()) {
            encodingFailMessage.setVisible(false);
        }
        if(wavOnlyMessage.isVisible()) {
            wavOnlyMessage.setVisible(false);
        }
        if(successLabel.isVisible()) {
            successLabel.setVisible(false);
        }
    }
}
