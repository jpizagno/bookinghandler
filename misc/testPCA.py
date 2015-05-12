import numpy
import random
import pdb

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



Ncolumns = 5
Nrows = 10


# random example
our_matrix = numpy.zeros((Nrows,Ncolumns))
for rowi in range(Nrows):
    for coli in range(Ncolumns):
        our_matrix[rowi,coli] = random.random() - 0.5
    
print "our_matrix"
print our_matrix


cov_our_matrix = numpy.cov(our_matrix.T)
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
our_matrix = numpy.zeros((Nrows,Ncolumns))

for rowi in range(Nrows):
    newrow = numpy.array( numpy.zeros(Ncolumns) )
    
    # 0.1x + 0.2x + 0.3x + 0.4x + 0.5x +0.6x + 0.7x + 0.8x + 0.9x + 1.0x
    x= random.random() - 0.5
    our_matrix[rowi,0] = 0.1*x
    our_matrix[rowi,1] = 0.2*x
    our_matrix[rowi,2] = 0.3*x
    our_matrix[rowi,3] = 0.4*x
    our_matrix[rowi,4] = 0.5*x
    
print " our_matrix correlated values:  "
print our_matrix


cov_our_matrix = numpy.cov(our_matrix.T)
(eigen_values , eigen_vectors) = numpy.linalg.eig(cov_our_matrix)
print " "
print " eigen_values.real:  "
print eigen_values.real
print " "
print " eigen_vectors.real"
print eigen_vectors.real
