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
package it.tn.rivadelgarda.comune.archivio.protocollo;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.annotations.Private;
import it.tn.rivadelgarda.comune.archivio.ProfiloUtente;
import it.tn.rivadelgarda.comune.archivio.base.entities.IUtente;
import it.tn.rivadelgarda.comune.archivio.base.entities.Utente;
import it.tn.rivadelgarda.comune.archivio.protocollo.entities.Protocollo;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 *     Modificato da comune di Riva del Garda
 */
public class ProtocolloPrivate {

    @Private
    public static Boolean protocolloPrivato(Protocollo protocollo) {

        // Se il protocollo non è registrato, sicuramente non è privato
        if (protocollo.getId() == null) {
            return false;
        }

        // Profilo dell'utente
        Utente utente = (Utente) Register.queryUtility(IUtente.class);

        if (utente.getSupervisoreprotocollo() || utente.getRicercatoreprotocollo()) {
            return false;
        }

        ProfiloUtente profilo = new ProfiloUtente();
        if (profilo.inSportello(protocollo, utente)) {
            return false;
        }

        return true;
    }
}