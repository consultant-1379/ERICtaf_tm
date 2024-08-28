define({
    "name": "Test Management System",
    "defaultApp": "tm",
    "properties": {
        "tmsuserbutton": {
            "inboxUrl": "#tm/inbox/",
            "adminUrl": "#tm/admin/",
            "reviewUrl": "#tm/review/",
            "statisticsUrl": "#statistics",
            "restUserUrl": "/tm-server/api/users/me",
            "restLogoutUrl": "/tm-server/api/login",
            "logoutUrl": "login/",
            "welcomeUrl": "#tm/"
        }
    },
    "components": [
        {
            "path": "tmsuserbutton"
        },
        {
            "path": "flyout"
        },
        {
            "path": "helpbutton"
        }
    ]
});
