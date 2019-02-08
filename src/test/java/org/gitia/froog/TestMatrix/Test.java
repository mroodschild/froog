/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gitia.froog.TestMatrix;

import java.util.Random;
import org.ejml.data.DMatrixRMaj;
import org.ejml.data.DMatrixSparseCSC;
import org.ejml.simple.SimpleMatrix;
import org.ejml.sparse.csc.CommonOps_DSCC;
import org.gitia.froog.statistics.Clock;
import org.gitia.froog.util.SparseMatrix;

/**
 *
 * @author Matias
 */
public class Test {

    public static int ROWS = 500;
    public static int COLS = 1000;
    public static int XCOLS = 1;

    public static void main(String[] args) {
        Random rand = new Random(234);

        double[] data = {1, 1.2, 0, 0, 1, 22.21234, 0, 0, 0, 0, 0, 6, 3, 0, 0, 0, 0, 0, 0, 0};
        SimpleMatrix B = SimpleMatrix.random_DDRM(ROWS, COLS, -10, 10, rand);

        Clock c = new Clock();
        c.start();
        DMatrixSparseCSC Z = SparseMatrix.randomOnesCSC(COLS, ROWS, (int) (ROWS * COLS * 0.1));
        c.stop();
        c.printTime("creación de matriz");
        DMatrixRMaj C = new DMatrixRMaj(COLS, COLS);

        c.start();
        CommonOps_DSCC.mult(Z, B.getDDRM(), C);
        c.stop();

        c.printTime("ejecución");

    }
}
