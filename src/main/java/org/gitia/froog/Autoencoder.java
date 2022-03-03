/*
 * Copyright 2022 
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
package org.gitia.froog;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.ejml.simple.SimpleMatrix;
import org.gitia.froog.layer.Dense;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class Autoencoder implements NeuralNetwork {

    List<Dense> encoder = new ArrayList<>();
    List<Dense> decoder = new ArrayList<>();
    //union de las dos anteriores
    List<Dense> layers = new ArrayList<>();

    /**
     * 
     */
    public Autoencoder() {

    }

    /**
     * Agregamos una capa al codificador. 
     * La capa nueva se agrega al final del codificador actual.
     * 
     * @param layer
     */
    public void addEncoderLayer(Dense layer) {
        encoder.add(layer);
        updateNet();
    }

    /**
     * Agregamos una capa al decodificador. 
     * La capa nueva se agrega al final del decodificador actual.
     * (Recordar que la última capa del decodificador es la salida de la RNA)
     * @param layer
     */
    public void addDecoderLayer(Dense layer) {
        decoder.add(layer);
        updateNet();
    }

    /**
     * Actualizamos las capas de la red.
     */
    protected void updateNet() {
        //esto limpia el listado, y vuelve a llenarlo con las
        //capas del codificador y decodificador
        layers.clear();
        for (int i = 0; i < encoder.size(); i++) {
            layers.add(encoder.get(i));
        }
        for (int i = 0; i < decoder.size(); i++) {
            layers.add(decoder.get(i));
        }
    }

    /**
     * 
     */
    @Override
    public SimpleMatrix output(double[] input) {
        return null;
    }

    /**
     * 
     */
    @Override
    public SimpleMatrix output(SimpleMatrix input) {
        return null;
    }

    /**
     * get encoder and decoder layers
     */
    @Override
    public List<Dense> layers() {
        return layers;
    }

    /**
     * devolver como red neuronal
     * @return encoder layers
     */
    public List<Dense> encoder() {
        return encoder;
    }

    /**
     * devolver como red neuronal
     * @return decoder layers
     */
    public List<Dense> decoder() {
        //se prefiere como redes
        return decoder;
    }

    /**
     * retornamos todas las salidas de todas las capas, cada fila de la entrada
     * y la salida es una característica y cada columna es un dato
     *
     * @param input
     * @return
     */
    @Override
    public List<SimpleMatrix> activations(SimpleMatrix input) {
        List<SimpleMatrix> A = new ArrayList<>();
        SimpleMatrix a = input;
        int size = layers.size();
        for (int i = 0; i < size; i++) {
            a = layers().get(i).output(a);
            A.add(a);
        }
        return A;
    }

    /**
     *
     *
     * @param input inputs to evaluate with dropout
     * @return outputs with dropout
     */
    public List<SimpleMatrix> activationsDropout(SimpleMatrix input) {
        List<SimpleMatrix> A = new ArrayList<>();
        SimpleMatrix a = input.copy();
        int size = layers.size();
        for (int i = 0; i < size; i++) {
            a = layers.get(i).outputDropout(a);
            A.add(a);
        }
        return A;
    }

    /**
     *
     * @return W1, W2, ..., Wn, B1, B2, ...,Bm [1 x n]
     */
    @Override
    public SimpleMatrix getParameters() {
        if (layers.isEmpty()) {
            System.err.println("Inicialice los pesos primero");
            return null;
        }
        SimpleMatrix w = getParamsW();
        SimpleMatrix b = getParamsB();
        return w.concatColumns(b);
    }

    /**
     *
     * @return W1, W2, ..., Wn
     */
    @Override
    public SimpleMatrix getParamsW() {
        if (layers.isEmpty()) {
            System.err.println("layers empty");
            return null;
        }
        int numW = 0;
        for (Dense next : layers) {
            numW += next.getW().getNumElements();
        }
        SimpleMatrix w = new SimpleMatrix(1, numW);
        int pos = 0;
        for (Dense next : layers) {
            w.setRow(0, pos, next.getW().getDDRM().getData());
            pos += next.getW().getDDRM().getNumElements();
        }
        return w;
    }

    /**
     *
     * @return B1, B2, ...,Bm
     */
    @Override
    public SimpleMatrix getParamsB() {
        if (layers.isEmpty()) {
            System.err.println("Inicialice los pesos primero");
            return null;
        }
        int numB = 0;
        for (Dense next : layers) {
            numB += next.getB().getNumElements();
        }
        SimpleMatrix b = new SimpleMatrix(1, numB);
        int pos = 0;
        for (Dense next : layers) {
            b.setRow(0, pos, next.getB().getDDRM().getData());
            pos += next.getB().getDDRM().getNumElements();
        }
        return b;
    }

    /**
     * Copiamos los pesos en W2 W3 B2 B3 manteniendo la estructura inicial de
     * esas matrices, la forma de copiado es: <br>
     * <br>
     * 1 2 3 <br>
     * 4 5 6 <br>
     * 7 8 9 <br>
     *
     * @param weights
     */
    public void setParameters(SimpleMatrix weights) {
        if (layers.isEmpty()) {
            System.err.println("Inicialice las capas primero");
        } else {
            int pos = 0;
            int size;
            double[] datos = weights.getDDRM().getData();
            // cargamos los w
            for (int i = 0; i < layers.size(); i++) {
                Dense layer = layers.get(i);
                size = layer.getW().getNumElements();
                layer.getW().getDDRM().setData(
                        ArrayUtils.subarray(datos, pos, pos + size));
                pos += size;
            }
            // cargamos los b
            for (int i = 0; i < layers.size(); i++) {
                Dense layer = layers.get(i);
                size = layer.getB().getNumElements();
                layer.getB().getDDRM().setData(
                        ArrayUtils.subarray(datos, pos, pos + size));
                pos += size;
            }
        }
    }

    /**
     * mejorar
     * @return
     */
    @Override
    public String toString() {
        String info = "";
        for (int i = 0; i < layers.size(); i++) {
            Dense l = layers.get(i);
            info += "l" + i + ": " + l.numNeuron() + "\t" + l.getFunction().toString() + "\t";
        }
        return info;
    }
}