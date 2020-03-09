$(document).ready(function () {
    initAdminPage();
    $('#create-user').click(createUser());
});

function formatDate(date) {
    let dd = date.getDate();
    if (dd < 10) dd = '0' + dd;

    let mm = date.getMonth() + 1;
    if (mm < 10) mm = '0' + mm;

    let yy = date.getFullYear() % 100;
    if (yy < 10) yy = '0' + yy;

    return dd + '.' + mm + '.' + yy;
}

function initAdminPage() {
    $.ajax({
        url: '/admin-page',
        type: 'POST',
        dataType: "text",
    }).done(function (data) {
        data = JSON.parse(data);
        $('#tbody-admin').remove();
        $('.table-head').after("<tbody id='tbody-admin'></tbody>");
        $(data).each(function (index, user) {
            $('#tbody-admin').append("<tr>"
                + "<td>" + user.id + "</td>"
                + "<td>" + user.name + "</td>"
                + "<td>" + user.login + "</td>"
                + "<td>" + user.password + "</td>"
                + "<td>" + user.email + "</td>"
                + "<td>" + user.roleName + "</td>"
                + "<td>" + user.country + "</td>"
                + "<td>" + user.city + "</td>"
                + "<td>" + formatDate(new Date(user.createDate)) + "</td>"
                + "<td><img src='/download?name=" + user.photoId + "' class='img-rounded real' alt='Cinque Terre' width='150px' height='100px'/></td>"
                + "<td>"
                + "<form action='/download' method='get'>"
                + "<input type='hidden' name='name' value='" + user.photoId + "'>"
                + "<input type='submit' value='Скачать фото' class='btn btn-info'>"
                + "</form>"
                + "</td>"
                + "<td class='update'>"
                + "<button type='button' class='btn btn-info' data-toggle='modal' data-target='#adminModal'>Обновить</button>"
                + "</td>"
                + "<td>"
                + "<input type='button' id='delete-disable' value='Удалить пользователя' class='btn btn-danger delete-user'>"
                + "<input type='hidden' id='image' value='" + user.photoId + "'>"
                + "</td></tr>")
                .on('click', '.delete-user', function (event) {
                    if ($(this).parent().parent()[0].cells[5].innerText !== "admin") {
                        let currentImage = $(this).parent().find('#image').val();
                        let currentId = $(this).parent().parent()[0].cells[0].innerText;
                        let reference = $(this).parent().parent();
                        deleteUser(currentId, currentImage, reference);
                    } else {
                        alert("Сначала поменяйте роль на user, при условии что вы залогинены не под этим пользователем!")
                    }
                    event.stopImmediatePropagation();
                    return false;
                }).on('click', '.update', function (event) {
                $('#adminModal').modal('show');
                userUpdate(this);
                event.stopImmediatePropagation();
                return false;
            });
        });
    }).fail(function (err) {
        console.log(err);
    });
}

function deleteUser(id, photo, reference) {
    $.ajax({
        type: 'POST',
        url: '/list',
        data: {
            id: id,
            image: photo,
            action: 'delete'
        },
    }).done(function () {
        $(reference).remove();
    }).fail(function (err) {
        console.log(err);
    });
}

