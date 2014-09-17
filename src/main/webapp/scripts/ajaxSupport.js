/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

 function checkLogin (login){  
   
     $.ajax({
        data:{name:login},
        type: 'GET',
        url: 'Registration',
        success: function(msg){ 
            if(msg.toString()=='login is free'){
            alert("ага");}else {alert(msg.toString());}
        }
    });
}