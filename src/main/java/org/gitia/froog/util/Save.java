/*
 * Copyright 2018 
 *   Matías Roodschild <mroodschild@gmail.com>.
 *   Jorge Gotay Sardiñas <jgotay57@gmail.com>.
 *   Adrian Will <adrian.will.01@gmail.com>.
 *   Sebastián Rodriguez <sebastian.rodriguez@gitia.org>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
import org.gitia.froog.layer.Dense;

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
        try {
            arch = new File(folder + "/" + name + ".xml");
            if (arch.exists()) {
                arch.delete();
                if (arch.createNewFile()) {
                    formatter = new Formatter(arch.getAbsoluteFile());
                    save(net);
                }
            } else {
                if (arch.createNewFile()) {
                    formatter = new Formatter(arch.getAbsoluteFile());
                    save(net);
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
                + "\tSystem       :   Froog Neural Network - gitia.org\n"
                + "\tCreated on   :   " + c.getTime().toString() + "\n"
                + "\tContact      :   Dr. jgotay57@gmail.com; Ing. mroodschild@gmail.com\n-->\n\n"
                + "<Datos>";
        formatter.format("%s\n", cabecera);
    }

    private static void process(Feedforward net) {

        for (int i = 0; i < net.getLayers().size(); i++) {
            Dense layer = net.getLayers().get(i);
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
