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
package org.gitia.froog.optimizer;

import org.gitia.froog.lossfunction.LossFunctionFactory;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;
import org.ejml.simple.SimpleMatrix;
import org.gitia.froog.Feedforward;
import org.gitia.froog.lossfunction.LossFunction;
import org.gitia.froog.optimizer.gradient.Gradient;
import org.gitia.froog.optimizer.gradient.StandardGradient;
//import org.gitia.froog.trainingalgorithm.updaterule.Momentum;
import org.gitia.froog.optimizer.updaterule.Update;
import org.gitia.froog.optimizer.updaterule.UpdateRule;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class TrainingAlgorithm {

    protected Feedforward net;//red a train
    protected SimpleMatrix input;//datos de entrada original a la red (cada fila es un dato, y cada columna una entrada)
    protected SimpleMatrix output;//salida original de la red correspondiente a la entrada
    protected SimpleMatrix inputTest;// = new SimpleMatrix();
    protected SimpleMatrix outputTest;// = new SimpleMatrix();
    protected double learningRate = 0.01;
    protected double L2_Lambda = 0;
    protected UpdateRule updateRule = new Update();
    protected List<SimpleMatrix> gradW_prev = new ArrayList<>();//deltas de los W calculados para cada capa
    protected List<SimpleMatrix> gradB_prev = new ArrayList<>();//deltas de los B calculados para cada capa
    protected List<SimpleMatrix> gradW = new ArrayList<>();//gradientes para cada capa
    protected List<SimpleMatrix> gradB = new ArrayList<>();//gradientes para cada capa
    protected int epoch = 0;
    //protected int cantidadBach;
    protected int testFrecuency = 1;
    protected boolean classification = false;
    protected Gradient gradient = new StandardGradient();

    protected LossFunction lossFunction = LossFunctionFactory.getLossFunction(LossFunction.MSE);

    ArrayList<Double> cost = new ArrayList<>();
    ArrayList<Double> costTest = new ArrayList<>();

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
        return L2_Lambda;
    }

    /**
     *
     * @param regularization default = 0
     */
    public void setRegularization(double regularization) {
        this.L2_Lambda = regularization;
        System.out.println("Regularization:\t" + this.L2_Lambda);
    }

    public ArrayList<Double> getCost() {
        return cost;
    }

    public void setCost(ArrayList<Double> cost) {
        this.cost = cost;
    }

//    public void setMomentum(double momentum) {
//        if (momentum > 0) {
//            updateRule = new Momentum();
//            this.updateRule.setMomentum(momentum);
//            System.out.println("Momentum:\t" + momentum);
//        }
//    }

    /**
     *
     * @return a matrix with all the gradients of dimensions [n x 1] GW1,GW2...
     * GB1, GB2...
     */
    public SimpleMatrix getGradients() {
        if (net.getLayers().isEmpty()) {
            System.err.println("Inicialice los gradientes primero");
            return null;
        }
        return getGradients(gradW, gradB);
    }

    /**
     *
     * @param gradW
     * @param gradB
     * @return a matrix with all the gradients of dimensions [n x 1] GW1,GW2...
     * GB1, GB2...
     */
    public SimpleMatrix getGradients(List<SimpleMatrix> gradW, List<SimpleMatrix> gradB) {
        double[] aux = new double[0];
        for (int i = 0; i < gradW.size(); i++) {
            aux = ArrayUtils.addAll(aux, gradW.get(i).getDDRM().getData());
        }
        for (int i = 0; i < gradB.size(); i++) {
            aux = ArrayUtils.addAll(aux, gradB.get(i).getDDRM().getData());
        }
        return new SimpleMatrix(aux.length, 1, true, aux);
    }

    /**
     *
     * @param g
     * @param gradW
     * @param gradB
     */
    public void setGradientsToList(SimpleMatrix g, List<SimpleMatrix> gradW, List<SimpleMatrix> gradB) {
        int posicion = 0;
        int size;
        double[] datos = g.getDDRM().getData();
        for (int i = 0; i < gradW.size(); i++) {
            size = gradW.get(i).getNumElements();
            gradW.get(i).getDDRM().setData(
                    ArrayUtils.subarray(datos, posicion, posicion + size));
            posicion += size;
        }
        for (int i = 0; i < gradB.size(); i++) {
            size = gradB.get(i).getNumElements();
            gradB.get(i).getDDRM().setData(
                    ArrayUtils.subarray(datos, posicion, posicion + size));
            posicion += size;
        }

    }
    
    /**
     * iniciamos los deltas según la cantidad de pesos y capas que hay en la red
     * y colocamos todos los deltas en 0.
     */
    public void init() {
        gradB.clear();
        gradW.clear();
        cost.clear();
        for (int i = 0; i < net.getLayers().size(); i++) {
            gradW.add(new SimpleMatrix(net.getLayers().get(i).getW()));
            gradB.add(new SimpleMatrix(net.getLayers().get(i).getB()));
            gradW.get(i).zero();
            gradB.get(i).zero();
        }
        updateRule.init(gradW, gradB);
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

    public void setNet(Feedforward net) {
        this.net = net;
    }

    public Feedforward getNet() {
        return net;
    }

    public List<SimpleMatrix> getGradW() {
        return gradW;
    }

    public List<SimpleMatrix> getGradB() {
        return gradB;
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

    public void setGradient(Gradient gradient) {
        this.gradient = gradient;
    }

    public Gradient getGradient() {
        return gradient;
    }
}
