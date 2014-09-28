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
            if ((msg.toString() == 'login is free')) {
                $(sender).siblings(".loginbusy.error").remove();
                if($(sender).siblings(".error").length==0) $(sender).css("color", "green");
            } else {
                $(sender).css("color", "red");
                if ( $(sender).siblings(".loginbusy.error").length == 0)
                    $(sender).before("<span  class='loginbusy error'>К сожалению логин уже занят</span>  <br class='loginbusy error'>");

            }

        }
    });
}