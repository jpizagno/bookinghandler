import numpy
import random

'''
    Examples
    A = numpy.matrix('1.0 2.0; 3.0 4.0')
    
    print A
    
    array1 = numpy.array([1,2,3,4,5])
    print array1
    
    array2 = numpy.array([6,7,8,9,10])
    print array2
    
    
    
    x = numpy.matrix(array1)
    
    print x
    
    x = numpy.vstack( [x,array2] )
    
    print x
    
    calulate eigenvectors
    
    A = numpy.matrix('1.0 2.0; 3.0 4.0')
    (eigen_values , eigen_vectors) = numpy.linalg.eig(A)
     eigen_values
        array([-0.37228132,  5.37228132])
    eigen_vectors
        matrix([[-0.82456484, -0.41597356],
            [ 0.56576746, -0.90937671]])
    
'''



Ncolumns = 10
Nrows = 5


# random example
zero_row = numpy.zeros(Ncolumns)
our_matrix = numpy.matrix( zero_row )
our_matrix = numpy.delete(our_matrix,zero_row, 0)

for rowi in range(Nrows):
    newrow = numpy.array( numpy.zeros(Ncolumns) )
    for i in range(Ncolumns):
        newrow[i] = random.random() - 0.5
    our_matrix = numpy.vstack( [our_matrix , newrow] )
    
print "our_matrix"
print our_matrix


cov_our_matrix = numpy.cov(our_matrix)
(eigen_values , eigen_vectors) = numpy.linalg.eig(cov_our_matrix)
print " "
print "eigen_values.real: "
print eigen_values.real
print " "
print "eigen_vectors.real:  "
print eigen_vectors.real


print " "
print "next build a matrix where the values are correlated"
# fully correlated example
zero_row = numpy.zeros(Ncolumns)
our_matrix = numpy.matrix( zero_row )
our_matrix = numpy.delete(our_matrix,zero_row, 0)

for rowi in range(Nrows):
    newrow = numpy.array( numpy.zeros(Ncolumns) )
    
    # 0.1x + 0.2x + 0.3x + 0.4x + 0.5x +0.6x + 0.7x + 0.8x + 0.9x + 1.0x
    x= random.random() - 0.5
    newrow[0] = 0.1*x
    newrow[1] = 0.2*x
    newrow[2] = 0.3*x
    newrow[3] = 0.4*x
    newrow[4] = 0.5*x
    newrow[5] = 0.6*x
    newrow[6] = 0.7*x
    newrow[7] = 0.8*x
    newrow[8] = 0.9*x
    newrow[9] = 1.0*x
    
    our_matrix = numpy.vstack( [our_matrix , newrow] )
    
print " our_matrix correlated values:  "
print our_matrix


cov_our_matrix = numpy.cov(our_matrix)
(eigen_values , eigen_vectors) = numpy.linalg.eig(cov_our_matrix)
print " "
print " eigen_values.real:  "
print eigen_values.real
print " "
print " eigen_vectorys.real"
print eigen_vectors.real
 

