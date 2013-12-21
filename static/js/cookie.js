function Cookie() {

}

Cookie.prototype.GetCookie = function (key) {
    var cookie = document.cookie;
    var length = cookie.length;
    if (length > 0) {
        cookie += ';';
        key += '=';
        if (cookie.indexOf(key) != -1) {
            var startIndex = cookie.indexOf(key) + key.length;
            var value = cookie.substring(startIndex, cookie.indexOf(';', startIndex));
            if (value.length > 0) {
                return value;
            } else {
                return "";
            }
        }
    }
    return "";
}

Cookie.prototype.DelCookie = function (key) {
    document.cookie = name + '=; expires=Thu, 01-Jan-70 00:00:01 GMT;';
}

Cookie.prototype.SetCookie = function (key, value) {
    document.cookie = key + "=" + value;
}

Cookie.prototype.IsCookieExist = function (key) {
    var TOKEN_NAME = "access_token" + "=";
    var cookie = document.cookie;
    var length = cookie.length;
    if (length > 0) {
        cookie += ';';
        if (cookie.indexOf(TOKEN_NAME) != -1) {
            var startIndex = cookie.indexOf(TOKEN_NAME) + TOKEN_NAME.length;
            var accessToken = cookie.substring(startIndex, cookie.indexOf(';', startIndex));
            console.log(accessToken);
            if (accessToken.length > 0) {
                return true;
            } else {
                return false;
            }
        }
    }
}

cookie = new Cookie;
console.log(cookie.GetCookie("access_token"));