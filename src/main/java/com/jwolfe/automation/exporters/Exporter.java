package com.jwolfe.automation.exporters;

import com.jwolfe.automation.types.Extract;

public interface Exporter {
    boolean export(Extract extract, String outputFilePath, boolean appendToFile, boolean backupExistingFile);
}
