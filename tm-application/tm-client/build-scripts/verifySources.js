'use strict';

var _ = require('underscore');
var fs = require('fs');
var glob = require('glob');

var paths = {
    sources: __dirname + '/../src/**/*.+(css|less|js|html)'
};

var violations = {};
var addViolation = function (message, file) {
    if (violations[message] == null) {
        violations[message] = [];
    }
    violations[message].push(file);
};

glob.sync(paths.sources).forEach(function (file) {
    var source = fs.readFileSync(file, {encoding: 'utf8'});
    if (source.search(/^ *\t/m) !== -1) {
        addViolation('Using tab characters for indentation is not allowed; use spaces', file);
    }
});

if (_.size(violations) > 0) {
    console.log('Source violations!');
    _.each(violations, function (files, message) {
        console.log('\n' + message);
        files.forEach(function (file) {
            console.log('-> ' + file);
        });
    });
    process.exit(1);
}
