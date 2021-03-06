package com.ts.main;

import java.io.IOException;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import com.ts.db.Repo;
import com.ts.libraries.Colaborador;
import com.ts.libraries.Colon;
import com.ts.libraries.Compania;
import com.ts.libraries.Hilera;
import com.ts.libraries.Mes;
import com.ts.libraries.Moneda;


public class ComandosNegativos extends TestCase{
	InterpreteMandatos interpreteMandatos;
	
	@BeforeTest
	public void setUpTestCase() throws IOException
	{
		Repo.limpiaListas();
	}
		
	@Test
	public void agregarColaboradorNegativeTest() throws Exception
	{
		setErrorsFileOutput("agregarColaboradorNegativeTestErrors.txt");
		interpreteMandatos = new InterpreteMandatos(false, outTestDirectory+"agregarColaboradorNegativeTest.txt");
		
		//wrong month
		String comando ="08/06/2013 16:45, Write jahzeel= CREAR_COLABORADOR(Jahzeel, 1-1111-1111, 15/1988/1998, 08/07/2013, true, 8445-1544, 0, $1000)";
		interpreteMandatos.ejecutaComando(comando);
		Colaborador colaborador= Colaborador.getColaborador(new Hilera("1-1111-1111"));
		Assert.assertNull(colaborador);
		
		//wrong year
		comando ="08/06/2013 16:15, Write jahzeel= CREAR_COLABORADOR(Jahzeel, 1-1111-1111, 15/11/12, 08/07/12, true, 8445-1544, 0, $1000)";
		interpreteMandatos.ejecutaComando(comando);
		colaborador= Colaborador.getColaborador(new Hilera("1-1111-1111"));
		Assert.assertNull(colaborador);
		
		//wrong monto
		comando ="08/06/2013 11:45, Write jahzeel= CREAR_COLABORADOR(Jahzeel, 1-1111-1111, 15/11/1988, 08/07/1988, true, 8445-1544, 0, 1000)";
		interpreteMandatos.ejecutaComando(comando);
		colaborador= Colaborador.getColaborador(new Hilera("1-1111-1111"));
		Assert.assertNull(colaborador);
		
		//wrong estado civil
		comando ="08/07/2013 16:45, Write jahzeel= CREAR_COLABORADOR(Jahzeel, 1-1111-1111, 15/11/1988, 08/07/1988, casado, 8445-1544, 0, $1000)";
		interpreteMandatos.ejecutaComando(comando);
		colaborador= Colaborador.getColaborador(new Hilera("1-1111-1111"));
		Assert.assertNull(colaborador);
		
		Assert.assertEquals(""+getErrors().size(), "4", "Deberian existir error en agregarColaboradorNegativeTest.");
	}
	
	@Test
	public void aumentaSalarioNegativeTestCase() throws Exception
	{
		setErrorsFileOutput("aumentaSalarioNegativeTestCaseError.txt");
		interpreteMandatos = new InterpreteMandatos(false, outTestDirectory+"aumentaSalarioNegativeTestCaseError.txt");
		
		//wrong moneda.
		String comando1 ="08/06/2013 15:45, Write marias= CREAR_COLABORADOR(Maria Arias, 2-2222-2223, 15/12/1988, 08/07/1988, true, 8445-1544, 0, ¢1000)";
		String comando2 ="08/06/2013 18:45, Execute marias.AUMENTAR_SALARIO($2000)";
		
		interpreteMandatos.ejecutaComando(comando1);
		interpreteMandatos.ejecutaComando(comando2);
		
		Colaborador colaborador=Colaborador.getColaborador(new Hilera("2-2222-2223"));	

		Assert.assertNotNull(colaborador);
		Assert.assertEquals(colaborador.getSalario().getClass(),Colon.class);
		Assert.assertEquals(colaborador.getSalario().getMonto(),1000.0);
		Assert.assertEquals(colaborador.getNombre(),"Maria Arias");
		Assert.assertEquals(""+getErrors().size(), "1", "Deberian existir solo un error en aumentaSalarioNegativeTestCases, por el cambio de moneda.");
				
		//aumento sin usuario existente.
		String comando ="09/06/2013 16:45, Execute marias.AUMENTAR_SALARIO($2000)";
		interpreteMandatos.ejecutaComando(comando);
		Assert.assertEquals(""+getErrors().size(), "2", "Deberian existir solo un error en aumentaSalarioNegativeTestCases, por que el usuario no existe");
		
		//wrong monto.
		 comando ="07/06/2013 16:45, Execute marias.AUMENTAR_SALARIO(2000)";
		interpreteMandatos.ejecutaComando(comando);
		Assert.assertEquals(""+getErrors().size(), "3", "Deberian existir solo un error en aumentaSalarioNegativeTestCases, por un monto incorrecto");
	

	}
	
