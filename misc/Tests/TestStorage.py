# To change this license header, choose License Headers in Project Properties.
# To change this template file, choose Tools | Templates
# and open the template in the editor.

__author__ = "jim"
__date__ = "$Apr 19, 2015 6:43:55 AM$"

import unittest
import os

from Storage.StorageManager import StorageManager

class TestStorage(unittest.TestCase):
    
    def test_isSpaceFree(self):
        """
            Tests the isSpaceFree method of the Storage Manager.
        """
        myStorageManager = StorageManager({"stagingArea": os.getcwd(), "storingArea": os.getcwd()})
        file = open("/tmp/test","w")
        file.write("delete me")
        file.close()
        self.assertTrue(myStorageManager.isSpaceFree(file))
        
    def test_store(self):
        """
            Tests to make sure that a file is properly "stored", which means it 
            has been moved from the staging area to the storage area.
        """
        file = open("/tmp/test_store.txt","w")
        file.write("delete me")
        file.close()
        myStorageManager = StorageManager({"stagingArea": os.getcwd(), "storingArea": os.getcwd()})
        myStorageManager.storingArea  = "/tmp/"
        myStorageManager.store(file)
        self.assertTrue(os.listdir(myStorageManager.storingArea).count("test_store.txt") == 1)
        

if __name__ == "__main__":
    unittest.main()
