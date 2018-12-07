confusionMatrix<-function(yObs, y, binarize = TRUE){
  rData <- .jnew('org/gitia/froog/r/rData')
  .jcall(rData, 'V', 'confusionMatrix',
         as.double(as.matrix(yObs)), as.integer(nrow(yObs)), as.integer(ncol(yObs)),
         as.double(as.matrix(y)), as.integer(nrow(y)), as.integer(ncol(y)),
         binarize
  )
}
