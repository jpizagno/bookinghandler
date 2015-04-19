# To change this license header, choose License Headers in Project Properties.
# To change this template file, choose Tools | Templates
# and open the template in the editor.

__author__ = "jim"
__date__ = "$Apr 16, 2015 8:47:53 PM$"

import threading
from WebDownload.WebDownloader import WebDownloader
from Image.Image import Image

class BlueYonderThread(threading.Thread): 
    """
        This class is a thread that will build an Image.
    
    """
    
    def __init__(self, url, fileName, myStorageManager):
        """
            Constructor method. Requires specific arguements for building.
            
        """
        super(BlueYonderThread, self).__init__()
        self.url = url
        self.fileName = fileName
        self.myStorageManager = myStorageManager

    def run(self):
        self.successfulRun = False
        print "Starting BlueYonderProcess " + self.name + " file="+self.fileName
        webDownloader = WebDownloader( {"timeOutSeconds": 60, "chunkDownload": 16 * 1024, "url": self.url, "fileName": self.fileName} )
        image = Image()
        image.build(webDownloader, self.myStorageManager)
        print "Exiting BlueYonderProcess " + self.name + " file="+self.fileName
        self.successfulRun = True

    
