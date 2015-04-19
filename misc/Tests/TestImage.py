# To change this license header, choose License Headers in Project Properties.
# To change this template file, choose Tools | Templates
# and open the template in the editor.

__author__ = "jim"
__date__ = "$Apr 19, 2015 6:43:19 AM$"

import unittest
import os

from WebDownload.WebDownloader import WebDownloader
from Storage.StorageManager import StorageManager
from Image.Image import Image

class TestImage(unittest.TestCase):
    
    def test_build(self):
        """
            Test to make sure Image is built.
        """
        url = "http://www.blue-yonder.com/images/images_portal/site/blue-yonder-logo.png"
        fileName = "test_file_name"
        myStorageManager = StorageManager({"stagingArea":  os.getcwd(), "storingArea":  os.getcwd()})
        webDownloader = WebDownloader( {"timeOutSeconds": 60, "chunkDownload": 16 * 1024, "url": url, "fileName": fileName} )
        image = Image()
        image.build(webDownloader, myStorageManager)
        self.assertTrue(image.successfulBuild)


if __name__ == "__main__":
    unittest.main()
