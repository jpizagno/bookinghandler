# To change this license header, choose License Headers in Project Properties.
# To change this template file, choose Tools | Templates
# and open the template in the editor.

__author__ = "jim"
__date__ = "$Apr 16, 2015 8:45:05 PM$"

import re
from multiprocessing import Process

from BlueYonderThread import BlueYonderThread

class ProcessorFactory():
    """
        This class downloads the files in a multi-threaded environment.
        
        It is a factory design pattern.
    """
    
    def __init__(self, *initial_data, **kwargs):
        """
            Initialization takes the myStoreManager
        """
        for dictionary in initial_data:
            for key in dictionary:
                setattr(self, key, dictionary[key])
        for key in kwargs:
            setattr(self, key, kwargs[key])
            
        self.urls_list = []
    
    def runThreads(self):
        """
            This methos sets the values of BlueYonderThread and runs it.
        """
        thread_list = []
        for url in self.urls_list:
            fields = url.split("/")
            fileName = self.myStorageManager.stagingArea + "/" + fields[len(fields)-1]
            fileName = fileName.replace("\n","")
            myThread = BlueYonderThread(url, fileName, self.myStorageManager)
            myThread.start()
            thread_list.append(myThread)
        
        for myThread in thread_list:
            # join threads
            myThread.join()

    def checkUrlFormat(self, inputLine):
        """
            Uses a simple regular expression to check if URL is valid. 
        """
        regex = re.compile(
            r'^https?://'  # http:// or https://
            r'(?:(?:[A-Z0-9](?:[A-Z0-9-]{0,61}[A-Z0-9])?\.)+[A-Z]{2,6}\.?|'  # domain...
            r'localhost|'  # localhost...
            r'\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3})' # ...or ip
            r'(?::\d+)?'  # optional port
            r'(?:/?|[/?]\S+)$', re.IGNORECASE)
        return inputLine is not None and regex.search(inputLine)
    
    def readFile(self,inputFileName):
        """
            Reads the file input bythe user, and validates the url
        """
        file = open(inputFileName,'r')
        for line in file.readlines():
            line = line.replace('/n','')
            if self.checkUrlFormat(line):
                self.urls_list.append(line)
            else:
                raise Exception("Poorly formed url in file. read line = "+line)
            

        
    
