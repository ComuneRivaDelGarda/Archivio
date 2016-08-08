/*
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
package it.tn.rivadelgarda.comune.archivio.pratiche;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.annotations.Callback;
import com.axiastudio.pypapi.annotations.CallbackType;
import com.axiastudio.pypapi.db.Database;
import com.axiastudio.pypapi.db.IDatabase;
import com.axiastudio.pypapi.db.Validation;
import it.tn.rivadelgarda.comune.archivio.base.entities.IUtente;
import it.tn.rivadelgarda.comune.archivio.base.entities.Utente;
import it.tn.rivadelgarda.comune.archivio.pratiche.entities.Pratica;

import javax.persistence.EntityManager;

/**
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 * adattato da Comune di Riva del Garda
 */
public class PraticaCallbacks {
    
    @Callback(type=CallbackType.BEFORECOMMIT)
    public static Validation validaPratica(Pratica pratica){
        String msg = "";
        Boolean res = true;
        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
        Boolean inUfficioGestore = PraticaUtil.utenteInGestorePraticaMod(pratica, autenticato);

        // se l'utente non è istruttore non può inserire o modificare pratiche,
        if( !autenticato.getIstruttorepratiche() ){
            msg = "Devi avere come ruolo \"istruttore\" per poter inserire\n";
            msg += "o modificare una pratica.";
            return new Validation(false, msg);
        }
                        
        // devono essese definite attribuzione e tipologia
        if( pratica.getAttribuzione() == null ){
            msg = "Devi selezionare un'attribuzione.";
            return new Validation(false, msg);
        } else if( pratica.getTipo() == null ){
            msg = "Devi selezionare un tipo di pratica.";
            return new Validation(false, msg);
        }


        if( pratica.getId() == null ){
            Database db = (Database) Register.queryUtility(IDatabase.class);
            EntityManager em = db.getEntityManagerFactory().createEntityManager();

            // se codifica pratica non ha progressivo, si controlla se la pratica non esiste già
            if (!PraticaUtil.codificaInternaUnivoca(pratica.getCodiceinterno())) {
                msg = "Esiste già una pratica con la codifica specificata.";
                return new Validation(false, msg);
            }

        } else {
            // l'amministratore pratiche modifica anche se non appartenente all'ufficio gestore e
            // anche se la pratica è archiviata.
            if( !autenticato.getSupervisorepratiche() ){
                // se l'utente non è inserito nell'ufficio gestore con flag modificapratiche non può modificare
                if( !inUfficioGestore ){
                    msg = "Per modificare la pratica devi appartenere all'ufficio gestore con i permessi di modifica, ed eventuali privilegi sulle pratiche riservate.";
                    return new Validation(false, msg);
                }
                // impossibile togliere gli uffici
                if( pratica.getGestione() == null || pratica.getAttribuzione() == null || pratica.getUbicazione() == null){
                    msg = "Non è permesso rimuovere attribuzione, gestione o ubicazione.";
                    return new Validation(false, msg);
                }
                // Se la pratica è archiviata, non posso modificarla, ma ciò viene implementato con il cambio di ufficio gestore
            }
        }

        return new Validation(true);
    }
}
