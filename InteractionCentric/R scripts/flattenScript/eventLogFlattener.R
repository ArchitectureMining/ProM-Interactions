
suppressMessages(suppressWarnings(library(tictoc)))
suppressMessages(suppressWarnings(library(dplyr)))

if(file.exists("tmp.csv")){
  readfile<- "tmp.csv"
}else{
  files <- list.files(pattern = "\\.csv$")
  if(length(files) == 0){
    stop("Provide inputfile as argument. No files found in directory", call.=FALSE)
  }else if(length(files) == 1){
    args[1] <- files[1]
  }else{
    cat("Choose inputFile from current directory \n")
    cnt<-1
    for(file in files){
      cat(paste(cnt, file, sep = ": "))
      cat("\n")
      cnt <- cnt + 1
    }
    cat("Choose file number:")
    a <- as.integer(readLines("stdin",n=1))
    readfile <- files[a]
  }
}

logFile <- read.csv2(readfile, sep=";") %>% mutate(across(everything(), as.character))
headers <- colnames(data.frame(logFile))
args = commandArgs(trailingOnly=TRUE)

printValues<-function(list){
  cnt<-1
  for(h in list){
    cat(paste(cnt, h, sep = ": "))
    cat("\n")
    cnt <- cnt + 1
  }
}

if (length(args)==0) {
  if(length(headers) < 3){
    stop("Not sufficient data in loaded csv.", call.=FALSE)
  }else{
    
    printValues(headers)
    cat("Choose Source fieldname with number:\n")
    a <- as.integer(readLines("stdin",n=1))
    SourceField <- headers[a]

    cat("Choose Target fieldname with number:\n")
    a <- as.integer(readLines("stdin",n=1))
    TargetField <- headers[a]

    cat("Choose EventType fieldname with number:\n")
    a <- as.integer(readLines("stdin",n=1))
    EventTypeField <- headers[a]

    cat("Choose Case ID fieldname with number:\n")
    a <- as.integer(readLines("stdin",n=1))
    TrackingIDField <- headers[a]
  }
  outputFile = "out.csv"
}else if(length(args)!= 5) {
  stop("Provide all arguments or none to prevent ambiguous behaviour.", call.=FALSE)
}else{
  SourceField <- args[1]
  TargetField <- args[2]
  EventTypeField <- args[3]
  TrackingIDField <- args[4]
  outputFile <- args[5]
}
tic("Flattening data")
buildingDataFrame <- matrix(ncol=3, nrow=0)
EventTypeIndex <- which(headers == EventTypeField)

for(i in 1:nrow(logFile)){
  currentRow <- logFile[i, ]
  eventType <- as.character(currentRow[EventTypeIndex])
  if(!grepl("_event", eventType, ignore.case = TRUE)){
    eventType <- paste(eventType, "event", sep = "_")
  }
  row1 <- currentRow[-EventTypeIndex]
  row1[TargetField] <- eventType
  
  row2 <- currentRow[-EventTypeIndex]
  row2[SourceField]  <- eventType
  
  buildingDataFrame <- rbind(buildingDataFrame, as.character(row1))
  buildingDataFrame <- rbind(buildingDataFrame, as.character(row2))
}

colnames(buildingDataFrame) <- c("CaseID", "Source", "Target")
write.csv2(buildingDataFrame, outputFile, row.names = FALSE)
invisible(file.remove("tmp.csv"))
toc()