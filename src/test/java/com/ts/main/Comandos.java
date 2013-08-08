package com.ts.main;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import com.ts.main.InterpreteMandatos;
import com.ts.objects.Colaborador;
import com.ts.objects.Colon;
import com.ts.objects.CommandException;
import com.ts.objects.Compannia;
import com.ts.objects.Dolar;
import com.ts.objects.Edificio;
import com.ts.objects.Moneda;



public class Comandos  extends TestCase{
	InterpreteMandatos interpreteMandatos;
		
	@BeforeTest
	public void setUpTestCase()
	{
		Repo.limpiaListas();
	}
	
	@AfterTest
	public void ends()
	{
		boolean isThereErrorsInTheExecution = 0 != err.length();
		if(isThereErrorsInTheExecution)
		{
			Assert.fail("Existen errores en la ejecucion de los comandos.");
		}
	}
	
	@Test
	public void agregarCompanniaTest() throws IOException, CommandException
	{
		setErrorsFileOutput("agregarCompanniaTestErrores.txt");
		interpreteMandatos = new InterpreteMandatos(false, outTestDirectory+"agregarCompanniaTest.txt");
		
		String comando ="t.CREAR_COMPANNIA(j456, ts)";
		interpreteMandatos.ejecutaComando(comando);
		Compannia compannia= Repo.getCompannia("j456");
		
		Assert.assertNotNull(compannia);
		Assert.assertEquals(Repo.getTamannoCompannia(),1);
	}
	
	@Test
	public void agregarEdificioTest() throws IOException, CommandException
	{
		setErrorsFileOutput("agregarEdificioTestErrores.txt");
		interpreteMandatos = new InterpreteMandatos(false, outTestDirectory+"agregarEdificioTest.txt");
		
		String comando ="elguarco.CREAR_EDIFICIO(ts5)";
		interpreteMandatos.ejecutaComando(comando);
		Edificio edificio= Repo.getEdificio("ts5");
		
		Assert.assertNotNull(edificio);
		Assert.assertEquals(edificio.getNombre(), "ts5");
		Assert.assertEquals(Repo.getTamannoEdificio(), 1);
		
	}
	
	@Test
	public void agregarColaboradorTest() throws IOException, CommandException
	{
		setErrorsFileOutput("agregarColaboradorTestErrores.txt");
		interpreteMandatos = new InterpreteMandatos(false, outTestDirectory+"agregarColaboradorTest.txt");
		
		String comando ="jahzeel= CREAR_COLABORADOR(Jahzeel, 1-1111-1111, 15/12/1988, 08/07/2013, true, 8445-1544, 0, $1000)";
		interpreteMandatos.ejecutaComando(comando);
		Colaborador colaborador= Repo.getColaborador("1-1111-1111");
		
		Assert.assertNotNull(colaborador);
		Assert.assertEquals(colaborador.getNombre(), "Jahzeel");
		Assert.assertEquals(Repo.getTamannoColaborador(), 1);
		
		comando ="Cguillen= CREAR_COLABORADOR(Cristian Guillen, 1-2222-1111, 15/12/1988, 08/07/2013, true, 8445-1544, 0, $2000)";
		interpreteMandatos.ejecutaComando(comando);
		colaborador= Repo.getColaborador("1-2222-1111");
		
		Assert.assertNotNull(colaborador);
		Assert.assertEquals(colaborador.getNombre(), "Cristian Guillen");
		Assert.assertEquals(Repo.getTamannoColaborador(), 2);	
	}
	
