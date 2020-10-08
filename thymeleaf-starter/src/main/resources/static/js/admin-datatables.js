"use strict";

/**
 * Converts the datatable parameters to a format that can be parsed by the Spring Framework.
 *
 * @param data The object that is created by the datatable.
 * @param features The datatable features.
 * @param prefix The prefix for the object properties.
 * @param springData The Spring framework data.
 * @returns {*} The object format that can be parsed by the Spring framework.
 */
function toSpringData(data, features, prefix = '', springData = {}) {
    for (let key of Object.keys(data)) {
        if (Array.isArray(data[key])) {
            data[key].forEach(
                (element, index) => toSpringData(element, features, key + '[' + index + '].', springData)
            );
        } else if (data[key] === Object(data[key])) {
            toSpringData(data[key], features, key + '.', springData)
        } else {
            springData[prefix + key]  = data[key];
        }
    }

    return springData;
}

/**
 * Renders a list of items to be displayed in a datatable.
 *
 * @param data The list of items. Must be an array.
 * @param mapFunc The function to apply to each item.
 * @returns {string} The string to display.
 */
function renderList(data, mapFunc) {
    return data.map(mapFunc).join(', ');
}

/**
 * Render a date to a string.
 *
 * @param date The date to render.
 * @returns {string} The rendered string.
 */
function renderDate(date) {
    if (date) {
        return date.slice(0, date.indexOf('T'));
    }

    return '';
}

/**
 * Create a datatable from a table.
 *
 * @param id The element identifier of the table.
 * @param url The URL from which to fetch the data.
 * @param columns The column configuration.
 */
function createDataTable(id, url, columns) {
    $(`#${id}`).DataTable({
        processing: true,
        serverSide: true,
        searching: false,
        ajax: {
            url: url,
            data: toSpringData
        },
        columns: columns
    });
}
