package com.singeev.service;

import ws.schild.jave.EncoderProgressListener;
import ws.schild.jave.MultimediaInfo;

public class ProgressListener implements EncoderProgressListener {

    public void sourceInfo(MultimediaInfo info) {

    }

    public void progress(int permil) {
        System.out.println("Encoding process - " + permil * 100 / 1000 + "%");
    }

    public void message(String message) {

    }
}
