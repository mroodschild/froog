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
     * Recibimos los datos ordenados de la siguiente manera:<br><br>
     * Donde las columnas son las características y las filas son los datos a
     * procesar <br>
     * <br>
     * <table>
     * <tr><td>|0.9</td><td>0.8</td><td>0.66|</td></tr>
     * <tr><td>|0.1</td><td>0.3</td><td>0.4 |</td></tr>
     * <tr><td>|...</td><td>...</td><td>... |</td></tr>
     * <tr><td>|0.1</td><td>0.3</td><td>0.4 |</td></tr>
     * </table>
     *
     * @param input
     * @return
     */
    @Override
    public SimpleMatrix outputAll(SimpleMatrix input) {
//        SimpleMatrix inputT = input.transpose();
//        SimpleMatrix out = new SimpleMatrix(input.numRows(), layers.get(layers.size() - 1).numNeuron());
//        int size = input.numRows();
//        for (int i = 0; i < size; i++) {
//            SimpleMatrix in = inputT.extractVector(false, i);
//            out.setRow(i, 0, output(in).transpose().getMatrix().getData());
//        }
//        return out;
        return output(input.transpose()).transpose();
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
     * retornamos todas las salidas de todas las capas
     *
     * @param input
     * @return
     */
    public List<SimpleMatrix> outputLayers(SimpleMatrix input) {
        List<SimpleMatrix> outputs = new ArrayList<>();
        SimpleMatrix aux = new SimpleMatrix(input);
        int size = layers.size();
        for (int i = 0; i < size; i++) {
            aux = layers.get(i).output(aux);
        outputs.add(aux);
        }
        return outputs;
    }

    /**
     * devuelve la z de la capa deseada
     *
     * @param input
     * @param layer
     * @return
     */
    public SimpleMatrix outputZ(SimpleMatrix input, int layer) {
        SimpleMatrix aux = new SimpleMatrix(input);
        aux = layers.get(0).outputZ(aux);
        for (int i = 1; i <= layer; i++) {
            aux = layers.get(i).output(aux);
        }
        return aux;
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
     * @return W1, W2, ..., Wn, B1, B2, ...,Bm
     */
    public SimpleMatrix getParams() {
        if (layers.isEmpty()) {
            System.err.println("Inicialice los pesos primero");
            return null;
        }
        double[] aux = new double[0];
        for (int i = 0; i < layers.size(); i++) {
            aux = ArrayUtils.addAll(aux, layers.get(i).getW().getMatrix().getData());
        }
        for (int i = 0; i < layers.size(); i++) {
            aux = ArrayUtils.addAll(aux, layers.get(i).getB().getMatrix().getData());
        }
        return new SimpleMatrix(1, aux.length, true, aux);
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
        double[] aux = new double[0];
        for (int i = 0; i < layers.size(); i++) {
            aux = ArrayUtils.addAll(aux, layers.get(i).getW().getMatrix().getData());
        }
        return new SimpleMatrix(1, aux.length, true, aux);
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
        double[] aux = new double[0];
        for (int i = 0; i < layers.size(); i++) {
            aux = ArrayUtils.addAll(aux, layers.get(i).getB().getMatrix().getData());
        }
        return new SimpleMatrix(1, aux.length, true, aux);
    }

    /**
     * Copiamos los pesos en W2 W3 B2 B3 manteniendo la estructura inicial de
     * esas matrices, la forma de copiado es: <br>
     * <br>
     * 1 2 3 <br>
     * 4 5 6 <br>
     * 7 8 9 <br>
     *
     * @param pesos
     */
    public void setPesos(SimpleMatrix pesos) {
        if (layers.isEmpty()) {
            System.err.println("Inicialice los capas primero");
        } else {
            double[] aux = new double[0];
            //reservamos los espacios de w
            for (int i = 0; i < layers.size(); i++) {
                aux = ArrayUtils.addAll(aux, layers.get(i).getW().getMatrix().getData());
            }
            //reservamos los espacios de b
            for (int i = 0; i < layers.size(); i++) {
                aux = ArrayUtils.addAll(aux, layers.get(i).getB().getMatrix().getData());
            }
            int posicion = 0;
            int size;
            double[] datos = pesos.getMatrix().getData();
            //cargamos los w
            for (int i = 0; i < layers.size(); i++) {
                Layer layer = layers.get(i);
                size = layer.getW().getNumElements();
                layer.getW().getMatrix().setData(
                        ArrayUtils.subarray(datos, posicion, posicion + size));
                posicion += size;
            }
            //cargamos los b
            for (int i = 0; i < layers.size(); i++) {
                Layer layer = layers.get(i);
                size = layer.getB().getNumElements();
                layer.getB().getMatrix().setData(
                        ArrayUtils.subarray(datos, posicion, posicion + size));
                posicion += size;
            }
        }
    }

    public List<Layer> getLayers() {
        return layers;
    }

    public void setLayers(List<Layer> layers) {
        this.layers = layers;
    }

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