	@Test
	public void pruebaValidarInstacias() throws IOException {
		setErrorsFileOutput("pruebaValidarInstaciasErrores.txt");
		interpreteMandatos = new InterpreteMandatos(false, outTestDirectory+"validarInstaciaEnLaTablaDeSimbolos.txt");
		try{
			Repo.validarInstaciaEnLaTablaDeSimbolos("ts4");
		}catch (CommandException commandException){					
			Assert.fail("ts4 nunca deberia estar duplicado");			
		}
		
		interpreteMandatos.ejecutaComando("ts4=CREAR_COMPANNIA(123HY4567,cecropia2)");
		try{
			Repo.validarInstaciaEnLaTablaDeSimbolos("ts4");
		}catch (CommandException commandException){					
			Assert.assertEquals(commandException.getMessage(),"La instancia ts4 ya existe, cambiela por una diferente.");	
		}
		
	}	
	
	@Test
	public void pruebaAgregaVacaciones() throws IOException {
		setErrorsFileOutput("pruebaAgregaVacacionesErrores.txt");
		interpreteMandatos = new InterpreteMandatos(false, outTestDirectory+"pruebaAgregaVacaciones.txt");
		Colaborador colaborador;
		Date fecha=null;
		
		String result1 = "06/08/2013";
		String result2 = "";
		
		String comando1 ="jlopez= CREAR_COLABORADOR(gerardo, 1-1111-2223, 15/12/1988, 08/07/1988, true, 8445-1544, 0, $1000)";
		String comando2 ="jlopez.TOMAR_VACACIONES("+result1+")";
		
		interpreteMandatos.ejecutaComando(comando1);
		interpreteMandatos.ejecutaComando(comando2);
		
		colaborador=Repo.getColaborador("1-1111-2223");	
		fecha= colaborador.getVacaciones().get(0);
		result2=InterpreteMandatos.getFechaConFormato(fecha);
		
		Assert.assertNotNull(colaborador);
		Assert.assertEquals(result1, result2);
		Assert.assertTrue(colaborador.getEstadoCivil());
		Assert.assertEquals(Repo.getTamannoColaborador(), 3);
		
		interpreteMandatos.ejecutaComando("jlopez.TOMAR_VACACIONES(07/08/2013)");
		interpreteMandatos.ejecutaComando("jlopez.TOMAR_VACACIONES(08/08/2013)");
		interpreteMandatos.ejecutaComando("jlopez.TOMAR_VACACIONES(09/08/2013)");
		interpreteMandatos.ejecutaComando("jlopez.TOMAR_VACACIONES(10/08/2013)");
		interpreteMandatos.ejecutaComando("jlopez.TOMAR_VACACIONES(11/08/2013)");
		
		colaborador=Repo.getColaborador("1-1111-2223");
		Assert.assertEquals(""+colaborador.getVacaciones().size(), "6", "El colaboradores deberia tener 6 dias de vacaciones. " );
		Assert.assertEquals(InterpreteMandatos.getFechaConFormato(colaborador.getVacaciones().get(0)), result1, "El primer dia de vacaciones deberia ser "+result1+". " );
		Assert.assertEquals(InterpreteMandatos.getFechaConFormato(colaborador.getVacaciones().get(1)), "07/08/2013", "El segundo dia de vacaciones deberia ser 07/08/2013. " );
		Assert.assertEquals(InterpreteMandatos.getFechaConFormato(colaborador.getVacaciones().get(2)), "08/08/2013", "El tercer dia de vacaciones deberia ser 08/08/2013. " );
		Assert.assertEquals(InterpreteMandatos.getFechaConFormato(colaborador.getVacaciones().get(3)), "09/08/2013", "El cuarto dia de vacaciones deberia ser 09/08/2013. " );
		Assert.assertEquals(InterpreteMandatos.getFechaConFormato(colaborador.getVacaciones().get(4)), "10/08/2013", "El quinto dia de vacaciones deberia ser 10/08/2013. " );
		Assert.assertEquals(InterpreteMandatos.getFechaConFormato(colaborador.getVacaciones().get(5)), "11/08/2013", "El sexto dia de vacaciones deberia ser 11/08/2013. " );
				
	}

