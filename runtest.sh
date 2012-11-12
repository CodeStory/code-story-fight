#!/bin/sh
mocha --reporter spec --compilers coffee:coffee-script --timeout 5000 src/test/acceptance/*.coffee $@
