define([
    './FormatCell/FormatCellFactory',
    './IconsCell/IconsCellFactory',
    './LinkCell/LinkCellFactory',
    './ColoredCell/ColoredCellFactory',
    './ButtonCell/ButtonCellFactory',
    './ObjectCell/ObjectCellFactory',
    './WidgetCell/WidgetCellFactory',
    './VersionCell/VersionSelectCell',
    './ArrayCell/ArrayCellFactory'
], function (formatCell, iconsCell, linkCell, coloredCell, buttonCell, objectCell, widgetCell, versionCell,
             arrayCellFactory) {
    'use strict';

    return {
        format: function (options) {
            return formatCell(options);
        },

        icons: function (options) {
            return iconsCell(options);
        },

        link: function (options) {
            return linkCell(options);
        },

        colored: function (options) {
            return coloredCell(options);
        },

        button: function (options) {
            return buttonCell(options);
        },

        object: function (options) {
            return objectCell(options);
        },

        widget: function (options) {
            return widgetCell(options);
        },

        versionCell: function (options) {
            return versionCell(options);
        },

        arrayCell: function (options) {
            return arrayCellFactory(options);
        }
    };
});
