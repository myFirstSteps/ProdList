/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
function ImgMenu(content,owner){
  
    this.tablet=$(content).children("div");
    this.amenu=$(content);
    this.aowner=owner;
    this.tabletHeight=0;
    this.TabletCount=0;
    popUp=function(){ $(this).parent().toggle("clip"); };
    popDown=function(){  $(content).hide(); };
    
    $(content).mouseleave(function(){$(content).fadeOut(4000);});
    $(content).mouseover(function(){$(content).stop().fadeIn(300);});
    var maxDim = 0;
    var tablCount = 0;
                $(this.tablet).each(function() {
                    maxDim = $(this).width() > maxDim ? $(this).width() : maxDim > $(this).height() ? maxDim : $(this).height();
                  $(this).mouseover(function() {
                        $(this).addClass("SelectedTablet");
                    });
                    $(this).mouseout(function() {
                      $(this).removeClass("SelectedTablet");
                      
                   });
                  $(this).click(function(){   $(owner).attr("value",$(this).children("span").text());  popDown(); });
                  ++tablCount;
                 
                });
                this.TabletCount=tablCount;
                this.tablet.width(maxDim);
                this.tablet.height(maxDim);
                $(content).hide();
                $(owner).click(function(){
                
                     $(content).css("position","absolute");
                     $(content).css("left"," -5000px");
                    var awidth = window.innerWidth - (15 * window.innerWidth / 100);
                    var i=awidth / maxDim;
                      
                    var HorCount=Math.floor(awidth /$(content).children("div").outerWidth(true));
                    var VerCount=Math.ceil(tablCount /HorCount);
                    alert(VerCount);
                    var aleft = (window.innerWidth - awidth) / 2;
                    var ctop = (window.innerHeight - $(content).innerHeight()) - window.innerHeight * 10 / 100;
                    var atop = ctop>window.innerHeight/2?ctop:window.innerHeight/2;
                    $(this).parent().position().top + $(this).parent().innerHeight();
                    alert("herer");
                    $(content).innerWidth($(content).children("div").outerWidth(true)*HorCount);
                    $(content).innerHeight($(content).children("div").outerHeight(true)*VerCount);
               
                 //   $(content).css("width", awidth);
                    $(content).css("left", aleft);
                    $(content).css("top", atop);
                    $(content).toggle("clip", 300);
                });

                $(content).css("visibility", "visible");
           
    
    
}

