package com.ts.main;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import com.ts.objects.CommandException;
import com.ts.objects.Compannia;
import com.ts.objects.Edificio;

public class InterpreteComandos {
	String outTestDirectory;
	File err;

	@BeforeClass
	public void setUp() throws FileNotFoundException
	{
		outTestDirectory = "test-output"+File.separator+getClass().getSimpleName()+"_logs"+File.separator;
		File outTestDirectoryFile = new File(outTestDirectory);
		File[] listOfFiles = outTestDirectoryFile.listFiles();

		outTestDirectoryFile.mkdirs();
		if(listOfFiles != null )
		{
			for(File file : listOfFiles )
			{
				file.delete();
			}
		}


		err = new File(outTestDirectory+"errors.txt");

		PrintStream printStream = new PrintStream(new FileOutputStream(err));
		System.setErr(printStream);
	}
	
	@BeforeTest
	public void setupTest(){
		Repo.limpiaListas();
	}

	@Test
	public void pruebaCargaArchivo() throws IOException, CommandException{

		InterpreteMandatos interprete = new InterpreteMandatos(false, ArchivoLog.LOG_NAME);
		interprete.ejecutaComando("tsoccidente.CREAR_EDIFICIO(occidente)");
		interprete.ejecutaComando("cecropia=CREAR_COMPANNIA(123JK456, ts)");
		interprete.ejecutaComando("ts.CREAR_COMPANNIA(789, testing)");
		Repo.limpiaListas();
		new InterpreteMandatos(false, ArchivoLog.LOG_NAME);
		
		Compannia compannia= Repo.getCompannia("123JK456");
		Edificio edificio= Repo.getEdificio("occidente");
		
		Assert.assertNotNull(compannia);
		Assert.assertEquals(compannia.getNombre(),"ts");
		Assert.assertEquals(Repo.getTamannoCompannia(), 2);
		
		Assert.assertNotNull(edificio);
		Assert.assertEquals(edificio.getNombre(),"occidente");
		Assert.assertEquals(Repo.getTamannoEdificio(), 1);
	
	}
	
	@Test
	public void pruebaParseComandos() throws IOException, CommandException{
		
		InterpreteMandatos interprete = new InterpreteMandatos(false, ArchivoLog.LOG_NAME);
		Comando comando =interprete.interpreteCadena("ts1=CREAR_COMPANNIA(123456789, cecropia)");
		Comando comando2 =interprete.interpreteCadena("juan.CREAR_COLABORADOR(juan, 123456)");
		
		Assert.assertEquals(comando.getMetodo(),"CREAR_COMPANNIA");		
		Assert.assertEquals(comando.getInstance(),"ts1");
		Assert.assertEquals(comando.getParametros()[0],"123456789");
		Assert.assertEquals(comando.getParametros()[1],"cecropia");
		
		
		Assert.assertEquals(comando2.getMetodo(),"CREAR_COLABORADOR");		
		Assert.assertEquals(comando2.getInstance(),"juan");
		Assert.assertEquals(comando2.getParametros()[0],"juan");
		Assert.assertEquals(comando2.getParametros()[1],"123456");
			
		
		
	}
	@Test
	public void pruebaParseEspacios() throws IOException, CommandException{
		
		InterpreteMandatos interprete = new InterpreteMandatos(false, ArchivoLog.LOG_NAME);
		Comando comando =interprete.interpreteCadena("ts2  =  CREAR_COMPANNIA  (  987654321  ,   solutions   )");	
		
		Assert.assertEquals(comando.getMetodo(),"CREAR_COMPANNIA");		
		Assert.assertEquals(comando.getInstance(),"ts2");
		Assert.assertEquals(comando.getParametros()[0],"987654321");
		Assert.assertEquals(comando.getParametros()[1],"solutions");
		
		
	
	}
	
}
