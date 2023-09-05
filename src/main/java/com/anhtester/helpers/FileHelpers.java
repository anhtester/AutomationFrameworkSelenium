/*
 * Copyright (c) 2022 Anh Tester
 * Automation Framework Selenium
 */

package com.anhtester.helpers;

import com.anhtester.utils.LogUtils;

import java.io.*;
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

    public static void writeTxtFile(String filepath, String text) {
        try {
            File file = new File(filepath);
            while (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(text + "\n" + "\n");
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readTxtFile(String filepath) {
        try {
            File f = new File(filepath);
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            fr.close();
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readLineTxtFile(String filepath, int line) {
        List<String> lines;
        String value;
        try {
            lines = Files.readAllLines(new File(filepath).toPath());
            value = lines.get(line);
            return value;
        } catch (IOException e) {
            throw new RuntimeException(e);
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
