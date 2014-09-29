/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//Проверка правильности заполнения  форм
function validate(senderForm) {
    switch (senderForm.id) {
        case "registration":
            $(".mandatory, .confirm").keyup();
                   
            setTimeout(function(){if ($("span.error").size() === 0) {
                $(senderForm).submit();
            }},1000);
    }
}
//Проверка, что поле обязательное для заполнения не пусто
function emptyTest(field) {
    if (field.value === "") {
        addErr(field, "<span   class='mandatory error'>Поле не может быть пустым</span><br class='mandatory error' >");
    }
    else {
        rmErr(field, '.mandatory.error');
    }
}
//Проверка, что значения полей которые должны иметь одинаковые значения действительно одинаковы.
function confirmationCheck(field) {
    var err = '';
    if ($(field).prop('value') !== $(".confirmt").prop('value')) {

        addErr(field, "<span class='confirm error'>Значение поля не совпадает с полем \"пароль\"</span><br class='confirm error' >");
    }
    else {
        rmErr(field, '.confirm.error');
    }
}
//Проверка валидности введенных данных.
function dataValidCheck(field) {
    var val = new String($(field).prop('value'));
    if (val !='' && (val.match('^[a-z,A-z,a-я,А-Я,0-9]+') === null)) {
        addErr(field, "<span class='invalid error'>Значение поля должно \n\
начинаться с цифры или буквы.</span><br class='invalid error' >");

    } else {
        rmErr(field, ".invalid.error");
    }
}
function addErr(field, text) {
    var res = new String(text).match(" class=['|\"]((?:.)+?)['|\"]");
    if ($(field).siblings("span[class='" + res[1] + "']").length === 0) {
        $(field).before(text);
        $(field).css("color", "red");
    }
}
function rmErr(field, err) {
    $(field).siblings(err).remove();
    if ($(field).siblings(".error").length === 0)
        $(field).css('color', 'green');
    else
        $(field).css('color', 'red');
}


