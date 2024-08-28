/*******************************************************************************
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.cifwk.tm.infrastructure;


import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public final class SchemaDump {

    private final Logger logger = LoggerFactory.getLogger(SchemaDump.class);

    public static void main(String[] args) {
        new SchemaDump().run();
    }

    private void run() {
        File dbFile = new File("dump.mv.db");
        logger.info("Dumping schema at " + dbFile.getAbsolutePath());
        dbFile.delete();

        String url = "jdbc:h2:./dump;MODE=MySQL;INIT=RUNSCRIPT FROM 'classpath:db/dev/init.sql'";
        Flyway flyway = new Flyway();
        PersistenceSetup persistenceSetup = new PersistenceSetup();
        long now = System.currentTimeMillis();
        persistenceSetup.setup(flyway, url, "sa", null, "dev");
        logger.info("Schema dump completed in {} sec", (System.currentTimeMillis() - now) / 1000.0);
    }

}
