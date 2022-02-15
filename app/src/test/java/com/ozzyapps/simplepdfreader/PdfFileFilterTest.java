package com.ozzyapps.simplepdfreader;

import junit.framework.TestCase;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class PdfFileFilterTest extends TestCase {


    String workingDir = System.getProperty("user.dir");
    String currentDir = workingDir + "\\src\\test\\java\\com\\ozzyapps\\simplepdfreader\\resources";

    public void testDisplayDirectoryContents() {
        PdfFileFilter pdfFileFilter = new PdfFileFilter(currentDir);

        File dir = new File(currentDir);

        pdfFileFilter.displayDirectoryContents(dir);
    }

    public void testFilterDirectory() {
        PdfFileFilter filefilter = new PdfFileFilter(currentDir);
        System.out.println(Arrays.toString(filefilter.filterDirectory()));

        assertEquals(Arrays.toString(filefilter.filterDirectory()), "[test.pdf, test2.pdf]");
    }
}