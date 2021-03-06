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
package it.tn.rivadelgarda.comune.archivio.protocollo.entities;


import com.axiastudio.pypapi.Register;
import it.tn.rivadelgarda.comune.archivio.ITimeStamped;
import it.tn.rivadelgarda.comune.archivio.ProfiloUtente;
import it.tn.rivadelgarda.comune.archivio.TimeStampedListener;
import it.tn.rivadelgarda.comune.archivio.base.entities.IUtente;
import it.tn.rivadelgarda.comune.archivio.base.entities.Ufficio;
import it.tn.rivadelgarda.comune.archivio.base.entities.Utente;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Comune di Riva del Garda on 30/06/14.
 */

@Entity
@Table(schema = "protocollo")
@EntityListeners(TimeStampedListener.class)
@SequenceGenerator(name = "genprotocollo", sequenceName = "protocollo.protocollo_id_seq", initialValue = 1, allocationSize = 1)
public class Protocollo implements Serializable, ITimeStamped
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genprotocollo")
    private Long id;
    @Column
    private String iddocumento = "";
    @Column
    private String oggetto = "";
    @Column
    @Enumerated(EnumType.STRING)
    private TipoProtocollo tipo = TipoProtocollo.ENTRATA;
    @Column
    private Integer anno = 0;
    @JoinColumn(name = "sportello", referencedColumnName = "id")
    @ManyToOne
    private Ufficio sportello;
    @Column(name="dataprotocollo")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataprotocollo;
    @Column
    private Boolean convalidaprotocollo = Boolean.FALSE;

    /* timestamped */
    @Column(name="rec_creato", insertable=false, updatable=false, columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date recordcreato;
    @Column(name="rec_creato_da")
    private String recordcreatoda;
    @Column(name="rec_modificato")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date recordmodificato;
    @Column(name="rec_modificato_da")
    private String recordmodificatoda;

    /* transient */
    @Transient
    private Ufficio tSportello;

    public Ufficio getTSportello() {
        return tSportello;
    }

    public void setTSportello(Ufficio tsportello) {
        this.tSportello = tsportello;
    }

    @PostLoad
    private void saveTransients() {
        tSportello = sportello;
    }

    public Date getDataprotocollo() {
        return dataprotocollo;
    }

    public void setDataprotocollo(Date dataprotocollo) {
        this.dataprotocollo = dataprotocollo;
    }

    public String getIddocumento() {
        return iddocumento;
    }

    public TipoProtocollo getTipo() {
        return tipo;
    }

    public void setIddocumento(String iddocumento) {
        this.iddocumento = iddocumento;
    }

    public String getOggetto() {
        return oggetto;
    }

    public void setOggetto(String oggetto) {
        this.oggetto = oggetto;
    }

    public String getOggettop() {
        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
        ProfiloUtente profilo = new ProfiloUtente();
        if (autenticato.getSupervisoreprotocollo() || autenticato.getRicercatoreprotocollo() || profilo.inSportello(this, autenticato)) {
            return this.getOggetto();
        }
        return "RISERVATO";
    }

    public void setOggettop(String oggetto) {
        // do nothing
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTipo(TipoProtocollo tipo) {
        this.tipo = tipo;
    }

    public Integer getAnno() {
        return anno;
    }

    public void setAnno(Integer anno) {
        this.anno = anno;
    }

    public Ufficio getSportello() {
        return sportello;
    }

    public void setSportello(Ufficio sportello) {
        this.sportello = sportello;
    }

    public Boolean getConvalidaprotocollo() {
        return convalidaprotocollo;
    }

    public void setConvalidaprotocollo(Boolean convalidaprotocollo) {
        this.convalidaprotocollo = convalidaprotocollo;
    }

    public Date getRecordcreato() {
        return recordcreato;
    }

    public void setRecordcreato(Date recordcreato) {
        this.recordcreato = recordcreato;
    }

    public String getRecordcreatoda() {
        return recordcreatoda;
    }

    public void setRecordcreatoda(String recordcreatoda) {
        this.recordcreatoda = recordcreatoda;
    }

    public Date getRecordmodificato() {
        return recordmodificato;
    }

    public void setRecordmodificato(Date recordmodificato) {
        this.recordmodificato = recordmodificato;
    }

    public String getRecordmodificatoda() {
        return recordmodificatoda;
    }

    public void setRecordmodificatoda(String recordmodificatoda) {
        this.recordmodificatoda = recordmodificatoda;
    }

    @Override
    public String toString() {
        return "Protocollo{" + "iddocumento='" + iddocumento + '\'' + '}';
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
        if (!(object instanceof Protocollo)) {
            return false;
        }
        Protocollo other = (Protocollo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

}
