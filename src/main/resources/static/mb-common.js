
let _MB_defaultXHRErrorHandler = function (xhr) {
    alert("Request Failed, Response Status Code: " + xhr.status
        + ", Response Status Text: " + xhr.statusText);
}

let _MB_defaultXHRSuccessHandler = function (callback) {
    return function (result, status, xhr) {
        if (xhr.status === 200) {
            if (callback && typeof callback === "function") {
                callback(result, status, xhr);
            }
            return;
        }
        _MB_defaultXHRErrorHandler(xhr);
    }
}
