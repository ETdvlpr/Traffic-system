/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.types;

/**
 *
 * @author dave
 */
public class Video {

    /**
     * @return the video
     */
    public String getVideo() {
        return video;
    }

    /**
     * @param video the video to set
     */
    public void setVideo(String video) {
        this.video = video;
    }

    /**
     * @return the xml
     */
    public String getXml() {
        return xml;
    }

    /**
     * @param xml the xml to set
     */
    public void setXml(String xml) {
        this.xml = xml;
    }

    /**
     * @return the file
     */
    public String getMaskFile() {
        return mask_file;
    }

    /**
     * @param file the file to set
     */
    public void setMaskFile(String file) {
        this.mask_file = file;
    }

    private String video, xml, mask_file, description;

    public Video(String video, String xml, String mask_file, String description) {
        this.video = video;
        this.xml = xml;
        this.mask_file = mask_file;
        this.description = description;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
