# To change this license header, choose License Headers in Project Properties.
# To change this template file, choose Tools | Templates
# and open the template in the editor.

__author__ = "jim"
__date__ = "$Apr 16, 2015 8:40:29 PM$"

from optparse import OptionParser
import sys
import os

from Storage.StorageManager import StorageManager
from Processor.ProcessorFactory import ProcessorFactory

def main():
    """
      This method parses arguements, opens up the storage manager, and then 
      starts the processor.
      
      Args:
        None
        
      Returns:
        None
        
      Raises:
        System exit with incorrect arguements
    """

    parser = OptionParser()

    usage = "usage: %python blueyonder.py -i <inputfile> -s <stagingpath> -t <storingpath>\n"
    usage += "    ex:  python blueyonder.py -i myfile.txt -s /tmp/ -t /users/me/home/"
    
    parser.add_option("-i", "--inputfile", type="string",
                  help="input file",
                  dest="inputfile")

    parser.add_option("-s", "--stagingpath", type="string",
                  help="initial path where images will be stored. default = pwd",
                  dest="stagingpath")
                  
    parser.add_option("-t", "--storingpath", type="string",
                  help="location where images will be stored. default = pwd",
                  dest="storingpath")

    (options, args) = parser.parse_args()
    
    inputfile = options.inputfile
    stagingpath = options.stagingpath
    storingpath = options.storingpath
        
    if inputfile is None:
        wrongArgsEnd(usage)
    if stagingpath is None:
        stagingpath = os.getcwd() 
    if storingpath is None:
        storingpath = os.getcwd()  
        
    print 'Input file is "', inputfile
    print 'staging path is "', stagingpath
    print 'storing path is "', storingpath
   
    myStorageManager = StorageManager({"stagingArea": stagingpath, "storingArea": storingpath})
    myProcessor = ProcessorFactory({"myStorageManager": myStorageManager})
    myProcessor.readFile(inputfile)
    myProcessor.runThreads()
   
def wrongArgsEnd(usage):
    print "wrong number of arguments:"
    print usage
    sys.exit(1)
   
if __name__ == "__main__":
    main()