function userUpdate(currentObject) {
    getAllRoles('#role');
    let image = $(currentObject).parent().find('#image').val();
    let name = $(currentObject).parent()[0].cells[1].innerText;
    let login = $(currentObject).parent()[0].cells[2].innerText;
    let password = $(currentObject).parent()[0].cells[3].innerText;
    let email = $(currentObject).parent()[0].cells[4].innerText;
    let id = $(currentObject).parent()[0].cells[0].innerText;
    $('.admin-content .modal-body').remove();
    $('.admin-header').after("<div class='modal-body admin-body'></div>");
    $('.admin-body').append(
        "<form>"
        + "<div class='form-group'>"
        + "<label>Имя: </label>"
        + "<input name='name' class='form-control' id='updateName' placeholder='Имя' value='" + name + "'>"
        + "</div>"
        + "<div class='form-group'>"
        + "<label>Логин: </label>"
        + "<input name='login' class='form-control' id='updateLogin' placeholder='Логин' value='" + login + "'>"
        + "</div>"
        + "<div class='form-group'>"
        + "<label>Пароль: </label>"
        + "<input name='password' class='form-control' id='updatePassword' placeholder='Пароль' value='" + password + "'>"
        + "</div>"
        + "<div class='form-group'>"
        + "<label>Почта: </label>"
        + "<input name='email' class='form-control' id='updateEmail' placeholder='Почта' value='" + email + "'>"
        + "</div>"
        + "<div style='width: 100%; display: flex;'>"
        + "<div class='dropdown admin-country-update' style='float: left'>"
        + "<select id='country-admin-update' style='margin-bottom: 10px'>"
        + "<option>Выберите Страну</option>"
        + "</select>"
        + "</div>"
        + "<div class='dropdown admin-city-update' style='margin-left: auto'>"
        + "<select id='city-admin-update' style='margin-bottom: 10px'>"
        + "<option>Выберите Город</option>"
        + "</select>"
        + "</div>"
        + "</div>"
        + "<div class='dropdown'>"
        + "<select id='role' style='margin-bottom: 10px'>"
        + "<option value='1'>Выберите роль</option>"
        + "</select>"
        + "</div>"
        + "<div class='last-div'>"
        + "<label>Название картинки: </label>"
        + "<input name='photo' class='form-control' id='uploadPhoto' placeholder='Картинка' value='" + image + "'>"
        + "</div>"
        + "</form>"
        + "<form id='uploadForm' style='margin-top: 10px'>"
        + "<label for='myFile'>Загрузка картинки:</label>"
        + "<input type='file' id='myFile' name='myFile'><br>"
        + "<button type='button' id='upload' class='btn btn-primary btn-sm'>Загрузить картинку</button>"
        + "</form>");

    loadAdminCountries('#country-admin-update', '#city-admin-update', '.admin-country-update');
    $('#upload').off().click(function () {
        let value = new FormData($("#uploadForm")[0]);
        $.ajax({
            url: '/upload',
            type: 'POST',
            data: value,
            processData: false,
            contentType: false,
            dataType: 'text',
        }).done(function (imageName) {
            if (typeof imageName !== 'undefined') {
                $('.last-div input').remove();
                $('.last-div label').after("<input name='photo' class='form-control'" +
                    " id='uploadPhoto' placeholder='Картинка' value='" + imageName + "'>");
            }
        }).fail(function (err) {
            console.log(err);
        });
    });

    $('#close').off().click(function () {
        let roleId = $('#role').val();
        let name = $('#updateName').val();
        let login = $('#updateLogin').val();
        let email = $('#updateEmail').val();
        let image = $('#uploadPhoto').val();
        let password = $('#updatePassword').val();
        let country = $('#country-admin-update :selected').text();
        let city = $('#city-admin-update :selected').text();
        $.ajax({
            type: 'POST',
            url: '/list',
            data: {
                id: id,
                name: name,
                login: login,
                password: password,
                email: email,
                image: image,
                role: roleId,
                country: country,
                city: city,
                action: 'update'
            },
        }).done(function () {
            initAdminPage();
        }).fail(function (err) {
            console.log(err);
        });
    })
}

