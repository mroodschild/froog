# Froog
                    Authors:  Roodschild Matías     / mroodschild@gmail.com
                              Jorge Gotay Sardiñas  / jgotay57@gmail.com
                              Adrián Will           / adrian.will.01@gmail.com
                              Sebastián Rodriguez   / sebastian.rodriguez@gitia.org
                            

## Introduction

This project was created with academic purpose for my PhD Tesis. Its design goals are; 1) to be accessible to both novices and experts, and 2) facilitate neural networks manipulations. Froog is free, written in 100% Java and has been released under M.I.T. license.

Currently Froog supports:

* Backpropagation Algorithm
* Stochastic Gradient Descent
* Momentum
* Weight Initialization (Default (Xavier), Pitfall, PositiveRandom, SmallRandom)
* Weight Normalization
* Loss Functions (RMSE, MSE, CrossEntropy)
* Transfer Functions (Logsig, Tansig, Softmax, Purelim)
* Transfer Functions (not well tested): Softplus, ReLU
* Confusion Matrix
* Early Stop (Max Iteration Only)

## Documentation - Example

```
        //Load data into X and T with JDataAnalysis
        SimpleMatrix X = CSV.open("src/main/resources/iris/iris-in.csv");
        SimpleMatrix T = CSV.open("src/main/resources/iris/iris-out.csv");

        //Create the neural network
        Feedforward net = new Feedforward();
        net.addLayer(new Layer(4, 10, TransferFunction.TANSIG));
        net.addLayer(new Layer(10, 3, TransferFunction.LOGSIG));

        //Configure training algorithm
        Backpropagation bp = new Backpropagation();
        bp.setEpoch(1000);
        bp.setMomentum(0.9);
        bp.setLossFunction(LossFunction.MSE);

        //train your neural network
        bp.entrenar(net, X, T);
        
        //Evaluating outputs of your data
        SimpleMatrix salidaNet = Compite.eval(net.outputAll(X));
        ConfusionMatrix cmatrix = new ConfusionMatrix();
        
        //print your Confusion Matrix
        cmatrix.eval(salidaNet, T);
```

## Maven - jitpack.io

Froog is in Maven jitpack.io and can easily be added to Maven, and similar project managers.

```
    <repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
    </repositories> 
    <dependencies>
        <dependency>
            <groupId>com.github.mroodschild</groupId>
            <artifactId>froog</artifactId>
            <version>v0.2</version>
        </dependency>
        <dependency>
            <groupId>org.ejml</groupId>
            <artifactId>all</artifactId>
            <version>0.30</version>
        </dependency>
    </dependencies>
```


## Dependencies
-----------------------------------------

The main Froog modules depends on the following libraries

- [ EJML 0.30         ]  ( http://code.google.com/p/efficient-java-matrix-library )
- [ Apache Commons-lang3          ]  ( https://commons.apache.org/proper/commons-lang/ )

The following is required for unit tests

- [ JUnit   ]       ( http://junit.sourceforge.net/                           )

## License

Froog is released under the M.I.T. open source license, but some libraries used in Froog are under Apache v2.0 License.
