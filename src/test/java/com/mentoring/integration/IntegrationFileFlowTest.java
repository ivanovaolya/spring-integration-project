package com.mentoring.integration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;

import com.mentoring.Application;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.aspectj.weaver.tools.cache.SimpleCacheFactory.path;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author ivanovaolyaa
 * @version 11/13/2017
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class IntegrationFileFlowTest {

    @Autowired
    private File inboundFileDirectory;

    @Autowired
    private File outboundFileDirectory;

    private final String FILE_NAME = "results";
    private final String FILE_TXT_EXT = ".txt";
    private final String FILE_LOG_EXT = ".log";
    private final String FILE_MESSAGE = "Test string";

    @Before
    public void setUp() throws Exception {
        try {
            Files.delete(new File("outbound/results.txt").toPath());
        } catch (NoSuchFileException x) {
            System.err.format("%s: no such" + " file or directory%n", path);
        } catch (DirectoryNotEmptyException x) {
            System.err.format("%s not empty%n", path);
        } catch (IOException x) {
            System.err.println(x);
        }
    }

    @Test
    public void testShouldProcessTxtFile() throws Exception {
        final File file = new File(inboundFileDirectory.getPath() + "/" + FILE_NAME + FILE_TXT_EXT);
        file.createNewFile();
        try (Writer writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(FILE_MESSAGE);
        }

        Thread.sleep(1500); // wait for txt-file to be processed

        assertNotNull(outboundFileDirectory.list((dir, name) ->
                (name.startsWith(FILE_NAME) && name.endsWith(FILE_TXT_EXT))));

        Thread.sleep(1500);

        assertEquals(0, inboundFileDirectory.list((dir, name) ->
                (name.startsWith(FILE_NAME) && name.endsWith(FILE_TXT_EXT))).length);

        assertEquals(FILE_MESSAGE, readFromFile(outboundFileDirectory.getPath() + "/" + FILE_NAME + FILE_TXT_EXT));
    }

    @Test
    public void testShouldNotProcessLogFile() throws Exception {
        final File file = new File(inboundFileDirectory.getPath() + "/" + FILE_NAME + FILE_LOG_EXT);
        file.createNewFile();

        Thread.sleep(1500);

        assertEquals(0, outboundFileDirectory.list((dir, name) ->
                (name.startsWith(FILE_NAME) && name.endsWith(FILE_LOG_EXT))).length);

        assertNotNull(inboundFileDirectory.list((dir, name) ->
                (name.startsWith(FILE_NAME) && name.endsWith(FILE_LOG_EXT))));
    }

    private String readFromFile(final String path) {
        final File file = new File(path);
        final StringBuilder payload = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                payload.append(currentLine);
            }
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }

        return payload.toString();
    }
}
