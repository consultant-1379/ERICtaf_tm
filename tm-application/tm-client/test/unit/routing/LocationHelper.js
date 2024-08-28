/*global describe, it, expect*/
/*jshint expr: true */
define([
    'tm/routing/LocationHelper'
], function (LocationHelper) {
    'use strict';

    describe('tm/routing/LocationHelper', function () {
        it('should be defined', function () {
            expect(LocationHelper).not.to.be.undefined;
        });

        it('should deconstruct URLs', function () {
            var parts = LocationHelper.parse('http://example.com:3000/pathname/?search=test#hash');
            expect(parts.protocol).to.equal('http:');
            expect(parts.hostname).to.equal('example.com');
            expect(parts.port).to.equal('3000');
            expect(parts.pathname).to.equal('/pathname/');
            expect(parts.search).to.equal('?search=test');
            expect(parts.hash).to.equal('#hash');
            expect(parts.host).to.equal('example.com:3000');
        });

        it('should deconstruct client routes', function () {
            var parts = LocationHelper.parse('/tm/page?key1=a');
            expect(parts.pathname).to.equal('/tm/page');
            expect(parts.search).to.equal('?key1=a');
        });

        it('should extract query keys', function () {
            var map = LocationHelper.parseQuery('?key1=a&key1=b&key2=c&=invalid&empty=');
            expect(map).to.deep.equal({
                key1: ['a', 'b'],
                key2: ['c'],
                empty: ['']
            });
        });

        it('should handle escaped characters in query', function () {
            var map = LocationHelper.parseQuery('?key1=d%26e&key2=a+b%2Bc');
            expect(map).to.deep.equal({
                key1: ['d&e'],
                key2: ['a b+c']
            });
        });

        it('should handle + characters in query', function () {
            var map = LocationHelper.parseQuery('?key1=d+++&key2=a+b+c&key3=++a+b+c+');
            expect(map).to.deep.equal({
                key1: ['d   '],
                key2: ['a b c'],
                key3: ['  a b c ']
            });
        });
    });
});
