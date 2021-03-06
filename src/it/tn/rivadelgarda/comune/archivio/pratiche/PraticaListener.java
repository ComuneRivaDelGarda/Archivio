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
package it.tn.rivadelgarda.comune.archivio.pratiche;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.Database;
import com.axiastudio.pypapi.db.IDatabase;
import it.tn.rivadelgarda.comune.archivio.pratiche.entities.FasePratica;
import it.tn.rivadelgarda.comune.archivio.pratiche.entities.Pratica;
import it.tn.rivadelgarda.comune.archivio.procedimento.entities.FaseProcedimento;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PrePersist;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 * adattato da Comune di Riva del Garda
 */
public class PraticaListener {

    /*
     *  Generazione del corretto identificativo di pratica, codifica, e uffici
     */
    @PrePersist
    void prePersist(Pratica pratica) {
        Calendar calendar = Calendar.getInstance();
        Integer year = calendar.get(Calendar.YEAR);
        Date date = calendar.getTime();
        Database db = (Database) Register.queryUtility(IDatabase.class);
        EntityManager em = db.getEntityManagerFactory().createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Pratica> cq = cb.createQuery(Pratica.class);
        Root<Pratica> root = cq.from(Pratica.class);
        cq.select(root);
        cq.where(cb.equal(root.get("anno"), year));
        cq.orderBy(cb.desc(root.get("idpratica")));
        TypedQuery<Pratica> tq = em.createQuery(cq).setMaxResults(1);
        Pratica max;
        pratica.setDatapratica(date);
        pratica.setAnno(year);
        try {
            max = tq.getSingleResult();
        } catch (NoResultException ex) {
            max=null;
        }
        String newIdpratica;
        if( max != null ){
            Integer i = Integer.parseInt(max.getIdpratica().substring(4));
            i++;
            newIdpratica = year+String.format("%05d", i);
        } else {
            newIdpratica = year+"00001";
        }
        pratica.setIdpratica(newIdpratica);

        // Codifica interna
        String codifica=pratica.getTipo().getCodice() + pratica.getContatorecodice();
        pratica.setCodiceinterno(codifica);

        // se mancano gestione e ubicazione, li fisso come l'attribuzione
        if( pratica.getGestione() == null ){
            pratica.setGestione(pratica.getAttribuzione());
        }
        if( pratica.getUbicazione() == null ){
            pratica.setUbicazione(pratica.getAttribuzione());
        }

        // eredito classificazione dalla codifica interna
        if (pratica.getFascicolo() == null) {
            pratica.setFascicolo(pratica.getTipo().getFascicolo());
        }

        // mi copio nella pratica le fasi di procedimento
        List<FaseProcedimento> fasiProcedimento = pratica.getTipo().getProcedimento().getFaseProcedimentoCollection();
        List<FasePratica> fasiPratica = new ArrayList<FasePratica>();
        Boolean prima=true;
        for( Integer i=0; i<fasiProcedimento.size(); i++ ){
            FaseProcedimento faseProcedimento = fasiProcedimento.get(i);
            FasePratica fasePratica = new FasePratica();
            fasePratica.setPratica(pratica);
            fasePratica.setFase(faseProcedimento.getFase());
            fasePratica.setTesto(faseProcedimento.getTesto());
            fasePratica.setDascartare(faseProcedimento.getDascartare());
            fasePratica.setCondizione(faseProcedimento.getCondizione());
            fasePratica.setAzione(faseProcedimento.getAzione());
            fasePratica.setConfermabile(faseProcedimento.getConfermabile());
            fasePratica.setTestoconfermata(faseProcedimento.getTestoconfermata());
            fasePratica.setRifiutabile(faseProcedimento.getRifiutabile());
            fasePratica.setTestorifiutata(faseProcedimento.getTestorifiutata());
            fasePratica.setUsoresponsabile(faseProcedimento.getUsoresponsabile());
            fasePratica.setCariche(faseProcedimento.getCariche());
            if( prima ){
                fasePratica.setAttiva(true);
                prima=false;
            }
            fasiPratica.add(fasePratica);
        }
        for( Integer i=0; i<fasiPratica.size(); i++ ){
            if( fasiProcedimento.get(i).getConfermata() != null ){
                fasiPratica.get(i).setConfermata(fasiPratica.get(fasiProcedimento.get(i).getConfermata().getProgressivo()));
            }
            if( fasiProcedimento.get(i).getRifiutata() != null ){
                fasiPratica.get(i).setRifiutata(fasiPratica.get(fasiProcedimento.get(i).getRifiutata().getProgressivo()));
            }
        }
        pratica.setFasePraticaCollection(fasiPratica);

    }

}
