#' Retrieve a Java instance of SpellCorrector.
#'
#' Retrieve a Java instance of SpellCorrector, with the training file
#' specified. Language model is trained before the instance is returned.
#' The spell corrector is adapted from Peter Norvig's demonstration.
#'
#'
#' @return a Java instance of SpellCorrector
#' @export
getFeedforward<-function() {
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

out<-function(net, matrix){
  a <- .jcall(net, '[D','out', matrix, as.integer(nrow(matrix)), as.integer(ncol(matrix)))
  nrowOutput <- .jcall(net,'I','getOutCount')
  m <- matrix(data = a, nrow = nrowOutput, ncol = as.integer(ncol(matrix)),byrow = TRUE)
  return(m)
}

summary<-function(net){
  a <- .jcall(net, 'S','summary')
  return(a)
}

bp<-function(net, input, output, epochs = 100, acceleration = "", acc_param = 0.9){
  .jcall(net, 'V','bp', input, as.integer(nrow(input)), as.integer(ncol(input)),
         output, as.integer(nrow(output)), as.integer(ncol(output)), as.integer(epochs),
         acceleration, acc_param)
}

sgd<-function(net, input, output, epochs = 100, batch_size = 10, acceleration = "", acc_param = 0.9){
  .jcall(net, 'V','sgd', input, as.integer(nrow(input)), as.integer(ncol(input)),
         output, as.integer(nrow(output)), as.integer(ncol(output)), as.integer(epochs),
         as.integer(batch_size), acceleration, acc_param)
}

cg<-function(net, input, output, epochs = 100, beta_rule = "fletcher_reeves"){
  .jcall(net, 'V','cg', input, as.integer(nrow(input)), as.integer(ncol(input)),
         output, as.integer(nrow(output)), as.integer(ncol(output)),
         as.integer(epochs), beta_rule)
}

scg<-function(net, input, output, epochs = 100){
  .jcall(net, 'V','scg', input, as.integer(nrow(input)), as.integer(ncol(input)),
         output, as.integer(nrow(output)), as.integer(ncol(output)),
         as.integer(epochs))
}
