SetupLogin({

    locales: ['en-us'],

    onSubmit: function (username, password, sessionId, isCookie) {
        Login.request({
            type: 'POST',
            url: '/tm-server/api/login',
            data: {
                username: username,
                password: password,
                storeSession: isCookie,
                sessionId: sessionId
            },
            success: function (xhr) {
                if (isCookie) {
                    var login = JSON.parse(xhr.responseText);
                    var currentDate = new Date(login.period);
                    document.cookie = 'sessionId=' + login.sessionId + '; expires=' + currentDate;
                }
                if (sessionId) {
                    var login = JSON.parse(xhr.responseText);
                    document.cookie = 'sessionId=' + login.sessionId;
                }
                Login.redirect();
            },
            error: function (xhr) {
                if (xhr.status === 503) {
                    Login.serverDown();
                } else if (!sessionId) {
                    Login.invalidCredentials();
                }
            }
        });
    }
});