
library(rmr2) 

# maper
map <- function(k,lines) {
  words.list <- strsplit(lines, '\\s') 
  words <- unlist(words.list)
  return( keyval(words, 1) )
}

# reducer
reduce <- function(word, counts) { 
  keyval(word, sum(counts))
}

# define main 
wordcount <- function (input, output=NULL) { 
  mapreduce(input=input, output=output, input.format="text", 
            map=map, reduce=reduce)
}

# delete previous result if any
system("/usr/bin/hadoop fs rm -r wordcount/out")

# Submit job
hdfs.root <- 'wordcount'
hdfs.data <- file.path(hdfs.root, 'data') 
hdfs.out <- file.path(hdfs.root, 'out') 
out <- wordcount(hdfs.data, hdfs.out)

# get results from Hadoop
results <- from.dfs(out)

# check top 10 frequent words
results.df <- as.data.frame(results, stringsAsFactors=F) 
colnames(results.df) <- c('word', 'count') 
head(results.df[order(results.df$count, decreasing=T), ], 10)

# https://ashokharnal.wordpress.com/2014/01/16/installing-r-rhadoop-and-rstudio-over-cloudera-hadoop-ecosystem-revised/
# rpm -ivh http://mirror.chpc.utah.edu/pub/epel/6/x86_64/epel-release-6-8.noarch.rpm
# yum install R R-devel
#
#R
#install.packages(c("rJava", "Rcpp", "RJSONIO", "bitops", "digest", "functional", "stringr", "plyr", "reshape2","caTools","dplyr", "R.methodsS3", "Hmisc", "memoise", "lazyeval", "rjson"))
#
#vi /etc/profile
#export HADOOP_HOME=/opt/cloudera/parcels/CDH/lib/hadoop-mapreduce
#export HADOOP_CMD=/usr/bin/hadoop
#export HADOOP_STREAMING=/opt/cloudera/parcels/CDH-5.4.4-1.cdh5.4.4.p0.4/lib/hadoop-mapreduce/hadoop-streaming.jar 
#source /etc/profile
#
#mkdir R_install
#cd R_install
#scp root@datanode01:"R_install/plyrmr_0.6.0.tar.gz R_install/ravro_1.0.4.tar.gz R_install/rmr2_3.3.1.tar.gz R_install/rhdfs_1.0.8.tar.gz R_install/rhbase_1.2.1.tar.gz" .
#
#wget https://github.com/RevolutionAnalytics/plyrmr/releases/download/0.6.0/plyrmr_0.6.0.tar.gz
#wget https://github.com/RevolutionAnalytics/ravro/blob/1.0.4/build/ravro_1.0.4.tar.gz
#wget https://github.com/RevolutionAnalytics/rmr2/releases/download/3.3.1/rmr2_3.3.1.tar.gz
#wget https://github.com/RevolutionAnalytics/rhdfs/blob/master/build/rhdfs_1.0.8.tar.gz
#wget https://github.com/RevolutionAnalytics/rhbase/blob/master/build/rhbase_1.2.1.tar.gz
#
# to run R:
# shell% R-devel
#
#R CMD INSTALL rmr2_3.3.1.tar.gz
#R CMD INSTALL plyrmr_0.6.0.tar.gz
#R CMD INSTALL rhdfs_1.0.8.tar.gz
#R CMD INSTALL rhbase_1.2.1.tar.gz
#R CMD INSTALL ravro_1.0.4.tar.gz
#
################################ R Test MapReduce ###########################################################
#
#echo "wer wer wer wer treekr erte tze tzer tze" > test.txt
#
#hadoop fs -mkdir "wordcount"
#hadoop fs -mkdir "wordcount/data"
#hadoop fs -copyFromLocal test.txt wordcount/data