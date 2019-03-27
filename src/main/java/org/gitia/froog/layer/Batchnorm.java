/*
 * Copyright 2019
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
package org.gitia.froog.layer;

import java.util.Random;
import java.util.stream.IntStream;
import org.ejml.data.MatrixType;
import org.ejml.simple.SimpleMatrix;
import org.gitia.froog.layer.initialization.WeightInit;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class Batchnorm implements Layer {

    protected SimpleMatrix gamma;   //[neurons x 1]
    protected SimpleMatrix beta;    //[neurons x 1]

    protected SimpleMatrix media;     //[neurons x 1]
    protected SimpleMatrix variance;  //[neurons x 1]

    protected SimpleMatrix xNormalized; //[neurons x batchSize]
    protected SimpleMatrix y;           //[neurons x batchSize]

    SimpleMatrix mean;
    SimpleMatrix xmean;
    SimpleMatrix sq;
    SimpleMatrix var;
    SimpleMatrix sqrtvar;
    SimpleMatrix ivar;
    SimpleMatrix xhat;
    SimpleMatrix gammax;
    SimpleMatrix out;

    protected double eps = 2e-8;

    protected Random random = new Random();

    /**
     *
     */
    public Batchnorm() {
    }

    /**
     * Init Layer. <br>
     *
     * to select a transfer function set TransferFunction.LOGSIG<br>
     * logsig, tansig, purelim, softplus <br>
     * <br>
     *
     * @param input number of inputs
     * @param output number of neurons
     * @param WeightInit
     * @param random
     */
    public Batchnorm(int input, int output, String WeightInit, Random random) {
        this(input, output, WeightInit, 0, random);
    }

    /**
     * Init Layer. <br>
     *
     * to select a transfer function set TransferFunction.LOGSIG<br>
     * logsig, tansig, purelim, softplus <br>
     * <br>
     *
     * @param input number of inputs
     * @param output number of neurons
     * @param WeightInit
     * @param keepProb for dropout
     * @param random
     */
    public Batchnorm(int input, int output, String WeightInit, double keepProb, Random random) {
        this.random = random;
        this.gamma = new SimpleMatrix(output, input);
        this.beta = new SimpleMatrix(output, 1);
    }

    /**
     * Init Layer. <br>
     *
     * to select a transfer function set TransferFunction.LOGSIG<br>
     * logsig, tansig, purelim, softplus <br>
     * <br>
     *
     * @param input cantidad de entradas
     * @param output cantidad de neuronas
     * @param random heredado
     */
    public Batchnorm(int input, int output, Random random) {
        this(input, output, WeightInit.DEFAULT, random);
    }

    /**
     * Init Layer. <br>
     *
     * to select a transfer function set TransferFunction.LOGSIG<br>
     * logsig, tansig, purelim, softplus <br>
     * <br>
     *
     * @param input
     * @param output
     */
    public Batchnorm(int input, int output) {
        this(input, output, new Random());
    }

    /**
     *
     * @param W
     * @param B
     */
    public Batchnorm(SimpleMatrix W, SimpleMatrix B) {
        this.gamma = W;
        this.beta = B;
    }

    /**
     *
     * Ingresamos la entrada en el formato horizontal
     *
     * @param input
     * @return
     */
    public SimpleMatrix output(double[] input) {
        SimpleMatrix x = new SimpleMatrix(input.length, 1, true, input);
        return output(x);
    }

    /**
     *
     * @param a [out prev layer - m] where m is the amount of data
     * @return a<sub>L(i)</sub> = f(a<sub>L(i)-1</sub>)
     */
    @Override
    public SimpleMatrix output(SimpleMatrix a) {
        media(a, media);
        variance(a, media, variance);
        xNormalized = normalize(a, media, variance, eps);
        y = scaleShift(xNormalized, gamma, beta);
        //normalizeScaleShift(a, xNormalized, y, media, variance, gamma, beta, eps);
        return y;
    }

    public void media(SimpleMatrix a, SimpleMatrix media) {
        int row = a.numRows();
        int cols = a.numCols();
        IntStream.range(0, row).parallel()
                .forEach(i -> {
                    int idx = i * cols;
                    double sum = 0;
                    for (int j = 0; j < cols; j++) {
                        sum += a.get(idx++);
                    }
                    media.set(i, sum / (double) cols);
                });
    }

    public void variance(SimpleMatrix a, SimpleMatrix media, SimpleMatrix variance) {
        int rows = a.numRows();
        int cols = a.numCols();
        IntStream.range(0, rows).parallel()
                .forEach(i -> {
                    int idx = i * cols;
                    double sum = 0;
                    double u = media.get(i);
                    double m = cols;
                    for (int j = 0; j < cols; j++) {
                        sum += Math.pow(a.get(idx++) - u, 2);
                    }
                    variance.set(i, sum / m);
                });
    }

    public SimpleMatrix normalize(SimpleMatrix a, SimpleMatrix media, SimpleMatrix variance, double eps) {
        int rows = a.numRows();
        int cols = a.numCols();
        SimpleMatrix x = new SimpleMatrix(rows, cols, MatrixType.DDRM);
        IntStream.range(0, rows).parallel()
                .forEach(i -> {
                    int idx = i * cols;
                    double u = media.get(i);
                    double o = variance.get(i);
                    for (int j = 0; j < cols; j++) {
                        x.set(idx, (a.get(idx++) - u) / (Math.sqrt(o + eps)));
                    }
                });
        return x;
    }

    private SimpleMatrix scaleShift(SimpleMatrix xNormalized, SimpleMatrix gamma, SimpleMatrix beta) {
        int rows = xNormalized.numRows();
        int cols = xNormalized.numCols();
        SimpleMatrix y = new SimpleMatrix(rows, cols, MatrixType.DDRM);
        IntStream.range(0, rows).parallel()
                .forEach(i -> {
                    int idx = i * cols;
                    double g = gamma.get(i);
                    double b = beta.get(i);
                    for (int j = 0; j < cols; j++) {
                        y.set(idx, g * xNormalized.get(idx++) + b);
                    }
                });
        return y;
    }

    public void forwardpass(SimpleMatrix a) {
        int row = a.numRows();
        int cols = a.numCols();

        mean = new SimpleMatrix(row, 1, MatrixType.DDRM);// [neuronas x 1]
        xmean = new SimpleMatrix(row, cols, MatrixType.DDRM);// [neuronas x datos]
        sq = new SimpleMatrix(row, cols, MatrixType.DDRM);// [neuronas x datos]**2
        var = new SimpleMatrix(row, 1, MatrixType.DDRM);// [neuronas x 1]
        sqrtvar = new SimpleMatrix(row, 1, MatrixType.DDRM);// [neuronas x 1]
        ivar = new SimpleMatrix(row, 1, MatrixType.DDRM);// [neuronas x 1]
        xhat = new SimpleMatrix(row, 1, MatrixType.DDRM);// [neuronas x datos]
        gammax = new SimpleMatrix(row, 1, MatrixType.DDRM);// [neuronas x datos]
        out = new SimpleMatrix(row, 1, MatrixType.DDRM);// [neuronas x datos]

        // step 1: calculate mean
        media(a, mean);
        subtractMean(a, mean, xmean);
        sq(sq, xmean);
        var(var, sq);
        sqrtvar(sqrtvar, var, eps);
        ivar(ivar, sqrtvar);
        xhat(xhat, xmean, ivar);
        gammax(gammax, gamma, xhat);
        out(out, gammax, beta);

    }

    /**
     * xmean = a - mean 
     * 
     * @param a
     * @param mean
     * @param xmean 
     */
    private void subtractMean(SimpleMatrix a, SimpleMatrix mean, SimpleMatrix xmean) {
        int row = a.numRows();
        int cols = a.numCols();
        IntStream.range(0, row).parallel()
                .forEach(i -> {
                    int idx = i * cols;
                    double mu = mean.get(row);
                    for (int j = 0; j < cols; j++) {
                        xmean.set(idx, a.get(idx++) - mu);
                    }
                });
    }

    /**
     * 
     * @param sq
     * @param xmean 
     */
    private void sq(SimpleMatrix sq, SimpleMatrix xmean) {
        int row = sq.numRows();
        int cols = sq.numCols();
        IntStream.range(0, row).parallel()
                .forEach(i -> {
                    int idx = i * cols;
                    for (int j = 0; j < cols; j++) {
                        sq.set(idx, Math.pow(xmean.get(idx++), 2));
                    }
                });
    }

    private void var(SimpleMatrix var, SimpleMatrix sq) {
        
    }

    private void sqrtvar(SimpleMatrix sqrtvar, SimpleMatrix var, double eps) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void ivar(SimpleMatrix ivar, SimpleMatrix sqrtvar) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void xhat(SimpleMatrix xhat, SimpleMatrix xmean, SimpleMatrix ivar) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void gammax(SimpleMatrix gammax, SimpleMatrix gamma, SimpleMatrix xhat) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void out(SimpleMatrix out, SimpleMatrix gammax, SimpleMatrix beta) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
