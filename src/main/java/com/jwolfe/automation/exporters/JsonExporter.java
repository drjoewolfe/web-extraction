package com.jwolfe.automation.exporters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwolfe.automation.types.Extract;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class JsonExporter implements Exporter {
    private static final Logger logger = LogManager.getLogger("JsonExporter");

    public boolean Export(Extract extract, String outputFilePath, boolean appendToFile, boolean backupExistingFile) {
        logger.info("Exporting extract to " + outputFilePath);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(df);

        StringBuilder json = new StringBuilder();
        try {
            json.append(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(extract));
        } catch (JsonProcessingException e) {
            logger.error("Error converting the extract to JSON");
            e.printStackTrace();
            return false;
        }

        Path path = Paths.get(outputFilePath);
        if(backupExistingFile & Files.exists(path)) {
            Path absolutePath = path.toAbsolutePath();
            String basePath = absolutePath.getParent().toString();
            String fileName = absolutePath.getFileName().toString();
            String baseName = FilenameUtils.getBaseName(fileName);
            String extension = FilenameUtils.getExtension(fileName);
            String dateStamp = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date());

            String backupFileName = baseName + " " + dateStamp + "." + extension;
            String backupFilePath = basePath + "/backups/" + backupFileName;

            try {
                logger.info("Backing up previous extract to " + backupFilePath);
                new File(backupFilePath).getParentFile().mkdirs();
                Files.copy(absolutePath, Paths.get(backupFilePath));
            } catch (IOException e) {
                logger.error("Error backing up the previous extract");
                e.printStackTrace();
                return false;
            }
        }

        try {
            Path outFile = Paths.get(outputFilePath);

            StringBuilder fileFragment = new StringBuilder();
            if(!Files.exists(outFile) || !appendToFile) {
                // Fresh file required.
                fileFragment.append("[ ");
                fileFragment.append(json.toString());
                fileFragment.append(" ]");
            }
            else {
                // File exists & need to append to existing file - Remove the last array closure ("]") & add comma (",")
                String currentContents = new Scanner(new File(outputFilePath)).useDelimiter("\\Z").next();
                fileFragment.append(currentContents.substring(0, currentContents.length() - 2));
                fileFragment.append(",\n");
                fileFragment.append(json.toString());
                fileFragment.append(" ]");
            }

            Files.write(Paths.get(outputFilePath), fileFragment.toString().getBytes());
        } catch (IOException e) {
            logger.error("Error exporting the extract");
            e.printStackTrace();
            return false;
        }

        logger.info("Extract export succeeded");
        return true;
    }
}
