/*global define, describe, it, expect*/
/*jshint expr: true */
define([
    'jscore/core',
    'tm/common/widgets/actionLink/ActionLink'
], function (core, ActionLink) {
    'use strict';

    describe('tm/common/widgets/actionLink/ActionLink', function () {

        it('ActionLink should be defined', function () {
            expect(ActionLink).not.to.be.undefined;
        });

        describe('ActionLink\'s elements are not undefined', function () {
            var options = getOptions();

            var actionLink = new ActionLink(options);
            var element = new core.Element('div');
            actionLink.attachTo(element);

            it('ActionLink icon should not be undefined', function () {
                expect(actionLink.getActionIcon()).not.to.be.undefined;
                expect(actionLink.getActionIcon()._iconKey).to.equal('lock');
            });

            it('ActionLink title should be equal to provided in options', function () {
                expect(actionLink.getActionIcon().getElement().getProperty('title')).to.equal('Lock');
                expect(actionLink.view.getLink().getText()).to.equal('Lock');
                expect(actionLink.getElement().getProperty('title')).to.equal('Lock');
            });
        });

        describe('ActionLink\'s elements are not undefined', function () {
            var actionLink = new ActionLink({link: {text: 'Lock'}});
            var element = new core.Element('div');
            actionLink.attachTo(element);

            it('ActionLink icon should be undefined because no options passed for icon', function () {
                expect(actionLink.getActionIcon()).to.be.undefined;
            });

            it('ActionLink title should be equal to provided in options', function () {
                expect(actionLink.view.getLink().getText()).to.equal('Lock');
                expect(actionLink.getElement().getProperty('title')).to.equal('Lock');
            });
        });

    });

    function getOptions () {
        return {
            icon: {
                iconKey: 'lock',
                interactive: true,
                title: 'Lock'
            },
            link: {
                text: 'Lock'
            }
        };
    }

});
