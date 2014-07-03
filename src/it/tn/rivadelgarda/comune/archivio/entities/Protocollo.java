package it.tn.rivadelgarda.comune.archivio.entities;


import it.tn.rivadelgarda.comune.archivio.entities.TipoProtocollo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by utente on 30/06/14.
 */

@Entity
@Table(schema = "protocollo")
@SequenceGenerator(name = "genprotocollo", sequenceName = "protocollo.protocollo_id_seq", initialValue = 1, allocationSize = 1)
public class Protocollo implements Serializable
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
    @Column
    private Long sportello;

    @Column(name="dataprotocollo", insertable=false, updatable=false, columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataprotocollo;

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

    public Long getSportello() {
        return sportello;
    }

    public void setSportello(Long sportello) {
        this.sportello = sportello;
    }

    @Override
    public String toString() {
        return "Protocollo{" + "iddocumento='" + iddocumento + '\'' + '}';
    }
}