	@Test
	public void pruebaMostrarVacaciones() throws IOException{
		setErrorsFileOutput("pruebaAgregaVacacionesErrores.txt");
		interpreteMandatos = new InterpreteMandatos(false, outTestDirectory+"MostrarVacaciones.txt");
		Colaborador colaborador;
		Date fecha;
		String result="";
		String comando12 ="fchinchilla= CREAR_COLABORADOR(Fernanda Chinchilla, 1-4444-4444, 15/12/1988, 08/07/1988, true, 8445-1544, 0, $1000)";
		String comando21 ="fchinchilla.TOMAR_VACACIONES(07/08/2013)";
		
		interpreteMandatos.ejecutaComando(comando12);
		interpreteMandatos.ejecutaComando(comando21);
		
		colaborador= Repo.getColaborador("1-4444-4444");
		fecha= colaborador.getVacaciones().get(0);
		result= InterpreteMandatos.getFechaConFormato(fecha);
		
		Assert.assertNotNull(colaborador);
		Assert.assertEquals(result, "07/08/2013");
		Assert.assertEquals(colaborador.getVacaciones().size(), 1);
	}
	
	@Test
	public void pruebaAumentarSalario() throws IOException {
		setErrorsFileOutput("pruebaAumentarSalarioErrores.txt");
		interpreteMandatos = new InterpreteMandatos(false, outTestDirectory+"AumentarSalario.txt");
		Colaborador colaborador;
		
		String comando1 ="marias= CREAR_COLABORADOR(Maria Arias, 2-2222-2223, 15/12/1988, 08/07/1988, true, 8445-1544, 0, $1000)";
		String comando2 ="marias.AUMENTAR_SALARIO($2000)";
		
		interpreteMandatos.ejecutaComando(comando1);
		interpreteMandatos.ejecutaComando(comando2);
		
		colaborador=Repo.getColaborador("2-2222-2223");	

		Assert.assertNotNull(colaborador);
		Assert.assertEquals(colaborador.getSalario().getClass(),Dolar.class);
		Assert.assertEquals(colaborador.getSalario().getMonto(),2000.0);
		Assert.assertEquals(colaborador.getNombre(),"Maria Arias");
		Assert.assertEquals(Repo.getTamannoColaborador(),1);
		
		comando1 ="cguillen= CREAR_COLABORADOR(Cristan, 2-2222-55, 15/12/1988, 08/07/1988, true, 8445-1544, 0,  �1000)";
		comando2 ="cguillen.AUMENTAR_SALARIO( �2000)";
		String comando3 ="cguillen.AUMENTAR_SALARIO( �3000)";
		
		interpreteMandatos.ejecutaComando(comando1);
		interpreteMandatos.ejecutaComando(comando2);
		interpreteMandatos.ejecutaComando(comando3);
		
		colaborador=Repo.getColaborador("2-2222-55");	

		Assert.assertNotNull(colaborador);
		Assert.assertEquals(colaborador.getSalario().getClass(),Colon.class);
		Assert.assertEquals(colaborador.getSalario().getMonto(),3000.0);
		Assert.assertEquals(colaborador.getNombre(),"Cristan");
		Assert.assertEquals(Repo.getTamannoColaborador(),2);
	}
	
	@Test
	public void pruebaMostrarSalario() throws IOException{
		setErrorsFileOutput("pruebaMostrarSalarioErrores.txt");
		interpreteMandatos = new InterpreteMandatos(false, outTestDirectory+"MostrarSalario.txt");
		Colaborador colaborador;

		String comando1 ="jguerrero= CREAR_COLABORADOR(jose guerrero, 3-2222-2233, 15/12/1988, 08/07/1988, true, 8445-1544, 0, $1000)";
		
		interpreteMandatos.ejecutaComando(comando1);

		colaborador= Repo.getColaborador("3-2222-2233");

		Assert.assertNotNull(colaborador);
		Assert.assertEquals(colaborador.getSalario().getMonto(), 1000.0);	
	}
	
}
