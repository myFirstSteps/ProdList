/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
function ImgMenu(content, owner) {

    this.tablet = $(content).children("div");
    this.amenu = $(content);
    this.TabletCount = 0;
    popUp = function() {
        $(content).stop(true,true);
        var tabletDim=$(content).children("div").outerWidth(true);    
        $(content).css("position", "absolute");
        $(content).css("left", " -5000px");
        var menuWidth = window.innerWidth - (window.innerWidth *0.15);
        var HorCount = Math.floor(menuWidth / tabletDim);
        var VerCount = Math.ceil(tablCount / HorCount);
        var left = (window.innerWidth - menuWidth) / 2;
        var formBottom=$(owner).closest("form").position().top+$(owner).closest("form").outerHeight();
           
      
        $(content).innerWidth(tabletDim * HorCount+tabletDim*0.24);
        $(content).innerHeight(tabletDim * VerCount);
        $(content).css("left", left);
        $(content).css("top", formBottom);
        $(content).toggle("clip", 300);
        $(content).fadeOut(4000);
        
       
    };
    popDown = function() {
        $(content).stop(true,true);
        $(content).hide();
    };

    $(content).mouseleave(function() {
        $(content).stop(true,true).fadeOut(3000);
    });
    $(content).mouseover(function() {
        $(content).stop(true,true).fadeIn(300);
    });
    var maxDim = 0;
    var tablCount = 0;
    $(this.tablet).each(function( ) {
        maxDim = $(this).width() > maxDim ? $(this).width() : maxDim > $(this).height() ? maxDim : $(this).height();
        $(this).mouseover(function() {
            $(this).addClass("SelectedTablet");
        });
        $(this).mouseout(function() {
            $(this).removeClass("SelectedTablet");
        });
        $(this).click(function() {
                   $( "#CategorySelect").val('');
                   $(owner).val($(this).children("span").text());
                   
            popDown();
        });
        ++tablCount;

    });
    tablCount;
    this.tablet.width(maxDim);
    this.tablet.height(maxDim);
    $(content).hide();
    $(owner).click(function() {
               popUp();
           
    });
    
    $(content).children("#closeMenu").css( "float", 'right').click(function() {
               popDown();    
    });
   
  



}

