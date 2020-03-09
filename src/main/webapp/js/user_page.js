$(document).ready(function () {
    initUserPage();
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
function initUserPage() {
    $.ajax({
        type: 'POST',
        url: '/page-user',
        data: false,
        dataType: 'text',
    }).done(function (user) {
        user = JSON.parse(user);
        let dateTime = new Date(user.createDate);
        $('#tbody tr:first').remove();
        $('#tbody').append("<tr>"
            + "<td>" + user.name + "</td>"
            + "<td>" + user.login + "</td>"
            + "<td>" + user.password + "</td>"
            + "<td>" + user.email + "</td>"
            + "<td>" + user.roleName + "</td>"
            + "<td>" + user.country + "</td>"
            + "<td>" + user.city + "</td>"
            + "<td>" + formatDate(dateTime) + "</td>"
            + "<td><img src='/download?name=" + user.photoId + "' class='img-rounded' alt='Cinque Terre' width='150px' height='100px'/></td>"
            + "<td>"
            + "<form action='/download' method='get'>"
            + "<input type='hidden' name='name' value='" + user.photoId + "'>"
            + "<input type='submit' value='Скачать фото' class='btn btn-info'>"
            + "</form>"
            + "</td>"
            + "<td>"
            + "<button type='button' class='btn btn-info' data-toggle='modal' data-target='#myModal'>Обновить</button>"
            + "</td>"
            + "<td>"
            + "<form action='/sing-out' method='get'>"
            + "<input type='submit' value='Разлогиниться' class='btn btn-info'>"
            + "</form>"
            + "</td>"
            + "</tr>");
        $('#myModal').click(modalUpdate(user));
    }).fail(function (err) {
        console.log(err);
    });
}

function modalUpdate(user) {
    $('.modal-body form:first').remove();
    $('.modal-body form:last').remove();
    $('.modal-body').append(
        "<form>"
        + "<div class='form-group'>"
        + "<label>Имя: </label>"
        + "<input name='name' class='form-control' id='updateName' placeholder='Имя' value='" + user.name + "'>"
        + "</div>"
        + "<div class='form-group'>"
        + "<label>Логин: </label>"
        + "<input name='login' class='form-control' id='updateLogin' placeholder='Логин' value='" + user.login + "'>"
        + "</div>"
        + "<div class='form-group'>"
        + "<label>Пароль: </label>"
        + "<input name='password' class='form-control' id='updatePassword' placeholder='Пароль' value='" + user.password + "'>"
        + "</div>"
        + "<div class='form-group'>"
        + "<label>Почта: </label>"
        + "<input name='email' class='form-control' id='updateEmail' placeholder='Почта' value='" + user.email + "'>"
        + "</div>"
        + "<div style='width: 100%; display: flex;'>"
        + "<div class='dropdown country' style='float: left'>"
        + "<select id='country-user' style='margin-bottom: 10px'>"
        + "<option>Выберите Страну</option>"
        + "</select>"
        + "</div>"
        + "<div class='dropdown city' style='margin-left: auto'>"
        + "<select id='city-user' style='margin-bottom: 10px'>"
        + "<option>Выберите Город</option>"
        + "</select>"
        + "</div>"
        + "</div>"
        + "<div class='last-div'>"
        + "<label>Название картинки: </label>"
        + "<input name='photo' class='form-control' id='uploadPhoto' placeholder='Картинка' value='" + user.photoId + "'>"
        + "</div>"
        + "</form>"
        + "<form id='uploadForm' style='margin-top: 10px'>"
        + "<label for='myFile'>Загрузка картинки:</label>"
        + "<input type='file' id='myFile' name='myFile'><br>"
        + "<button type='button' id='upload' class='btn btn-success btn-sm'>Загрузить картинку</button>"
        + "</form>"
    );

    loadCountries();

    $('#upload').click(function () {
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

    $('#close').click(function () {
        let id = user.id;
        let name = $('#updateName').val();
        let login = $('#updateLogin').val();
        let email = $('#updateEmail').val();
        let image = $('#uploadPhoto').val();
        let password = $('#updatePassword').val();
        let country = $('#country-user :selected').text();
        let city = $('#city-user :selected').text();
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
                country: country,
                city: city,
                action: 'update'
            },
            dataType: 'text',
        }).done(function () {
            initUserPage();
        }).fail(function (err) {
            console.log(err);
        });
    });
}

function loadCountries() {
    $("#city-user").prop("disabled", true);
    $.ajax({
        type: 'POST',
        url: '/city',
        data: {action: "all"},
        dataType: 'text',
    }).done(function (user) {
        user = JSON.parse(user);
        for (let key in user) {
            $('#country-user option:last').after("<option value='" + key + "'>" + user[key] + "</option>");
        }
        $(".country").change(function () {
            loadCities();
        });
    }).fail(function (err) {
        console.log(err);
    });
}

function loadCities() {
    $('#city-user option').remove();
    $('#city-user').append("<option>Выберите Город</option>");
    let idCountry = $('#country-user').val();
    $.ajax({
        type: 'POST',
        url: '/city',
        data: {action: "cities", id: idCountry},
        dataType: 'text',
    }).done(function (cities) {
        cities = JSON.parse(cities);
        for (let key in cities) {
            $('#city-user option:last').after("<option value='" + key + "'>" + cities[key] + "</option>");
        }
        $("#city-user").prop("disabled", false);
    }).fail(function (err) {
        console.log(err);
    });
}