'use strict';

var fs = require('fs');
var paths = {
    source: __dirname + '/../src/tm/appInfo.json',
    target: __dirname + '/../target/src/tm/appInfo.json'
};

var args = process.argv,
    version = args[2],
    buildDate = args[3];

var appInfo = JSON.parse(fs.readFileSync(paths.source).toString());
appInfo.version = version;
appInfo.buildDate = buildDate;
appInfo.sse = false; // FIXME: Investigate SSE robustness

console.log(appInfo);
fs.writeFileSync(paths.source, JSON.stringify(appInfo, null, '    ') + '\n');
process.exit();
