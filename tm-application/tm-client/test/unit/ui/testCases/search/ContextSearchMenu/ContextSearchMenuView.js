/*global define, describe, it, expect*/
/*jshint expr: true */
define([
    'tm/common/widgets/ContextSearchMenu/ContextSearchMenuView'
], function (ContextSearchMenuView) {
    'use strict';

    describe('tm/common/widgets/ContextSearchMenu/ContextSearchMenuView', function () {

        it('ContextSearchMenuView should be defined', function () {
            expect(ContextSearchMenuView).not.to.be.undefined;
        });

        describe('ContextSearchMenuView\'s getters should be defined', function () {
            var contextSearchMenuView = new ContextSearchMenuView();
            contextSearchMenuView.render();
            contextSearchMenuView.afterRender();

            it('contextSearchMenuView.getProductSelectBoxHolder() should be defined', function () {
                expect(contextSearchMenuView.getProductSelectBoxHolder()).not.to.be.undefined;
            });

            it('contextSearchMenuView.getFeatureSelectBoxHolder() should be defined', function () {
                expect(contextSearchMenuView.getFeatureSelectBoxHolder()).not.to.be.undefined;
            });

            it('contextSearchMenuView.getComponentSelectBoxHolder() should be defined', function () {
                expect(contextSearchMenuView.getComponentSelectBoxHolder()).not.to.be.undefined;
            });

        });

    });

});
