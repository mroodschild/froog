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
package org.gitia.froog.trainingalgorithm;

import org.gitia.froog.Feedforward;
import org.gitia.froog.layer.Layer;
import org.gitia.froog.transferfunction.TransferFunction;
import java.util.ArrayList;
import java.util.List;
import org.ejml.simple.SimpleMatrix;
import org.gitia.froog.statistics.Clock;
import org.gitia.froog.statistics.Compite;
import org.gitia.froog.statistics.ConfusionMatrix;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class Backpropagation extends TrainingAlgorithm {

    Feedforward net;//red a entrenar
    SimpleMatrix input;//datos de entrada original a la red (cada fila es un dato, y cada columna una entrada)
    SimpleMatrix output;//salida original de la red correspondiente a la entrada
    SimpleMatrix inputTest = new SimpleMatrix();
    SimpleMatrix outputTest = new SimpleMatrix();
    SimpleMatrix inputBach = new SimpleMatrix();//datos de entrada a la red (cada fila es un dato, y cada columna una entrada)
    SimpleMatrix outputBach = new SimpleMatrix();//salida de la red correspondiente a la entrada
    double costOverall;//costo despues de pasar todos los datos hasta terminar el mini bach
    double costOverallTest = -1;
    double costPartial;//costo parcial dato por dato
    double ND;//número de datos de entrenamiento
    int batchSize = 0;
    int cantidadBach;
    int testFrecuency = 1;
    List<SimpleMatrix> deltasW = new ArrayList<>();//deltas de los W calculados para cada capa
    List<SimpleMatrix> deltasB = new ArrayList<>();//deltas de los B calculados para cada capa
    List<SimpleMatrix> deltasWprev = new ArrayList<>();//deltas de los W calculados para cada capa
    List<SimpleMatrix> deltasBprev = new ArrayList<>();//deltas de los B calculados para cada capa
    List<SimpleMatrix> gradW = new ArrayList<>();//gradientes para cada capa
    List<SimpleMatrix> gradB = new ArrayList<>();//gradientes para cada capa
    List<SimpleMatrix> deriv = new ArrayList<>();//derivadas
    int iteracion = 0;
    boolean classification = false;

    public Backpropagation() {
    }

    //Algoritmo de Entrenamiento
    /**
     * Antes de entrenar poner las épocas
     *
     * @param net
     * @param input
     * @param output
     */
    public void entrenar(Feedforward net, SimpleMatrix input, SimpleMatrix output) {
        this.net = net;
        this.input = new SimpleMatrix(input);
        this.output = new SimpleMatrix(output);
        double aciertoTrain = 0;
        double aciertoTest = 0;
        inicializar();
        inicializarBatch();
        Clock clock = new Clock();
        double costAllTrain = -1;
        for (int i = 0; i < this.epoch; i++) {
            for (int j = 0; j < cantidadBach; j++) {
                clock.start();
                bachDatos(j);
                //Paso 1:   inicializar los DeltaW  y los DeltaB
                deltasZero();
                //Paso 1.1: Computar el costo
                costOverall = loss(net.outputAll(inputBach), outputBach);
                this.cost.add(costOverall);
                //Paso 2:   calcular los D_w y los D_b
                calcularGradientes();
                //Paso 3:   Actualizar los parámetros
                if (momentum > 0) {
                    actualizarParametrosMomentum(this.inputBach.numRows());
                } else {
                    actualizarParametros(this.inputBach.numRows());
                }
                if ((iteracion % testFrecuency) == 0 && inputTest.getMatrix() != null) {
                    SimpleMatrix yCalcTrain = net.outputAll(this.input);
                    SimpleMatrix yCalcTest = net.outputAll(this.inputTest);
                    costAllTrain = loss(yCalcTrain, this.output);
                    costOverallTest = loss(yCalcTest, outputTest);
                    this.costTest.add(costOverallTest);
                    if (classification) {
                        ConfusionMatrix cMatrixTrain = new ConfusionMatrix();
                        cMatrixTrain.eval(Compite.eval(yCalcTrain), this.output);
                        aciertoTrain = cMatrixTrain.getAciertosPorc();
                        ConfusionMatrix cMatrixTest = new ConfusionMatrix();
                        cMatrixTest.eval(Compite.eval(yCalcTest), outputTest);
                        aciertoTest = cMatrixTest.getAciertosPorc();
                    }
                }
                clock.stop();
                if ((iteracion % testFrecuency) != 0 || inputTest.getMatrix() == null) {
                    System.out.println(
                            "It:\t" + iteracion
                            + "\tTrain:\t" + costOverall
                            + "\tTime:\t" + clock.timeSec() + " s.");
                } else {
                    if (classification) {
                        System.out.println(
                                "It:\t" + iteracion
                                + "\tTrain:\t" + costOverall
                                + "\tTrain Complete:\t" + costAllTrain
                                + "\tTest:\t" + costOverallTest
                                + "\tTrain Aciertos:\t" + aciertoTrain + "\t%."
                                + "\tTest Aciertos:\t" + aciertoTest + "\t%."
                                + "\tTime:\t" + clock.timeSec() + "\ts."
                        );
                    } else {
                        System.out.println(
                                "It:\t" + iteracion
                                + "\tTrain:\t" + costOverall
                                + "\tTrain Complete:\t" + costAllTrain
                                + "\tTest:\t" + costOverallTest
                                + "\tTime:\t" + clock.timeSec() + "\ts."
                        );
                    }

                }
                iteracion++;
            }
        }
    }

    /**
     * Aquí inicializamos el bach, indicamos cuantas partes serán formadas,
     * según el bachSize, esto sera guardado en cantidadBach, que luego será
     * utilizado en la selección de los datos.<br>
     *
     * Si bachSize está en 0 o un número negativo, se tomarán todos los datos
     * como tamaño de bachSize.
     *
     */
    protected void inicializarBatch() {
        if (batchSize <= 0) {
            batchSize = this.input.numRows();
            cantidadBach = 1;
        } else {
            cantidadBach = this.input.numRows() / batchSize;
        }
    }

    /**
     * Aquí indicamos los datos que irán al entrenamiento, si en la división de
     * datos hay resto, estos se ubicarán en la última parte
     *
     * @param part
     */
    protected void bachDatos(int part) {
        if (part < (cantidadBach - 1)) {
            inputBach = this.input.extractMatrix(batchSize * part, batchSize * part + batchSize, 0, SimpleMatrix.END);
            outputBach = this.output.extractMatrix(batchSize * part, batchSize * part + batchSize, 0, SimpleMatrix.END);
        } else {//como es la última parte tomamos los datos restantes.
            inputBach = this.input.extractMatrix(batchSize * part, SimpleMatrix.END, 0, SimpleMatrix.END);
            outputBach = this.output.extractMatrix(batchSize * part, SimpleMatrix.END, 0, SimpleMatrix.END);
        }
    }

    /**
     *
     * @param net
     * @param input
     * @param output
     */
    public void entrenar(Feedforward net, double[][] input, double[][] output) {
        entrenar(net, new SimpleMatrix(input), new SimpleMatrix(output));
    }

    /**
     * iniciamos los deltas según la cantidad de pesos y capas que hay en la red
     * y colocamos todos los deltas en 0.
     */
    public void inicializar() {
        iteracion = 0;
        deltasB.clear();
        deltasW.clear();
        deriv.clear();
        gradB.clear();
        gradW.clear();
        deltasWprev.clear();
        deltasBprev.clear();
        for (int i = 0; i < net.getLayers().size(); i++) {
            //agregamos los deltas de todos los pesos
            deltasW.add(new SimpleMatrix(net.getLayers().get(i).getW()));
            deltasWprev.add(new SimpleMatrix(net.getLayers().get(i).getW()));
            //agregamos todos los deltas de los bias
            deltasB.add(new SimpleMatrix(net.getLayers().get(i).getB()));
            deltasBprev.add(new SimpleMatrix(net.getLayers().get(i).getB()));
            //agregamos las matrices de derivadas 1 por neurona
            deriv.add(new SimpleMatrix());
            gradW.add(new SimpleMatrix());
            gradB.add(new SimpleMatrix());
        }
        cost.clear();
        for (int i = 0; i < deltasW.size(); i++) {
            deltasWprev.get(i).zero();
            deltasBprev.get(i).zero();
        }
        deltasZero();//inicializamos los deltas en 0
    }

    /**
     * colocamos los deltas en 0;
     */
    protected void deltasZero() {
        for (int i = 0; i < deltasW.size(); i++) {
            deltasW.get(i).zero();
            deltasB.get(i).zero();
        }
    }

    protected void calcularGradientes() {
        deltasZero();//quitar?
        ND = inputBach.numRows();
        SimpleMatrix in;
        SimpleMatrix yObs;
        for (int i = 0; i < ND; i++) {//Aquí debemos paralelizar
            //extraemos la fila y la ponemos vertical
            in = inputBach.extractVector(true, i).transpose();
            yObs = outputBach.extractVector(true, i).transpose();
            //obtenemos la salida de todas las capas para ganar tiempo
            List<SimpleMatrix> outputs = net.outputLayers(in);
            //calculamos los delta
            SimpleMatrix yCalc = outputs.get(outputs.size() - 1);
            derivadaOutputLayers(yCalc, yObs);
            derivadaHiddenLayers(outputs);

            //calculamos los gradientes
            //la primera entrada corresponde a los datos
            SimpleMatrix a_t = in.transpose();
            for (int j = 0; j < gradW.size(); j++) {
                SimpleMatrix d = deriv.get(j);
                //calculamos el gradiente
                gradW.set(j, d.mult(a_t));
                gradB.set(j, d);
                //preparamos la entrada para la siguiente capa
                a_t = outputs.get(j).transpose();
            }

            for (int j = 0; j < gradW.size(); j++) {
                //agregamos el delta
                deltasW.set(j, deltasW.get(j).plus(gradW.get(j)));
                deltasB.set(j, deltasB.get(j).plus(gradB.get(j)));
            }
        }
    }

    /**
     * Calculamos el costo de la iteración
     *
     * @param yCalc
     * @param yObs
     * @return loss = lossfunction + regularization * ||W||²
     */
    protected double loss(SimpleMatrix yCalc, SimpleMatrix yObs) {
        double loss;
        loss = lossFunction.costAll(yCalc, yObs);
        return loss + lossRegularization();
    }

    protected double lossRegularization() {
        double loss = 0;
        if (regularization > 0) {
            SimpleMatrix weights = net.getParamsW();
            loss += weights.mult(weights.transpose()).scale(regularization).get(0);
        }
        return loss;
    }

    /**
     * El input y la Ycalc entran en formato vertical<br>
     * calcula el delta de la salida (-1) * (Yobs - Ycalc) .* f'(z)
     *
     * @param yCalc
     * @param yObs salida
     */
    protected void derivadaOutputLayers(SimpleMatrix yCalc, SimpleMatrix yObs) {
        //System.out.println("DERIVADA OUTPUT");
        //vemos que número de capa es
        int lastLayer = net.getLayers().size() - 1;
        //obtenemos su función de transferencia para la derivada
        TransferFunction function = net.getLayers().get(lastLayer).getFunction();
        //obtenemos la salida de la red, el "a" calculado
        //SimpleMatrix yCalc = net.output(input);//mejorar rendimiento

        SimpleMatrix d = function.derivative(yCalc, yObs);
        //ponemos la derivada de la salida en la posición de la salida
        deriv.set(lastLayer, d);
    }

    /**
     * primero se debe calcular el delta de salida<br>
     * d_i = (W_i+1)^t * d_i+1 .* f'(z_i)
     *
     * @param outputs
     */
    protected void derivadaHiddenLayers(List<SimpleMatrix> outputs) {
        //List<SimpleMatrix> outputs = net.outputLayers(in);
        //iniciamos en la penúltima capa
        if ((deriv.size() - 2) >= 0) {
            for (int i = deriv.size() - 2; i >= 0; i--) {
                TransferFunction function = net.getLayers().get(i).getFunction();
                SimpleMatrix W = net.getLayers().get(i + 1).getW();
                //en la softmax la derivada sale del gradiente parcial
                SimpleMatrix d = deriv.get(i + 1);//cual es la derivada de la softmax? aqui falta aclarar
                SimpleMatrix f_a = function.derivative(outputs.get(i));
                deriv.set(i, W.transpose().mult(d).elementMult(f_a));
            }
        }
    }

    /**
     * W(+1) = W - stepSize * [(Dw / m)+ W * reg] <br>
     * B(+1) = B - stepSize * (Db / m) <br>
     *
     * @param m cantidad de datos
     */
    protected void actualizarParametros(double m) {
        //double div = stepSize / m;
        for (int i = 0; i < net.getLayers().size(); i++) {
            Layer layer = net.getLayers().get(i);
            SimpleMatrix W = layer.getW();
            SimpleMatrix B = layer.getB();
            SimpleMatrix reg = W.scale(regularization);
            W = W.minus(deltasW.get(i).divide(m).plus(reg).scale(learningRate));
            B = B.minus(deltasB.get(i).divide(m).scale(learningRate));
            layer.setW(W);
            layer.setB(B);
            net.getLayers().set(i, layer);
        }
    }

    /**
     * W(+1) = W - stepSize * [(Dw / m)+ W * reg] <br>
     * B(+1) = B - stepSize * (Db / m) <br>
     *
     * @param m cantidad de datos
     */
    protected void actualizarParametrosMomentum(double m) {
        for (int i = 0; i < net.getLayers().size(); i++) {
            Layer layer = net.getLayers().get(i);
            SimpleMatrix W = layer.getW();
            SimpleMatrix B = layer.getB();
            SimpleMatrix reg = W.scale(regularization);

            SimpleMatrix vW = deltasWprev.get(i).scale(momentum).minus(deltasW.get(i).divide(m).plus(reg).scale(learningRate));
            W = W.plus(vW);
            SimpleMatrix vB = deltasBprev.get(i).scale(momentum).minus(deltasB.get(i).divide(m).scale(learningRate));
            B = B.plus(vB);

            layer.setW(W);
            layer.setB(B);
            net.getLayers().set(i, layer);
            deltasBprev.set(i, vB);
            deltasWprev.set(i, vW);
        }
    }

    public void setNet(Feedforward net) {
        this.net = net;
    }

    public Feedforward getNet() {
        return net;
    }

    /**
     *
     * @param bachSize por defecto = 0 (sin bach)
     */
    public void setBatchSize(int bachSize) {
        this.batchSize = bachSize;
        System.out.println("Batch Size:\t" + this.batchSize);
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setInputTest(SimpleMatrix inputTest) {
        this.inputTest = inputTest;
        System.out.println("Input Test:\t" + inputTest.numRows() + "\tx\t" + inputTest.numCols());
    }

    public void setOutputTest(SimpleMatrix outputTest) {
        this.outputTest = outputTest;
    }

    public void setTestFrecuency(int testFrecuency) {
        this.testFrecuency = testFrecuency;
        System.out.println("Test Frecuency:\t" + this.testFrecuency);
    }

    public void setClassification(boolean classification) {
        this.classification = classification;
        System.out.println("Classification:\t" + this.classification);
    }

}
