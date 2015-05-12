

> data <- read.csv(header=TRUE, sep="," , "/Users/jim/DataScience/JimMachineLearning/inputdata.csv")
> library(caret)
Loading required package: lattice
Loading required package: ggplot2
> splitdf <- function(dataframe, seed=NULL) {
+ 	if (!is.null(seed)) set.seed(seed)
+ 	index <- 1:nrow(dataframe)
+ 	trainindex <- sample(index, trunc(length(index)/2))
+ 	trainset <- dataframe[trainindex, ]
+ 	testset <- dataframe[-trainindex, ]
+ 	list(trainset=trainset,testset=testset)
+ }
> library(ggplot2)
> 
> data_split <- splitdf(data, seed=809)
> summary(data)
 population       x1                 x2                   x3          
 a: 88      Min.   :0.003409   Min.   :-0.4998023   Min.   :0.000292  
 b: 72      1st Qu.:0.260296   1st Qu.:-0.3780253   1st Qu.:1.127665  
 c:840      Median :0.488699   Median :-0.2490524   Median :2.533467  
            Mean   :0.495190   Mean   :-0.2471653   Mean   :2.462901  
            3rd Qu.:0.738369   3rd Qu.:-0.1131317   3rd Qu.:3.778055  
            Max.   :0.999581   Max.   :-0.0000866   Max.   :4.994441  
> str(data_split)
List of 2
 $ trainset:'data.frame':	500 obs. of  4 variables:
  ..$ population: Factor w/ 3 levels "a","b","c": 3 3 3 3 3 3 3 3 3 3 ...
  ..$ x1        : num [1:500] 0.422 0.82 0.932 0.877 0.662 ...
  ..$ x2        : num [1:500] -0.00334 -0.4299 -0.27725 -0.48383 -0.43369 ...
  ..$ x3        : num [1:500] 1.68 4.98 3.76 1.69 4.15 ...
 $ testset :'data.frame':	500 obs. of  4 variables:
  ..$ population: Factor w/ 3 levels "a","b","c": 3 1 3 3 2 1 3 3 2 2 ...
  ..$ x1        : num [1:500] 0.811 0.811 0.991 0.976 0.202 ...
  ..$ x2        : num [1:500] -0.4704 -0.4221 -0.488 -0.3692 -0.0936 ...
  ..$ x3        : num [1:500] 4.034 0.287 4.802 3.479 0.714 ...
> lapply(data_split,nrow)
$trainset
[1] 500

$testset
[1] 500

> training <- data_split$trainset
> testing <- data_split$testset
> summary(testing)
 population       x1                 x2                  x3          
 a: 46      Min.   :0.003409   Min.   :-0.499802   Min.   :0.000292  
 b: 36      1st Qu.:0.254704   1st Qu.:-0.381668   1st Qu.:1.138300  
 c:418      Median :0.483191   Median :-0.252966   Median :2.662130  
            Mean   :0.494975   Mean   :-0.250538   Mean   :2.516911  
            3rd Qu.:0.737837   3rd Qu.:-0.112729   3rd Qu.:3.844736  
            Max.   :0.999581   Max.   :-0.001735   Max.   :4.990103  
> ggplot(testing, aes(x = x1, y = x2) ) + geom_point( aes(color=population))
> 
> ggplot(testing, aes(x = x1, y = x3) ) + geom_point( aes(color=population))
>  library(rpart)
> model <- rpart(population ~ x1 + x2 + x3, method="class", data=training)
> print(model)
n= 500 

node), split, n, loss, yval, (yprob)
      * denotes terminal node

 1) root 500 78 c (0.084000000 0.072000000 0.844000000)  
   2) x3< 0.6849099 82 40 a (0.512195122 0.426829268 0.060975610)  
     4) x3< 0.4082219 43  2 a (0.953488372 0.046511628 0.000000000) *
     5) x3>=0.4082219 39  6 b (0.025641026 0.846153846 0.128205128)  
      10) x1< 0.7229924 31  1 b (0.032258065 0.967741935 0.000000000) *
      11) x1>=0.7229924 8  3 c (0.000000000 0.375000000 0.625000000) *
   3) x3>=0.6849099 418  1 c (0.000000000 0.002392344 0.997607656) *
> predict <- predict(model , newdata=testing , type="class")
> v <- predict==testing$pop
> print(summary(v))
   Mode   FALSE    TRUE    NA's 
logical      22     478       0 
> length(predict)
[1] 500
> accuracy <- 478/length(predict)
> accuracy
[1] 0.956
> 
> 
> 
> library(randomForest)
randomForest 4.6-10
Type rfNews() to see new features/changes/bug fixes.
> model_forest <- randomForest(pop ~ fsc_small + fsc_perp + fsc_big + pe + chl_big + chl_small, data=training)
Error in eval(expr, envir, enclos) : object 'pop' not found
> model_forest <- randomForest(population ~ x1 + x2 + x3, method="class", data=training)
> 
> predict_forest <- predict(model_forest, newdata=testing, type="class")
> v_forest <- predict_forest==testing$pop
> print(summary(v_forest))
   Mode   FALSE    TRUE    NA's 
logical      12     488       0 
> accuracy_forest <- 488/length(v_forest)
> accuracy_forest
[1] 0.976
> 
> print "The gini parameters.  The higher the number, the more the gini impurity score decreases by branching on this variable, indicating that the variable is more important."
Error: unexpected string constant in "print "The gini parameters.  The higher the number, the more the gini impurity score decreases by branching on this variable, indicating that the variable is more important.""
> 
> importance(model)
Error in UseMethod("importance") : 
  no applicable method for 'importance' applied to an object of class "rpart"
> importance(v_forest)
Error in UseMethod("importance") : 
  no applicable method for 'importance' applied to an object of class "logical"
> importance(model_forest
+ )
   MeanDecreaseGini
x1         14.26402
x2         13.08901
x3        108.88400
> library(e1071)
> model_svm <- svm(population ~ x1 + x2 + x3, method="class", data=training)
> predict_svm <- predict(model_svm , newdata=testing , type="class")
> 
> print(summary(predict_svm))
  a   b   c 
 41  22 437 
> accuracy <- 437 / length(predict_svm)
> accuracy
[1] 0.874
> 
> table(pred = predict_forest, true = testing$pop)
    true
pred   a   b   c
   a  45   7   0
   b   1  26   1
   c   0   3 417
> 