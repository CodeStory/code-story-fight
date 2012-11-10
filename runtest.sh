#!/bin/sh
mocha --reporter spec --compilers coffee:coffee-script --timeout 4000 src/test/acceptance/*.coffee $@
