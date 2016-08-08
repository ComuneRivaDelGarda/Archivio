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
package it.tn.rivadelgarda.comune.archivio;

import it.tn.rivadelgarda.comune.archivio.base.entities.UfficioUtente;
import it.tn.rivadelgarda.comune.archivio.base.entities.Utente;
import it.tn.rivadelgarda.comune.archivio.protocollo.entities.Protocollo;

/**
 * Created by Comune di Riva del Garda on 29/10/2014.
 */
public class ProfiloUtente {

    public boolean inSportello(Protocollo protocollo, Utente utente) {
        if (utente.getUfficioUtenteCollection() != null) {
            for (UfficioUtente uu : utente.getUfficioUtenteCollection()) {
                if (uu.getUfficio().equals(protocollo.getSportello())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean inTSportello(Protocollo protocollo, Utente utente) {
        if (utente.getUfficioUtenteCollection() != null) {
            if ( protocollo.getTSportello() == null ) {
                protocollo.setTSportello(protocollo.getSportello());
            }
            for (UfficioUtente uu : utente.getUfficioUtenteCollection()) {
                if (uu.getUfficio().equals(protocollo.getTSportello())) {
                    return true;
                }
            }
        }
        return false;
    }
}
