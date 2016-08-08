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
import com.axiastudio.pypapi.db.Database;
import com.axiastudio.pypapi.db.IDatabase;
import it.tn.rivadelgarda.comune.archivio.base.entities.UfficioUtente;
import it.tn.rivadelgarda.comune.archivio.base.entities.Utente;
import it.tn.rivadelgarda.comune.archivio.pratiche.entities.Pratica;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 * adattato da Comune di Riva del Garda
 */
public class PraticaUtil {

    // Verifica se esiste già una pratica con il codice specificato, x tipologie senza progressivo
    public static Boolean codificaInternaUnivoca(String codifica){
        if (codifica == null || codifica.length()<4) {
            return Boolean.FALSE;
        }
        Database db = (Database) Register.queryUtility(IDatabase.class);
        EntityManager em = db.getEntityManagerFactory().createEntityManager();
        Query q = em.createQuery("select count(p.codiceinterno) from Pratica p where p.codiceinterno = '" + codifica + "'");
        Long i = (Long) q.getSingleResult();
        return (i == 0);
    }

    public static Boolean utenteInGestorePratica(Pratica pratica, Utente autenticato) {
        Boolean inUfficioGestore = Boolean.FALSE;
        for( UfficioUtente uu: autenticato.getUfficioUtenteCollection() ){
            if( uu.getUfficio().equals(pratica.gettGestione()) ){
                // se la pratica è riservata, mi serve anche il flag
                if( !pratica.getRiservata() || uu.getRiservato() ){
                    inUfficioGestore = true;
                    break;
                }
            }
        }
        return inUfficioGestore;
    }

    public static Boolean utenteInGestorePraticaMod(Pratica pratica, Utente autenticato) {
        Boolean inUfficioGestore = Boolean.FALSE;
        for( UfficioUtente uu: autenticato.getUfficioUtenteCollection() ){
            if( uu.getUfficio().equals(pratica.gettGestione()) && uu.getModificapratica() ){
                // se la pratica è riservata, mi serve anche il flag
                if( !pratica.getRiservata() || uu.getRiservato() ){
                    inUfficioGestore = true;
                    break;
                }
            }
        }
        return inUfficioGestore;
    }
}
