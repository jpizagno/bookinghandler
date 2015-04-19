# To change this license header, choose License Headers in Project Properties.
# To change this template file, choose Tools | Templates
# and open the template in the editor.

__author__ = "jim"
__date__ = "$Apr 16, 2015 8:58:25 PM$"

class StorageFile():
    """
        Abstract class that can be re-used/inherited by other projects for 
            storage functionality.
        Any class that inherits class(StorageFile) can be stored with:
            1. set self.data = <file> 
            2. then call  self.store(self.myStorageManager)
    """
        
    def store(self):
        """ask StorageManager to store file and test result"""
        response = self.myStorageManager.store(self.data)
        return response
        
    