/*
 * Copyright (C) 2012 AXIA Studio (http://www.axiastudio.com)
 *
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
package it.tn.rivadelgarda.comune.archivio.login;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.Database;
import com.axiastudio.pypapi.db.IDatabase;
import it.tn.rivadelgarda.comune.archivio.base.entities.IUtente;
import it.tn.rivadelgarda.comune.archivio.base.entities.Utente;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class Login extends QDialog {
    private final QLineEdit username;
    private final QLineEdit password;
    private Utente utente;

    public Login() {
        super();
        this.setWindowIcon(new QIcon("classpath:it/tn/rivadelgarda/comune/archivio/resources/pypapi128.png"));
        QVBoxLayout layout = new QVBoxLayout();
        QGridLayout gridLayout = new QGridLayout();
        QLabel labelUsername = new QLabel("Utente");
        gridLayout.addWidget(labelUsername, 0, 0);
        this.username = new QLineEdit();
        gridLayout.addWidget(username, 0, 1);
        QLabel labelPassword = new QLabel("Password");
        gridLayout.addWidget(labelPassword, 1, 0);
        this.password = new QLineEdit();
        this.password.setEchoMode(QLineEdit.EchoMode.Password);
        gridLayout.addWidget(password, 1, 1);
        QPushButton pushButtonCancel = new QPushButton("Annulla");
        gridLayout.addWidget(pushButtonCancel, 2, 0);
        QPushButton pushButtonOk = new QPushButton("OK");
        gridLayout.addWidget(pushButtonOk, 2, 1);
        QLabel logo = new QLabel();
        logo.setPixmap(new QPixmap("classpath:it/tn/rivadelgarda/comune/archivio/resources/oldpaper_s.png"));
        layout.addWidget(logo, 0, Qt.AlignmentFlag.AlignCenter);
        layout.addLayout(gridLayout);
        this.setLayout(layout);
        pushButtonOk.clicked.connect(this, "accept()");
        pushButtonCancel.clicked.connect(this, "reject()");
        pushButtonOk.setDefault(true);
    }
    
    @Override
    public void accept() {
        Database db = (Database) Register.queryUtility(IDatabase.class);
        EntityManager em = db.getEntityManagerFactory().createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Object> cq = cb.createQuery();
        Root from = cq.from(Utente.class);
        cq.select(from);
        Predicate predicate = cb.equal(from.get("login"), this.username.text().toLowerCase());
        cq = cq.where(predicate);
        Query q = em.createQuery(cq);
        List entities = q.getResultList();
        if( entities.size() == 1 ){
            utente = (Utente) entities.get(0);
            String pwd = this.password.text();
            ICheckLogin checkLogin = (ICheckLogin) Register.queryUtility(ICheckLogin.class);
            if( checkLogin != null ){
                if( checkLogin.check(this.username.text(), this.password.text()) ){
                    Register.registerUtility(utente, IUtente.class);
                    super.accept();
                    return;
                }
            } else {
                if( digest(pwd).equals(utente.getPassword()) ){
                    Register.registerUtility(utente, IUtente.class);
                    super.accept();
                    return;
                }
            }
        }
        QMessageBox.critical(this, "Utente o password errati", "Il nome utente o la password risultano errati.");
    }

    public static String digest(String s){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(s.getBytes());
            byte byteData[] = md.digest();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Utente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
