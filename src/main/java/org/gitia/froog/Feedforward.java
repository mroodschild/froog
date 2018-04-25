/*
 * The MIT License
 *
 * Copyright 2017 - 2018
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
package org.gitia.froog;

import org.gitia.froog.layer.Layer;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;
import org.ejml.simple.SimpleMatrix;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class Feedforward implements NeuralNetwork {

    List<Layer> layers = new ArrayList<>();

    public Feedforward() {
    }

    /**
     * Agrega una capa en la última posición
     *
     * @param layer
     */
    public void addLayer(Layer layer) {
        layers.add(layer);
    }

    /**
     *
     * @param input Horizontal
     * @return
     */
    @Override
    public SimpleMatrix output(double[] input) {
        SimpleMatrix a = new SimpleMatrix(input.length, 1, true, input);
        return output(a);
    }

    /**
     *
     * @param input
     * @return
     */
    @Override
    public SimpleMatrix output(SimpleMatrix input) {
        //SimpleMatrix aux = new SimpleMatrix(input);
        SimpleMatrix aux = input;
        for (int i = 0; i < layers.size(); i++) {
            aux = layers.get(i).output(aux);
        }
        return aux;
    }

    /**
     *
     * @param input entrada a la red
     * @param layer salida de la Layer deseada
     * @return
     */
    public SimpleMatrix output(SimpleMatrix input, int layer) {
        SimpleMatrix aux = new SimpleMatrix(input);
        for (int i = 0; i <= layer; i++) {
            aux = layers.get(i).output(aux);
        }
        return aux;
    }

    /**
     * retornamos todas las salidas de todas las capas, cada fila de la entrada
     * y la salida es una característica y cada columna es un dato
     *
     * @param input
     * @return
     */
    public List<SimpleMatrix> activations(SimpleMatrix input) {
        List<SimpleMatrix> A = new ArrayList<>();
        SimpleMatrix a = input.copy();
        int size = layers.size();
        for (int i = 0; i < size; i++) {
            a = layers.get(i).output(a);
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
     * retornamos todas Z de todas las capas
     *
     * @param input
     * @return
     */
    public List<SimpleMatrix> outputZLayers(SimpleMatrix input) {
        List<SimpleMatrix> outputs = new ArrayList<>();
        SimpleMatrix aux = new SimpleMatrix(input);
        SimpleMatrix z;
        int size = layers.size();
        for (int i = 0; i < size; i++) {
            z = layers.get(i).outputZ(aux);
            aux = layers.get(i).outputN(z);
            outputs.add(z);
        }
        return outputs;
    }

    /**
     *
     * @return W1, W2, ..., Wn, B1, B2, ...,Bm [1 x n]
     */
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
    public SimpleMatrix getParamsW() {
        if (layers.isEmpty()) {
            System.err.println("layers empty");
            return null;
        }
        int numW = 0;
        for (Layer next : layers) {
            numW += next.getW().getNumElements();
        }
        SimpleMatrix w = new SimpleMatrix(1, numW);
        int pos = 0;
        for (Layer next : layers) {
            w.setRow(0, pos, next.getW().getDDRM().getData());
            pos += next.getW().getDDRM().getNumElements();
        }
        return w;
    }

    /**
     *
     * @return B1, B2, ...,Bm
     */
    public SimpleMatrix getParamsB() {
        if (layers.isEmpty()) {
            System.err.println("Inicialice los pesos primero");
            return null;
        }
        int numB = 0;
        for (Layer next : layers) {
            numB += next.getB().getNumElements();
        }
        SimpleMatrix b = new SimpleMatrix(1, numB);
        int pos = 0;
        for (Layer next : layers) {
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
            System.err.println("Inicialice los capas primero");
        } else {
            int pos = 0;
            int size;
            double[] datos = weights.getDDRM().getData();
            //cargamos los w
            for (int i = 0; i < layers.size(); i++) {
                Layer layer = layers.get(i);
                size = layer.getW().getNumElements();
                layer.getW().getDDRM().setData(
                        ArrayUtils.subarray(datos, pos, pos + size));
                pos += size;
            }
            //cargamos los b
            for (int i = 0; i < layers.size(); i++) {
                Layer layer = layers.get(i);
                size = layer.getB().getNumElements();
                layer.getB().getDDRM().setData(
                        ArrayUtils.subarray(datos, pos, pos + size));
                pos += size;
            }
        }
    }

    /**
     * 
     * @return 
     */
    public List<Layer> getLayers() {
        return layers;
    }

    /**
     * 
     * @param layers 
     */
    public void setLayers(List<Layer> layers) {
        this.layers = layers;
    }

    /**
     * 
     * @return 
     */
    @Override
    public String toString() {
        String info = "";
        for (int i = 0; i < layers.size(); i++) {
            Layer l = layers.get(i);
            info += "l" + i + ": " + l.numNeuron() + "\t" + l.getFunction().toString() + "\t";
        }
        return info;
    }
}
