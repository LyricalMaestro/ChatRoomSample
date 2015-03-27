function getParamsFromQuery(){
	var params = {}
	if(window.location.search.length == 0){
		return params;
	}
	//	?以降の文字列からパラメータを取得	
	var query = window.location.search.substring( 1 );
    var parameters = query.split('&');
    for( var i = 0; i < parameters.length; i++ )
    {
        var element = parameters[ i ].split( '=' );
        var paramName = decodeURIComponent(element[ 0 ]);
        var paramValue = decodeURIComponent(element[ 1 ]);
        params[paramName] = paramValue;
    }
    return params;
}