	@Test
	public void pruebaAgregaVacacionesNegativeTest() throws Exception {
		setErrorsFileOutput("pruebaAgregaVacacionesNegativeTestErrores.txt");
		interpreteMandatos = new InterpreteMandatos(false, outTestDirectory+"pruebaAgregaVacacionesNegativeTest.txt");
		
		interpreteMandatos.ejecutaComando("06/06/2013 16:45, Write jlopez.TOMAR_VACACIONES(08/08/2013)");
		Assert.assertEquals(""+getErrors().size(), "1", "Deberia existir un error por que la variable de instancia no existe.");
		
		interpreteMandatos.ejecutaComando("20/06/2013 16:45, Write jlopez= CREAR_COLABORADOR(gerardo, 1-1111-2223, 15/12/1988, 08/07/1988, true, 8445-1544, 0, $1000)");
		interpreteMandatos.ejecutaComando("21/06/2013 16:45, Execute jlopez.TOMAR_VACACIONES(08/2013/2013)");
		interpreteMandatos.ejecutaComando("22/06/2013 16:45, Execute jlopez.TOMAR_VACACIONES(09/08/13)");
		interpreteMandatos.ejecutaComando("10/06/2013 16:45, Execute jlopez.TOMAR_VACACIONES(32/08/2013)");
		Assert.assertEquals(""+getErrors().size(), "4", "Deberia existir 4 errores por parseo incorrecto de la fecha.");		
	}
	
	@Test
	public void pruebaCambiarRangoRentaNegativeTest() throws Exception{
		setErrorsFileOutput("pruebaCambiarRangoRentaNegativeErrors.txt");
		interpreteMandatos = new InterpreteMandatos(false, outTestDirectory+"pruebaCambiarRangoRentaNegativeTest.txt");

		String comando ="01/08/2013 08:17, Write ts=CREAR_COMPANNIA(310146598,cecropia2, colon)";
		interpreteMandatos.ejecutaComando(comando);
		
		Hacienda.intervalosRenta.clear();
		
		//intervalos con , de separador entre digitos
		comando = "01/08/2013 08:18, Execute ts.ESTABLECER_RANGO_RENTA(¢1,¢7,14000,0)";
		interpreteMandatos.ejecutaComando(comando);
		Assert.assertEquals(""+getErrors().size(), "1", "Deberian existir solo un error, el parse de ,");
		
		//intervalos con . de separador entre digitos
		comando = "01/08/2013 08:18, Execute ts.ESTABLECER_RANGO_RENTA(¢1,¢7.14000,0)";
		interpreteMandatos.ejecutaComando(comando);
		Assert.assertEquals(""+getErrors().size(), "2", "Deberian existir solo un error, el parse de .");
		
		//Sin signos en los montos 
		comando = "01/08/2013 08:18, Execute ts.ESTABLECER_RANGO_RENTA(1,714000,0)";
		interpreteMandatos.ejecutaComando(comando);
		
		Assert.assertEquals(""+getErrors().size(), "3", "Deberian existir solo un error, por no tener signos en el monto");
		
		//Con signos diferentes en montos
		comando = "01/08/2013 08:18, Execute ts.ESTABLECER_RANGO_RENTA($1,¢714000,0)";
		interpreteMandatos.ejecutaComando(comando);
		
		Assert.assertEquals(""+getErrors().size(), "4", "Deberian existir solo un error, por tener signos diferentes en el monto");
		
		//Signo % en porcientos
		comando = "01/08/2013 08:18, Execute ts.ESTABLECER_RANGO_RENTA($1,¢714000,%0)";
		interpreteMandatos.ejecutaComando(comando);
		
		Assert.assertEquals(""+getErrors().size(), "5", "Deberian existir solo un error, por tener % en el porciento");
		
		//Tipos de moneda diferentes
		comando = "01/08/2013 08:18, Execute ts.ESTABLECER_RANGO_RENTA(¢1,¢714000,0)";
		String comando2 = "01/08/2013 08:19, ts.ESTABLECER_RANGO_RENTA($714000,$1085000,10)";
		interpreteMandatos.ejecutaComando(comando);
		interpreteMandatos.ejecutaComando(comando2);
		Assert.assertEquals(""+getErrors().size(), "6", "Deberian existir solo un error, por la integridad del intervalo Mayor case 0 y menor case 1");
		
		//intervalos Mayor case 0 y menor case 1 diferentes
		comando = "01/08/2013 08:18, Execute ts.ESTABLECER_RANGO_RENTA(¢1,¢714000,0)";
		comando2 = "01/08/2013 08:19, Execute ts.ESTABLECER_RANGO_RENTA(¢720000,¢1085000,10)";
		interpreteMandatos.ejecutaComando(comando);
		interpreteMandatos.ejecutaComando(comando2);
		Assert.assertEquals(""+getErrors().size(), "7", "Deberian existir solo un error, por la integridad del intervalo Mayor case 0 y menor case 1");
		
		Compania.limpiaRangoRenta();
		
		//intervalos Mayor case 1 y menor case 2 diferentes
		comando = "01/08/2013 08:18, Execute ts.ESTABLECER_RANGO_RENTA(¢1,¢714000,0)";
		comando2 = "01/08/2013 08:19, Execute ts.ESTABLECER_RANGO_RENTA(¢714000,¢1085000,10)";
		String comando3 = "01/08/2013 08:19, Execute ts.ESTABLECER_RANGO_RENTA(¢1090000, ¢9999999,15)";
		interpreteMandatos.ejecutaComando(comando);
		interpreteMandatos.ejecutaComando(comando2);
		interpreteMandatos.ejecutaComando(comando3);
		Assert.assertEquals(""+getErrors().size(), "8", "Deberian existir solo un error, por la integridad del intervalo Mayor case 1 y menor case 2");
	}
	
