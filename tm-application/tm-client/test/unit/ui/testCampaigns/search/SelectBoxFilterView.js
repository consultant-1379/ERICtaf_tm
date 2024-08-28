/*global define, describe, it, expect*/
/*jshint expr: true */
define([
    'tm/common/widgets/SelectBoxFilter/SelectBoxFilterView'
], function (SelectBoxFilterView) {
    'use strict';

    describe('tm/common/widgets/SelectBoxFilter/SelectBoxFilterView', function () {

        it('SelectBoxFilterView should be defined', function () {
            expect(SelectBoxFilterView).not.to.be.undefined;
        });

        describe('SelectBoxFilterView\'s getters should be defined', function () {
            var selectBoxFilterView = new SelectBoxFilterView();
            selectBoxFilterView.render();

            it('selectBoxFilterView.getProductSelectBoxHolder() should be defined', function () {
                expect(selectBoxFilterView.getProductSelectBoxHolder()).not.to.be.undefined;
            });

            it('selectBoxFilterView.getDropSelectBoxHolder() should be defined', function () {
                expect(selectBoxFilterView.getDropSelectBoxHolder()).not.to.be.undefined;
            });

            it('selectBoxFilterView.getFeatureSelectBoxHolder() should be defined', function () {
                expect(selectBoxFilterView.getFeatureSelectBoxHolder()).not.to.be.undefined;
            });

            it('selectBoxFilterView.getComponentSelectBoxHolder() should be defined', function () {
                expect(selectBoxFilterView.getComponentSelectBoxHolder()).not.to.be.undefined;
            });

        });

    });

});
