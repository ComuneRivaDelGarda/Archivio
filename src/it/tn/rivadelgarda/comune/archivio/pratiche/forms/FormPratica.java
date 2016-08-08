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
package it.tn.rivadelgarda.comune.archivio.pratiche.forms;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.*;
import com.axiastudio.pypapi.ui.*;
import com.axiastudio.pypapi.ui.widgets.PyPaPiComboBox;
import com.trolltech.qt.gui.*;
import it.tn.rivadelgarda.comune.archivio.base.entities.IUtente;
import it.tn.rivadelgarda.comune.archivio.base.entities.Ufficio;
import it.tn.rivadelgarda.comune.archivio.base.entities.UfficioUtente;
import it.tn.rivadelgarda.comune.archivio.base.entities.Utente;
import it.tn.rivadelgarda.comune.archivio.pratiche.entities.Fase;
import it.tn.rivadelgarda.comune.archivio.pratiche.entities.FasePratica;
import it.tn.rivadelgarda.comune.archivio.pratiche.entities.Pratica;
import it.tn.rivadelgarda.comune.archivio.pratiche.entities.TipoPratica;
import it.tn.rivadelgarda.comune.archivio.protocollo.entities.Fascicolo;
import it.tn.rivadelgarda.comune.archivio.protocollo.forms.FormTitolario;

import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 * adattato da Comune di Riva del Garda
 */
public class FormPratica extends Window {


