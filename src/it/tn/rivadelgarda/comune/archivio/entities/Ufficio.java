package it.tn.rivadelgarda.comune.archivio.entities;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by utente on 03/07/14.
 */



@Entity
@Table(schema = "base")
@SequenceGenerator(name = "genufficio", sequenceName = "base.ufficio_id_seq", initialValue = 1, allocationSize = 1)
public class Ufficio implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genufficio")
    private Long id;
    @Column
    private String descrizione;

    public String getDescrizione()
    {
        return descrizione;
    }

    public void setDescrizione(String descrizione)
    {
        this.descrizione = descrizione;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    @Override
    public String toString()
    {
        return " (" + id + ") " + this.getDescrizione();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ufficio ufficio = (Ufficio) o;

        if (id != null ? !id.equals(ufficio.id) : ufficio.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

}
