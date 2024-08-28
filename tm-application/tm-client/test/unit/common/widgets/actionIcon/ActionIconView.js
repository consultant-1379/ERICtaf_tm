/*global define, describe, it, expect*/
/*jshint expr: true */
define([
    'tm/common/widgets/actionIcon/ActionIconView'
], function (ActionIconView) {
    'use strict';

    describe('tm/common/widgets/actionIcon/ActionIconView', function () {

        it('ActionIconView should be defined', function () {
            expect(ActionIconView).not.to.be.undefined;
        });

        it('ActionIconView.getElement() should be defined', function () {
            var actionIconView = new ActionIconView();
            actionIconView.render();

            expect(actionIconView.getElement()).not.to.be.undefined;
        });

    });

});
