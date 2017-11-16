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
