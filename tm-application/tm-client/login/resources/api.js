(function () {
    'use strict';

    var options, strings;

    window.Login = {
        _resetLogin: function () {
            eLoginView.username.value = '';
            eLoginView.password.value = '';
        },

        _showMessage: function (key) {
            eLoginView.message.textContent = strings[key];
        },

        _clearMessage: function () {
            eLoginView.message.textContent = '';
        },

        invalidCredentials: function () {
            this._resetLogin();
            this._showMessage('invalidUsernamePassword');
        },

        serverDown: function () {
            this._resetLogin();
            this._showMessage('serverAppearsDown');
        },

        redirect: function () {
            window.location.href = (options.redirectPath ? options.redirectPath : '../') + window.location.hash;
        }
    };

    window.SetupLogin = function (opts) {
        options = opts;
        var sessionId = getCookie('sessionId');
        doGet('locales/' + getLocale(options) + '/dictionary.json', function (xhr) {
            strings = JSON.parse(xhr.responseText);

            for (var key in strings) {
                strings[key] = strings[key].replace('{{year}}',new  Date().getFullYear());
            }

            doGet('resources/template.html', function (xhr) {
                container.innerHTML = xhr.responseText.replace(/{{(\w+)}}/g, function (full, inner) {
                    return strings[inner];
                });

                loadElementVariables();
                document.title = strings.title;

                eLoginView.licenceBtn.onclick = showLogin;
                eLoginView.loginBtn.onclick = submitLogin;

                var sessionId = getCookie('sessionId');

                if (sessionId) {
                    options.onSubmit.call(Login, "quick", "quick", sessionId, false);
                }
                if (opts.showLicence) {
                    showLicence();
                } else {
                    showLogin();
                }
            });
        });
    };

    function showLicence() {
        eLoginView.licence.style.display = 'block';
        eLoginView.login.style.display = 'none';
    }

    function showLogin() {
        eLoginView.licence.style.display = 'none';
        eLoginView.login.style.display = 'block';
    }

    function submitLogin() {
        var username = eLoginView.username.value.trim();
        var password = eLoginView.password.value.trim();
        var rememberMe = eLoginView.rememberMeCheckbox.checked;

        removeErrorState(eLoginView.username);
        removeErrorState(eLoginView.password);
        Login._clearMessage();

        if (username === '' && password === '') {
            setErrorState(eLoginView.username);
            setErrorState(eLoginView.password);
            Login._showMessage('missingUsernamePassword');
        } else if (username === '') {
            setErrorState(eLoginView.username);
            Login._showMessage('missingUsername');
        } else if (password === '') {
            setErrorState(eLoginView.password);
            Login._showMessage('missingPassword');
        } else if (rememberMe) {
            options.onSubmit.call(Login, username, password, null, true);
        } else {
            options.onSubmit.call(Login, username, password, null, false);
        }
    }

    function removeErrorState(element) {
        element.classList.remove('eaLogin-loginField_error_true');
    }

    function setErrorState(element) {
        element.classList.add('eaLogin-loginField_error_true');
    }

    function loadElementVariables() {
        window.eLoginView = {
            title: container.querySelector('.eaLogin-title'),
            username: container.querySelector('.eaLogin-loginUsername'),
            password: container.querySelector('.eaLogin-loginPassword'),
            copyright: container.querySelector('.eaLogin-copy'),
            message: container.querySelector('.eaLogin-messagesBox'),
            licence: container.querySelector('.eaLogin-licence'),
            login: container.querySelector('.eaLogin-login'),
            licenceBtn: container.querySelector('.eaLogin-licence button'),
            loginBtn: container.querySelector('.eaLogin-login button'),
            rememberMeCheckbox: container.querySelector('.eaLogin-inputRememberMe')
        };
    }

    function getLocale(options) {
        var preferredLocale = typeof navigator === 'undefined' ? 'root' :
            ((navigator.languages ? navigator.languages[0] : navigator.language) ||
            navigator.userLanguage || 'root').toLowerCase();

        var parts = preferredLocale.split('-'),
            exact = false,
            matched = false,
            availableLocales = options.locales;

        availableLocales.forEach(function (language) {
            var lang = language.toLowerCase();
            if (preferredLocale.toLowerCase() === lang) {
                exact = lang;
            }
            if (parts[0] === lang.split('-')[0] && matched === false) {
                matched = lang;
            }
        });
        return exact || matched || availableLocales[0];
    }

    function doGet(url, callback, errback) {
        Login.request({
            url: url,
            success: callback,
            error: errback
        });
    }

    function doPost(url, data, callback, errback) {
        Login.request({
            url: url,
            data: data,
            success: callback,
            error: errback,
            type: 'POST'
        });
    }

    function getCookie(value) {
        var name = value + "=";
        var data = document.cookie.split(';');
        for(var i = 0; i < data.length; i++) {
            var c = data[i];
            while (c.charAt(0) == ' ') c = c.substring(1);
            if (c.indexOf(name) == 0) return c.substring(name.length, c.length);
        }
        return "";
    }

    Login.request = function (options) {
        var xhr = new XMLHttpRequest();
        xhr.open(options.type || 'GET', options.url, true);

        if (options.data) {
            xhr.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
        }

        xhr.onload = function () {
            if (xhr.status < 400) {
                options.success(xhr);
            } else {
                options.error(xhr);
            }
        };

        xhr.onerror = function () {
            options.error(xhr);
        };

        xhr.send(options.data ? JSON.stringify(options.data) : null);
    }

})();