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
package it.tn.rivadelgarda.comune.archivio;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.Database;
import com.axiastudio.pypapi.db.IDatabase;
import com.axiastudio.pypapi.db.IFactory;
import com.axiastudio.pypapi.db.Store;
import com.axiastudio.pypapi.ui.IForm;
import com.axiastudio.pypapi.ui.IUIFile;
import com.axiastudio.pypapi.ui.Util;
import com.axiastudio.pypapi.ui.Window;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QSignalMapper;
import com.trolltech.qt.gui.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class Mdi extends QMainWindow implements IMdi {
    
    private static String ICON = "classpath:com/axiastudio/pypapi/ui/resources/pypapi32.png";
    private QMdiArea workspace;
    private QTreeWidget tree;
    private QSystemTrayIcon trayIcon;
    private QMenu menuWindows;
    private QAction actionCloseAll;
    private QAction actionTile;
    private QAction actionCascade;
    private QAction actionNext;
    private QAction actionPrevious;
    private QAction actionSeparator;
    private QAction actionClose;
    private QSignalMapper windowMapper;
    
    public Mdi(){
        this.setWindowIcon(new QIcon(ICON));
        this.createWorkspace();
        this.createTree();
        //this.createSystemTray();
        this.createMenu();

        Register.registerUtility(this, IMdi.class);

    }
    
    private void createMenu(){
        menuWindows = this.menuBar().addMenu("Finestre");
        actionClose = new QAction("Chiudi", this);
        actionClose.triggered.connect(this.workspace, "closeActiveSubWindow()");
        actionCloseAll = new QAction("Chiudi tutte", this);
        actionCloseAll.triggered.connect(this.workspace, "closeAllSubWindows()");
        actionTile = new QAction("Allinea", this);
        actionTile.triggered.connect(this.workspace, "tileSubWindows()");
        actionCascade = new QAction("Disponi a cascata", this);
        actionCascade.triggered.connect(this.workspace, "cascadeSubWindows()");
        actionNext = new QAction("Finestra successiva", this);
        actionNext.triggered.connect(this.workspace, "activateNextSubWindow()");
        actionPrevious = new QAction("Finestra precedente", this);
        actionPrevious.triggered.connect(this.workspace, "activatePreviousSubWindow()");
        actionSeparator = new QAction(this);
        actionSeparator.setSeparator(true);
        
        menuWindows.aboutToShow.connect(this, "refreshMenuWindows()");     
    }
    
    private void refreshMenuWindows(){
        
        menuWindows.clear();
        menuWindows.addAction(actionClose);
        menuWindows.addAction(actionCloseAll);
        menuWindows.addAction(actionSeparator);
        menuWindows.addAction(actionTile);
        menuWindows.addAction(actionCascade);
        menuWindows.addAction(actionSeparator);
        menuWindows.addAction(actionNext);
        menuWindows.addAction(actionPrevious);
        menuWindows.addAction(actionSeparator);
        menuWindows.addAction(actionCloseAll);
        
        for( QMdiSubWindow subWindow: this.workspace.subWindowList() ){
            
            String title="";
            if( subWindow.widget() instanceof QMainWindow ){
                title = ((QMainWindow) subWindow.widget()).windowTitle();
            }
            if( subWindow.widget() instanceof QDialog ){
                title = ((QDialog) subWindow.widget()).windowTitle();
            }
            
            QAction action = menuWindows.addAction(title);
            action.setCheckable(true);
            action.setChecked(subWindow.equals(this.workspace.activeSubWindow()));
            action.triggered.connect(windowMapper, "map()");
            windowMapper.setMapping(action, subWindow);
        }
    }
    
    private void createSystemTray(){
        QMenu menu = new QMenu(this);
        menu.addAction("prova");
        this.trayIcon = new QSystemTrayIcon(new QIcon(ICON), this);
        this.trayIcon.setContextMenu(menu);
        this.trayIcon.show();
        this.trayIcon.showMessage("PyPaPi Suite", "Applicazione avviata.");
        
    }
    
    private void createWorkspace() {
        QSplitter splitter = new QSplitter();
        this.tree = new QTreeWidget(splitter);
        this.workspace = new QMdiArea(splitter);
        this.setCentralWidget(splitter);        
        this.workspace.subWindowActivated.connect(this, "refreshMenuWindows()");
        windowMapper = new QSignalMapper(this);
        windowMapper.mappedQObject.connect(this, "setActiveSubWindow(QObject)");
    }
    
    private void setActiveSubWindow(QObject obj){
        this.workspace.setActiveSubWindow((QMdiSubWindow) obj);
    }
    
    private void createTree() {
        this.tree.setColumnCount(2);
        this.tree.setHeaderLabel("Gestione archivio");
        this.tree.setColumnHidden(1, true);

        /* scrivania */
        QTreeWidgetItem itemScrivania = new QTreeWidgetItem(this.tree);
        itemScrivania.setText(0, "Scrivania");
        itemScrivania.setIcon(0, new QIcon("classpath:it/tn/rivadelgarda/comune/archivio/resources/house.png"));
        itemScrivania.setText(1, "SCRIVANIA");

        this.tree.activated.connect(this, "runTask()");
        this.tree.setMinimumWidth(200);

    }
    
    private void runTask() {
        String formName = this.tree.currentItem().text(1);
        if (formName == null || formName.equals("")) {
            return;  // item di raggruppamento
        }

        String mode = this.tree.currentItem().text(2);
        /* chianata form speciale */
        if( "XXXXXX".equals(formName) ){
/*            Xxxxx form = new Xxxx();
            this.workspace.addSubWindow(form);
            form.show();  */
/*           Xxxx passDlg = new Xxxxx(this);
            this.workspace.addSubWindow(xxxxxx);
            int exec = Xxxx.exec();   */
        } else {
            /* form registrata */
            Window form=null;
            Class<? extends Window> formClass = (Class) Register.queryUtility(IForm.class, formName);
            String uiFile = (String) Register.queryUtility(IUIFile.class, formName);
            Class factory = (Class) Register.queryUtility(IFactory.class, formName);
            try {
                Constructor<? extends Window> constructor = formClass.getConstructor(new Class[]{String.class, Class.class, String.class});
                try {
                    form = constructor.newInstance(new Object[]{uiFile, factory, ""});
                } catch (InstantiationException ex) {
                    Logger.getLogger(Mdi.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(Mdi.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(Mdi.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvocationTargetException ex) {
                    Logger.getLogger(Mdi.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (NoSuchMethodException ex) {
                Logger.getLogger(Mdi.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(Mdi.class.getName()).log(Level.SEVERE, null, ex);
            }
            // A store with a new element
            Store store = null;

            if( "NEW".equals(mode) ){
                store = new Store(new ArrayList<Object>());
                try {
                    Constructor entityConstructor = factory.getConstructor(new Class[]{});
                    Object entity = entityConstructor.newInstance(new Object[]{});
                    store.add(entity);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else if( mode.startsWith("NAMEDQUERY") ){
                String[] split = mode.split(":");
                String namedQueryName = split[1];

                Database db = (Database) Register.queryUtility(IDatabase.class);
                EntityManagerFactory emf = db.getEntityManagerFactory();
                EntityManager em = emf.createEntityManager();
                TypedQuery namedQuery = em.createNamedQuery(namedQueryName, factory);

                // TODO: aggiungere qualche controllo?
                for( Integer i=2; i<split.length; i++){
                    String parameters = split[i];
                    String[] split1 = parameters.split(",");
                    String fieldName = split1[0];
                    String typeName = split1[1];
                    String stringValue = split1[2];
                    if( "Integer".equals(typeName) ){
                        Object value = Integer.parseInt(stringValue);
                        namedQuery = namedQuery.setParameter(fieldName, value);
                    }
                }

                List<?> resultList = namedQuery.getResultList();
                store = new Store(resultList);
            }

            if( store != null ){
                if ( store.size()>0 ) {
                    form.init(store);
                }
                else {
                    Util.warningBox(this, "Attenzione", "Nessun record trovato");
                    return;
                }
            } else {
                form.init();
            }
            this.workspace.addSubWindow(form);
            this.showForm(form);
            if( store != null ) {
                form.getContext().getDirty();
            }
            this.menuWindows.addAction(form.toString());
        }
    }
    
    private void showForm(Window form) {
        if( this.workspace.subWindowList().size()>1 ){
            form.show();
        } else {
            form.showMaximized();
        }
    }

    @Override
    public QMdiArea getWorkspace() {
        return workspace;
    }

    @Override
    protected void closeEvent(QCloseEvent event) {
        boolean iHaveToClose = true;
        for( QMdiSubWindow subWindow: this.workspace.subWindowList() ){
            if( subWindow.widget() instanceof QMainWindow ){
                subWindow.widget().setFocus();
                iHaveToClose = iHaveToClose && subWindow.close();
            }
        }
        if( iHaveToClose ) {
            super.closeEvent(event);
            return;
        }
        event.ignore();
    }
}
