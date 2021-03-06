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
 * You should have received a copy of the GNU Afffero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package it.tn.rivadelgarda.comune.archivio.pratiche.entities;


import it.tn.rivadelgarda.comune.archivio.base.entities.Utente;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author AXIA Studio (http://www.axiastudio.com)
 * adattato da Comune di Riva del Garda
 */
@Entity
@Table(schema="PRATICHE")
@SequenceGenerator(name="genutentepratica", sequenceName="pratiche.utentepratica_id_seq", initialValue=1, allocationSize=1)
public class UtentePratica implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="genutentepratica")
    private Long id;
    @JoinColumn(name = "pratica", referencedColumnName = "idpratica")
    @ManyToOne
    private Pratica pratica;
    @JoinColumn(name = "utente", referencedColumnName = "id")
    @ManyToOne
    private Utente utente;
    @Column(name="responsabile")
    private Boolean responsabile=false;
    @Column(name="istruttore")
    private Boolean istruttore=false;
    @Column(name="dal")
    @Temporal(TemporalType.DATE)
    private Date dal;
    @Column(name="al")
    @Temporal(TemporalType.DATE)
    private Date al;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pratica getPratica() {
        return pratica;
    }

    public void setPratica(Pratica pratica) {
        this.pratica = pratica;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public Boolean getResponsabile() {
        return responsabile;
    }

    public void setResponsabile(Boolean responsabile) {
        this.responsabile = responsabile;
    }

    public Boolean getIstruttore() {
        return istruttore;
    }

    public void setIstruttore(Boolean istruttore) {
        this.istruttore = istruttore;
    }

    public Date getDal() {
        return dal;
    }

    public void setDal(Date dal) {
        this.dal = dal;
    }

    public Date getAl() {
        return al;
    }

    public void setAl(Date al) {
        this.al = al;
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
        if (!(object instanceof UtentePratica)) {
            return false;
        }
        UtentePratica other = (UtentePratica) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.axiastudio.suite.procedimenti.entities.UtenteProcedimento[ id=" + id + " ]";
    }
    
}
