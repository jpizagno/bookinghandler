# To change this license header, choose License Headers in Project Properties.
# To change this template file, choose Tools | Templates
# and open the template in the editor.

__author__ = "jim"
__date__ = "$Apr 19, 2015 6:43:33 AM$"

import unittest
import os
from Storage.StorageManager import StorageManager
from Processor.BlueYonderThread import BlueYonderThread

class TestBlueYonderThread(unittest.TestCase):
    
    def test_run(self):
        """
            Test to make sure thread is run.
        """
        url = "http://www.blue-yonder.com/images/images_portal/site/blue-yonder-logo.png"
        fileName = "test_file_name"
        myStorageManager = StorageManager({"stagingArea":  os.getcwd(), "storingArea":  os.getcwd()})
        myBlueYonderThread = BlueYonderThread(url, fileName, myStorageManager)
        myBlueYonderThread.start()
        myBlueYonderThread.join()
        self.assertTrue(myBlueYonderThread.successfulRun)

if __name__ == "__main__":
    unittest.main()
