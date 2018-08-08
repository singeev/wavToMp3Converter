package com.singeev;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Constants {

    public static final ObservableList<String> BIT_RATES = FXCollections.observableArrayList(
            "8 kbps",
            "16 kbps",
            "24 kbps",
            "32 kbps",
            "40 kbps",
            "48 kbps",
            "56 kbps",
            "64 kbps",
            "80 kbps",
            "96 kbps",
            "112 kbps",
            "128 kbps",
            "160 kbps",
            "192 kbps",
            "224 kbps",
            "256 kbps",
            "320 kbps");

    public static final ObservableList<String> SAMPLING_FREQUENCY = FXCollections.observableArrayList(
            "8000 herz",
            "11025 herz",
            "12000 herz",
            "16000 herz",
            "22050 herz",
            "24000 herz",
            "32000 herz",
            "44100 herz");

    public static final ObservableList<String> CHANEL_MODE = FXCollections.observableArrayList(
            "mono",
            "stereo");
}
