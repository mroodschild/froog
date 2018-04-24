/*
 * The MIT License
 *
 * Copyright 2018 Matías Rodschild <mroodschild@gmail.com>.
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
        SimpleMatrix bach_in = bachData(0, input);
        SimpleMatrix bach_out = bachData(0, output);

        //primeraDireccion();//paso1
        rk = computeGradient(net, bach_in, bach_out).negative();
        pk = rk.copy();
        success = true;
        for (k = 0; k < epoch; k++) {
            for (int j = 0; j < cantidadBach; j++) {
                bach_in = bachData(j, input);
                bach_out = bachData(j, output);
                for (int i = 0; i < batchRefresh; i++) {
                    
                    //informacionSegOrden();//paso 2
                    if (success == true) {
                        sigmaK = sigma / NormOps_DDRM.normP2(pk.getDDRM());
                        net.setParameters(Wk);
                        SimpleMatrix g1 = computeGradient(net, bach_in, bach_out);
//                Wk.printDimensions();
//                pk.printDimensions();
                        net.setParameters(Wk.plus(pk.transpose().scale(sigmaK)));
                        SimpleMatrix g2 = computeGradient(net, bach_in, bach_out);
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
                    double E = lossFunction.costAll(net.output(bach_in), bach_out);
                    net.setParameters(Wk.plus(pk.transpose().scale(alphak)));
                    double E_conj = lossFunction.costAll(net.output(bach_in), bach_out);
                    nablaK = 2 * deltaK * (E - E_conj) / Math.pow(uk, 2);
                    //evalNabla();//paso 7
                    if (nablaK >= 0) {
                        W_new = Wk.plus(pk.transpose().scale(alphak));
                        net.setParameters(W_new);
                        r_new = computeGradient(net, bach_in, bach_out).negative();
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
     * Aquí inicializamos el bach, indicamos cuantas partes serán formadas,
     * según el bachSize, esto será guardado en cantidadBach, que luego será
     * utilizado en la selección de los datos.<br>
     *
     * Si bachSize menor o igual a 0, se tomarán todos los datos como tamaño de
     * bachSize.
     *
     */
    protected void initBatch() {
        if (batchSize <= 0) {
            batchSize = this.input.numCols();
            cantidadBach = 1;
        } else {
            cantidadBach = this.input.numCols() / batchSize;
        }
    }

    /**
     *
     * @param part
     * @param data
     * @return
     */
    protected SimpleMatrix bachData(int part, SimpleMatrix data) {
        SimpleMatrix bach;
        if (part < (cantidadBach - 1)) {
            bach = data.extractMatrix(0, SimpleMatrix.END, batchSize * part, batchSize * part + batchSize);
        } else {//como es la última parte tomamos los datos restantes.
            bach = data.extractMatrix(0, SimpleMatrix.END, batchSize * part, SimpleMatrix.END);
        }
        return bach;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public void setBatchRefresh(int batchRefresh) {
        this.batchRefresh = batchRefresh;
    }
    
    

}
