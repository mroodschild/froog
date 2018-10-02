[![](https://jitpack.io/v/mroodschild/froog.svg)](https://jitpack.io/#mroodschild/froog)

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
* Conjugate Gradient
* Scaled Conjugate Gradient 
* Accelerate methods (Momentum, Momentum Rumelhart, Adam)
* Weight Initialization (Default (Xavier), He, Pitfall, PositiveRandom, SmallRandom)
* Weight Normalization L2
* Dropout
* Loss Functions (RMSE, MSE, CrossEntropy, Logistic)
* Transfer Functions (Logsig, Tansig, Softmax, Purelim, Softplus, ReLU)
* Confusion Matrix
* Early Stop (Max Iteration Only)

## Documentation - Example

```
//get data
        SimpleMatrix input = CSV.open("src/main/resources/iris/iris-in.csv");
        SimpleMatrix output = CSV.open("src/main/resources/iris/iris-out.csv");

        //Standard Desviation
        STD std = new STD();
        std.fit(input);

        //normalization
        input = std.eval(input);
        Random random = new Random(1);
        
        //set data in horizontal format (a column is a register and a row is a feature)
        input = input.transpose();
        output = output.transpose();

        //setting backpropagation
        Backpropagation bp = new Backpropagation();
        bp.setEpoch(1000);
        bp.setMomentum(0.9);
        bp.setClassification(true);
        bp.setLossFunction(LossFunction.CROSSENTROPY);

        //number of neurons
        int Nhl = 2;

        Feedforward net = new Feedforward();

        //add layers to neural network
        net.addLayer(new Layer(input.numRows(), Nhl, TransferFunction.TANSIG, random));
        net.addLayer(new Layer(Nhl, output.numRows(), TransferFunction.SOFTMAX, random));
        
        //train your net
        bp.train(net, input, output);
        
        //show results
        System.out.println("Print all output");
        SimpleMatrix salida = net.output(input);
        ConfusionMatrix confusionMatrix = new ConfusionMatrix();
        confusionMatrix.eval(Compite.eval(salida.transpose()), output.transpose());
        confusionMatrix.printStats();
        
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
            <version>0.4</version>
        </dependency>
        <dependency>
            <groupId>org.ejml</groupId>
            <artifactId>ejml-all</artifactId>
            <version>0.36</version>
        </dependency>
    </dependencies>
```


## Dependencies
-----------------------------------------

The main Froog modules depends on the following libraries

- [ EJML 0.36         ]  ( http://ejml.org )
- [ Apache Commons-lang3          ]  ( https://commons.apache.org/proper/commons-lang/ )

The following is required for unit tests

- [ JUnit   ]       ( http://junit.sourceforge.net/                           )

## License

Froog is released under the M.I.T. open source license, but some libraries used in Froog are under Apache v2.0 License.
