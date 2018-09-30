<?php
	/*
	  Esta funcao foi deprecada, por isso foi necessario defini-la aqui.
	  Ela torna uma variavel acessivel globalmente.
	  ENTRADA: Variavel que sera registrada globalmente.
	*/
	function session_register($name)
	{
		global $$name;
		$_SESSION[$name] = $$name;
		$$name = &$_SESSION[$name];
	}

	/*
	  Esta funcao foi deprecada, por isso foi necessario defini-la aqui.
	  Ela verifica se uma variavel foi registradada globalmente
	  (se ela esta acessivel globalmente).
	  ENTRADA: Variavel qualquer.
	*/
	function session_is_registered($name)
	{
		if ( isset( $_SESSION[$name] ) ||  isset( $name ))
		{
			return true;
		}
		return false;
	}
	
	/*
	  Esta funcao foi deprecada, por isso foi necessario defini-la aqui.
	  Ela faz com que uma variavel acessivel globalmente nao o seja mais.
	  ENTRADA: Variavel que ESTA ACESSIVEL GLOBALMENTE.
	*/
	function session_unregister($name)
	{
		unset($_SESSION[$name]);
		unset($name);
	}
?>
