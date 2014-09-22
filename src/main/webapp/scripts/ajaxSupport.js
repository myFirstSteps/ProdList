/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function checkLogin(sender) {

    $.ajax({
        data: {name: sender.value},
        type: 'GET',
        url: 'Registration',
        success: function(msg) {
            if (msg.toString() == 'login is free') {
                $(sender).css("color", "green");
                $("#loginerror").remove();
            } else {
                $(sender).css("color", "red");
                if ($("#loginerror").size() < 1)
                    $(sender).before("<span id='loginerror' class='error'>К сожалению логин уже занят</span>");

            }

        }
    });
}