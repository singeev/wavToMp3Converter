package com.singeev.service;

import com.jfoenix.controls.JFXProgressBar;
import javafx.application.Platform;
import javafx.scene.control.Label;
import ws.schild.jave.EncoderProgressListener;
import ws.schild.jave.MultimediaInfo;

public class ProgressListener implements EncoderProgressListener {

    private JFXProgressBar progressBar;
    private Label processPercendLabel;

    public ProgressListener(JFXProgressBar progressBar, Label processPercendLabel) {
        this.progressBar = progressBar;
        this.processPercendLabel = processPercendLabel;
    }

    public void sourceInfo(MultimediaInfo info) {

    }

    public void progress(int permil) {
        Platform.runLater(() -> {
            progressBar.setProgress((double) permil / 1000);
            processPercendLabel.setText(permil * 100 / 1000 + "%");
        });
    }

    public void message(String message) {

    }
}
