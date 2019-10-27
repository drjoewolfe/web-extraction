package com.jwolfe.automation.exporters;

import com.jwolfe.automation.types.Extract;

public interface Exporter {
    boolean Export(Extract extract, String outputFilePath, boolean appendToFile, boolean backupExistingFile);
}
