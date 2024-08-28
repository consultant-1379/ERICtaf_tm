'use strict';

var fs = require('fs');
var paths = {
    source: __dirname + '/index.template.html',
    target: __dirname + '/../configs/index.html'
};

console.log('Adding timestamps to index...');
var timestamp = Date.now();
var index = fs.readFileSync(paths.source, {encoding: 'utf8'});
index = index.replace(/src="(.*)"/g, 'src="$1?' + timestamp + '"');
fs.writeFileSync(paths.target, index);
