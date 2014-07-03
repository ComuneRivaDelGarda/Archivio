package it.tn.rivadelgarda.comune.archivio.forms;

import com.axiastudio.pypapi.annotations.Callback;
import com.axiastudio.pypapi.annotations.CallbackType;
import com.axiastudio.pypapi.db.Validation;
import com.axiastudio.pypapi.ui.BooleanItemField;
import com.axiastudio.pypapi.ui.IntegerItemField;
import com.axiastudio.pypapi.ui.StringItemField;
import it.tn.rivadelgarda.comune.archivio.entities.Protocollo;
import sun.plugin2.message.Message;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by utente on 02/07/14.
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
            System.out.println(protocollo.getDataprotocollo().toString());
            msg += "Devi specificare la data. \n";
            res = false;
        }
        else
        {
            DateFormat df = new SimpleDateFormat("yyyy");
            anno = df.format(protocollo.getDataprotocollo());
            // System.out.println(anno);
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
            msg += "Data maggiore di 25/05/1996. \n";
            res = false;
        }
        /*
        if( ! (protocollo.getIddocumento().compareTo(maxProtocollo) == -1) )
        {
            msg += "idprotocollo maggiore di 199600012454. \n";
            res = false;
        }
        */
        if(  (Long.parseLong(protocollo.getIddocumento()) >= Long.parseLong(maxProtocollo) ) )
        {
            msg += "idprotocollo maggiore o uguale di 199600012454. \n";
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
