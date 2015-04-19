# To change this license header, choose License Headers in Project Properties.
# To change this template file, choose Tools | Templates
# and open the template in the editor.

__author__ = "jim"
__date__ = "$Apr 19, 2015 6:43:45 AM$"

import unittest

from Processor.ProcessorFactory import ProcessorFactory

class TestProcessorFactory(unittest.TestCase):
    
    def test_checkUrlFormat(self):
        """
            Tests the checkUrlFormat method of the ProcessorFactory class.
        """
        myProcessorFactory = ProcessorFactory()
        self.assertTrue(myProcessorFactory.checkUrlFormat("http://www.google.com"))
        self.assertFalse(myProcessorFactory.checkUrlFormat("error"))

if __name__ == "__main__":
    unittest.main()