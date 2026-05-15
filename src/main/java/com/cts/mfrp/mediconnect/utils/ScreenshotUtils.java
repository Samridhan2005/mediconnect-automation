package com.cts.mfrp.mediconnect.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ScreenshotUtils {

    private static final DateTimeFormatter STAMP =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");

    private ScreenshotUtils() {
    }

    /**
     * Capture a PNG screenshot of the current driver state into the configured screenshot folder.
     * @return absolute path of the saved file, or null if no driver / capture failed.
     */
    public static String capture(String testName) {
        WebDriver driver;
        try {
            driver = DriverManager.getDriver();
        } catch (IllegalStateException noDriver) {
            return null; // API-only test, nothing to capture
        }

        try {
            byte[] png = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            String dir = ConfigReader.get("screenshot.path");
            new File(dir).mkdirs();
            Path target = Path.of(dir, testName + "_" + STAMP.format(LocalDateTime.now()) + ".png");
            Files.write(target, png);
            return target.toAbsolutePath().toString();
        } catch (IOException io) {
            System.err.println("Screenshot save failed: " + io.getMessage());
            return null;
        }
    }
}
