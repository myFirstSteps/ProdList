/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//Проверка правильности заполнения  форм
function validate(senderForm) {
    $("#error").empty();
    var empty = false;
    switch (senderForm.id) {
        case "registration":
            $mandFields = $("#" + senderForm.id + " input.mandatory");
            for (i = 0; i < $mandFields.length; i++) {
                if (emptyTest($mandFields[i]))
                    empty = true;
            }
            if (empty) {
                $("#error").append("<p>Заполнены не все обязательные поля.</p>");
            }
            if (confirmationCheck($("input.mandatory.confirm")) == "error")
            {
                $("#error").append("<p>\n\
                Значения полей 'пароль' и 'подтвердить пароль' не совпадают.</p>");
            }
            if ($("#error p").size() == 0) {
                $(senderForm).submit();
            }
    }
}
//Проверка, что поле обязательное для заполнения не пусто
function emptyTest(field) {
    if (field.value == "") {
        if ($(field).siblings(".mandatory.error").length == 0) {
            $(field).before("<span   class='mandatory error'>Поле не может быть пустым</span><br class='mandatory error' >");
            $(field).css("color", "red");
            return "true";
        }

    }
    else {
        $(field).siblings(".mandatory.error").remove();
        if ($(field).siblings(".error").length == 0)
            $(field).css("color", "green");
    }
}
//Проверка, что значения полей которые должны иметь одинаковые значения действительно одинаковы.
function confirmationCheck(field) {
    alert (field.value); alert($(".confirmt").prop('value'));
    if ($(field).prop('value') != $(".confirmt").prop('value')) {
        $(field).before("<span class='confirm error'>Значение поля не совпадает с полем \"пароль\"</span><br class='confirm error' >");
        $(field).css("color", "red");
    }
    else {
        $(field).siblings(".confirm.error").remove();
        if ($(field).siblings(".error").length == 0)
            $(field).css("color", "green");
    }


}

