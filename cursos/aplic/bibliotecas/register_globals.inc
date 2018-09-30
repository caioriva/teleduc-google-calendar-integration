<?php
/*
   Esta funcao cria variaveis globais a partir das superglobals. Isto emula a existencia
   da opcao register_globals que foi deprecada.
   A funcao tambem emula a opcao magic_quotes_gpc, que foi deprecada.
   Deve-se testar o valor a ser atribuido globalmente, pois addslashes so deve ser feito
   em strings.
*/
function globalizar()	
{
	foreach ($_REQUEST as $key => $val)
	{
		global ${$key};
		if(is_string($val))
		{
			${$key} = addslashes($val);
		}
		else
			${$key} = $val;
	}
	foreach($_SESSION as $key => $val)
	{
		global ${$key};
		if(is_string($val))
		{
			${$key} = addslashes($val);
		}
		else
			${$key} = $val;
	}
	foreach($_COOKIE as $key => $val)
	{
		global ${$key};
		if(is_string($val))
		{
			${$key} = addslashes($val);
		}
		else
			${$key} = $val;
	}
	foreach ($_FILES as $key => $value) 
	{
	    $GLOBALS[$key] = $_FILES[$key]['tmp_name'];
	}
	foreach($_SERVER as $key => $val)
	{
		global ${$key};
		if(is_string($val))
		{
			${$key} = addslashes($val);
		}
		else
			${$key} = $val;
	}
	foreach($_ENV as $key => $val)
	{
		global ${$key};
		if(is_string($val))
		{
			${$key} = addslashes($val);
		}
		else
			${$key} = $val;
	}
	foreach($_POST as $key => $val)
	{
		global ${$key};
		if(is_string($val))
		{
			${$key} = addslashes($val);
		}
		else
			${$key} = $val;
	}
	//Desnecessario, pois $_GET ja esteve em um dos lacos anteriores. 
	/*foreach($_GET as $key => $val)
	{
		global ${$key};
		${$key} = addslashes($val);
	}*/
}

globalizar();


if(!function_exists('session_register'))
{
	include ("deprecadas.inc");
}
	
?>
