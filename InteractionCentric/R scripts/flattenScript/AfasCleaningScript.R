#TestScript
suppressMessages(suppressWarnings(library(dplyr)))

#!/usr/bin/env Rscript
args = commandArgs(trailingOnly=TRUE)
if (length(args)==0) {
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
    inputFile <- files[a]
  }
}else{
  inputFile <- args[1]
}


EventTypeField <- "Tag_Type"
SourceField <- "Tag_StreamTypeName"
TargetField <- "Tag_EventHandlerInstance"
TrackingIDField <- "Tag_MC.TrackingId"
MetricNameField <- "MetricName"

kibana <- read.csv2(inputFile, sep=";")

useful_columns <- c(TrackingIDField, SourceField, TargetField, EventTypeField, MetricNameField)

kibana_cleaned <- dplyr::select(kibana, all_of(useful_columns))

#clean the field when they do not have the right things. 
# could be implemented cleaner...
kibana_cleaned <- kibana_cleaned %>% filter(Tag_EventHandlerInstance != "") %>% filter(Tag_StreamTypeName != "") %>% filter(MetricName != "EventProcessingDelay") %>% filter(MetricName != "CreatedCommittable") %>% filter(MetricName != "RepositoryExecuteEvent") %>% mutate(across(everything(), as.character))


# Get rid of the second part in tag_Streamtypename (,AFAS.runtime)
kibana_cleaned$Tag_StreamTypeName = sub("\\,.*", "", as.character(kibana_cleaned$Tag_StreamTypeName))

write.csv2(kibana_cleaned, "tmp.csv", row.names = FALSE)
