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
            for (UfficioUtente uu : utente.getUfficioUtenteCollection()) {
                if (uu.getUfficio().equals(protocollo.getTSportello())) {
                    return true;
                }
            }
        }
        return false;
    }

}
