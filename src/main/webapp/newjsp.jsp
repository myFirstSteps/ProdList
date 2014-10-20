



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
.ui-autocomplete-loading {
background: white url("resources/common_image/loading.gif") right center no-repeat;
}
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
//alert(request.term);

request.term=JSON.stringify([{vf:"bf"},{ff:"gh"}]); //$(".ter:input").serializeArray();              //[{category:"фрукты"},{name:"бананы"}];
alert(request.term);
$.getJSON( "addProduct", request, function( data, status, xhr ) {
//cache[ term ] = data;
response( data );
});
}

});
});
</script>
</head>
<body>
<div class="ui-widget">
<label for="birds">Birds: </label>
<input id="birds">
<input class="ter" name="gogo" value="bubu">
<input class="ter" name="dodo" value="fufu">
</div>
<div class="ui-widget" style="margin-top:2em; font-family:Arial">
Result:
<div id="log" style="height: 200px; width: 300px; overflow: auto;" class="ui-widget-content"></div>
</div>
</body>
</html>