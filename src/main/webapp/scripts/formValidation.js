/* 
 * JavaScript и jQuery я никогда не изучал. И на момент начала работы с ним прочитал не более половины книги. 
 * Тем не менее он показался довольно интуитивным и понятным, так что решил написать простенькую валидацию данных формы.
 * К тому же обязательно нужно было попробовать ajax,без него как я понимаю сейчас ничего не сделаешь. Понимаю, что написаный код СИЛЬНО далек
 * от качественного и для решаемых задач наверняка есть готовые библиотеки, но в рамках этой задачи удалось давольно неплохо покапаться в API на jQuery.com
 * и в волю походить по граблям.
 */
//Проверка правильности заполнения  формы перед отправкой запроса.
function validate(senderForm) {
    switch (senderForm.id) {
        case "registration":;
        case "newProduct":
            $(".mandatory, .confirm").keyup();
                  
            setTimeout(function(){if ($("span.error").size() === 0) {
                              $(senderForm).submit();
            }},1000);
    }
}
//Проверка, что поле обязательное для заполнения не пусто
function emptyCheck(field,errText) {
    var sel=getClassSelector(errText);
    if (field.value === "") {    
              addErr(field, sel,errText);
    }
    else {
        rmErr(field, sel);
    }
}
//Проверка, что значения полей которые должны иметь одинаковые значения действительно одинаковы.
function confirmationCheck(field,target,errText) {
    var sel=getClassSelector(errText);
    if ($(field).prop('value') !== $(target).prop('value')) {
       text= new String (errText);
      text=text.substring(0,text.indexOf("</"))+" "+$(target).prop('title').toLowerCase()+ text.substring(text.indexOf("</"),text.length);
                addErr(field,sel,text ); 
    }
    else {
        rmErr(field, sel);
    }
}
//Проверка валидности введенных данных.
function dataValidCheck(field,pattern,errText) {
    var val = new String($(field).prop('value'));
    var sel=getClassSelector(errText);
    if (val !='' && (val.match(pattern) === null)) {
        
        addErr(field,sel,errText);
    } else {
        rmErr(field, sel);
    }
}
//Проверка уникальности вводимых данных (отсутствие в DB).
function uniqueCheck(field,data,method,url,errText){ 
     $.ajax({
        data: data,
        type: method,
        url: url,
        success: function(msg) {
            var sel=getClassSelector(errText);
            if(msg.toString() == field.value){
             
                addErr(field,sel, errText);
            }else
            {
                rmErr(field,sel);
            }
        }});
}

function getClassSelector(text){
     var init = new String(text).match(" class=['|\"]((?:.)+?)['|\"]");
      return  "*[class='" + new String(init[1]) + "']"; //'.'+new String(init[1]).replace(' ','.');
}
function addErr(field, selector, errText) {
    if ($(field).siblings(selector).length === 0) {
        $(field).before(errText);
        $(field).css("color", "red");
    }
}
function rmErr(field, err) {
    $(field).siblings(err).length;
    $(field).siblings(err).remove();
    if ($(field).siblings(".error").length === 0)
        $(field).css('color', 'green');
    else
        $(field).css('color', 'red');
}


