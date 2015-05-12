# load data
 data <- read.csv(header=TRUE, sep="," , "/Users/jim/Desktop/send2Stefan/sample-user.csv")
 
# define splitting funciton
 library(caret)
 splitdf <- function(dataframe, seed=NULL) {if (!is.null(seed)) set.seed(seed)
 index <- 1:nrow(dataframe)
 trainindex <- sample(index, trunc(length(index)/2))
 trainset <- dataframe[trainindex, ]
 testset <- dataframe[-trainindex, ]
  list(trainset=trainset,testset=testset)
 }
 
# apply splitting funcion
 data_split <- splitdf(data, seed=809)
 lapply(data_split,nrow)

# get training and testing data sets
 training <- data_split$trainset
 testing <- data_split$testset

# run some plots
 ggplot(data, aes(x = cache_price_adult, y = checked_price_adult) ) + geom_point( aes(color=checked_price_total))
 ggplot(data, aes(x =checked_price_total , y = (checked_price_adult-cache_price_adult) ) ) + geom_point( aes(color=hotel))
 ggplot(data, aes(x = time, y = checked_price_adult) ) + geom_point( aes(color=action))
 ggplot(testing, aes(x = time, y = checked_price_adult) ) + geom_point( aes(color=hotel))
 hist(training$time)

# load tree library
 library(rpart)

# build model tree using training dataset
 model <- rpart(hotel ~ time + cache_price_adult ,   method="class", data=training)
 print(model)
# n= 107 
# node), split, n, loss, yval, (yprob)
#       * denotes terminal node
# 
#  1) root 107 78 1762 (0.0093 0.27 0.11 0.0093 0.093 0.084 0.019 0.11 0.019 0.019 0.019 0.019 0.019 0.16 0.037)  
#    2) time>=1.390734e+09 22  2 1762 (0 0.91 0 0 0 0 0.045 0 0 0 0 0.045 0 0 0) *
#    3) time< 1.390734e+09 85 68 37565 (0.012 0.11 0.14 0.012 0.12 0.11 0.012 0.14 0.024 0.024 0.024 0.012 0.024 0.2 0.047)  
#      6) time>=1.390733e+09 22 15 14163 (0.045 0 0.18 0 0.32 0 0 0.27 0 0 0 0 0 0 0.18)  
#       12) time< 1.390733e+09 7  0 14163 (0 0 0 0 1 0 0 0 0 0 0 0 0 0 0) *
#       13) time>=1.390733e+09 15  9 24393 (0.067 0 0.27 0 0 0 0 0.4 0 0 0 0 0 0 0.27) *
#      7) time< 1.390733e+09 63 46 37565 (0 0.14 0.13 0.016 0.048 0.14 0.016 0.095 0.032 0.032 0.032 0.016 0.032 0.27 0)  
#       14) cache_price_adult>=251.5 24 18 12661 (0 0.17 0.25 0.042 0 0.083 0 0.21 0.083 0 0.083 0 0.042 0.042 0)  
#         28) time>=1.390726e+09 16 10 12661 (0 0.25 0.38 0.062 0 0.12 0 0.062 0 0 0 0 0.062 0.062 0) *
#         29) time< 1.390726e+09 8  4 24393 (0 0 0 0 0 0 0 0.5 0.25 0 0.25 0 0 0 0) *
#       15) cache_price_adult< 251.5 39 23 37565 (0 0.13 0.051 0 0.077 0.18 0.026 0.026 0 0.051 0 0.026 0.026 0.41 0) *

# build model tree using training testing
 model_test <- rpart(hotel ~ time + cache_price_adult ,   method="class", data=testing)
 print(model_test)
