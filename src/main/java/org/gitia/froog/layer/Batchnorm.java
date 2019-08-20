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
import org.gitia.froog.util.Matrix;

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

    //forward pass
    SimpleMatrix mean;
    SimpleMatrix xmean;
    SimpleMatrix sq;
    SimpleMatrix var;
    SimpleMatrix sqrtvar;
    SimpleMatrix ivar;
    SimpleMatrix xhat;
    SimpleMatrix gammax;
    SimpleMatrix out;
    //backward pass
    SimpleMatrix dgamma;
    SimpleMatrix dbeta;
    SimpleMatrix dx;

    protected double eps = 2e-8;

    protected Random random = new Random();

    public Batchnorm() {
    }

    /**
     *
     * @param input number of inputs
     * @param output number of neurons
     * @param WeightInit
     * @param random
     */
    public Batchnorm(int input, int output, Random random) {
        this.random = random;
        this.gamma = new SimpleMatrix(output, input);
        this.beta = new SimpleMatrix(output, 1);
        gamma.fill(0.5);
        beta.fill(0.1);
        this.random = random;
    }

    /**
     * @param input
     * @param output
     */
    public Batchnorm(int input, int output) {
        this(input, output, new Random());
    }

    /**
     *
     * @param gamma
     * @param beta
     */
    public Batchnorm(SimpleMatrix gamma, SimpleMatrix beta) {
        this.gamma = gamma;
        this.beta = beta;
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

    @Override
    public SimpleMatrix output(SimpleMatrix a) {
        media = Matrix.mean(a, 1);
        variance(a, media, variance);
        xNormalized = normalize(a, media, variance, eps);
        y = scaleShift(xNormalized, gamma, beta);
        //normalizeScaleShift(a, xNormalized, y, mean, variance, gamma, beta, eps);
        return y;
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

    public void backwardpass(SimpleMatrix dout){
        
        int N = dout.numCols();
        int D = dout.numRows();
        
        //step 9
        dbeta = Matrix.sum(dout, 0);
        SimpleMatrix dgammax = dout;
        
        //step 8
        SimpleMatrix dgamma = Matrix.sum(xhat, 0);
        SimpleMatrix dxhat = dgammax.mult(dgamma);
        
        
        
    }
    
    public SimpleMatrix forwardpass(SimpleMatrix a) {
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
        mean = Matrix.mean(a, 0);
        //System.out.println("mean");
        //mean.print();
        //step2: subtract mean vector of every trainings example
        xmean = subtractMean(a, mean);
        //System.out.println("xmean");
        //xmean.print();
        //step3: following the lower branch - calculation denominator
        sq = sq(xmean);
        //System.out.println("sq");
        //sq.print();
        //step4: calculate variance
        var = var(sq);
        //System.out.println("var");
        //var.print();
        //step5: add eps for numerical stability, then sqrt
        sqrtvar = sqrtvar(var, eps);
        //System.out.println("sqrtvar");
        //sqrtvar.print();
        //step6: invert sqrtwar
        ivar = ivar(sqrtvar);
        //System.out.println("ivar");
        //ivar.print();
        //step7: execute normalization
        xhat = xhat(xmean, ivar);
        //System.out.println("xhat");
        //xhat.print();
        //step8: Nor the two transformation steps
        gammax = gammax(gamma, xhat);
        //System.out.println("gammax");
        //gammax.print();
        //step9: out = gammax + beta
        out = out(gammax, beta);
        //System.out.println("out");
        //out.print();
        return out;
    }

    /**
     * xmean = a - mean
     *
     * @param a
     * @param mean
     * @param xmean
     */
    private SimpleMatrix subtractMean(SimpleMatrix a, SimpleMatrix mean) {
        int row = a.numRows();
        int cols = a.numCols();

        SimpleMatrix xmean = new SimpleMatrix(row, cols);

        IntStream.range(0, row).parallel()
                .forEach(i -> {
                    int idx = i * cols;
                    double mu = mean.get(i);
                    for (int j = 0; j < cols; j++) {
                        xmean.set(idx, a.get(idx++) - mu);
                    }
                });
        return xmean;
    }

    /**
     *
     * @param sq
     * @param xmean
     */
    private SimpleMatrix sq(SimpleMatrix xmean) {
        int row = xmean.numRows();
        int cols = xmean.numCols();
        SimpleMatrix sq = new SimpleMatrix(row, cols);
        IntStream.range(0, row).parallel()
                .forEach(i -> {
                    int idx = i * cols;
                    for (int j = 0; j < cols; j++) {
                        sq.set(idx, Math.pow(xmean.get(idx++), 2));
                    }
                });
        return sq;
    }

    private SimpleMatrix var(SimpleMatrix sq) {
        int row = sq.numRows();
        int cols = sq.numCols();
        double N = (double)sq.numCols();
        SimpleMatrix var = new SimpleMatrix(row, 1);
        IntStream.range(0, row).parallel()
                .forEach(i -> {
                    int idx = i * cols;
                    double sum = 0;
                    for (int j = 0; j < cols; j++) {
                        sum += sq.get(idx);
                    }
                    var.set(i, sum / N);
                });
        return var;
    }

    private SimpleMatrix sqrtvar(SimpleMatrix var, double eps) {
        int row = var.numRows();
        int cols = var.numCols();
        int elements = var.getNumElements();
        SimpleMatrix sqrtvar = new SimpleMatrix(row, cols);
        for (int i = 0; i < elements; i++) {
            sqrtvar.set(i, Math.sqrt(var.getDDRM().get(i) + eps));
        }
        return sqrtvar;
    }

    private SimpleMatrix ivar(SimpleMatrix sqrtvar) {
        int row = sqrtvar.numRows();
        int cols = sqrtvar.numCols();
        int elements = sqrtvar.getNumElements();
        SimpleMatrix ivar = new SimpleMatrix(row, cols);
        for (int i = 0; i < elements; i++) {
            ivar.set(i, 1 / sqrtvar.getDDRM().get(i));
        }
        return ivar;
    }

    private SimpleMatrix xhat(SimpleMatrix xmean, SimpleMatrix ivar) {
        int row = xmean.numRows();
        int cols = xmean.numCols();
        SimpleMatrix xHat = new SimpleMatrix(row, cols);
        IntStream.range(0, row).parallel()
                .forEach(i -> {
                    int idx = i * cols;
                    double i_var = ivar.get(i);
                    for (int j = 0; j < cols; j++) {
                        xHat.set(idx, xmean.get(idx++) * i_var);
                    }
                });
        return xHat;
    }

    private SimpleMatrix gammax(SimpleMatrix gamma, SimpleMatrix xhat) {
        int row = xhat.numRows();
        int cols = xhat.numCols();
        SimpleMatrix gammax = new SimpleMatrix(row, cols);
        IntStream.range(0, row).parallel()
                .forEach(i -> {
                    int idx = i * cols;
                    double gamm = gamma.get(i);
                    for (int j = 0; j < cols; j++) {
                        gammax.set(idx, xhat.get(idx++) * gamm);
                    }
                });
        return gammax;
    }

    private SimpleMatrix out(SimpleMatrix gammax, SimpleMatrix beta) {
        int row = gammax.numRows();
        int cols = gammax.numCols();
        SimpleMatrix out = new SimpleMatrix(row, cols);
        IntStream.range(0, row).parallel()
                .forEach(i -> {
                    int idx = i * cols;
                    double B = beta.get(i);
                    for (int j = 0; j < cols; j++) {
                        out.set(idx, gammax.get(idx++) + B);
                    }
                });
        return out;
    }

}