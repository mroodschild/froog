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
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.ejml.simple.SimpleMatrix;
import org.gitia.froog.Feedforward;
import org.gitia.froog.layer.Layer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class Open {

    private static File archivo = null;
    private static Feedforward net;

    public static Feedforward getNet(String file, String folder) {
        net = new Feedforward();
        archivo = new File(folder + "/" + file);
        if (archivo != null) {
            agregarCapas();
        }
        return net;
    }

    private static String obtenerNodoValor(String strTag, Element eNodo) {
        Node nValor = (Node) eNodo.getElementsByTagName(strTag).item(0).getFirstChild();
        return nValor.getNodeValue();
    }

    private static void agregarCapas() {

        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(archivo);//new File(archivo.getAbsolutePath()));
            doc.getDocumentElement().normalize();
            //creamos un listado de nodos Capa
            NodeList layers = doc.getElementsByTagName("layer");
            //recorremos capa por capa
            for (int i = 0; i < layers.getLength(); i++) {
                //creamos un Nodo para obtener el primer elemento
                Node layerNode = layers.item(i);
                if (layerNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element layerElement = (Element) layerNode;
                    String function = obtenerNodoValor("transferFunction", layerElement);
                    String bias = obtenerNodoValor("bias", layerElement);
                    String weight = obtenerNodoValor("w", layerElement);
                    double[] b = Arrays.stream(bias.split(",")).mapToDouble(Double::valueOf).toArray();
                    double[] w = Arrays.stream(weight.split(",")).mapToDouble(Double::valueOf).toArray();
                    net.addLayer(new Layer(
                            new SimpleMatrix(b.length, w.length / b.length, true, w),
                            new SimpleMatrix(b.length, 1, true, b),
                            function));
                }//fin de creacion de las capas
            }
        } catch (SAXException | IOException | ParserConfigurationException ex) {
            Logger.getLogger(Open.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the archivo
     */
    public File getArchivo() {
        return archivo;
    }

    /**
     * @param archivo the archivo to set
     */
    public void setArchivo(File archivo) {
        this.archivo = archivo;
    }
}
