/*
 * Copyright (C) 2012 AXIA Studio (http://www.axiastudio.com)
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
package it.tn.rivadelgarda.comune.archivio.protocollo.forms;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.*;
import com.axiastudio.pypapi.ui.*;
import it.tn.rivadelgarda.comune.archivio.base.entities.IUtente;
import it.tn.rivadelgarda.comune.archivio.base.entities.Ufficio;
import it.tn.rivadelgarda.comune.archivio.base.entities.UfficioUtente;
import it.tn.rivadelgarda.comune.archivio.base.entities.Utente;
import com.trolltech.qt.gui.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 *     Modificato da Comune di Riva del Garda
 */
public class FormProtocollo extends Window {
    private QTabWidget tabWidget;
    Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
    
    public FormProtocollo(FormProtocollo form){
        super(form.uiFile, form.entityClass, form.title);
    }
    
    public FormProtocollo(String uiFile, Class entityClass, String title){
        super(uiFile, entityClass, title);

        try {
            Method storeFactory = this.getClass().getMethod("storeSportello");
            Register.registerUtility(storeFactory, IStoreFactory.class, "Sportello");
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(FormProtocollo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(FormProtocollo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
     * Uno store contenente solo gli uffici dell'utente
     */
    public Store storeSportello(){
        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
        List<Ufficio> uffici = new ArrayList();
        for(UfficioUtente uu: autenticato.getUfficioUtenteCollection()){
            if( uu.getUfficio().getSportello() ){
                uffici.add(uu.getUfficio());
            }
        }
        return new Store(uffici);
    }

}
