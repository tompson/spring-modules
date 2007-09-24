var XT = {};


XT.version = 20070924;


XT.defaultLoadingElementId = null;


XT.defaultLoadingImage = null;


XT.doAjaxAction = function(eventId, sourceElement, serverParams, clientParams) {
    if (! clientParams) {
        clientParams = {};
    }
    if ((! clientParams.loadingElementId) || (! clientParams.loadingImage)) {
        clientParams.loadingElementId = this.defaultLoadingElementId; 
        clientParams.loadingImage = this.defaultLoadingImage;
    }
    
    var ajaxClient = new XT.ajax.Client();
    
    return ajaxClient.doAjaxAction(eventId, sourceElement, serverParams, clientParams);
};


XT.doAjaxSubmit = function(eventId, sourceElement, serverParams, clientParams) {
    if (! clientParams) {
        clientParams = {};
    }
    if ((! clientParams.loadingElementId) || (! clientParams.loadingImage)) {
        clientParams.loadingElementId = this.defaultLoadingElementId; 
        clientParams.loadingImage = this.defaultLoadingImage;
    }
    
    var ajaxClient = new XT.ajax.Client();
    
    return ajaxClient.doAjaxSubmit(eventId, sourceElement, serverParams, clientParams);
};


XT.ajax = {};


XT.ajax.Client = function() {
    
    var ajaxParameter = "ajax-request";
    var eventParameter = "event-id";
    var elementParameter = "source-element";
    var elementIdParameter = "source-element-id";
    var jsonParameters = "json-params";
    
    this.doAjaxAction = function(eventId, sourceElement, serverParams, clientParams) {
        var ajaxRequestType = "ajax-action";
        var queryString = prepareQueryString(ajaxRequestType, eventId, sourceElement, serverParams);
        
        var ajaxRequest = new XT.taconite.AjaxRequest(document.URL);
        
        configureRequest(ajaxRequest, clientParams);
        
        ajaxRequest.addFormElements(document.forms[0]);
        ajaxRequest.setQueryString(ajaxRequest.getQueryString() + "&" + queryString);
        
        ajaxRequest.sendRequest();
    };
    
    this.doAjaxSubmit = function(eventId, sourceElement, serverParams, clientParams) {
        var ajaxRequestType = "ajax-submit";
        
        if (clientParams && clientParams.enableUpload && clientParams.enableUpload == true) {
            var queryParameters = prepareQueryParameters(ajaxRequestType, eventId, sourceElement, serverParams);
            
            var iframeRequest = new XT.taconite.IFrameRequest(document.forms[0], document.URL, queryParameters);
            
            configureRequest(iframeRequest, clientParams);
            
            iframeRequest.sendRequest();
        } else {
            var queryString = prepareQueryString(ajaxRequestType, eventId, sourceElement, serverParams);
            
            var ajaxRequest = new XT.taconite.AjaxRequest(document.URL);
            
            configureRequest(ajaxRequest, clientParams);
            
            ajaxRequest.addFormElements(document.forms[0]);
            ajaxRequest.setQueryString(ajaxRequest.getQueryString() + "&" + queryString);
            ajaxRequest.setUsePOST();
            
            ajaxRequest.sendRequest();
        }
    };
    
    function prepareQueryString(ajaxRequestType, eventId, sourceElement, serverParams) {
        var qs = "";
        if (ajaxRequestType) {
            qs = ajaxParameter + "=" + ajaxRequestType;
        }
        if (eventId) {
            qs = qs + "&" + eventParameter + "=" + eventId;
        }
        if (sourceElement) {
            if (sourceElement.name != null) {
                qs = qs + "&" + elementParameter + "=" + sourceElement.name;
            }
            if (sourceElement.id != null) {
                qs = qs + "&" + elementIdParameter + "=" + sourceElement.id;
            }
        }
        if (serverParams) {
            qs = qs + "&" + jsonParameters + "=" + encodeURIComponent(serverParams.toJSONString());
        }
        return qs;
    };
    
    function prepareQueryParameters(ajaxRequestType, eventId, sourceElement, serverParams) {
        var params = {};
        params[ajaxParameter] = ajaxRequestType;
        params[eventParameter] = eventId;
        if (sourceElement) {
            if (sourceElement.name != null) {
                params[elementParameter] = sourceElement.name;
            }
            if (sourceElement.id != null) {
                params[elementIdParameter] = sourceElement.id;
            }
        }
        if (serverParams) {
            params[jsonParameters] = encodeURIComponent(serverParams.toJSONString());
        }
        return params;
    };
    
    function configureRequest(request, clientParams) {
        if (! clientParams) {
            return;
        }
        
        if (clientParams.loadingElementId != null && clientParams.loadingImage != null) {
            request.loadingElementId = clientParams.loadingElementId; 
            request.loadingImage = clientParams.loadingImage;
            request.setPreRequest(showLoadingSign);
            request.setPostRequest(hideLoadingSign);
        }
    };
    
    function showLoadingSign(request) {
        var targetEl = document.getElementById(request.loadingElementId);
        if (targetEl != null) {
            var img = document.createElement("img");
            img.setAttribute("src", request.loadingImage);
            targetEl.appendChild(img);
        }
    };
    
    function hideLoadingSign(request) {
        var targetEl = document.getElementById(request.loadingElementId);
        if (targetEl != null && targetEl.childNodes.length > 0) {
            targetEl.removeChild(targetEl.childNodes[0]);
        }
    };
};
