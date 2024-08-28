package com.ericsson.cifwk.tm.integration.fileStorage;

import org.apache.poi.util.IOUtils;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by egergle on 20/07/2015.
 */

public class FileStreaming {

    public StreamingOutput stream(File file) {

        final File fileToStream = file;

        return new StreamingOutput() {
            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {
                IOUtils.copy(new FileInputStream(fileToStream), os);
                os.flush();
                os.close();
            }
        };
    }

}
