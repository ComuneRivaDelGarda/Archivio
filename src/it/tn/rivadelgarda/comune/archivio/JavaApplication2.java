package it.tn.rivadelgarda.comune.archivio;

/**
 * Created with IntelliJ IDEA.
 * User: Mickey
 * Date: 31/03/14
 * Time: 18.24
 * To change this template use File | Settings | File Templates.
 */

    import java.io.ByteArrayInputStream;
    import java.io.InputStream;

    import javax.print.Doc;
    import javax.print.DocFlavor;
    import javax.print.DocPrintJob;
    import javax.print.PrintService;
    import javax.print.PrintServiceLookup;
    import javax.print.SimpleDoc;
    import javax.print.attribute.AttributeSet;
    import javax.print.attribute.HashAttributeSet;
    import javax.print.attribute.standard.PrinterName;

public class JavaApplication2 {

        public void executePrint(String sample){
//data for printing
            String data = sample;

            try {
//locate printer
                AttributeSet attributeSet = new HashAttributeSet();
                attributeSet.add(new PrinterName("Zebra", null));
/*                PrintService[] printService = PrintServiceLookup.lookupPrintServices(null, attributeSet);
                if (printService.length == 0) {
                    System.out.println("Nessuna stampante trovata");
                    return;
                } else {
                    System.out.println("Printer online: "+printService[0]);
                }
   */
                PrintService[] printService = PrintServiceLookup.lookupPrintServices(null, null);
                for (PrintService printer: printService) {
                    if ( printer.getName().contains("Zeb") ) {
                        System.out.println("Trovata " + printer.getName());
                    } else {
                        System.out.println(printer.getName() + " non va bene... ");
                    }
                }

//create a print job
                DocPrintJob job = printService[0].createPrintJob();

//define the format of print document
                InputStream is = new ByteArrayInputStream(data.getBytes());
                DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;

//print the data
                Doc doc = new SimpleDoc(is, flavor, null);
                job.print(doc, null);

                is.close();
                System.out.println("Printing Done!!");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static void main(String[] args) {

            new JavaApplication2().executePrint("\n" +
                    "N\n" +
                    "S1\n" +
                    "A245,15,0,b,1,1,N,\"COMUNE\"\n" +
                    "A331,28,0,3,1,1,N,\"di\"\n" +
                    "A365,15,0,b,1,1,N,\"RIVA D/G\"\n" +
                    "A235,45,0,a,1,1,N,\"2014-0002388\"\n" +
                    "b474,8,D,h6,\"c_h330#201400002388#0131#a451494d10\"\n" +
                    "A235,100,0,3,1,1,N,\"31-01-2014 11:11\"\n" +
                    "A247,125,0,2,1,1,N,\"c_h330 - RSERVIZI\"\n" +
                    "B260,160,0,2,3,9,55,N,\"2014002388\"\n" +
                    "P1\n");
        }
    }



