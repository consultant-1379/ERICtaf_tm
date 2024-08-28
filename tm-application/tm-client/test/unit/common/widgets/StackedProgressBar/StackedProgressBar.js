/*global define, describe, it, expect*/
/*jshint expr: true */
define([
    'jscore/core',
    'tm/common/widgets/StackedProgressBar/StackedProgressBar'
], function (core, StackedProgressBar) {
    'use strict';

    describe('tm/common/widgets/StackedProgressBar/StackedProgressBar', function () {
        var stackedProgressBar = new StackedProgressBar();
        var element = core.Element.parse('<div class="eaTM-rTest"></div>');
        stackedProgressBar.attachTo(element);

        it('StackedProgressBar should be defined', function () {
            expect(stackedProgressBar).not.to.be.undefined;
        });

        describe('StackedProgressBar\'s test widget', function () {

            it('StackedProgressBarView is defined', function () {
                expect(stackedProgressBar.view).not.to.be.undefined;
            });

            it('StackedProgressBar set Items', function () {
                var data = [
                    {
                        value: 10,
                        color: 'red',
                        title: 'Fail'
                    },
                    {
                        value: 30,
                        color: 'orange',
                        title: 'Waiting'
                    },
                    {
                        value: 40,
                        color: 'green',
                        title: 'Pass'
                    },
                    {
                        value: 20,
                        color: 'paleBlue',
                        title: 'Blocked'
                    }
                ];
                stackedProgressBar.setItems(data);

                expect(stackedProgressBar.getItems().length).equals(4);
                expect(stackedProgressBar.getItems()[0].title).equals('Fail');
                expect(stackedProgressBar.getItems()[1].title).equals('Waiting');
                expect(stackedProgressBar.getItems()[2].title).equals('Pass');
                expect(stackedProgressBar.getItems()[3].title).equals('Blocked');
            });

            it('StackedProgressBar set Items greater than 100%', function () {
                var badData = [
                    {
                        value: 10,
                        color: 'red',
                        title: 'Fail'
                    },
                    {
                        value: 30,
                        color: 'orange',
                        title: 'Waiting'
                    },
                    {
                        value: 40,
                        color: 'green',
                        title: 'Pass'
                    },
                    {
                        value: 50,
                        color: 'paleBlue',
                        title: 'Blocked'
                    }
                ];
                stackedProgressBar.setItems(badData);
                expect(stackedProgressBar.getItems().length).equals(0);
            });

            it('StackedProgressBar set Items with minus and decimal percentage', function () {
                var badData = [
                    {
                        value: -1,
                        color: 'red',
                        title: 'Fail'
                    },
                    {
                        value: 0.4,
                        color: 'paleBlue',
                        title: 'Blocked'
                    }
                ];
                stackedProgressBar.setItems(badData);
                expect(stackedProgressBar.getItems().length).equals(2);
            });
        });

    });

});
