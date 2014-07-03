/*
 * Copyright (C) 2013 Comune di Riva del Garda
 * (http://www.comune.rivadelgarda.tn.it)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package it.tn.rivadelgarda.comune.archivio;

import com.axiastudio.pypapi.Application;
import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.Resolver;
import com.axiastudio.pypapi.db.Database;
import com.axiastudio.pypapi.db.IDatabase;
import com.axiastudio.pypapi.ui.Window;
import it.tn.rivadelgarda.comune.archivio.entities.Protocollo;
import it.tn.rivadelgarda.comune.archivio.forms.ProtocolloCallbacks;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 * 
 */
public class Start {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Application app = new Application(args);
        InputStream propertiesStream = Start.class.getResourceAsStream("gda.properties");

        app.setLanguage("it");

        Database db = (Database) Register.queryUtility(IDatabase.class);
        configure(app, propertiesStream);


        Mdi mdi = new Mdi();
        mdi.showMaximized();
        mdi.setWindowTitle("Archivio");
        mdi.show();

        app.setCustomApplicationName("Archivio");
        app.exec();

        System.exit(0);
    }

    protected static void configure(Application app, InputStream propertiesStream) {

        String jdbcUrl = null;
        String jdbcUser = null;
        String jdbcPassword = null;
        String jdbcDriver = null;

        String logLevel = null;

        // file di Properties
        Properties properties = new Properties();
        try {
            if( propertiesStream != null ) {
                properties.load(propertiesStream);

                jdbcUrl = properties.getProperty("jdbc.url");
                jdbcUser = properties.getProperty("jdbc.user");
                jdbcPassword = properties.getProperty("jdbc.password");
                jdbcDriver = properties.getProperty("jdbc.driver");

                logLevel = properties.getProperty("suite.loglevel");
            }
        } catch (IOException e) {
            String message = "Unable to read properties file: " + propertiesStream;
            Logger.getLogger(Start.class.getName()).log(Level.WARNING, message, e);
        }

        /*
         *  OVerriding della configurazione
         */

        // jdbc
        if( System.getProperty("jdbc.url") != null ) {
            jdbcUrl = System.getProperty("jdbc.url");
        }
        if( System.getProperty("jdbc.user") != null ) {
            jdbcUser = System.getProperty("jdbc.user");
        }
        if( System.getProperty("jdbc.password") != null ) {
            jdbcPassword = System.getProperty("jdbc.password");
        }
        if( System.getProperty("jdbc.driver") != null ) {
            jdbcDriver = System.getProperty("jdbc.driver");
        }
        // log
        if( System.getProperty("suite.loglevel") != null ) {
            logLevel = System.getProperty("suite.loglevel");
        }

        Map mapProperties = new HashMap();
        mapProperties.put("javax.persistence.jdbc.url", jdbcUrl);
        if( jdbcUser != null ){
            mapProperties.put("javax.persistence.jdbc.user", jdbcUser);
        }
        if( jdbcPassword != null ){
            mapProperties.put("javax.persistence.jdbc.password", jdbcPassword);
        }
        if( jdbcDriver != null ){
            mapProperties.put("javax.persistence.jdbc.driver", jdbcDriver);
        }
        if( logLevel != null ){
            mapProperties.put("eclipselink.logging.level", logLevel);
            mapProperties.put("eclipselink.logging.parameters", "true");
        }

        Database db = new Database();
        mapProperties.put("eclipselink.ddl-generation", "");
        db.open("SuitePU", mapProperties);
        Register.registerUtility(db, IDatabase.class);

        // jdbc
        app.setConfigItem("jdbc.url", jdbcUrl);

        // Protocollo
        Register.registerForm
                (
                db.getEntityManagerFactory(),
                "classpath:it/tn/rivadelgarda/comune/archivio/forms/Protocollo.ui",
                Protocollo.class,
                Window.class
                );

        Register.registerCallbacks(Resolver.callbacksFromClass(ProtocolloCallbacks.class));

    }

}
