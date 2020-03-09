$(document).ready(function () {
    let validLogin = false;
    let validPassword = false;
    $("#search").click(function(event) {
        event.preventDefault();
        let login = $('#login').val();
        let password = $('#pwd').val();
        if (login !== '' && login.length >= 3) {
            validLogin = true;
            addSuccessSpan('.parent-login', 'Логин:')
        } else {
            validLogin = false;
            addErrorSpan('.parent-login', 'Логин должен содержать 3 и более символов')
        }

        if (password !== '' && password.length >= 3) {
            validPassword = true;
            addSuccessSpan('.parent-pwd', 'Пароль:')
        } else {
            validPassword = false;
            addErrorSpan('.parent-pwd', 'Пароль должен содержать 3 и более символов')
        }
        if (validLogin === true && validPassword === true) {
            $.ajax({
                type: 'POST',
                url: '/sing-in',
                data: {login: login, password: password},
                dataType: 'text',
            }).done(function (data) {
                data = JSON.parse(data);
                if (typeof data.url !== 'undefined') {
                    window.location = data.url;
                } else {
                    $('.parent-login label:first').remove();
                    $('.parent-login input:first')
                        .before("<label style='color: #ff4500' for='login'>" + data.message + "</label>");
                }
            }).fail(function (err) {
                console.log(err);
            });
        }
    });
});

function addSuccessSpan(nameClass, nameLabel) {
    $('' + nameClass + ' label:first').remove();
    $('' + nameClass + ' input:first').before("<label>" + nameLabel + "</label>");
    $('' + nameClass + ' span').remove();
    $('' + nameClass + '').removeClass('has-error').addClass('has-success');
    $('' + nameClass + ' input').after("<span class='glyphicon glyphicon-ok form-control-feedback'></span>");
}

function addErrorSpan(nameClass, nameMessage) {
    $('' + nameClass + ' label:first').remove();
    $('' + nameClass + ' input:first').before("<label style='color: #ff4500'>" + nameMessage + "</label>");
    $('' + nameClass + ' span').remove();
    $('' + nameClass + '').removeClass('has-success').addClass('has-error')
        .append("<span class='glyphicon glyphicon-remove form-control-feedback'></span>");
}