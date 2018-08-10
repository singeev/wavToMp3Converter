package com.singeev.service;

import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextField;
import com.singeev.Constants;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import ws.schild.jave.*;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ConvertService {

    private int bitRate = 64000;
    private int channels = 1;
    private int samplingRate = 44100;
    private File sourceFile;
    private long sourceFileDuration;
    private String sourcePath;
    private String destinationPath;
    private String newFileName;
    private boolean deleteSourceFile = false;

    public void encodeFile(
            JFXProgressBar progressBar,
            Label processPercendLabel,
            EventHandler<WorkerStateEvent> onSuccessHandler,
            EventHandler<WorkerStateEvent> onFailHandler) {

        File target = new File(destinationPath + "\\" + newFileName);
        AudioAttributes audio = new AudioAttributes();
        audio.setCodec(Constants.MP3_CODEC);
        audio.setBitRate(bitRate);
        audio.setChannels(channels);
        audio.setSamplingRate(samplingRate);
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setFormat("mp3");
        attrs.setAudioAttributes(audio);
        ProgressListener listener = new ProgressListener(progressBar, processPercendLabel);

        Task encoder = new EncoderTask(
                new MultimediaObject(sourceFile),
                target,
                attrs,
                listener);

        encoder.setOnSucceeded(onSuccessHandler);
        encoder.setOnFailed(onFailHandler);

        new Thread(encoder).start();
    }

    private String estimateFileSizeWithBitrate(long duration, int bitRate) {
        long bytes = (duration / 1000 * bitRate) / 8;
        return convertFileSize(bytes);
    }

    private String convertFileSize(long bytes) {
        int unit = 1024;
        if (bytes < unit) return bytes + " b";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = ("kMGTPE").charAt(exp - 1) + ("");
        return String.format("%.1f %sb", bytes / Math.pow(unit, exp), pre);
    }


    private String convertLength(long msec) {
        Date date = new Date(msec);
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return formatter.format(date);
    }

    private Date getFileCreationDate(File source) {
        String fileName = source.getName();
        if (fileName.startsWith("ts3_recording")) {
            String dateString = fileName.substring(14, fileName.length() - 4);
            try {
                return new SimpleDateFormat("y_MM_dd_HH_m_ss").parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            return new Date(source.lastModified());
        }
        return null;
    }

    private String getFilePath(File source) {
        String absolutePath = source.getAbsolutePath();
        setSourcePath(source.getAbsolutePath().substring(0, absolutePath.lastIndexOf(File.separator)));
        return getSourcePath();
    }

    public void estimateResultFileSizeAndShowIt(Label estimatedSizeLabel) {
        estimatedSizeLabel.setText(estimateFileSizeWithBitrate(sourceFileDuration, bitRate));
    }

    public void getSourceFileInfoAndShowIt(File source, Label sourceFileNameLabel, Label sourceFileLengthLabel,
                                           Label sourceFileDate, Label sourceFileSizeLabel, Label destinationPathLabel,
                                           JFXTextField newFileNameField, Label estimatedSizeLabel) {
        sourceFile = source;

        try {
            MultimediaInfo mi = new MultimediaObject(source).getInfo();
            sourceFileDuration = mi.getDuration();
            Date fileCreationDate = getFileCreationDate(sourceFile);
            sourceFileNameLabel.setText(source.getName());
            sourceFileLengthLabel.setText(convertLength(sourceFileDuration));
            sourceFileDate.setText(new SimpleDateFormat("dd.MM.yyyy, HH:mm").format(fileCreationDate));
            sourceFileSizeLabel.setText(convertFileSize(source.length()));
            if (destinationPath == null) {
                destinationPath = getFilePath(source);
            }
            destinationPathLabel.setText(destinationPath);
            newFileName = new SimpleDateFormat("yyyy.MM.dd_HH-mm").format(fileCreationDate) + ".mp3";
            newFileNameField.setText(newFileName);
            estimatedSizeLabel.setText(estimateFileSizeWithBitrate(sourceFileDuration, bitRate));
        } catch (EncoderException e) {
            e.printStackTrace();
        }
    }

    public void deleteSourceFileIfNeeded() {
        if (deleteSourceFile) {
            sourceFile.delete();
        }
    }

    public int getBitRate() {
        return bitRate;
    }

    public void setBitRate(int bitRate) {
        this.bitRate = bitRate;
    }

    public int getChannels() {
        return channels;
    }

    public void setChannels(int channels) {
        this.channels = channels;
    }

    public int getSamplingRate() {
        return samplingRate;
    }

    public void setSamplingRate(int samplingRate) {
        this.samplingRate = samplingRate;
    }

    public File getSourceFile() {
        return sourceFile;
    }

    public void setSourceFile(File sourceFile) {
        this.sourceFile = sourceFile;
    }

    public long getSourceFileDuration() {
        return sourceFileDuration;
    }

    public void setSourceFileDuration(long sourceFileDuration) {
        this.sourceFileDuration = sourceFileDuration;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public String getDestinationPath() {
        return destinationPath;
    }

    public void setDestinationPath(String destinationPath) {
        this.destinationPath = destinationPath;
    }

    public String getNewFileName() {
        return newFileName;
    }

    public void setNewFileName(String newFileName) {
        this.newFileName = newFileName;
    }

    public boolean isDeleteSourceFile() {
        return deleteSourceFile;
    }

    public void setDeleteSourceFile(boolean deleteSourceFile) {
        this.deleteSourceFile = deleteSourceFile;
    }
}
