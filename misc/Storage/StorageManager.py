# To change this license header, choose License Headers in Project Properties.
# To change this template file, choose Tools | Templates
# and open the template in the editor.

__author__ = "jim"
__date__ = "$Apr 16, 2015 9:17:07 PM$"

import os

class StorageManager():
    """
        This class moves files from a staging area to a storing area.
    
    """
    
    stagingArea = "/tmp/"
    storingArea = "/tmp/"
    
    def __init__(self,  *initial_data, **kwargs):
        """
            The initialization takes a staging area path and storing area path,
            like so:  {"stagingArea": stagingpath, "storingArea": storingpath}
        """
        for dictionary in initial_data:
            for key in dictionary:
                setattr(self, key, dictionary[key])
        for key in kwargs:
            setattr(self, key, kwargs[key])
    
    def isSpaceFree(self, file):
        """
            This methods checks if there is enough disk space to store the file.
            Args:
                File to store
            Return:
                boolean , True is free disk space is larger than file size
        """
        fileSizeBytes = os.path.getsize(file.name)
        
        statvfs = os.statvfs(self.storingArea)
        freeSpaceBytes = statvfs.f_frsize * statvfs.f_bfree     
        
        if freeSpaceBytes > fileSizeBytes:
            return True
        else:
            return False
            
    
    def store(self, file):
        """
            This methos stores the file object passed to it.
            Args:
                File to be moved from staging to storge area
            Return:
                None
        """
        if self.isSpaceFree(file):
            fields = file.name.split('/')
            destination = self.storingArea + "/" + fields[len(fields)-1]
            os.rename(file.name, destination)
        else:
            raise Exception("out of disk space for "+stagingArea+" given file.name="+file.name)
