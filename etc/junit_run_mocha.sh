#!/bin/sh
if [ -n "$2" ]; then
    export PORT=$2
fi
export NODE_PATH=/usr/local/share/npm/lib/node_modules

/usr/local/share/npm/bin/mocha --no-colors --compilers coffee:coffee-script --timeout 4000 $1
#> /dev/null 2>&1