# n= 107 
# node), split, n, loss, yval, (yprob)
#       * denotes terminal node
# 
#   1) root 107 76 1762 (0.028 0.29 0.0093 0.065 0.0093 0.093 0.084 0.056 0.0093 0.028 0.0093 0.037 0.0093 0.028 0.2 0.047)  
#     2) time>=1.390734e+09 20  0 1762 (0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0) *
#     3) time< 1.390734e+09 87 66 37565 (0.034 0.13 0.011 0.08 0.011 0.11 0.1 0.069 0.011 0.034 0.011 0.046 0.011 0.034 0.24 0.057)  
#       6) time>=1.390733e+09 18 12 14163 (0.17 0 0 0.056 0 0.33 0 0.17 0 0 0 0 0 0 0 0.28) *
#       7) time< 1.390733e+09 69 48 37565 (0 0.16 0.014 0.087 0.014 0.058 0.13 0.043 0.014 0.043 0.014 0.058 0.014 0.043 0.3 0)  
#        14) time< 1.390732e+09 58 47 1762 (0 0.19 0.017 0.1 0.017 0.069 0.16 0.052 0.017 0.052 0.017 0.069 0.017 0.052 0.17 0)  
#          28) time>=1.39073e+09 10  3 1762 (0 0.7 0 0 0 0 0 0 0 0 0 0.3 0 0 0 0) *
#          29) time< 1.39073e+09 48 38 37565 (0 0.083 0.021 0.12 0.021 0.083 0.19 0.062 0.021 0.062 0.021 0.021 0.021 0.062 0.21 0)  
#            58) cache_price_adult>=255.5 16 10 12661 (0 0.062 0.062 0.38 0.062 0.062 0 0 0 0.062 0 0 0.062 0.12 0.12 0) *
#            59) cache_price_adult< 255.5 32 23 21436 (0 0.094 0 0 0 0.094 0.28 0.094 0.031 0.062 0.031 0.031 0 0.031 0.25 0)  
#             118) time< 1.390728e+09 18 15 1762 (0 0.17 0 0 0 0.17 0.11 0.17 0.056 0.11 0.056 0.056 0 0 0.11 0) *
#             119) time>=1.390728e+09 14  7 21436 (0 0 0 0 0 0 0.5 0 0 0 0 0 0 0.071 0.43 0) *
#        15) time>=1.390732e+09 11  0 37565 (0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0) *

# test the accuracy of the model
 predict <- predict(model , newdata=testing , type="class")
 v <- predict==testing$hotel
 print(summary(v))
#    Mode   FALSE    TRUE    NA's 
# logical      58      49       0 
 
# show how well one can predict the hotel
 table(pred = predict, true=testing$hotel)
#        true
# pred    524 1762 8607 12661 13448 14163 21436 24393 27438 27623 28251 34054 35346 36112 37565 40747
#   524     0    0    0     0     0     0     0     0     0     0     0     0     0     0     0     0
#   1762    0   20    0     0     0     0     0     0     0     0     0     0     0     0     0     0
#   12661   0    4    0     6     1     1     0     1     0     1     0     3     0     2     6     0
#   13448   0    0    0     0     0     0     0     0     0     0     0     0     0     0     0     0
#   14163   0    0    0     0     0     6     0     1     0     0     0     0     0     0     0     0
#   21436   0    0    0     0     0     0     0     0     0     0     0     0     0     0     0     0
#   24187   0    0    0     0     0     0     0     0     0     0     0     0     0     0     0     0
#   24393   3    0    1     1     0     0     0     2     0     0     0     0     1     0     0     5
#   27438   0    0    0     0     0     0     0     0     0     0     0     0     0     0     0     0
#   27623   0    0    0     0     0     0     0     0     0     0     0     0     0     0     0     0
#   28251   0    0    0     0     0     0     0     0     0     0     0     0     0     0     0     0
#   34054   0    0    0     0     0     0     0     0     0     0     0     0     0     0     0     0
#   36112   0    0    0     0     0     0     0     0     0     0     0     0     0     0     0     0
#   37565   0    7    0     0     0     3     9     2     1     2     1     1     0     1    15     0
#   40747   0    0    0     0     0     0     0     0     0     0     0     0     0     0     0     0
 
# load the random forest model
 library(randomForest)
 model_forest <- randomForest(hotel ~ time + cache_price_adult ,   method="class", data=training)
 print(model_forest)

