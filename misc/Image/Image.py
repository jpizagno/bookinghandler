# To change this license header, choose License Headers in Project Properties.
# To change this template file, choose Tools | Templates
# and open the template in the editor.

__author__ = "jim"
__date__ = "$Apr 16, 2015 8:56:07 PM$"

from Storage.StorageFile import StorageFile

class Image(StorageFile):
    """
        This class builds an image using Builder design pattern method that just 
            sets attributes and builds the object.  
            
        This class inherits the StorageFile functionality so that it can be stored.
        
        To make build an Image use:
            image = Image()
            image.build(WebDownloader instance, StorageManager instance)
        
    """
    
    def build(self, webDownloader, myStorageManager):
        """
            This builder method builds each object.
        """
        self.successfulBuild = False
        self.myStorageManager = myStorageManager
        self.webDownloader = webDownloader
        self.download()
    
    def download(self):
        """
            This method will download the image from the url.
        """
        result = self.webDownloader.download()
        if result:
            self.data = open(self.webDownloader.fileName, 'r')
            self.store()
            self.successfulBuild = True
        else:
            raise Exception("image not downloaded")

        
        
