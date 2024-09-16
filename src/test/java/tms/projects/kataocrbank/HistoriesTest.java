/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package tms.projects.kataocrbank;

import java.io.IOException;
import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author A1047782
 */
public class HistoriesTest {

    String scannedFile = "src/main/resources/";

    public HistoriesTest() {
    }

    @Test
    public void History1() throws IOException {
        String result = Histories.hostoriy1(scannedFile + "scanned_file_h1");
        Assert.assertEquals("123456789", result);
    }

    @Test
    public void History3() throws IOException {
        String result = Histories.hostoriy3(scannedFile + "scanned_file_h3");
        Assert.assertEquals("1234?678? ILL", result);
    }
    
    
    @Test
    public void History4() throws IOException {
        String result = Histories.hostoriy4(scannedFile + "scanned_file_h4");
        Assert.assertEquals("888888888 AMB [888886888, 888888988, 888888880]", result);
    }
}
