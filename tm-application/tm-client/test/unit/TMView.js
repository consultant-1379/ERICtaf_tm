/*global define, describe, it, expect*/
/*jshint expr: true */
define([
    'tm/TMView'
], function (TMView) {
    'use strict';

    describe('tm/TMView', function () {

        it('TMView should be defined', function () {
            expect(TMView).not.to.be.undefined;
        });

        describe('TMView\'s getters should be defined', function () {
            var tmView = new TMView();
            tmView.render();
            tmView.afterRender();

            it('TMView.getElement() should be defined', function () {
                expect(tmView.getElement()).not.to.be.undefined;
            });

            it('tmView.getPageTitle() should be defined', function () {
                expect(tmView.getPageTitle()).not.to.be.undefined;
            });

            it('tmView.getQuickActionBar() should be defined', function () {
                expect(tmView.getQuickActionBar()).not.to.be.undefined;
            });

            it('tmView.getNavigation() should be defined', function () {
                expect(tmView.getNavigation()).not.to.be.undefined;
            });

            it('tmView.getContentBlock() should be defined', function () {
                expect(tmView.getContentBlock()).not.to.be.undefined;
            });

            it('tmView.getVersion() should be defined', function () {
                expect(tmView.getVersion()).not.to.be.undefined;
            });
        });

    });

});
