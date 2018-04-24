/*
 * The MIT License
 *
 * Copyright 2017 
 *   Matías Roodschild <mroodschild@gmail.com>.
 *   Jorge Gotay Sardiñas <jgotay57@gmail.com>.
 *   Adrian Will <adrian.will.01@gmail.com>.
 *   Sebastián Rodriguez <sebastian.rodriguez@gitia.org>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.gitia.froog.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.gitia.froog.Feedforward;
import org.gitia.froog.layer.Layer;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class Save {

    static File arch;
    static Formatter formatter;

    public Save() {
    }

    public static void saveNet(Feedforward net, String name, String folder) {

        //System.out.println("1");
        try {
            //System.out.println("2");
            arch = new File(folder + "/" + name + ".xml");
            //System.out.println("3");
            if (arch.exists()) {
                //System.out.println("4");
                //System.out.println("File alrady exist");
                arch.delete();
                //System.out.println("File deleted");
                if (arch.createNewFile()) {
                    formatter = new Formatter(arch.getAbsoluteFile());
                    save(net);
                    //System.out.println("File saved");
                } else {
                    //System.out.println("File not saved");
                }
            } else {
                //System.out.println("5");
                if (arch.createNewFile()) {
                    //System.out.println("6");
                    formatter = new Formatter(arch.getAbsoluteFile());
                    save(net);
                    //System.out.println("File saved");
                } else {
                    //System.out.println("File not saved");
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Save.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void save(Feedforward net) throws FileNotFoundException {
        inicioXML();
        process(net);
        finXML();
    }

    private static void inicioXML() throws FileNotFoundException {
        Calendar c = Calendar.getInstance();
        String cabecera = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<!--\n"
                + "\tSystem       :   Neural Network - gitia.org\n"
                + "\tCreated on   :   " + c.getTime().toString() + "\n"
                + "\tContact      :   Dr. jgotay@gmail.com; Ing. mroodschild@gmail.com\n-->\n\n"
                + "<Datos>";
        formatter.format("%s\n", cabecera);
    }

    private static void process(Feedforward net) {

        for (int i = 0; i < net.getLayers().size(); i++) {
            Layer layer = net.getLayers().get(i);
            formatter.format("\n<layer>");
            formatter.format("\n\t<transferFunction>%s</transferFunction>",
                    layer.getFunction().toString());
            formatter.format("\n\t<w>%s</w>",
                    Arrays.toString(layer.getW().getDDRM().data).replace("[", "").replace("]", ""));
            formatter.format("\n\t<bias>%s</bias>",
                    Arrays.toString(layer.getB().getDDRM().data).replace("[", "").replace("]", ""));
            formatter.format("\n</layer>");
        }
    }

    private static void finXML() {
        formatter.format("\n</Datos>");
        //System.out.println("Entrando a Cerrar XML");
        if (formatter != null) {
            //System.out.println("Cerrando XML");
            formatter.close();
        }
    }

}
