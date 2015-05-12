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



Ncolumns = 9
Nrows = 329


# random example
our_matrix = numpy.zeros((Nrows,Ncolumns))

file = open("places.txt","r")
ClimateList=[] 
HousingCostList=[]
HlthCareList=[] 
CrimeList=[]  
TranspList=[] 
EducList=[] 
ArtsList=[]  
RecreatList=[] 
EconList=[]
file.readline() # read header
for line in file.readlines():
    fields = line.split()
    ClimateList.append(numpy.log10(float(fields[1])))
    HousingCostList.append(numpy.log10(float(fields[2])))
    HlthCareList.append(numpy.log10(float(fields[3]))) 
    CrimeList.append(numpy.log10(float(fields[4])))  
    TranspList.append(numpy.log10(float(fields[5]))) 
    EducList.append(numpy.log10(float(fields[6]))) 
    ArtsList.append(numpy.log10(float(fields[7])))  
    RecreatList.append(numpy.log10(float(fields[8])))
    EconList.append(numpy.log10(float(fields[9])))
file.close()

# subtract mean
ClimateListMean = numpy.mean(ClimateList)
HousingCostListMean = numpy.mean(HousingCostList)
HlthCareListMean = numpy.mean(HlthCareList)
CrimeListMean = numpy.mean(CrimeList)
TranspListMean = numpy.mean(TranspList)
EducListMean = numpy.mean(EducList)
ArtsListMean = numpy.mean(ArtsList)
RecreatListMean = numpy.mean(RecreatList)
EconListMean = numpy.mean(EconList)

# build matrix
for rowi in range(Nrows):
    our_matrix[rowi,0] = ClimateList[rowi] - ClimateListMean
    our_matrix[rowi,1] = HousingCostList[rowi] - HousingCostListMean
    our_matrix[rowi,2] = HlthCareList[rowi] - HlthCareListMean
    our_matrix[rowi,3] = CrimeList[rowi] - CrimeListMean
    our_matrix[rowi,4] = TranspList[rowi] - TranspListMean
    our_matrix[rowi,5] = EducList[rowi] - EducListMean
    our_matrix[rowi,6] = ArtsList[rowi] - ArtsListMean 
    our_matrix[rowi,7] = RecreatList[rowi] - RecreatListMean
    our_matrix[rowi,8] = EconList[rowi] - EconListMean

print "our_matrix Mean removed"
print our_matrix


#eigen_vectors, eigen_values, V = numpy.linalg.svd(our_matrix.T, full_matrices=False)
cov_our_matrix = numpy.cov(our_matrix.T)
(eigen_values , eigen_vectors) = numpy.linalg.eig(cov_our_matrix)

projected_data = numpy.dot(our_matrix, eigen_vectors)
sigma = projected_data.std(axis=0).mean()
#print " "
#print "eigen_values.real: "
#print eigen_values.real

print " "
print " normalized eigen values, greater than 0.001 "
n_greater_than_1thousandth = 0
sum_eigenvalues = numpy.sum(eigen_values.real)
for i in range(len(eigen_values.real)):
    frac = eigen_values.real[i] / sum_eigenvalues
    if frac > 0.001:
        n_greater_than_1thousandth = n_greater_than_1thousandth + 1
        print frac

print " "
print "n_greater_than_1thousandth:  "
print n_greater_than_1thousandth

print " "
print " all eigenvectors"
print (eigen_vectors)

print " "
print "eigen_vectors greater than 0.001 by eigenvector i:  "
eigen_vectors_trans = eigen_vectors.T
for i in range(n_greater_than_1thousandth):
    print i,":  ",eigen_vectors_trans[i]
    print " "

print " "
print "eigen component = eigen_vector * data-mean"
for i in range(n_greater_than_1thousandth):
    eigen_component = []
    eigen_component.append(eigen_vectors_trans[i][0]*ClimateListMean)
    eigen_component.append(eigen_vectors_trans[i][1]*HousingCostListMean ) 
    eigen_component.append(eigen_vectors_trans[i][2]*HlthCareListMean )  
    eigen_component.append(eigen_vectors_trans[i][3]*CrimeListMean )
    eigen_component.append(eigen_vectors_trans[i][4]*TranspListMean )  
    eigen_component.append(eigen_vectors_trans[i][5]*EducListMean )
    eigen_component.append(eigen_vectors_trans[i][6]*ArtsListMean )
    eigen_component.append(eigen_vectors_trans[i][7]*RecreatListMean)  
    eigen_component.append(eigen_vectors_trans[i][8]*EconListMean)
    print i,":  ",eigen_component
    print " "
