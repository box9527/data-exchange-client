package com.dataexchange.client.sftp;

import com.dataexchange.client.config.model.MainConfiguration;
import com.dataexchange.client.config.model.DownloadPollerConfiguration;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;

public abstract class SftpDownloadTestAbstract extends SftpTestServer {

    @Autowired
    MainConfiguration config;

    String realRemoteFolder;
    File remoteSourceFile;
    String outputFolder;
    File outputFile;
    DownloadPollerConfiguration downloadPoller;

    @Before
    public void setup() throws Exception {
        super.setup();
        downloadPoller = config.getSftps().get(0).getDownloadPollers().get(0);
        outputFolder = downloadPoller.getOutputFolder();
        realRemoteFolder = sftpRootFolder.toString() + File.separator + downloadPoller.getRemoteInputFolder();

        FileUtils.forceMkdir(new File(realRemoteFolder));
        FileUtils.forceMkdir(new File(outputFolder));

        cleanupWorkingDirs();

        remoteSourceFile = File.createTempFile("anyfile", ".txt", new File(realRemoteFolder));
        outputFile = new File(outputFolder + File.separator + remoteSourceFile.getName());
    }

    private void cleanupWorkingDirs() throws IOException {
        FileUtils.cleanDirectory(new File(outputFolder));
    }

    @After
    public void teardown() throws Exception {
        super.teardown();
        cleanupWorkingDirs();
    }
}
