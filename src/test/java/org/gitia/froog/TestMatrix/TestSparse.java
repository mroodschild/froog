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
package org.gitia.froog.TestMatrix;

import java.util.Random;
import org.ejml.data.DMatrixSparseCSC;
import org.ejml.data.MatrixType;
import org.ejml.simple.SimpleMatrix;
import org.ejml.sparse.csc.CommonOps_DSCC;
import org.ejml.sparse.csc.RandomMatrices_DSCC;
import org.gitia.froog.statistics.Clock;
import org.gitia.froog.util.SparseMatrix;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class TestSparse {

    public static int ROWS = 716;
    public static int COLS = 50000;

    public static void main(String[] args) {
        Clock c = new Clock();
        c.start();
        DMatrixSparseCSC Z = SparseMatrix.randomOnesColumnsCSC(ROWS, COLS, 0.1);
        c.stop();
        c.printTime("creación de matriz");
        //Z.print();
        

        SimpleMatrix B = SimpleMatrix.random_DDRM(300, 716, -10, 10, new Random());
        
        //CommonOps_DSCC.elementMult(Z, , Z, gw, gx);
        SimpleMatrix C = new SimpleMatrix(300, COLS, MatrixType.DDRM);
        c.start();
        //CommonOps_DSCC.elementMult(Z, Z, Z, gw, gx);
        Random r = new Random();
        c.start();
        DMatrixSparseCSC A = RandomMatrices_DSCC.rectangle(ROWS, COLS, (int) (ROWS * COLS * 0.1), r);
        c.stop();
        c.printTime("A sparse");;

    }
}
