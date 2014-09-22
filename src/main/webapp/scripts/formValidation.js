/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//Проверка правильности заполнения  форм
function validate(senderForm) {
    $(".error").empty();
    var empty = false;
    switch (senderForm.id) {
        case "registration":
            alert($(".error p").size());
            // if($(".gogo").e)
            $mandFields = $("#" + senderForm.id + " input.mandatory");
            for (i = 0; i < $mandFields.length; i++) {
                if (emptyTest($mandFields[i]))
                    empty = true;
            }
            if (empty) {
                $(".error").append("<p>Заполнены не все обязательные поля.</p>");
            }
            if (confirmationCheck($("input.mandatory.confirm")) == "error")
            {
                $(".error").append("<p>\n\
                Значения полей 'пароль' и 'подтвердить пароль' не совпадают.</p>");
            }
            alert($(".error p").size());
            if ($(".error p").size() == 0) {
                alert($(".error p").size());
                $(senderForm).submit();
            }
            alert("wof");



    }
}
//Проверка, что поле обязательное для заполнения не пусто
function emptyTest(field) {
    if (field.value == "") {
        $(field).css("color", "red");
        return "true";
    }
    else
        $(field).css("color", "green");
}
//Проверка, что значения полей которые должны иметь одинаковые значения действительно одинаковы.
function confirmationCheck(values) {
    for (i = 0; values.length; i++) {
        if (i > 0 && values[i].value != values[i - 1].value) {
            $(values[i]).css("color", "red");
            $(values[i - 1]).css("color", "red");
            return "error";
        }
    }
    return "no error";

}