# Call:
#  randomForest(formula = hotel ~ time + cache_price_adult, data = training,      method = "class") 
#                Type of random forest: regression
#                      Number of trees: 500
# No. of variables tried at each split: 1
# 
#          Mean of squared residuals: 79133686
#                     % Var explained: 56.36

# test the prediction for the random forest
 predict_forest <- predict(model_forest, newdata=testing, type="class")
 v_forest <- predict_forest==testing$hotel
 print(summary(v_forest))
#    Mode   FALSE    NA's 
# logical     107       0 

# show the prediction table for the random forest
 table(pred = predict_forest, true=testing$hotel)
#                   true
# pred               524 1762 8607 12661 13448 14163 21436 24393 27438 27623 28251 34054 35346 36112 37565 40747
#   1893.91062272727   0    2    0     0     0     0     0     0     0     0     0     0     0     0     0     0
#   2359.5081          0    4    0     0     0     0     0     0     0     0     0     0     0     0     0     0
#   2456.94172272727   0    2    0     0     0     0     0     0     0     0     0     0     0     0     0     0
#   4468.49261969697   0    1    0     0     0     0     0     0     0     0     0     0     0     0     0     0
#   5972.1612          0    2    0     0     0     0     0     0     0     0     0     0     0     0     0     0
#   6062.83407792208   0    1    0     0     0     0     0     0     0     0     0     0     0     0     0     0
#   6225.13354444444   0    1    0     0     0     0     0     0     0     0     0     0     0     0     0     0
#   6573.78106666667   0    1    0     0     0     0     0     0     0     0     0     0     0     0     0     0
#   6648.74693333333   0    2    0     0     0     0     0     0     0     0     0     0     0     0     0     0
#   6929.78270299695   0    1    0     0     0     0     0     0     0     0     0     0     0     0     0     0
#   7253.34085238095   0    1    0     0     0     0     0     0     0     0     0     0     0     0     0     0
#   7320.28395238095   0    1    0     0     0     0     0     0     0     0     0     0     0     0     0     0
#   7371.58738715729   0    1    0     0     0     0     0     0     0     0     0     0     0     0     0     0
#   7724.42146666666   0    2    0     0     0     0     0     0     0     0     0     0     0     0     0     0
#   7918.35289948818   0    1    0     0     0     0     0     0     0     0     0     0     0     0     0     0
#  ... 
#   38247.9156666667   0    0    0     0     0     0     0     0     0     0     0     0     0     0     0     1

# load the library for SVM
 library(e1071)
 model_svm <- svm(hotel ~ time + cache_price_adult ,   method="class", data=training)
 predict_svm <- predict(model_svm , newdata=testing , type="class")
 print(summary(predict_svm))
#    Min. 1st Qu.  Median    Mean 3rd Qu.    Max. 
#    2343   12220   16940   16290   21900   26250 

# show the prediction table for the SVM model
 table(pred = predict_svm, true=testing$hotel)
#                   true
# pred               524 1762 8607 12661 13448 14163 21436 24393 27438 27623 28251 34054 35346 36112 37565 40747
#   2343.49177653577   0    1    0     0     0     0     0     0     0     0     0     0     0     0     0     0
#   3100.63056248479   0    1    0     0     0     0     0     0     0     0     0     0     0     0     0     0
#   3106.49271984148   0    1    0     0     0     0     0     0     0     0     0     0     0     0     0     0
#   3110.3184998153    0    1    0     0     0     0     0     0     0     0     0     0     0     0     0     0
#   3149.34996355112   0    1    0     0     0     0     0     0     0     0     0     0     0     0     0     0
#   3190.58225414079   0    1    0     0     0     0     0     0     0     0     0     0     0     0     0     0
#   3351.97437984041   0    1    0     0     0     0     0     0     0     0     0     0     0     0     0     0
#   3426.88080520948   0    1    0     0     0     0     0     0     0     0     0     0     0     0     0     0
#   3579.60946708813   0    1    0     0     0     0     0     0     0     0     0     0     0     0     0     0
#  ….
#   26246.412241051    0    0    0     0     0     0     1     0     0     0     0     0     0     0     0     0
