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

import com.axiastudio.pypapi.ui.Window;
import com.axiastudio.pypapi.ui.widgets.PyPaPiToolBar;
import com.trolltech.qt.gui.QKeySequence;

/**
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 * adattato da Comune di Riva del Garda
 */

public class PraticaToolbar extends PyPaPiToolBar {

    public PraticaToolbar(String title, Window parent){
        super(title, parent);
        this.insertButton("apriDettaglio", "Apri il dettaglio collegato",
                "classpath:com/axiastudio/suite/resources/arrow_turn_right.png",
                "Apri il dettaglio collegato", parent);
        this.insertButton("cercaDaEtichetta", "Ricerca da etichetta",
                "classpath:com/axiastudio/suite/resources/datamatrix_find.png",
                "Ricerca da etichetta", parent, new QKeySequence(tr("F9")));
        this.insertButton("stampaEtichetta", "Stampa etichetta",
                "classpath:com/axiastudio/suite/resources/datamatrix.png",
                "Stampa etichetta", parent);
        this.insertButton("apriDocumenti", "Apri documenti",
                "classpath:com/axiastudio/suite/menjazo/resources/menjazo.png",
                "Apre lo spazio documenti", parent);
    }
}

