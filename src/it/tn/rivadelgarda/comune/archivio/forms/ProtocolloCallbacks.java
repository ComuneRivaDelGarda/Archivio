package it.tn.rivadelgarda.comune.archivio.forms;

import com.axiastudio.pypapi.annotations.Callback;
import com.axiastudio.pypapi.annotations.CallbackType;
import com.axiastudio.pypapi.db.Validation;
import com.axiastudio.pypapi.ui.BooleanItemField;
import com.axiastudio.pypapi.ui.StringItemField;
import it.tn.rivadelgarda.comune.archivio.entities.Protocollo;
import sun.plugin2.message.Message;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by utente on 02/07/14.
 */
public class ProtocolloCallbacks
{
    @Callback(type= CallbackType.BEFORECOMMIT)
    public static Validation beforeCommit(Protocollo protocollo)
    {
        Boolean res = true;
        String anno = "";
        String msg = "";

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
            msg += "Anno sbagliato. \n";
            res = false;
        }
        else
            protocollo.setAnno(Integer.parseInt(anno));



        if(res == false ){
            return new Validation(false, msg);
        } else {
            return new Validation(true);
        }
    }
}
