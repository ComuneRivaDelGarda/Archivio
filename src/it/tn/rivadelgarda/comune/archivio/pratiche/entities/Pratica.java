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
package it.tn.rivadelgarda.comune.archivio.pratiche.entities;

import com.axiastudio.pypapi.Register;
import it.tn.rivadelgarda.comune.archivio.base.entities.IUtente;
import it.tn.rivadelgarda.comune.archivio.base.entities.Ufficio;
import it.tn.rivadelgarda.comune.archivio.base.entities.Utente;
import it.tn.rivadelgarda.comune.archivio.ITimeStamped;
import it.tn.rivadelgarda.comune.archivio.TimeStampedListener;
import it.tn.rivadelgarda.comune.archivio.pratiche.PraticaListener;
import it.tn.rivadelgarda.comune.archivio.pratiche.PraticaUtil;
import it.tn.rivadelgarda.comune.archivio.protocollo.entities.Fascicolo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 * adattato da Comune di Riva del Garda
 */
@Entity
@EntityListeners({PraticaListener.class, TimeStampedListener.class})
@Table(schema="PRATICHE")
@SequenceGenerator(name="genpratica", sequenceName="pratiche.pratica_id_seq", initialValue=1, allocationSize=1)
public class Pratica implements Serializable, ITimeStamped {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="genpratica")
    private Long id;
    @Column(name="idpratica", unique=true)
    private String idpratica;
    @Column(name="codiceinterno", unique=true)
    private String codiceinterno;
    @Column(name="codiceaggiuntivo")
    private String codiceaggiuntivo;
    @Column(name="datapratica")
    @Temporal(TemporalType.DATE)
    private Date datapratica;
    @Column(name="anno")
    private Integer anno;
    @Column(name="descrizione")
    private String descrizione="";
    @Column(name="note")
    private String note="";
    @JoinColumn(name = "attribuzione", referencedColumnName = "id")
    @ManyToOne
    private Ufficio attribuzione;
    @JoinColumn(name = "gestione", referencedColumnName = "id")
    @ManyToOne
    private Ufficio gestione;
    @JoinColumn(name = "ubicazione", referencedColumnName = "id")
    @ManyToOne
    private Ufficio ubicazione;
    @Column(name="dettaglioubicazione")
    private String dettaglioubicazione;
    @JoinColumn(name = "tipo", referencedColumnName = "id")
    @ManyToOne
    private TipoPratica tipo;
    @Column(name="riservata")
    private Boolean riservata=false;
    @Column(name="archiviata")
    private Boolean archiviata=false;
    @JoinColumn(name = "fascicolo", referencedColumnName = "id")
    @ManyToOne
    private Fascicolo fascicolo;
    @JoinColumn(name = "fase", referencedColumnName = "id")
    @ManyToOne
    private Fase fase;
    @OneToMany(mappedBy = "pratica", orphanRemoval = true, cascade=CascadeType.ALL)
    @OrderColumn(name="progressivo")
    private List<FasePratica> fasePraticaCollection;

    /* timestamped */
    @Column(name="rec_creato", insertable=false, updatable=false, columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date recordcreato;
    @Column(name="rec_creato_da")
    private String recordcreatoda;
    @Column(name="rec_modificato")
    @Temporal(TemporalType.TIMESTAMP)
    private Date recordmodificato;
    @Column(name="rec_modificato_da")
    private String recordmodificatoda;

    /* transient */
    @Transient
    private Ufficio tGestione;

    public Ufficio gettGestione() {
        return tGestione;
    }

    @PostLoad
    private void saveTransients() {
        tGestione = gestione;
    }

    @Transient
    private String contatorecodice;

    public String getContatorecodice() {
        return contatorecodice;
    }

    public void setContatorecodice(String contatorecodice) {
        this.contatorecodice = contatorecodice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAnno() {
        return anno;
    }

    public void setAnno(Integer anno) {
        this.anno = anno;
    }

    public Date getDatapratica() {
        return datapratica;
    }

    public void setDatapratica(Date datapratica) {
        this.datapratica = datapratica;
    }

    public String getIdpratica() {
        return idpratica;
    }

    public void setIdpratica(String idpratica) {
        this.idpratica = idpratica;
    }

    public String getCodiceinterno() {
        return codiceinterno;
    }

    public void setCodiceinterno(String codiceInterno) {
        this.codiceinterno = codiceInterno;
    }

    public String getCodiceaggiuntivo() {
        return codiceaggiuntivo;
    }

    public void setCodiceaggiuntivo(String codiceaggiuntivo) {
        this.codiceaggiuntivo = codiceaggiuntivo;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String oggetto) {
        this.descrizione = oggetto;
    }
    
    public String getDescrizioner() {
        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
        if( this.getRiservata() != null && this.getRiservata() == true && !PraticaUtil.utenteInGestorePratica(this, autenticato) &&
                !autenticato.getSupervisorepratiche() ){
            return "RISERVATA";
        }
        return this.getDescrizione();
    }

    public void setDescrizioner(String descrizione) {
        /* nulla da fare */
    }
    
    public Ufficio getAttribuzione() {
        return attribuzione;
    }

    public void setAttribuzione(Ufficio attribuzione) {
        this.attribuzione = attribuzione;
    }

    public Ufficio getGestione() {
        return gestione;
    }

    public void setGestione(Ufficio gestione) {
        this.gestione = gestione;
    }

    public Ufficio getUbicazione() {
        return ubicazione;
    }

    public void setUbicazione(Ufficio ubicazione) {
        this.ubicazione = ubicazione;
    }

    public String getDettaglioubicazione() {
        return dettaglioubicazione;
    }

    public void setDettaglioubicazione(String dettaglioubicazione) {
        this.dettaglioubicazione = dettaglioubicazione;
    }

    public TipoPratica getTipo() {
        return tipo;
    }

    public void setTipo(TipoPratica tipo) {
        this.tipo = tipo;
    }

    public Boolean getRiservata() {
        return riservata;
    }

    public void setRiservata(Boolean riservata) {
        this.riservata = riservata;
    }

    public Boolean getArchiviata() {
        return archiviata;
    }

    public void setArchiviata(Boolean archiviata) {
        this.archiviata = archiviata;
    }

    public Fascicolo getFascicolo() {
        return fascicolo;
    }

    public void setFascicolo(Fascicolo fascicolo) {
        this.fascicolo = fascicolo;
    }

    public Fase getFase() {
        return fase;
    }

    public void setFase(Fase fase) {
        this.fase = fase;
    }

    public List<FasePratica> getFasePraticaCollection() {
        return fasePraticaCollection;
    }

    public void setFasePraticaCollection(List<FasePratica> fasePraticaCollection) {
        this.fasePraticaCollection = fasePraticaCollection;
    }


    /*
             * timestamped
             */
    @Override
    public Date getRecordcreato() {
        return recordcreato;
    }

    public void setRecordcreato(Date recordcreato) {
        this.recordcreato = recordcreato;
    }

    @Override
    public Date getRecordmodificato() {
        return recordmodificato;
    }

    public void setRecordmodificato(Date recordmodificato) {
        this.recordmodificato = recordmodificato;
    }
    
    @Override
    public String getRecordcreatoda() {
        return recordcreatoda;
    }

    public void setRecordcreatoda(String recordcreatoda) {
        this.recordcreatoda = recordcreatoda;
    }

   @Override
   public String getRecordmodificatoda() {
        return recordmodificatoda;
    }

    public void setRecordmodificatoda(String recordmodificatoda) {
        this.recordmodificatoda = recordmodificatoda;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pratica)) {
            return false;
        }
        Pratica other = (Pratica) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    /*
     * Le pratiche riservate non riportano l'oggetto
     */
    @Override
    public String toString() {
        String out = this.getCodiceinterno() + " - " + this.getDescrizioner();
        return out;
    }
    
}
