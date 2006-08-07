/**
 @fileoverview
 This JavaScript file describes actions for sending ajax requests using Taconite.
 */

function doAjaxAction(eventId, sourceElement) {
    var ajaxRequest = new AjaxRequest(document.URL);
    ajaxRequest.addFormElementsByFormEl(document.forms[0]);
    ajaxRequest.setQueryString(ajaxRequest.getQueryString() + "&ajax-request=ajax-action" + "&event-id=" + eventId + createSimpleQueryString(sourceElement));
    ajaxRequest.sendRequest();
}

function doAjaxSubmit(eventId, sourceElement) {
    var ajaxRequest = new AjaxRequest(document.URL);
    ajaxRequest.addFormElementsByFormEl(document.forms[0]);
    ajaxRequest.setQueryString(ajaxRequest.getQueryString() + "&ajax-request=ajax-submit" + "&event-id=" + eventId + createSimpleQueryString(sourceElement));
    ajaxRequest.setUsePOST();
    ajaxRequest.sendRequest();
}

function createSimpleQueryString(sourceElement) {
    var qs = "";
    if (sourceElement != undefined && sourceElement != null) {
        if (sourceElement.name != null && sourceElement.name != "") {
            qs = qs + "&source-element=" + sourceElement.name;
        }
        if (sourceElement.id != null && sourceElement.id != "") {
            qs = qs + "&source-element-id=" + sourceElement.id;
        }
    }
    return qs;
}
