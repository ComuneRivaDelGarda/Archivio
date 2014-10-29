package it.tn.rivadelgarda.comune.archivio.protocollo;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.annotations.Callback;
import com.axiastudio.pypapi.annotations.CallbackType;
import com.axiastudio.pypapi.db.Validation;
import it.tn.rivadelgarda.comune.archivio.ProfiloUtente;
import it.tn.rivadelgarda.comune.archivio.base.entities.IUtente;
import it.tn.rivadelgarda.comune.archivio.base.entities.Utente;
import it.tn.rivadelgarda.comune.archivio.protocollo.entities.Protocollo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Comune di riva del Garda on 02/07/14.
 */
public class ProtocolloCallbacks
{
    @Callback(type= CallbackType.BEFORECOMMIT)
    public static Validation beforeCommit(Protocollo protocollo) throws ParseException
    {
        Boolean res = true;
        String anno = "";
        String msg = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final Date maxData = sdf.parse("1996-05-25");
        final String maxProtocollo = "199600012454";

        // L'utente può salvare se supervisore, ricercatore o e inserito nell'ufficio 'sportello'
        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
        ProfiloUtente profilo = new ProfiloUtente();
        if ( !(autenticato.getSupervisoreprotocollo() || autenticato.getRicercatoreprotocollo() ||
                profilo.inTSportello(protocollo, autenticato))) {
            msg += "Non si ha l'autorizzazione per salvare il protocollo.\n";
            return new Validation(false, msg);
        }

        //Controllo che sportello non sia vuoto
        if (protocollo.getSportello() == null)
        {
            msg += "Deve essere dichiarato uno sportello ricevente.\n";
            res = false;
        }

        //Controllo che oggetto non sia vuoto
        if( protocollo.getOggetto() == null || protocollo.getOggetto().isEmpty())
        {
            msg += "Devi compilare l'oggetto. \n";
            res = false;
        }

        //Controllo che dataprotocollo non sia vuoto
        if( protocollo.getDataprotocollo() == null )
        {
            msg += "Devi specificare la data. \n";
            res = false;
        }
        else
        {
            DateFormat df = new SimpleDateFormat("yyyy");
            anno = df.format(protocollo.getDataprotocollo());
        }

        //Controllo lunghezza iddocumento
        String iddoc = protocollo.getIddocumento();
        if (iddoc.length() != 12)
        {
            msg += "Lunghezza idprotocollo errata. \n";
            res = false;
        }

        //controllo che sia specificato l'anno in idprotocollo
        if ( !iddoc.substring(2,4).equals(anno.substring(2,4)) )
        {
            msg += "Anno sbagliato/errore idprotocollo. \n";
            res = false;
        }
        else
            protocollo.setAnno(Integer.parseInt(anno));

        //controllo range anno
        if(  protocollo.getDataprotocollo().after(maxData) )
        {
            SimpleDateFormat sdfMsg = new SimpleDateFormat("dd/MM/yyyy");
            msg += "Data maggiore di " + sdfMsg.format(maxData) + ". \n";
            res = false;
        }

        if(  (Long.parseLong(protocollo.getIddocumento()) >= Long.parseLong(maxProtocollo) ) )
        {
            msg += "idprotocollo maggiore o uguale di " + maxProtocollo + ". \n";
            res = false;
        }

        if (protocollo.getConvalidaprotocollo() != null && protocollo.getConvalidaprotocollo())
        {
            msg += "Protocollo in sola lettura! \n";
            res = false;
        }

        if(res == false )
        {
            return new Validation(false, msg);
        }
        else
        {
            return new Validation(true);
        }
    }
}
