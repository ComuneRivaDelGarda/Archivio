package it.tn.rivadelgarda.comune.archivio.entities;

import com.sun.javafx.beans.IDProperty;
import com.sun.org.glassfish.gmbal.IncludeSubclass;

import javax.persistence.*;
import java.io.Serializable;

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
    private String tipo = "ENTRATA";
    @Column
    private Integer anno = 1850;
    @Column
    private Long sportello = 43L;

    public String getIddocumento() {
        return iddocumento;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
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
