package com.singeev.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.singeev.Constants;
import com.singeev.MainApp;
import com.singeev.service.ConvertService;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DragAndDropController implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(DragAndDropController.class);

    @FXML
    private Label ddLabel;

    @FXML
    private Label orLabel;

    @FXML
    private JFXButton findFileBtn;

    @FXML
    private FontAwesomeIconView compressionSettingsBtn;

    @FXML
    private FontAwesomeIconView finishEditBtn;

    @FXML
    private FontAwesomeIconView ddIcon;

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
    private Label newFileNameLabel;

    @FXML
    private Label estimatedSizeLabel;

    @FXML
    private JFXComboBox<String> bitrateComboBox;

    @FXML
    private JFXComboBox<String> chanelsComboBox;

    @FXML
    private JFXComboBox<String> frequencyComboBox;

    private ConvertService service = new ConvertService();

    public void initialize(URL location, ResourceBundle resources) {
        bitrateComboBox.setItems(Constants.BIT_RATES);
        bitrateComboBox.getSelectionModel().select("64 kbps");
        bitrateComboBox.setDisable(true);

        chanelsComboBox.setItems(Constants.CHANEL_MODE);
        chanelsComboBox.getSelectionModel().select("mono");
        chanelsComboBox.setDisable(true);

        frequencyComboBox.setItems(Constants.SAMPLING_FREQUENCY);
        frequencyComboBox.getSelectionModel().select("44100 herz");
        frequencyComboBox.setDisable(true);
    }

    @FXML
    private void handleDrugAndDrop(DragEvent event) {
        if (event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.ANY);
            ddLabel.setVisible(false);
            orLabel.setVisible(false);
            findFileBtn.setVisible(false);
            ddIcon.setVisible(true);
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
                    newFileNameLabel,
                    estimatedSizeLabel);
        }
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
                    newFileNameLabel,
                    estimatedSizeLabel);
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
        if (service.getSourcePath() != null) {
            directoryChooser.setInitialDirectory(new File(service.getSourcePath()));
        }
        File path = directoryChooser.showDialog(MainApp.getMainStage());
        destinationPathLabel.setText(path.getAbsolutePath());
    }


}
