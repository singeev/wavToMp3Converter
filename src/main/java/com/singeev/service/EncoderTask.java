package com.singeev.service;

import javafx.concurrent.Task;
import ws.schild.jave.EncoderException;
import ws.schild.jave.EncodingAttributes;
import ws.schild.jave.MultimediaObject;

import java.io.File;

public class EncoderTask extends Task<Boolean> {

    private MultimediaObject source;
    private File target;
    private EncodingAttributes attributes;
    private ProgressListener listener;

    public EncoderTask(MultimediaObject source, File target, EncodingAttributes attributes, ProgressListener listener) {
        this.source = source;
        this.target = target;
        this.attributes = attributes;
        this.listener = listener;
    }

    protected Boolean call() {
        ws.schild.jave.Encoder encoder = new ws.schild.jave.Encoder();
        try {
            encoder.encode(source, target, attributes, listener);
        } catch (EncoderException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
