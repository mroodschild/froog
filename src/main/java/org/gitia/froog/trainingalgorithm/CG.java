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

import org.apache.commons.lang3.ArrayUtils;
import org.ejml.dense.row.NormOps_DDRM;
import org.ejml.simple.SimpleMatrix;
import org.gitia.froog.Feedforward;
import org.gitia.froog.statistics.Clock;
import org.gitia.froog.trainingalgorithm.conjugategradient.beta.BetaRule;
import org.gitia.froog.trainingalgorithm.conjugategradient.beta.PolakRebiere;

public class CG extends Backpropagation {

    double rho = 0.01; // usado en la condición del paso
    double beta = 0.1;
    double f; // error de la función J
    SimpleMatrix pPrev; //= new SimpleMatrix();
    SimpleMatrix gPrev;// = new SimpleMatrix();
    double pBeta;
    BetaRule betaRule = new PolakRebiere();
    SimpleMatrix p;// = new SimpleMatrix();
    SimpleMatrix g;

    public CG() {
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
        //Clock ck = new Clock();
        this.net = net;
        this.input = new SimpleMatrix(input);
        this.output = new SimpleMatrix(output);
        init();
        //ck.stop();
        //ck.printTime("Inicio de datos");
        //ck.start();

        Activations = net.activations(input);
        costOverall = loss(Activations.get(Activations.size() - 1), output);//f=function(x)
        this.cost.add(costOverall);
        //computeGradient(input, output);//calculamos el g
        gradient.compute(net, Activations, gradW, gradB, input, output);
        f = costOverall;//asignamos el f
        g = getGradients();//asignamos el g
        pPrev = g.negative();
        gPrev = g.copy();
        pBeta = 0;
        //ck.stop();
        //ck.printTime("primer gradiente");
        for (int i = 0; i < this.epoch; i++) {
            //System.out.println("----Iteracion----\t"+i);
            Clock ckIt = new Clock();
            ckIt.start();
            SimpleMatrix x = net.getParameters();
            //pbeta = 0;
            if (iteracion > 0) {
                //Activations = net.activations(inputBach);
                //f = loss(Activations.get(Activations.size() - 1), outputBach);
                //ck.start();
                //computeGradient(input, output);
                gradient.compute(net, Activations, gradW, gradB, input, output);
                g = getGradients();//asignamos el g
                pBeta = betaRule.compute(g, gPrev);
                //ck.stop();
                //ck.printTime("segundo gradiente");
            }

            //ck.start();
            p = pPrev.scale(pBeta).minus(g);//-g+pbeta*ppr

            double norm = NormOps_DDRM.normPInf(g.getDDRM());
            if (norm < 1e-6) {//     % Condición de mínimo
                break;
            }
            double alpha = 1;
            SimpleMatrix xnew;
            double fxnew = 0;
            //double k_it = 0;
            for (int k = 0; k < 10; k++) {
                //k_it = k;
                xnew = x.plus(p.scale(alpha).transpose());
                net.setParameters(xnew);
                Activations = net.activations(input);
                fxnew = loss(Activations.get(Activations.size() - 1), output);
                if (fxnew <= f + alpha * rho * g.transpose().mult(p).get(0)) {
                    break;
                } else {
                    alpha = alpha * beta;
                }
            }
            f = fxnew;

            pPrev = p;
            gPrev = g;
            iteracion++;
            //ck.stop();
            //ck.printTime("ajuste");
            ckIt.stop();
            //System.out.println("It: \t" + i + "\tk_it:\t" + k_it + "\tMSE:\t" + f + "\tnorm:\t" + norm + "\ttime:\t" + ckIt.timeSec() + "\ts");
            System.out.println("It: \t" + i + "\ttrain:\t" + f + "\ttime:\t" + ckIt.timeSec() + "\ts");
        }
    }

    @Override
    public SimpleMatrix getGradients() {
        if (net.getLayers().isEmpty()) {
            System.err.println("Inicialice los gradientes primero");
            return null;
        }
        double[] aux = new double[0];
        for (int i = 0; i < gradW.size(); i++) {
            aux = ArrayUtils.addAll(aux, gradW.get(i).getDDRM().getData());
        }
        for (int i = 0; i < gradB.size(); i++) {
            aux = ArrayUtils.addAll(aux, gradB.get(i).getDDRM().getData());
        }
        return new SimpleMatrix(aux.length, 1, true, aux);
    }

    public void setRho(double rho) {
        this.rho = rho;
    }

    public void setBetaRule(BetaRule betaRule) {
        this.betaRule = betaRule;
    }
}