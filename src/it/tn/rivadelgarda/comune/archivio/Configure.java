/*
 * Copyright (C) 2013 AXIA Studio (http://www.axiastudio.com)
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
 * You should have received a copy of the GNU Afffero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package it.tn.rivadelgarda.comune.archivio;

import com.axiastudio.pypapi.db.Database;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
public class Configure {

    public static void configure(Database db){

        adapters();
        callbacks();
        privates();

        forms(db);
        plugins();
        templates();

    }

    private static void adapters() {
    }

    private static void callbacks() {
    }

    private static void privates() {
    }

    private static void plugins() {

    }

    private static void templates() {

    }

    private static void forms(Database db) {

    }
    
}
