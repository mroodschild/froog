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

import org.gitia.froog.lossfunction.LossFunctionFactory;
import java.util.ArrayList;
import org.ejml.simple.SimpleMatrix;
import org.gitia.froog.lossfunction.LossFunction;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class TrainingAlgorithm {

    double learningRate = 0.01;
    double regularization = 0;
    double momentum = 0;
    int epoch = 0;

    LossFunction lossFunction = LossFunctionFactory.getLossFunction(LossFunction.MSE);

    ArrayList<Double> cost = new ArrayList<>();
    ArrayList<Double> costTest = new ArrayList<>();

    //Funciones de Costo
    /**
     * Calculamos el costo
     *
     * La entrada y la salida deben estar en formato vertical<br>
     *
     * @param Ycalc
     * @param Yobs
     * @return (||(Yobs - Ycalc)||^2)/2
     */
    public double cost(SimpleMatrix Ycalc, SimpleMatrix Yobs) {
        return lossFunction.cost(Ycalc, Yobs);
    }

    /**
     * Calculamos el costo para una entrada
     *
     * @param Ycalc entradas X[X1, X2, ... ,Xn]
     * @param Yobs salida Yobs[Y1, Y2, ... , Ym]
     * @return (||(Yobs - Ycalc)||^2)/2
     */
    public double cost(double[] Ycalc, double[] Yobs) {
        SimpleMatrix Yob = new SimpleMatrix(Yobs.length, 1, true, Yobs);
        SimpleMatrix Ycal = new SimpleMatrix(Ycalc.length, 1, true, Ycalc);
        return cost(Ycal, Yob);
    }

    public double getLearningRate() {
        return learningRate;
    }

    /**
     *
     * @param alpha default = 0.01
     */
    public void setLearningRate(double alpha) {
        this.learningRate = alpha;
        System.out.println("Learning Rate:\t" + this.learningRate);
    }

    public double getRegularization() {
        return regularization;
    }

    /**
     *
     * @param regularization default = 0
     */
    public void setRegularization(double regularization) {
        this.regularization = regularization;
        System.out.println("Regularization:\t" + this.regularization);
    }

    public ArrayList<Double> getCost() {
        return cost;
    }

    public void setCost(ArrayList<Double> cost) {
        this.cost = cost;
    }

    public double getMomentum() {
        return momentum;
    }

    public void setMomentum(double momentum) {
        this.momentum = momentum;
        System.out.println("Momentum:\t" + this.momentum);
    }

    public LossFunction getLossFunction() {
        return lossFunction;
    }

    public void setEpoch(int epoch) {
        this.epoch = epoch;
        System.out.println("Epoch:\t" + this.epoch);
    }

    public int getEpoch() {
        return epoch;
    }

    /**
     * por defecto esta seleccionada la función de costo LinearRegression
     *
     * @param lossFunction
     */
    public void setLossFunction(String lossFunction) {
        this.lossFunction = LossFunctionFactory.getLossFunction(lossFunction);
        System.out.println("Loss Function:\t" + this.lossFunction.toString());
    }

    public ArrayList<Double> getCostTest() {
        return costTest;
    }

    public void setCostTest(ArrayList<Double> costTest) {
        this.costTest = costTest;
    }
}
