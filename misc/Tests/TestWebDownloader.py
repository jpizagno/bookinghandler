# To change this license header, choose License Headers in Project Properties.
# To change this template file, choose Tools | Templates
# and open the template in the editor.

__author__ = "jim"
__date__ = "$Apr 19, 2015 6:45:07 AM$"

import unittest
import os

from WebDownload.WebDownloader import WebDownloader

class TestStorage(unittest.TestCase):
    
    def test_download(self):
        """
            Tests if an image is downloaded from the internet.  
            (Note:  does require a network connection)
        """
        url = "http://www.blue-yonder.com/images/images_portal/site/blue-yonder-logo.png"
        fileName = "/tmp/test_download.txt"
        webDownloader = WebDownloader( {"timeOutSeconds": 60, "chunkDownload": 16 * 1024, "url": url, "fileName": fileName} )
        webDownloader.download()
        self.assertTrue(os.listdir("/tmp/").count("test_download.txt") == 1)

if __name__ == "__main__":
    unittest.main()
