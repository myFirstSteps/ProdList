



<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>jQuery UI Autocomplete - Default functionality</title>
<link rel="stylesheet" href="http://localhost:8080/ProdList/CSSdoc/jquery-ui.css"> 
<script src="scripts/jquery-1.11.1.min.js"></script>
<script src="scripts/jquery-ui.js"></script>
 
 <style>

</style>
<script>
$(function() {
function log( message ) {
$( "<div>" ).text( message ).prependTo( "#log" );
$( "#log" ).scrollTop( 0 );
}
$( "#birds" ).autocomplete({

minLength: 2,

source: function( request, response ) {
var term = request.term;
/*if ( term in cache ) {
response( cache[ term ] );
return;
}*/    

request.term=JSON.stringify($("input#birds").serializeArray().concat($("input.ter").serializeArray())); //$(".ter:input").serializeArray();              //[{category:"фрукты"},{name:"бананы"}];

$.getJSON( "addProduct", request, function( data, status, xhr ) {
   // alert(data.name);
  // alert(data.name);
    response(data);
//cache[ term ] = data;
   
});
}

});
});
</script>
</head>
<body>
<div class="ui-widget">
<label for="birds">Birds: </label>
<input id="birds" name="name" value="ом">
<input class="ter" name="subName" value="">
<input class="ter" name="producer" value="любой">
</div>
<div class="ui-widget" style="margin-top:2em; font-family:Arial">
Result:
<div id="log" style="height: 200px; width: 300px; overflow: auto;" class="ui-widget-content"></div>
</div>
</body>
</html>