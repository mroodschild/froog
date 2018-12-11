##levantamos los datos
x <- read.csv(url("https://raw.githubusercontent.com/mroodschild/froog-examples/master/src/main/resources/iris/iris-in.csv"), sep = ",", header = FALSE)
y <- read.csv(url("https://raw.githubusercontent.com/mroodschild/froog-examples/master/src/main/resources/iris/iris-out.csv"), sep = ",", header = FALSE)

##generamos el modelo
model <- sequencial_model()
addLayer(net = model, input = 4, neurons = 5, activation = "tansig")
addLayer(net = model, input = 5, neurons = 3,activation = "softmax")

##configuramos el algoritmo de entrenamiento
scg(net = model,input = t(x), output = t(y), epochs = 50, loss_function = "crossEntropy", accuracy = TRUE)

##hacemos predicciones
prediccion = t(out(net = model,matrix = t(x)))
##ejecutamos la matriz de confusión
confusionMatrix(y, prediccion, TRUE)


## bp(net = model,input = t(X),output = t(y),epochs = 50,loss_function = "crossEntropy",acceleration = "momentum_rumelhart",acc_param = 0.9, accuracy = TRUE)

##cg(net = model, input = t(X), output = t(y), epochs = 50, loss_function = "crossEntropy")

snnsModel <- mlp(x = x, y = y, size = 5, maxit = 50, learnFunc = "SCG", hiddenActFunc = "Act_TanH")
yCal = predict(snnsModel, x)
yCal
as.matrix(yCal)

confusionMatrix(yObs = y, y = as.matrix.data.frame(yCal),binarize = TRUE)
