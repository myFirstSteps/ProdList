/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
function ImgMenu(content,owner){
    var inter;
    this.content=content;
    this.owner=owner;
    var transp=1.0;
    popup=function(){};
    
    $(this.content).mouseleave(function(){
          $(content).fadeOut(5000); 
        
   });
    $(this.content).mouseover(function(){  clearInterval(inter);   $(content).fadeIn(500);} );
}