    public FormPratica(String uiFile, Class entityClass, String title){
        super(uiFile, entityClass, title);

        /* fascicolazione */
        QToolButton toolButtonTitolario = (QToolButton) this.findChild(QToolButton.class, "toolButtonTitolario");
        toolButtonTitolario.setIcon(new QIcon("classpath:com/axiastudio/suite/resources/book_open.png"));
        toolButtonTitolario.clicked.connect(this, "apriTitolario()");

        try {
            Method storeFactory = this.getClass().getMethod("storeAttribuzione");
            Register.registerUtility(storeFactory, IStoreFactory.class, "Attribuzione");
            storeFactory = this.getClass().getMethod("storeTipo");
            Register.registerUtility(storeFactory, IStoreFactory.class, "Tipo");
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(FormPratica.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(FormPratica.class.getName()).log(Level.SEVERE, null, ex);
        }

        ((QComboBox) this.findChild(QComboBox.class, "comboBox_fase")).currentIndexChanged.connect(this, "aggiornaFase()");
    }


//  by FormProtocollo
    private void apriTitolario() {
        FormTitolario titolario = new FormTitolario();
        int exec = titolario.exec();
        if( exec == 1 ){
            Fascicolo selection = titolario.getSelection();
            PyPaPiComboBox comboBoxTitolario = (PyPaPiComboBox) this.findChild(PyPaPiComboBox.class, "comboBoxTitolario");
            comboBoxTitolario.select(selection);
            Pratica pratica = (Pratica) this.getContext().getCurrentEntity();
            pratica.setFascicolo(selection);
            aggiornaFascicolo(selection);
            this.getContext().getDirty();
        }
    }

    private void aggiornaFascicolo(Fascicolo fascicolo) {
        QLineEdit lineEdit_titolario = (QLineEdit) this.findChild(QLineEdit.class, "lineEditTitolario");
        QLineEdit lineEdit_desctitolario = (QLineEdit) this.findChild(QLineEdit.class, "lineEditDescTitolario");
        if (fascicolo == null) {
            lineEdit_titolario.setText("");
            lineEdit_titolario.hide();
            lineEdit_desctitolario.hide();
        } else {
            if (fascicolo.getAl() == null) {
                lineEdit_titolario.setText("");
                lineEdit_titolario.hide();
            } else {
                lineEdit_titolario.setText(fascicolo.toString());
                lineEdit_titolario.show();
            }
            lineEdit_desctitolario.setText(fascicolo.getDescTitolario());
            lineEdit_desctitolario.show();
        }
    }

    /*
     * Uno store contenente solo gli uffici dell'utente
     */
    public Store storeAttribuzione(){
        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
        List<Ufficio> uffici = new ArrayList<Ufficio>();
        for(UfficioUtente uu: autenticato.getUfficioUtenteCollection()){
            if( uu.getInseriscepratica() ){
                uffici.add(uu.getUfficio());
            }
        }
        return new Store(uffici);
    }

    /*
     * Uno store contenente gli oggetti ordinati x descrizione; solo tipologie 'foglie' e non obsolete
     */
    public Store storeTipo(){
        Database db = (Database) Register.queryUtility(IDatabase.class);
        Controller controller = db.createController(TipoPratica.class);
        Store storeTipo = controller.createFullStore();
        List<TipoPratica> oggetti = new ArrayList<TipoPratica>();
        for(Object ogg: storeTipo){
            if ( ((TipoPratica) ogg).getObsoleta().equals(Boolean.FALSE) && ((TipoPratica) ogg).getFoglia().equals(Boolean.TRUE) ) {
                oggetti.add((TipoPratica) ogg);
            }
        }
        Collections.sort(oggetti, TipoPratica.Comparators.CODICE);
        return new Store(oggetti);
    }

    /*
     * Uno store contenente gli oggetti ordinati x descrizione
     */
    public Store storeFase(){
        List<Fase> fasiprat = new ArrayList<Fase>();

        if (this.getContext() == null || this.getContext().getCurrentEntity() == null) {
            return new Store(fasiprat);
        }

        Pratica pratica = (Pratica) this.getContext().getCurrentEntity();
        if (pratica.getId() == null) {
            return new Store(fasiprat);
        }

        if (pratica.getFasePraticaCollection().isEmpty()) {
            Database db = (Database) Register.queryUtility(IDatabase.class);
            Controller controller = db.createController(Fase.class);
            Store storeFase = controller.createFullStore();
            for(Object ogg: storeFase){
                fasiprat.add((Fase) ogg);
            }
            Collections.sort(fasiprat, Fase.Comparators.DESCRIZIONE);
        } else {
            Fase fase = new Fase();
            for(FasePratica ogg: pratica.getFasePraticaCollection()){
                fasiprat.add((Fase) ogg.getFase());
            }
        }
        return new Store(fasiprat);
    }


    @Override
    protected void indexChanged(int row) {
        super.indexChanged(row);
        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
        Pratica pratica = (Pratica) this.getContext().getCurrentEntity();
        Boolean nuovoInserimento = pratica.getId() == null;

        // Abilitazione scelta della tipologia
        Util.setWidgetReadOnly((QWidget) this.findChild(QWidget.class, "comboBox_tipo"), !nuovoInserimento);
        ((QToolButton) this.findChild(QToolButton.class, "toolButtonTipo")).setEnabled(nuovoInserimento);
        
        // Se non sei nell'ufficio gestore, ti blocco l'ufficio gestore e il check riservato
        Boolean inUfficioGestore = false;
        for( UfficioUtente uu: autenticato.getUfficioUtenteCollection() ){
            if( uu.getUfficio().equals(pratica.getGestione()) && uu.getModificapratica() ){
                // se la pratica è riservata, mi serve anche il flag
                if( !pratica.getRiservata() || uu.getRiservato() ){
                    inUfficioGestore = true;
                    break;
                }
            }
        }
        ((QComboBox) this.findChild(QComboBox.class, "comboBox_attribuzione")).setEnabled(nuovoInserimento);
        ((QComboBox) this.findChild(QComboBox.class, "comboBox_gestione")).setEnabled(nuovoInserimento || inUfficioGestore);
        ((QCheckBox) this.findChild(QCheckBox.class, "checkBox_riservata")).setEnabled(nuovoInserimento || inUfficioGestore);

        // fascicolazione
        aggiornaFascicolo(pratica.getFascicolo());

        Store store = storeFase();
        PyPaPiComboBox fase=((PyPaPiComboBox) this.findChild(PyPaPiComboBox.class, "comboBox_fase"));
        fase.setLookupStore(store);
        this.getColumn("Fase").setLookupStore(store);
        fase.select(pratica.getFase());
        if ( pratica.getFase()!= null && pratica.getFase().getEvidenza() ) {
            fase.setStyleSheet("QComboBox{background: rgb(255, 0, 0);}");
        } else {
            fase.setStyleSheet("QComboBox{background: rgb(255, 255, 255);}");
        }
    }

    private void aggiornaFase() {
        if ( this.getContext()!=null ) {
            PyPaPiComboBox cmbFase=((PyPaPiComboBox) this.findChild(PyPaPiComboBox.class, "comboBox_fase"));
            if ( cmbFase.getCurrentEntity()!=null && ((Fase) cmbFase.getCurrentEntity()).getEvidenza() ) {
                cmbFase.setStyleSheet("QComboBox{background: rgb(255, 0, 0);}");
            } else {
                cmbFase.setStyleSheet("QComboBox{background: rgb(255, 255, 255);}");
            }
        }
    }

    private void aggiornaCodiceInterno() {
        if ( this.getContext()!=null ) {
            PyPaPiComboBox cmbTipo=((PyPaPiComboBox) this.findChild(PyPaPiComboBox.class, "comboBox_tipo"));
            if ( cmbTipo.getCurrentEntity()!=null ) {
                Pratica pratica = (Pratica) this.getContext().getCurrentEntity();
                String codice = ((TipoPratica) cmbTipo.getCurrentEntity()).getCodice();
                pratica.setCodiceinterno(codice);
                ((QLineEdit) this.findChild(QLineEdit.class, "lineEdit_CodInterno")).setText(codice);
            }
        }

    }
}
