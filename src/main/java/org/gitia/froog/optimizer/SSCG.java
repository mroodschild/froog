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

import java.util.List;
import org.ejml.dense.row.NormOps_DDRM;
import org.ejml.simple.SimpleMatrix;
import org.gitia.froog.Feedforward;

public class SSCG extends Backpropagation {

    SimpleMatrix pk;// = new SimpleMatrix();
    SimpleMatrix rk;// -gradient
    SimpleMatrix r_new;
    SimpleMatrix p_new;
    SimpleMatrix Sk;
    double deltaK;
    double uk, alphak;
    double sigma = 1e-8;// 0 < sigma <= 1e-4
    double sigmaK = 0;
    double lambdaT = 0;
    double lambdaK = 1e-7;// 0 < lambdaK <= 1e-6
    double nablaK = 0;
    int k = 0;
    int N = 0;
    boolean success = true;
    protected int cantidadBatch;

    SimpleMatrix Wk;
    SimpleMatrix W_new;

    int batchSize = 0;
    int batchRefresh = 0;

    public SSCG() {
    }

    //Training Algorithm
    /**
     *
     * @param net neural network to train
     * @param input every row is a feature and every column is a register
     * @param output every row is a feature and every column is a register
     */
    @Override
    public void train(Feedforward net, SimpleMatrix input, SimpleMatrix output) {
        this.net = net;
        iteracion =0;
        N = net.getParameters().getNumElements();
        this.Wk = net.getParameters();
        W_new = Wk.copy();
        lambdaK = 1e-7;
        lambdaT = 0;

        this.input = input;
        this.output = output;
        init();//
        initBatch();
        SimpleMatrix batch_in = batchData(0, input);
        SimpleMatrix batch_out = batchData(0, output);

        //primeraDireccion();//paso1
        rk = computeGradient(net, batch_in, batch_out).negative();
        pk = rk.copy();
        success = true;
        for (k = 0; k < epoch; k++) {
            for (int j = 0; j < cantidadBatch; j++) {
                batch_in = batchData(j, input);
                batch_out = batchData(j, output);
                for (int i = 0; i < batchRefresh; i++) {
                    
                    //informacionSegOrden();//paso 2
                    if (success == true) {
                        sigmaK = sigma / NormOps_DDRM.normP2(pk.getDDRM());
                        net.setParameters(Wk);
                        SimpleMatrix g1 = computeGradient(net, batch_in, batch_out);
//                Wk.printDimensions();
//                pk.printDimensions();
                        net.setParameters(Wk.plus(pk.transpose().scale(sigmaK)));
                        SimpleMatrix g2 = computeGradient(net, batch_in, batch_out);
                        Sk = g2.minus(g1).divide(sigmaK);
                        deltaK = pk.transpose().mult(Sk).get(0);
                    }
                    //escalado();//paso 3
                    deltaK = deltaK + (lambdaK - lambdaT) * Math.pow(NormOps_DDRM.normP2(pk.getDDRM()), 2);
                    //hessianPositive();//paso 4
                    if (deltaK <= 0) {
                        lambdaT = 2 * (lambdaK - deltaK / Math.pow(NormOps_DDRM.normP2(pk.getDDRM()), 2));
                        deltaK = -deltaK + lambdaK * Math.pow(NormOps_DDRM.normP2(pk.getDDRM()), 2);
                        lambdaK = lambdaT;
                    }
                    //tamanoPaso();//paso 5
                    uk = pk.transpose().mult(rk).get(0);
                    alphak = uk / deltaK;
                    //comparacionParametros();//paso 6
                    net.setParameters(Wk);
                    double E = lossFunction.costAll(net.output(batch_in), batch_out);
                    net.setParameters(Wk.plus(pk.transpose().scale(alphak)));
                    double E_conj = lossFunction.costAll(net.output(batch_in), batch_out);
                    nablaK = 2 * deltaK * (E - E_conj) / Math.pow(uk, 2);
                    //evalNabla();//paso 7
                    if (nablaK >= 0) {
                        W_new = Wk.plus(pk.transpose().scale(alphak));
                        net.setParameters(W_new);
                        r_new = computeGradient(net, batch_in, batch_out).negative();
                        lambdaT = 0;
                        success = true;
                        if (k % N == 0) {
                            p_new = r_new;
                        } else {
                            double norm2 = Math.pow(NormOps_DDRM.normP2(r_new.getDDRM()), 2);
                            double beta = (norm2 - r_new.transpose().mult(rk).get(0)) / uk;
                            p_new = r_new.plus(pk.scale(beta));
                        }
                        if (nablaK >= 0.75) {
                            lambdaK = lambdaK * 0.25;
                        }
                    } else {
                        lambdaT = lambdaK;
                        success = false;
                    }
                    //evalSmallNabla();//paso 8
                    if (nablaK < 0.25) {
                        lambdaK = lambdaK + (deltaK * (1 - nablaK) / Math.pow(NormOps_DDRM.normP2(pk.getDDRM()), 2));
                    }
                    //actualizarPesos();
                    //paso 9 
                    if (rk.elementSum() != 0) {
                        rk = r_new;
                        pk = p_new;
                        Wk = W_new.copy();
                    } else {
                        break;
                    }
                    System.out.println("it: " + iteracion+"\tE:\t"+E+"\tE_conj:\t"+E_conj);
                    iteracion++;
                }
            }
        }
    }

    /**
     *
     * @param net
     * @param X
     * @param Y
     * @return
     */
    public SimpleMatrix computeGradient(Feedforward net, SimpleMatrix X, SimpleMatrix Y) {
        List<SimpleMatrix> A = net.activations(X);
        gradient.compute(net, A, gradW, gradB, X, Y);
        return getGradients(gradW, gradB);
    }

    /**
     * Aquí inicializamos el batch, indicamos cuantas partes serán formadas,
     * según el batchSize, esto será guardado en cantidadBatch, que luego será
     * utilizado en la selección de los datos.<br>
     *
     * Si batchSize menor o igual a 0, se tomarán todos los datos como tamaño de
     * batchSize.
     *
     */
    protected void initBatch() {
        if (batchSize <= 0) {
            batchSize = this.input.numCols();
            cantidadBatch = 1;
        } else {
            cantidadBatch = this.input.numCols() / batchSize;
        }
    }

    /**
     *
     * @param part
     * @param data
     * @return
     */
    protected SimpleMatrix batchData(int part, SimpleMatrix data) {
        SimpleMatrix batch;
        if (part < (cantidadBatch - 1)) {
            batch = data.extractMatrix(0, SimpleMatrix.END, batchSize * part, batchSize * part + batchSize);
        } else {//como es la última parte tomamos los datos restantes.
            batch = data.extractMatrix(0, SimpleMatrix.END, batchSize * part, SimpleMatrix.END);
        }
        return batch;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public void setBatchRefresh(int batchRefresh) {
        this.batchRefresh = batchRefresh;
    }
    
    

}