function createUser() {
    $('.new-user-form .create-new-user').remove();
    $('.new-user-header').after("<div class='modal-body create-new-user'>");
    $('.create-new-user').append(
        "<form>"
        + "<div class='form-group'>"
        + "<label>Имя: </label>"
        + "<input name='name' class='form-control' id='createName' placeholder='Имя'>"
        + "</div>"
        + "<div class='form-group has-feedback login-valid'>"
        + "<label for='createLogin'>Логин: </label>"
        + "<input name='login' class='form-control' id='createLogin' placeholder='Логин' value=''>"
        + "</div>"
        + "<div class='form-group has-feedback pwd-valid'>"
        + "<label for='createPassword'>Пароль: </label>"
        + "<input name='password' class='form-control' id='createPassword' placeholder='Пароль' value=''>"
        + "</div>"
        + "<div class='form-group has-feedback email-valid'>"
        + "<label for='createEmail'>Почта: </label>"
        + "<input name='email' class='form-control' id='createEmail'placeholder='Почта'>"
        + "</div>"
        + "<div style='width: 100%; display: flex;'>"
        + "<div class='dropdown admin-country-create' style='float: left'>"
        + "<select id='country-admin-create' style='margin-bottom: 10px'>"
        + "<option>Выберите Страну</option>"
        + "</select>"
        + "</div>"
        + "<div class='dropdown admin-city-create' style='margin-left: auto'>"
        + "<select id='city-admin-create' style='margin-bottom: 10px'>"
        + "<option>Выберите Город</option>"
        + "</select>"
        + "</div>"
        + "</div>"
        + "<div class='dropdown'>"
        + "<select id='create-role' style='margin-bottom: 10px'>"
        + "<option value='1'>Выберите роль</option>"
        + "</select>"
        + "</div>"
        + "<div class='last-div-create'>"
        + "<label>Название картинки: </label>"
        + "<input name='photo' class='form-control' id='uploadPhotoCreate' placeholder='Картинка'>"
        + "</div>"
        + "</form>"
        + "<form id='uploadNewImage' style='margin-top: 10px'>"
        + "<label for='newFile'>Загрузка картинки:</label>"
        + "<input type='file' id='newFile' name='newFile'><br>"
        + "<button type='button' id='uploadNewUser' class='btn btn-success btn-sm'>Загрузить картинку</button>"
        + "</form>"
    );
    loadAdminCountries('#country-admin-create', '#city-admin-create', '.admin-country-create');
    getAllRoles('#create-role');
    $('#uploadNewUser').click(function () {
        let value = new FormData($("#uploadNewImage")[0]);
        $.ajax({
            url: '/upload',
            type: 'POST',
            data: value,
            processData: false,
            contentType: false,
            dataType: 'text',
        }).done(function (imageName) {
            if (typeof imageName !== 'undefined') {
                $('.last-div-create input').remove();
                $('.last-div-create label').after("<input name='photo' class='form-control'" +
                    " id='uploadPhotoCreate' placeholder='Картинка' value='" + imageName + "'>");
            }
        }).fail(function (err) {
            console.log(err);
        });
    });

    $('#close-create').click(function () {
        let validLogin = false;
        let validPassword = false;
        let validEmail = false;
        let idRole = $('#create-role').val();
        let name = $('#createName').val();
        let login = $('#createLogin').val();
        let email = $('#createEmail').val();
        let image = $('#uploadPhotoCreate').val();
        let password = $('#createPassword').val();
        let country = $('#country-admin-create :selected').text();
        let city = $('#city-admin-create :selected').text();

        if (login !== '' && login.length >= 3) {
            validLogin = true;
            addSuccessSpan('.login-valid', 'Логин:');
        } else {
            validLogin = false;
            addErrorSpan('.login-valid', 'Логин должен содержать 3 и более символов')
        }

        if (email !== '' && email.length >= 3) {
            validEmail = true;
            addSuccessSpan('.email-valid', 'Почта:')
        } else {
            validEmail = false;
            addErrorSpan('.email-valid', 'Введите корректный адрес почты')
        }

        if (password !== '' && password.length >= 3) {
            validPassword = true;
            addSuccessSpan('.pwd-valid', 'Пароль:')
        } else {
            validPassword = false;
            addErrorSpan('.pwd-valid', 'Пароль должен быть длинее 3 символов')
        }

        if (validEmail && validLogin && validPassword) {
            $.ajax({
                type: 'POST',
                url: '/list',
                data: {
                    name: name,
                    login: login,
                    password: password,
                    email: email,
                    image: image,
                    role: idRole,
                    country: country,
                    city: city,
                    action: 'add'
                },
                dataType: 'text',
            }).done(function () {
                initAdminPage();
                $('.create-new-user form:first')[0].reset();
                $('#uploadPhotoCreate').val('');
                $('#newFile').val('');
                $('.create-new-user div').removeClass('has-success');
                $('.create-new-user span').remove();
                $('#create-user').modal('hide');
            }).fail(function (err) {
                console.log(err);
            });
        }
    });
}

function getAllRoles(idRole) {
    $.ajax({
        url: '/role',
        type: 'POST',
        dataType: "text",
    }).done(function (data) {
        data = JSON.parse(data);
        $(data).each(function (index, role) {
            $('' + idRole + ' option:first').after("<option value='" + role.id + "'>" + role.role + "</option>");
        })
    }).fail(function (error) {
        console.log(error);
    })
}

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

function loadAdminCountries(idCountry, idCity, classCountry) {
    $('' + idCity + '').prop("disabled", true);
    $.ajax({
        type: 'POST',
        url: '/city',
        data: {action: "all"},
        dataType: 'text',
    }).done(function (user) {
        user = JSON.parse(user);
        for (let key in user) {
            $('' + idCountry + ' option:last').after("<option value='" + key + "'>" + user[key] + "</option>");
        }
        $('' + classCountry + '').change(function () {
            loadAdminCities(idCountry, idCity);
        });
    }).fail(function (err) {
        console.log(err);
    });
}

function loadAdminCities(idCountry, idCity,) {
    $('' + idCity + ' option').remove();
    $('' + idCity + '').append("<option>Выберите Город</option>");
    let id = $('' + idCountry + '').val();
    $.ajax({
        type: 'POST',
        url: '/city',
        data: {action: "cities", id: id},
        dataType: 'text',
    }).done(function (cities) {
        cities = JSON.parse(cities);
        for (let key in cities) {
            $('' + idCity + ' option:last').after("<option value='" + key + "'>" + cities[key] + "</option>");
        }
        $('' + idCity + '').prop("disabled", false);
    }).fail(function (err) {
        console.log(err);
    });
}