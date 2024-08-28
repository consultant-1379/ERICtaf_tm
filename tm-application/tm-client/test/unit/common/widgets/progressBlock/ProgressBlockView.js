/*global define, describe, it, expect*/
/*jshint expr: true */
define([
    'tm/common/widgets/progressBlock/ProgressBlockView'
], function (ProgressBlockView) {
    'use strict';

    describe('tm/common/widgets/progressBlock/ProgressBlockView', function () {

        it('ProgressBlockView should be defined', function () {
            expect(ProgressBlockView).not.to.be.undefined;
        });

        describe('ProgressBlockView\'s getters should be defined', function () {
            var progressBlockView = new ProgressBlockView();
            progressBlockView.render();
            progressBlockView.afterRender();

            it('progressBlockView.getElement() should be defined', function () {
                expect(progressBlockView.getElement()).not.to.be.undefined;
            });

            it('progressBlockView.getMessagesBlock() should be defined', function () {
                expect(progressBlockView.getMessagesBlock()).not.to.be.undefined;
            });

            it('progressBlockView.getProgressBlock() should be defined', function () {
                expect(progressBlockView.getProgressBlock()).not.to.be.undefined;
            });

        });

    });

});