	@Test
	public void pruebaActualizarMontoConyugeHijoNegativeTest() throws Exception {
		setErrorsFileOutput("pruebaActualizarMontoConyugeHijooErrors.txt");
		interpreteMandatos = new InterpreteMandatos(false, outTestDirectory+"pruebaActualizarMontoConyugeHijoTest.txt");			
		
		String comando2="01/08/2013 08:17, Execute tsa=ACTUALIZAR_MONTO_CONYUGE_HIJO(2000,1340)";
		interpreteMandatos.ejecutaComando(comando2);	
		
		Assert.assertEquals(""+getErrors().size(), "1", "Deberian existir un solo error en pruebaActualizarMontoConyugeHijoNegativeTest, por un incorrecto");
		
		comando2="01/08/2013 08:17, Execute tsa=ACTUALIZAR_MONTO_CONYUGE_HIJO($2000,1340)";
		interpreteMandatos.ejecutaComando(comando2);	
		
		Assert.assertEquals(""+getErrors().size(), "2", "Deberian existir dos errores en pruebaActualizarMontoConyugeHijoNegativeTest, por montos incorrectos");
	}	
	
	@Test
	public void pruebaMostrarRetencionesFuenteRentaNegativeErrors() throws Exception {
		setErrorsFileOutput("pruebaMostrarRetencionesFuenteRentaNegativeErrors.txt");
		interpreteMandatos = new InterpreteMandatos(false, outTestDirectory+"pruebaMostrarRetencionesFuenteRentaNegativeErrors.txt");			
		Colaborador colaborador=null;
		
		String comando1 ="10/08/2013 15:17, WRITE ybolannios= CREAR_COLABORADOR(Yoselyn Bolannios, 2-0357-0387, 15/12/1988, 08/07/1988, true, 8445-1544, 0, $1000)";
		interpreteMandatos.ejecutaComando(comando1);					
		
		String comando2="08/08/2013 15:17, SHOW ybolannios=MOSTRAR_RETENCIONES_FUENTE(02/2013)";
		interpreteMandatos.ejecutaComando(comando2);	
		
		Assert.assertEquals(""+getErrors().size(), "1", "Deberian existir solo un error, por que el colaborador no posee retenciones fuente");
		
		colaborador=Colaborador.getColaborador(new Hilera("2-0357-0387"));	
		
		Moneda monto= Sistema.getMoneda( new Hilera("¢4000.0"));		
		
		colaborador.retencionesFuentes.put(new Mes(02,2013),monto);
		
		String comando3="16/08/2013 15:17, SHOW ybolannios=MOSTRAR_RETENCIONES_FUENTE(08/2013)";
		interpreteMandatos.ejecutaComando(comando3);
		
		Assert.assertEquals(""+getErrors().size(), "2", "Deberian existir solo un error, por que el periodo consultado es igual a la fecha actual");		
		
		String comando4="16/08/2013 15:17, SHOW ybolannios=MOSTRAR_RETENCIONES_FUENTE(09/2013)";
		interpreteMandatos.ejecutaComando(comando4);	
		
		Assert.assertEquals(""+getErrors().size(), "3", "Deberian existir solo un error, por que el periodo consultado es superior a la fecha actual");		

	}
}
