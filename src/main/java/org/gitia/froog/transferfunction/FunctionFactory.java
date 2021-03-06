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
package org.gitia.froog.transferfunction;

/**
 *
 * @author Matías Roodschild <mroodschild@gmail.com>
 */
public class FunctionFactory {

    /**
     * Implemented Functions:<br>
     * "tansig", "logsig", "purelim", "softplus"
     *
     * @param function
     * @return
     */
    public static TransferFunction getFunction(String function) {
        switch (function) {
            case TransferFunction.TANSIG:
                return new Tansig();
            case TransferFunction.LOGSIG:
                return new Logsig();
            case TransferFunction.PURELIM:
                return new Purelim();
            case TransferFunction.SOFTPLUS:
                return new Softplus();
            case TransferFunction.SOFTMAX:
                return new Softmax();
            case TransferFunction.RELU:
                return new Relu();
            case TransferFunction.PRELU:
                return new PRelu();
            default:
                System.err.println("Function: '" + function + "' not found, "
                        + "please try with another one.");
                System.exit(0);
        }
        return null;
    }
}
