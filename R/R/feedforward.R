#' Retrieve a Java instance of SpellCorrector.
#'
#' Retrieve a Java instance of SpellCorrector, with the training file
#' specified. Language model is trained before the instance is returned.
#' The spell corrector is adapted from Peter Norvig's demonstration.
#'
#'
#' @return a Java instance of SpellCorrector
#' @export
sequencial_model<-function() {
  .jaddLibrary('froog', 'inst/java/froog-0.4.jar')
  .jaddClassPath('inst/java/froog-0.4,jar')
  net <- .jnew('org/gitia/froog/r/rFeedforward')
  return(net)
}

#'  @param net
#'  @param input
#'  @param neurons
#'  @param activation
addLayer<-function(net, input, neurons, activation){
  .jcall(net, 'V','addLayer', as.integer(input), as.integer(neurons), activation)
}

# Cambiar nombres y poner predict en vez de out, y a matrix ponerle input
#' @param net
#' @param matrix
out<-function(net, matrix){
  a <- .jcall(net, '[D','out', matrix, as.integer(nrow(matrix)), as.integer(ncol(matrix)))
  nrowOutput <- .jcall(net,'I','getOutCount')
  m <- matrix(data = a, nrow = nrowOutput, ncol = as.integer(ncol(matrix)), byrow = TRUE)
  return(m)
}

#' @param net
#' @param input
#' @param output
#' @param epochs
#' @param loss_function
#' @param acceleration
#' @param acc_param
#' @param accuracy
bp<-function(net, input, output, epochs = 100, loss_function ="rmse", acceleration = "", acc_param = 0.9, accuracy = FALSE){
  .jcall(net, 'V','bp',
         input, as.integer(nrow(input)), as.integer(ncol(input)),
         output, as.integer(nrow(output)), as.integer(ncol(output)),
         as.integer(epochs),
         loss_function,
         acceleration,
         acc_param,
         as.logical(accuracy))
}

#' @param net
#' @param input
#' @param output
#' @param epochs
#' @param batch_size
#' @param loss_function
#' @param acceleration
#' @param acc_param
#' @param accuracy
sgd<-function(net, input, output, epochs = 100, batch_size = 10, loss_function ="rmse", acceleration = "", acc_param = 0.9, accuracy = FALSE){
  .jcall(net, 'V','sgd',
         input, as.integer(nrow(input)), as.integer(ncol(input)),
         output, as.integer(nrow(output)), as.integer(ncol(output)),
         as.integer(epochs),
         as.integer(batch_size),
         loss_function,
         acceleration,
         acc_param,
         accuracy)
}

#' @param net
#' @param input
#' @param output
#' @param epochs
#' @param beta_rule
cg<-function(net, input, output, epochs = 100, beta_rule = "fletcher_reeves"){
  .jcall(net, 'V','cg',
         input, as.integer(nrow(input)), as.integer(ncol(input)),
         output, as.integer(nrow(output)), as.integer(ncol(output)),
         as.integer(epochs),
         beta_rule)
}

#' @param net
#' @param input
#' @param output
#' @param epochs
scg<-function(net, input, output, epochs = 100){
  .jcall(net, 'V','scg',
         input, as.integer(nrow(input)), as.integer(ncol(input)),
         output, as.integer(nrow(output)), as.integer(ncol(output)),
         as.integer(epochs))
}

setTestData<-function(net, input, output){
  .jcall(net, 'V', 'setTestData',
         input, as.integer(nrow(input)), as.integer(ncol(input)),
         output, as.integer(nrow(output)), as.integer(ncol(output))
         )
}

