package com.ozzyapps.simplepdfreader;


import java.io.File;
import java.io.FileFilter;

import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PdfFileFilter {

    private final String pathName;

    public PdfFileFilter(String pathName){
        this.pathName = pathName;
    }

    public List<File> displayDirectoryContents(File dir) {
        ArrayList<File> rtnFiles = new ArrayList<File>();

        File[] files = dir.listFiles();

        for (File file : files) {

            if (file.isDirectory()) {
                rtnFiles.addAll(displayDirectoryContents(file));

            } else {
                if (file.getName().endsWith((".pdf"))) {
//                    System.out.println("     file:" + file.toURI());
                    rtnFiles.add(file);
                }
            }
        }

        return rtnFiles;
    }

    public String[] filterDirectory(){

        File dir = new File(pathName);

        String[] names = dir.list(
                new FilenameFilter()
                {
                    public boolean accept(File dir, String name)
                    {
                        return name.endsWith(".pdf");
                        // Example
                        // return name.endsWith(".mp3");
                    }
                });

        return names;
    }

}