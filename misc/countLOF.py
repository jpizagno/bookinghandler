import os
import numpy

def bootStrap(x,y,sigma_y_measure,num_bootstrap_samples):
    '''
        Given data points x,y,sigma_y_measure will perform a Bootstrap sampling of the data (num_bootstrap_samples) times.
        returns the distributions of slopes and intercepts for the multiple fits.
    '''
    #peform bootstrap on sampling on x,y get intercept,sigma at each step, do it 100 times
    num_bootstrap_samples = 200
    temp_intercept = []
    temp_slope = []
    #randonly select subsamples
    for i in range(num_bootstrap_samples):
        # select N times
        Ns = numpy.random.random_integers(0,high=N-1,size=N)
        x_i = [x[Ns[j]] for j in range(N)]
        y_i = [y[Ns[j]] for j in range(N)]
        intercept_i,junk,slope_i,junk = fitline(x_i,y_i,sigma_y_measure)
        del(x_i)
        del(y_i)
        temp_slope.append(slope_i)
        temp_intercept.append(intercept_i)
        #print intercept_i,slope_i
    
        
    intercepts = numpy.array(temp_intercept)
    slopes = numpy.array(temp_slope)
    return intercepts.std()/numpy.sqrt(N),slopes.std()/numpy.sqrt(N)

def makeData(N,slope,intercept,sigma_y_measure):
    '''
        Make linear data of size (N), with (slope, intercept),
        where data has intrinsic scatter (sigma_y_measure).
        
        Used to test fitline
    '''
    x = numpy.linspace(0,100,N)
    x = x - x[len(x)/2]
    sigma_y_measure = sigma_y_measure*numpy.random.random_sample(size=N) - sigma_y_measure/2.0
    y = slope*x + intercept + sigma_y_measure
    return x,y

def fitline(x,y,sigma_y):
    '''
        Fits line to x/y list.
        sigma_y is assumed to be fixed here.
    '''
    S = 0.0
    Sxx = 0.0
    Sy = 0.0
    Sxy = 0.0
    Sx = 0.0
    for i in range(len(x)):
        S = S + 1./(sigma_y*sigma_y)
        Sx = Sx + x[i]/(sigma_y*sigma_y)
        Sy = Sy + y[i]/(sigma_y*sigma_y)
        Sxx = Sxx + x[i]*x[i]/(sigma_y*sigma_y)
        Sxy = Sxy + x[i]*y[i]/(sigma_y*sigma_y)
    D = S*Sxx - Sx*Sx
    if D!=0:
        intercept = (Sxx*Sy - Sx*Sxy)/(D)
        slope = (S*Sxy - Sx*Sy)/D
        sigma_intercept = numpy.sqrt(Sxx/D)
        sigma_slope = numpy.sqrt(S/D)
    else:
        intercept = -100
        slope = -100
        sigma_intercept = -100
        sigma_slope = -100
    return intercept,sigma_intercept,slope,sigma_slope

def main():
    files = os.listdir('.')
    for filea in files:
        file = open(filea,'r')
        sum = 0  # number of times action=lof is seen in User impressions
        sumall = 0
        time=[]
        preis=[]
        sigma_y = []
        hasBooking = "false"  # always true for training data
        hasPriceOver3000 =[]  # number of times a cache_adult_price is greater than 3000
        for line in file.readlines():
            if line.count("time")==0:  # ignore first line/header
                if line.count(",b,")>0:
                    hasBooking= "true"
                fields = line.split(",")
                tmp = fields[4]
                if float(tmp)>0:  # ignore imporessions withtout a price
                    time.append(float(fields[0]))
                    preis.append(float(tmp))
                    if float(tmp)>3000:
                        hasPriceOver3000.append(1)
                sumall = sumall +1
                if line.count(",lof,")>0:
                    sum=sum+1
        if len(preis)>3:
            intercept,sigma_intercept,slope,sigma_slope = fitline(time,preis,0.1)
            print filea, sum, sumall, float(sum)/float(sumall), slope, sigma_slope, hasBooking, slope/sigma_slope,len(hasPriceOver3000 )
        else:
            print filea, sum, sumall, float(sum)/float(sumall), -100 , -100, hasBooking, 0,len( hasPriceOver3000)
    
    file.close()
    
    
main()