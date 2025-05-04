/*
 * Copyright (c) 2022 Anh Tester
 * Automation Framework Selenium
 */

package com.anhtester.helpers;

import com.anhtester.utils.LogUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class FileHelpers {

    public FileHelpers() {
        super();
    }

    public static String readFile(String filePath) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            char[] buffer = new char[8192];
            int read;
            while ((read = reader.read(buffer)) != -1) {
                content.append(buffer, 0, read);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return content.toString();
    }

    public static void writeTxtFile(String filepath, String text) {
        File file = new File(filepath);

        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            // Thay mọi kiểu newline về chuẩn Windows \r\n
            String normalizedText = text.replaceAll("\\r?\\n", "\r\n");

            try (BufferedWriter bw = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(file, true), StandardCharsets.UTF_8))) {
                bw.write(normalizedText);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readTxtFile(String filepath) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(filepath), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    public static String readLineTxtFile(String filepath, int lineNumber) {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(filepath), StandardCharsets.UTF_8))) {

            String line;
            int currentLine = 0;

            while ((line = br.readLine()) != null) {
                if (currentLine == lineNumber) {
                    return line;
                }
                currentLine++;
            }

            throw new IndexOutOfBoundsException("Dòng số " + lineNumber + " không tồn tại trong file.");

        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi đọc file: " + e.getMessage(), e);
        }
    }

    public static void copyFile(String source_FilePath, String target_FilePath) {
        try {
            Files.copy(Paths.get(source_FilePath), Paths.get(target_FilePath), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static File getFileLastModified(String folderPath) {
        File dir = new File(folderPath);
        if (dir.isDirectory()) {
            Optional<File> opFile = Arrays.stream(dir.listFiles(File::isFile)).max((f1, f2) -> Long.compare(f1.lastModified(), f2.lastModified()));
            if (opFile.isPresent()) {
                LogUtils.info("getFileLastModified: " + opFile.get().getPath());
                return opFile.get();
            } else {
                LogUtils.info("getFileLastModified: " + opFile.get().getPath());
                return null;
            }
        }

        return null;
    }

}
